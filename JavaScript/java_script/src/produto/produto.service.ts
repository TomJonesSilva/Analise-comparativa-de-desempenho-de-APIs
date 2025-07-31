import { Injectable } from '@nestjs/common';
import { MysqlService } from '../mysql.service';
import { CreateProdutoDto } from '../create_.dto';

@Injectable()
export class ProdutosService {
  constructor(private readonly mysqlService: MysqlService) {}

  async create(dto: CreateProdutoDto): Promise<any> {
    const {
      nome,
      descricao,
      preco,
      quantidade_em_estoque = 0,
      categoria,
      ativo = true,
      fornecedor,
      codigo_barras,
    } = dto;

    const sql = `
      INSERT INTO produtos (
        nome, descricao, preco, quantidade_em_estoque,
        categoria, ativo, fornecedor, codigo_barras
      ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    `;

    const params = [
      nome,
      descricao ?? null,
      preco,
      quantidade_em_estoque,
      categoria ?? null,
      ativo,
      fornecedor ?? null,
      codigo_barras,
    ];

    await this.mysqlService.query(sql, params);
    return { message: 'Produto criado com sucesso' };
  }

  async findAll(): Promise<any[]> {
    const sql = `SELECT * FROM produtos`;
    return this.mysqlService.query(sql);
  }

  async findCategoria(categoria: string): Promise<any[]> {
    const sql = 'SELECT * FROM produtos WHERE categoria = ?';
    return this.mysqlService.query(sql, [categoria]);
  }
}
