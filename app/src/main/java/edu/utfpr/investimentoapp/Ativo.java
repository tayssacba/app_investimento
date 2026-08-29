package edu.utfpr.investimentoapp;

import java.io.Serializable;

public class Ativo implements Serializable {
    private int id;
    private String nomeProduto;
    private String categoria;
    private double saldoAtual;
    private int idInstituicao;
    private String instituicao;
    private String tipoRenda;
    private boolean favorito;
    private double valorInicial;
    private String anotacoes;

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public String getTipoRenda() {
        return tipoRenda;
    }

    public void setTipoRenda(String tipoRenda) {
        this.tipoRenda = tipoRenda;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public double getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(double valorInicial) {
        this.valorInicial = valorInicial;
    }

    public String getAnotacoes() {
        return anotacoes;
    }

    public void setAnotacoes(String anotacoes) {
        this.anotacoes = anotacoes;
    }

    public Ativo() {
    }

    public Ativo(int id, String nomeProduto, String categoria, double saldoAtual, int idInstituicao) {
        this.id = id;
        this.nomeProduto = nomeProduto;
        this.categoria = categoria;
        this.saldoAtual = saldoAtual;
        this.idInstituicao = idInstituicao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getSaldoAtual() {
        return saldoAtual;
    }

    public void setSaldoAtual(double saldoAtual) {
        this.saldoAtual = saldoAtual;
    }

    public int getIdInstituicao() {
        return idInstituicao;
    }

    public void setIdInstituicao(int idInstituicao) {
        this.idInstituicao = idInstituicao;
    }

    @Override
    public String toString() {
        return nomeProduto;
    }
}
