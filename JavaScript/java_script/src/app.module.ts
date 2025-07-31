import { Module } from '@nestjs/common';
import { ProdutoController } from './produto/produto.controller';
import { ProdutosService } from './produto/produto.service';
import { VendasController } from './venda/venda.controller';
import { VendasService } from './venda/venda.service';
import { MysqlService } from './mysql.service';
@Module({
  imports: [],
  controllers: [ProdutoController, VendasController],
  providers: [ProdutosService, VendasService, MysqlService],
})
export class AppModule {}
