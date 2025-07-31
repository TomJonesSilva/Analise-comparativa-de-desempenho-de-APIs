import { Controller, Post, Get, Body, Param } from '@nestjs/common';
import { VendasService } from './venda.service';
import { CreateVendaDto } from '../create_.dto';

@Controller('vendas')
export class VendasController {
  constructor(private readonly vendasService: VendasService) {}

  @Post()
  async create(@Body() dto: CreateVendaDto) {
    return this.vendasService.create(dto);
  }

  @Get()
  async findAll() {
    return this.vendasService.findAll();
  }

  @Get('relatorio')
  async gerarRelatorioDeVendas() {
    return this.vendasService.gerarRelatorioVendasMensais();
  }

  @Post('juros-compostos')
  simularJurosCompostos(
    @Body()
    body: {
      aporteMensal: number;
      taxaMensal: number;
      meses: number;
      simulacoes: number;
    },
  ) {
    const { aporteMensal, taxaMensal, meses, simulacoes } = body;
    //console.log(body);
    const resultados: number[] = [];

    const start = Date.now();

    for (let s = 0; s < simulacoes; s++) {
      let total = 0;

      for (let m = 0; m < meses; m++) {
        total = (total + aporteMensal) * (1 + taxaMensal);
      }

      resultados.push(parseFloat(total.toFixed(2)));
    }

    const end = Date.now();

    // Total investido (aporteMensal * meses)
    const totalInvestido = aporteMensal * meses;

    // Valor médio das simulações
    const media = resultados.reduce((acc, val) => acc + val, 0) / simulacoes;

    // Lucro bruto (antes de impostos)
    const lucro = media - totalInvestido;

    // === IOF ===
    let iof = 0;
    if (meses < 1) {
      // IOF de 96% sobre o lucro (exemplo fictício para simulação)
      iof = lucro * 0.96;
    }

    // === IR ===
    let aliquotaIR = 0;
    const dias = meses * 30;

    if (dias <= 180) aliquotaIR = 0.225;
    else if (dias <= 360) aliquotaIR = 0.2;
    else if (dias <= 720) aliquotaIR = 0.175;
    else aliquotaIR = 0.15;

    const ir = lucro * aliquotaIR;

    // Lucro líquido após impostos
    const lucroLiquido = lucro - iof - ir;

    // Valor líquido final
    const valorFinalLiquido = totalInvestido + lucroLiquido;

    return {
      simulacoes,
      meses,
      aporteMensal,
      taxaMensal,
      media_valor_bruto: parseFloat(media.toFixed(2)),
      total_investido: parseFloat(totalInvestido.toFixed(2)),
      lucro_bruto: parseFloat(lucro.toFixed(2)),
      iof: parseFloat(iof.toFixed(2)),
      ir: parseFloat(ir.toFixed(2)),
      valor_final_liquido: parseFloat(valorFinalLiquido.toFixed(2)),
      tempo_execucao_ms: end - start,
    };
  }

  /* 
  @Post('calculo-produto')
  calcularMetricaProduto(
    @Body()
    body: {
      vendas: { preco: number; quantidade: number }[];
      meses: number;
      taxa: number;
      iteracoes: number;
    },
  ) {
    const { vendas, meses, taxa, iteracoes } = body;

    // 1. Cálculo da média ponderada do preço
    let totalQuantidade = 0;
    let somaPonderada = 0;

    for (const venda of vendas) {
      somaPonderada += venda.preco * venda.quantidade;
      totalQuantidade += venda.quantidade;
    }

    const mediaPonderada = somaPonderada / totalQuantidade;

    // 2. Receita total (média * total de itens)
    const receitaTotal = mediaPonderada * totalQuantidade;

    // 3. Simulação pesada do valor futuro da receita com juros compostos
    let valorFuturo = receitaTotal;

    for (let i = 0; i < iteracoes; i++) {
      valorFuturo = receitaTotal * Math.pow(1 + taxa, meses);
    }

    return {
      media_ponderada_preco: parseFloat(mediaPonderada.toFixed(2)),
      receita_total: parseFloat(receitaTotal.toFixed(2)),
      valor_futuro: parseFloat(valorFuturo.toFixed(2)),
      iteracoes,
      meses,
      taxa_mensal: taxa,
    };
  }
*/
}
