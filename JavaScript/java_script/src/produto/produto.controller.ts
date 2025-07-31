import { Controller, Post, Get, Body, Param } from '@nestjs/common';
import { ProdutosService } from './produto.service';
import { CreateProdutoDto } from '../create_.dto';

@Controller('produtos')
export class ProdutoController {
  constructor(private readonly produtosService: ProdutosService) {}

  @Post()
  async create(@Body() dto: CreateProdutoDto) {
    return this.produtosService.create(dto);
  }

  @Get()
  async findAll() {
    return this.produtosService.findAll();
  }

  @Get(':categoria')
  async findCategoria(@Param('categoria') categoria: string) {
    return this.produtosService.findCategoria(categoria);
  }
}
