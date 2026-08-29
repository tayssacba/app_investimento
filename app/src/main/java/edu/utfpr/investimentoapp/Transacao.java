package edu.utfpr.investimentoapp;

public class Transacao {
    private int id;
    private String dataOperacao;
    private String tipoMovimentacao;
    private double valor;
    private int idAtivo;

    public Transacao() {
    }

    public Transacao(int id, String dataOperacao, String tipoMovimentacao, double valor, int idAtivo) {
        this.id = id;
        this.dataOperacao = dataOperacao;
        this.tipoMovimentacao = tipoMovimentacao;
        this.valor = valor;
        this.idAtivo = idAtivo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataOperacao() {
        return dataOperacao;
    }

    public void setDataOperacao(String dataOperacao) {
        this.dataOperacao = dataOperacao;
    }

    public String getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public void setTipoMovimentacao(String tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getIdAtivo() {
        return idAtivo;
    }

    public void setIdAtivo(int idAtivo) {
        this.idAtivo = idAtivo;
    }

}
