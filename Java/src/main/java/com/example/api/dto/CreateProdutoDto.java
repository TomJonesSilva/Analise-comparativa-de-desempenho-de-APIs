package com.example.api.dto;

public class CreateProdutoDto {
    public String nome;
    public String descricao;
    public Double preco;
    public Integer quantidade_em_estoque = 0;
    public String categoria;
    public Boolean ativo = true;
    public String fornecedor;
    public String codigo_barras;
}
