package com.example.api.venda;

import com.example.api.dto.CreateVendaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @PostMapping
    public Map<String, Object> create(@RequestBody CreateVendaDto dto) {
        return vendaService.create(dto);
    }

    @GetMapping
    public List<Map<String, Object>> findAll() {
        return vendaService.findAll();
    }

    @GetMapping("/relatorio")
    public List<Map<String, Object>> gerarRelatorioDeVendas() {
        return vendaService.gerarRelatorioVendasMensais();
    }

    @PostMapping("/calculo-produto")
    public Map<String, Object> calcularMetricaProduto(@RequestBody Map<String, Object> body) {
        List<Map<String, Object>> vendas = (List<Map<String, Object>>) body.get("vendas");
        int meses = (int) body.get("meses");
        double taxa = ((Number) body.get("taxa")).doubleValue();
        int iteracoes = (int) body.get("iteracoes");

        double somaPonderada = 0;
        int totalQuantidade = 0;

        for (Map<String, Object> v : vendas) {
            double preco = ((Number) v.get("preco")).doubleValue();
            int quantidade = (int) v.get("quantidade");
            somaPonderada += preco * quantidade;
            totalQuantidade += quantidade;
        }

        double mediaPonderada = somaPonderada / totalQuantidade;
        double receitaTotal = mediaPonderada * totalQuantidade;
        double valorFuturo = receitaTotal;

        for (int i = 0; i < iteracoes; i++) {
            valorFuturo = receitaTotal * Math.pow(1 + taxa, meses);
        }

        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("media_ponderada_preco", round(mediaPonderada));
        resultado.put("receita_total", round(receitaTotal));
        resultado.put("valor_futuro", round(valorFuturo));
        resultado.put("iteracoes", iteracoes);
        resultado.put("meses", meses);
        resultado.put("taxa_mensal", taxa);

        return resultado;
    }

    @PostMapping("/juros-compostos")
    public Map<String, Object> simularJurosCompostos(@RequestBody Map<String, Object> body) {
        double aporteMensal = ((Number) body.get("aporteMensal")).doubleValue();
        double taxaMensal = ((Number) body.get("taxaMensal")).doubleValue();
        int meses = (int) body.get("meses");
        int simulacoes = (int) body.get("simulacoes");

        List<Double> resultados = new ArrayList<>();
        long start = System.currentTimeMillis();

        for (int s = 0; s < simulacoes; s++) {
            double total = 0;
            for (int m = 0; m < meses; m++) {
                total = (total + aporteMensal) * (1 + taxaMensal);
            }
            resultados.add(round(total));
        }

        long end = System.currentTimeMillis();

        double totalInvestido = aporteMensal * meses;
        double media = resultados.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double lucro = media - totalInvestido;

        double iof = (meses < 1) ? lucro * 0.96 : 0;
        double aliquotaIR = meses * 30 <= 180 ? 0.225 : meses * 30 <= 360 ? 0.20 : meses * 30 <= 720 ? 0.175 : 0.15;
        double ir = lucro * aliquotaIR;
        double lucroLiquido = lucro - iof - ir;
        double valorFinalLiquido = totalInvestido + lucroLiquido;

        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("simulacoes", simulacoes);
        resultado.put("meses", meses);
        resultado.put("aporteMensal", aporteMensal);
        resultado.put("taxaMensal", taxaMensal);
        resultado.put("media_valor_bruto", round(media));
        resultado.put("total_investido", round(totalInvestido));
        resultado.put("lucro_bruto", round(lucro));
        resultado.put("iof", round(iof));
        resultado.put("ir", round(ir));
        resultado.put("valor_final_liquido", round(valorFinalLiquido));
        resultado.put("tempo_execucao_ms", end - start);

        return resultado;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
