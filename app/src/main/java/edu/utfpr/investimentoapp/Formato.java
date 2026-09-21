package edu.utfpr.investimentoapp;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public final class Formato {

    private Formato() {
    }

    public static String moeda(double valor) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        nf.setCurrency(Currency.getInstance("BRL"));
        return nf.format(valor);
    }
}
