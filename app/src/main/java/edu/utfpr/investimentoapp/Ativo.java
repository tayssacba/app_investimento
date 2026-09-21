package edu.utfpr.investimentoapp;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "ativos")
public class Ativo implements Serializable {

    public static final int RENDA_FIXA = 0;
    public static final int RENDA_VARIAVEL = 1;

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nomeProduto;
    private int categoria;
    private String instituicao;
    private int tipoRenda;
    private boolean favorito;
    private double valorInicial;
    private String anotacoes;

    public Ativo() {
    }

    @Ignore
    public Ativo(String nomeProduto, int categoria, String instituicao, int tipoRenda, boolean favorito, double valorInicial, String anotacoes) {
        this.nomeProduto = nomeProduto;
        this.categoria = categoria;
        this.instituicao = instituicao;
        this.tipoRenda = tipoRenda;
        this.favorito = favorito;
        this.valorInicial = valorInicial;
        this.anotacoes = anotacoes;
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

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public int getTipoRenda() {
        return tipoRenda;
    }

    public void setTipoRenda(int tipoRenda) {
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

    @Override
    public String toString() {
        return nomeProduto;
    }
}
