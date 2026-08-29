package edu.utfpr.investimentoapp;

public class Instituicao {
    private int id;
    private String nomeBancoCorretora;

    public Instituicao() {
    }

    public Instituicao(int id, String nomeBancoCorretora) {
        this.id = id;
        this.nomeBancoCorretora = nomeBancoCorretora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeBancoCorretora() {
        return nomeBancoCorretora;
    }

    public void setNomeBancoCorretora(String nomeBancoCorretora) {
        this.nomeBancoCorretora = nomeBancoCorretora;
    }

    @Override
    public String toString() {
        return nomeBancoCorretora;
    }
}
