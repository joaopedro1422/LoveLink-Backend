package com.lovelink.LoveLink_Backend.Models;

import com.lovelink.LoveLink_Backend.Dto.ParceiroRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_parceiros")
@Builder
@AllArgsConstructor
public class Parceiro implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String nome;
    private String email;
    private String senha;
    private Double precoCompra;

    public Parceiro(ParceiroRequestDTO parceiroRequestDTO){
        this.nome = parceiroRequestDTO.nome();
        this.email = parceiroRequestDTO.email();;
        this.senha = parceiroRequestDTO.senha();
        this.precoCompra = parceiroRequestDTO.precoCompra();
    }

    public Parceiro() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Double getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(Double precoCompra) {
        this.precoCompra = precoCompra;
    }
}
