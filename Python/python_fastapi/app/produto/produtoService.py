from app.database import get_connection
from app.schemas import CreateProdutoDto

def criar_produto(dto: CreateProdutoDto):
    conn = get_connection()
    try:
        with conn.cursor() as cursor:
            sql = """
                INSERT INTO produtos (
                    nome, descricao, preco, quantidade_em_estoque,
                    categoria, ativo, fornecedor, codigo_barras
                ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
            """
            params = (
                dto.nome,
                dto.descricao,
                dto.preco,
                dto.quantidade_em_estoque,
                dto.categoria,
                dto.ativo,
                dto.fornecedor,
                dto.codigo_barras,
            )
            cursor.execute(sql, params)
            conn.commit()
            return {"message": "Produto criado com sucesso"}
    finally:
        conn.close()

def listar_todos_produtos():
    conn = get_connection()
    try:
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM produtos")
            return cursor.fetchall()
    finally:
        conn.close()

def buscar_por_categoria(categoria: str):
    conn = get_connection()
    try:
        with conn.cursor() as cursor:
            sql = "SELECT * FROM produtos WHERE categoria = %s"
            cursor.execute(sql, (categoria,))
            return cursor.fetchall()
    finally:
        conn.close()
