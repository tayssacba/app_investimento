package edu.utfpr.investimentoapp;

public class Investimento {
    private String nome;
    private String categoria;
    private String risco;
    private String valorMinimo;

    public Investimento(String nome, String categoria, String risco, String valorMinimo) {
        this.nome = nome;
        this.categoria = categoria;
        this.risco = risco;
        this.valorMinimo = valorMinimo;
    }

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getRisco() {
        return risco;
    }

    public String getValorMinimo() {
        return valorMinimo;
    }
}
