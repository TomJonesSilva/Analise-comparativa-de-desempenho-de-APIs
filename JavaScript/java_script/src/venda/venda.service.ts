import { Injectable } from '@nestjs/common';
import { MysqlService } from '../mysql.service';
import { CreateVendaDto } from '../create_.dto';

@Injectable()
export class VendasService {
  constructor(private readonly mysqlService: MysqlService) {}

  async create(dto: CreateVendaDto): Promise<any> {
    const { produto_id, quantidade, preco_unitario, data_venda } = dto;

    const sql = `
      INSERT INTO vendas (
        produto_id, quantidade, preco_unitario, data_venda
      ) VALUES (?, ?, ?, ?)
    `;

    const params = [produto_id, quantidade, preco_unitario, data_venda];
    await this.mysqlService.query(sql, params);

    return { message: 'Venda registrada com sucesso' };
  }

  async findAll(): Promise<any[]> {
    const sql = `
      SELECT v.*, p.nome AS nome_produto
      FROM vendas v
      JOIN produtos p ON p.id = v.produto_id
      ORDER BY v.data_venda DESC
    `;
    return this.mysqlService.query(sql);
  }
  async gerarRelatorioVendasMensais(): Promise<any[]> {
    // 1. Buscar todos os dados de vendas e produtos
    const sql = `
      SELECT
        v.data_venda,
        v.quantidade,
        v.preco_unitario,
        p.categoria
      FROM vendas v
      JOIN produtos p ON p.id = v.produto_id
    `;

    const vendas = await this.mysqlService.query(sql);

    // 2. Processar os dados no código
    const agrupado = new Map<
      string,
      {
        ano: number;
        mes: string;
        categoria: string;
        quantidade_total: number;
        valor_total: number;
      }
    >();

    for (const venda of vendas) {
      const data = new Date(venda.data_venda);
      const ano = data.getFullYear();
      const mes = String(data.getMonth() + 1).padStart(2, '0');
      const categoria = venda.categoria;
      const chave = `${ano}-${mes}-${categoria}`;
      const valor = venda.quantidade * venda.preco_unitario;

      const acumulado = agrupado.get(chave) ?? {
        ano,
        mes,
        categoria,
        quantidade_total: 0,
        valor_total: 0,
      };

      acumulado.quantidade_total += venda.quantidade;
      acumulado.valor_total += valor;

      agrupado.set(chave, acumulado);
    }

    // 3. Gerar projeções e preparar para inserção
    const taxa = 0.015;
    const meses = 12;

    const relatorioFinal = Array.from(agrupado.values()).map((grupo) => ({
      ...grupo,
      projecao_12_meses: parseFloat(
        (grupo.valor_total * Math.pow(1 + taxa, meses)).toFixed(2),
      ),
    }));

    // 4. Escrever no banco (inserir linha por linha)
    for (const item of relatorioFinal) {
      await this.mysqlService.query(
        `
        INSERT INTO relatorio_vendas_mensais (
          ano, mes, categoria, quantidade_total,
          valor_total, projecao_12_meses
        ) VALUES (?, ?, ?, ?, ?, ?)
      `,
        [
          item.ano,
          item.mes,
          item.categoria,
          item.quantidade_total,
          item.valor_total,
          item.projecao_12_meses,
        ],
      );
    }

    return relatorioFinal;
  }
}
