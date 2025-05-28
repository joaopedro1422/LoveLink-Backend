package com.lovelink.LoveLink_Backend.Models;

import com.lovelink.LoveLink_Backend.Dto.PaginaRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_paginas")
@Builder
@AllArgsConstructor
public class Pagina implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    private UUID idParceiro;
    private String nomeCasal;
    private Long pagamentoId;
    private String status;
    private double valor;
    private String slug;
    private String musica;
    private String planoSelecionado;
    private LocalDate dataCriacao;
    private String email;
    @Column(length = 1000)
    private String mensagem;
    private String autor;
    private LocalDate data;
    private String titulo;
    private String videoId;
    @ElementCollection
    @CollectionTable(name = "pagina_imagens", joinColumns = @JoinColumn(name = "pagina_id"))
    @Column(name = "imagem_url")
    private List<String> imagens;
    @ElementCollection
    @CollectionTable(name = "pagina_playlist", joinColumns = @JoinColumn(name = "pagina_id"))
    @Column(name = "musica_id")
    private List<String> playlist;
    @ElementCollection
    @CollectionTable(name = "pagina_albuns", joinColumns = @JoinColumn(name = "pagina_id"))
    private List<Album> album;
    public Pagina(PaginaRequestDto dto) {
        this.nomeCasal = dto.nomeCasal();
        this.musica = dto.musica();
        this.idParceiro = dto.idParceiro();
        this.planoSelecionado = dto.planoSelecionado();
        this.email = dto.email();
        this.mensagem = dto.mensagem();
        this.autor = dto.autor();
        this.data = dto.data();
        this.titulo = dto.titulo();
        this.videoId = dto.videoId();
        this.imagens = dto.imagens();
        this.playlist = dto.playlist();
        this.slug = gerarSlug(dto.nomeCasal());
        this.status = dto.status();
        this.pagamentoId = dto.pagamentoId();
        this.dataCriacao = LocalDate.now();
        this.valor = dto.valor();

        // Para o album, converte cada AlbumDto para Album
        if (dto.album() != null) {
            this.album = dto.album().stream()
                    .map(a -> {
                        Album alb = new Album();
                        alb.url = a.url();
                        alb.descricao = a.descricao();
                        alb.data = a.data();
                        return alb;
                    })
                    .toList();
        } else {
            this.album = List.of();
        }
    }
    public Pagina() {
    }
    public String gerarSlug(String nomeCasal) {
        String slug = Normalizer.normalize(nomeCasal, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-");
        return slug;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Album {
        private String url;
        private String descricao;
        private LocalDate data;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public LocalDate getData() {
            return data;
        }

        public void setData(LocalDate data) {
            this.data = data;
        }

    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public UUID getIdParceiro() {
        return idParceiro;
    }

    public void setIdParceiro(UUID idParceiro) {
        this.idParceiro = idParceiro;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPagamentoId() {
        return pagamentoId;
    }

    public void setPagamentoId(Long pagamentoId) {
        this.pagamentoId = pagamentoId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNomeCasal() {
        return nomeCasal;
    }

    public void setNomeCasal(String nomeCasal) {
        this.nomeCasal = nomeCasal;
    }

    public String getMusica() {
        return musica;
    }

    public void setMusica(String musica) {
        this.musica = musica;
    }

    public String getPlanoSelecionado() {
        return planoSelecionado;
    }

    public void setPlanoSelecionado(String planoSelecionado) {
        this.planoSelecionado = planoSelecionado;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public List<String> getImagens() {
        return imagens;
    }

    public void setImagens(List<String> imagens) {
        this.imagens = imagens;
    }

    public List<String> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<String> playlist) {
        this.playlist = playlist;
    }

    public List<Album> getAlbum() {
        return album;
    }

    public void setAlbum(List<Album> album) {
        this.album = album;
    }
}
