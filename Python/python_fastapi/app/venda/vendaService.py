from fastapi import HTTPException
from app.database import get_connection

def criar_venda(dto):
    conn = get_connection()
    try:
        with conn.cursor() as cursor:
            sql = """
            INSERT INTO vendas (produto_id, quantidade, preco_unitario, data_venda)
            VALUES (%s, %s, %s, %s)
            """
            params = (dto.produto_id, dto.quantidade, dto.preco_unitario, dto.data_venda)
            cursor.execute(sql, params)
            conn.commit()
            return {"message": "Venda registrada com sucesso"}
    finally:
        conn.close()

def listar_vendas():
    conn = get_connection()
    try:
        with conn.cursor() as cursor:
            sql = """
            SELECT v.*, p.nome AS nome_produto
            FROM vendas v
            JOIN produtos p ON p.id = v.produto_id
            ORDER BY v.data_venda DESC
            """
            cursor.execute(sql)
            return cursor.fetchall()
    finally:
        conn.close()

def gerar_relatorio_mensal():
    conn = get_connection()
    try:
        with conn.cursor() as cursor:
            sql = """
            SELECT v.data_venda, v.quantidade, v.preco_unitario, p.categoria
            FROM vendas v
            JOIN produtos p ON p.id = v.produto_id
            """
            cursor.execute(sql)
            vendas = cursor.fetchall()

        agrupado = {}
        for venda in vendas:
            data = venda["data_venda"]
            ano = data.year
            mes = f"{data.month:02}"
            categoria = venda["categoria"]
            chave = f"{ano}-{mes}-{categoria}"
            valor = float(venda["quantidade"]) * float(venda["preco_unitario"])

            if chave not in agrupado:
                agrupado[chave] = {
                    "ano": ano,
                    "mes": mes,
                    "categoria": categoria,
                    "quantidade_total": 0,
                    "valor_total": 0.0,
                }

            agrupado[chave]["quantidade_total"] += venda["quantidade"]
            agrupado[chave]["valor_total"] += valor

        taxa = 0.015
        meses = 12

        relatorio_final = []
        for grupo in agrupado.values():
            projecao = round(grupo["valor_total"] * ((1 + taxa) ** meses), 2)
            grupo["projecao_12_meses"] = projecao
            relatorio_final.append(grupo)

        with conn.cursor() as cursor:
            for item in relatorio_final:
                cursor.execute(
                    """
                    INSERT INTO relatorio_vendas_mensais
                    (ano, mes, categoria, quantidade_total, valor_total, projecao_12_meses)
                    VALUES (%s, %s, %s, %s, %s, %s)
                    """,
                    (
                        item["ano"], item["mes"], item["categoria"],
                        item["quantidade_total"], item["valor_total"],
                        item["projecao_12_meses"],
                    )
                )
            conn.commit()

        return relatorio_final
    finally:
        conn.close()

def calcular_metrica_produto(body):
    vendas = body.vendas
    meses = body.meses
    taxa = body.taxa
    iteracoes = body.iteracoes

    total_quantidade = 0
    soma_ponderada = 0.0

    for venda in vendas:
        soma_ponderada += venda.preco * venda.quantidade
        total_quantidade += venda.quantidade

    if total_quantidade == 0:
        raise HTTPException(status_code=400, detail="Quantidade total não pode ser zero")

    media = soma_ponderada / total_quantidade
    receita = media * total_quantidade

    valor_futuro = receita
    for _ in range(iteracoes):
        valor_futuro = receita * ((1 + taxa) ** meses)

    return {
        "media_ponderada_preco": round(media, 2),
        "receita_total": round(receita, 2),
        "valor_futuro": round(valor_futuro, 2),
        "iteracoes": iteracoes,
        "meses": meses,
        "taxa_mensal": taxa,
    }

def simular_juros_compostos(body):
    aporte = body.aporteMensal
    taxa = body.taxaMensal
    meses = body.meses
    simulacoes = body.simulacoes

    resultados = []
    import time
    start = time.time()

    for _ in range(simulacoes):
        total = 0
        for _ in range(meses):
            total = (total + aporte) * (1 + taxa)
        resultados.append(round(total, 2))

    end = time.time()

    investido = aporte * meses
    media = sum(resultados) / simulacoes
    lucro = media - investido

    iof = lucro * 0.96 if meses < 1 else 0

    dias = meses * 30
    if dias <= 180:
        ir = 0.225
    elif dias <= 360:
        ir = 0.2
    elif dias <= 720:
        ir = 0.175
    else:
        ir = 0.15

    imposto_renda = lucro * ir
    lucro_liquido = lucro - iof - imposto_renda
    valor_final = investido + lucro_liquido

    return {
        "simulacoes": simulacoes,
        "meses": meses,
        "aporteMensal": aporte,
        "taxaMensal": taxa,
        "media_valor_bruto": round(media, 2),
        "total_investido": round(investido, 2),
        "lucro_bruto": round(lucro, 2),
        "iof": round(iof, 2),
        "ir": round(imposto_renda, 2),
        "valor_final_liquido": round(valor_final, 2),
        "tempo_execucao_ms": round((end - start) * 1000),
    }
