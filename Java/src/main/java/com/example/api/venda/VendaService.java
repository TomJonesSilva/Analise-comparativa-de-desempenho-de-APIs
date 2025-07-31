package com.example.api.venda;

import com.example.api.Database;

import com.example.api.dto.CreateVendaDto;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;
import java.util.Date;

@Service
public class VendaService {

    public Map<String, Object> create(CreateVendaDto dto) {
        String sql = """
                    INSERT INTO vendas (
                      produto_id, quantidade, preco_unitario, data_venda
                    ) VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, dto.produto_id);
            stmt.setInt(2, dto.quantidade);
            stmt.setDouble(3, dto.preco_unitario);
            stmt.setString(4, dto.data_venda);
            stmt.executeUpdate();

            Map<String, Object> res = new HashMap<>();
            res.put("message", "Venda registrada com sucesso");
            return res;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar venda", e);
        }
    }

    public List<Map<String, Object>> findAll() {
        List<Map<String, Object>> vendas = new ArrayList<>();
        String sql = """
                    SELECT v.*, p.nome AS nome_produto
                    FROM vendas v
                    JOIN produtos p ON p.id = v.produto_id
                    ORDER BY v.data_venda DESC
                """;

        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> venda = new HashMap<>();
                venda.put("id", rs.getInt("id"));
                venda.put("produto_id", rs.getInt("produto_id"));
                venda.put("quantidade", rs.getInt("quantidade"));
                venda.put("preco_unitario", rs.getDouble("preco_unitario"));
                venda.put("data_venda", rs.getString("data_venda"));
                venda.put("nome_produto", rs.getString("nome_produto"));
                vendas.add(venda);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar vendas", e);
        }

        return vendas;
    }

    public List<Map<String, Object>> gerarRelatorioVendasMensais() {
        String sql = """
                    SELECT
                      v.data_venda,
                      v.quantidade,
                      v.preco_unitario,
                      p.categoria
                    FROM vendas v
                    JOIN produtos p ON p.id = v.produto_id
                """;

        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            Map<String, Map<String, Object>> agrupado = new HashMap<>();

            while (rs.next()) {
                Date dataVenda = rs.getDate("data_venda");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dataVenda);
                int ano = calendar.get(Calendar.YEAR);
                String mes = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
                String categoria = rs.getString("categoria");
                String chave = ano + "-" + mes + "-" + categoria;

                double valor = rs.getInt("quantidade") * rs.getDouble("preco_unitario");

                Map<String, Object> grupo = agrupado.getOrDefault(chave, new HashMap<>());
                grupo.put("ano", ano);
                grupo.put("mes", mes);
                grupo.put("categoria", categoria);
                grupo.put("quantidade_total",
                        (int) grupo.getOrDefault("quantidade_total", 0) + rs.getInt("quantidade"));
                grupo.put("valor_total", (double) grupo.getOrDefault("valor_total", 0.0) + valor);

                agrupado.put(chave, grupo);
            }

            double taxa = 0.015;
            int meses = 12;

            List<Map<String, Object>> relatorioFinal = new ArrayList<>();

            for (Map<String, Object> grupo : agrupado.values()) {
                double valorTotal = (double) grupo.get("valor_total");
                double projecao = valorTotal * Math.pow(1 + taxa, meses);
                grupo.put("projecao_12_meses", Math.round(projecao * 100.0) / 100.0);
                relatorioFinal.add(grupo);
            }

            // Inserção no banco (opcional)
            for (Map<String, Object> item : relatorioFinal) {
                try (PreparedStatement insertStmt = conn.prepareStatement("""
                            INSERT INTO relatorio_vendas_mensais (
                              ano, mes, categoria, quantidade_total,
                              valor_total, projecao_12_meses
                            ) VALUES (?, ?, ?, ?, ?, ?)
                        """)) {
                    insertStmt.setInt(1, (int) item.get("ano"));
                    insertStmt.setString(2, (String) item.get("mes"));
                    insertStmt.setString(3, (String) item.get("categoria"));
                    insertStmt.setInt(4, (int) item.get("quantidade_total"));
                    insertStmt.setDouble(5, (double) item.get("valor_total"));
                    insertStmt.setDouble(6, (double) item.get("projecao_12_meses"));
                    insertStmt.executeUpdate();
                }
            }

            return relatorioFinal;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao gerar relatório", e);
        }
    }
}
