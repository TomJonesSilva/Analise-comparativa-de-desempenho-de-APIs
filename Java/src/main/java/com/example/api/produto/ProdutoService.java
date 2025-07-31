package com.example.api.produto;

import com.example.api.Database;
import com.example.api.dto.CreateProdutoDto;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

@Service
public class ProdutoService {

    public Map<String, Object> create(CreateProdutoDto dto) {
        String sql = """
                    INSERT INTO produtos (
                      nome, descricao, preco, quantidade_em_estoque,
                      categoria, ativo, fornecedor, codigo_barras
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dto.nome);
            stmt.setString(2, dto.descricao);
            stmt.setDouble(3, dto.preco);
            stmt.setInt(4, dto.quantidade_em_estoque != null ? dto.quantidade_em_estoque : 0);
            stmt.setString(5, dto.categoria);
            stmt.setBoolean(6, dto.ativo != null ? dto.ativo : true);
            stmt.setString(7, dto.fornecedor);
            stmt.setString(8, dto.codigo_barras);
            stmt.executeUpdate();

            Map<String, Object> res = new HashMap<>();
            res.put("message", "Produto criado com sucesso");
            return res;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir produto", e);
        }
    }

    public List<Map<String, Object>> findAll() {
        List<Map<String, Object>> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";

        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> p = new HashMap<>();
                p.put("id", rs.getInt("id"));
                p.put("nome", rs.getString("nome"));
                p.put("descricao", rs.getString("descricao"));
                p.put("preco", rs.getDouble("preco"));
                p.put("quantidade_em_estoque", rs.getInt("quantidade_em_estoque"));
                p.put("categoria", rs.getString("categoria"));
                p.put("ativo", rs.getBoolean("ativo"));
                p.put("fornecedor", rs.getString("fornecedor"));
                p.put("codigo_barras", rs.getString("codigo_barras"));
                produtos.add(p);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos", e);
        }

        return produtos;
    }

    public List<Map<String, Object>> findByCategoria(String categoria) {
        List<Map<String, Object>> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE categoria = ?";

        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> p = new HashMap<>();
                p.put("id", rs.getInt("id"));
                p.put("nome", rs.getString("nome"));
                p.put("descricao", rs.getString("descricao"));
                p.put("preco", rs.getDouble("preco"));
                p.put("quantidade_em_estoque", rs.getInt("quantidade_em_estoque"));
                p.put("categoria", rs.getString("categoria"));
                p.put("ativo", rs.getBoolean("ativo"));
                p.put("fornecedor", rs.getString("fornecedor"));
                p.put("codigo_barras", rs.getString("codigo_barras"));
                produtos.add(p);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produtos por categoria", e);
        }

        return produtos;
    }
}
