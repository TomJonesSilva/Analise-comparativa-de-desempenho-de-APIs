package com.example.api.produto;

import com.example.api.dto.CreateProdutoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    public Map<String, Object> create(@RequestBody CreateProdutoDto dto) {
        return produtoService.create(dto);
    }

    @GetMapping
    public List<Map<String, Object>> findAll() {
        return produtoService.findAll();
    }

    @GetMapping("/{categoria}")
    public List<Map<String, Object>> findByCategoria(@PathVariable String categoria) {
        return produtoService.findByCategoria(categoria);
    }
}
