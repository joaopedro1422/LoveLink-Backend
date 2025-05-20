package com.lovelink.LoveLink_Backend.Models;

import com.lovelink.LoveLink_Backend.Dto.PlanoRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "tb_planos")
@Builder
@AllArgsConstructor
public class Plano implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    private String nome;
    private Double preco;
    private Double precoAntigo;

    public Plano(PlanoRequestDto planoDto){
        this.nome = planoDto.nome();
        this.precoAntigo = planoDto.precoAntigo();
        this.preco = planoDto.preco();
    }

    public Plano(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Double getPrecoAntigo() {
        return precoAntigo;
    }

    public void setPrecoAntigo(Double precoAntigo) {
        this.precoAntigo = precoAntigo;
    }
}
