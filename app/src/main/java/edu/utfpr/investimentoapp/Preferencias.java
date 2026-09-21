package edu.utfpr.investimentoapp;

import android.content.Context;
import android.content.SharedPreferences;

public final class Preferencias {
    public static final String ARQUIVO = "invst_prefs";

    public static final String ORDENACAO = "sort_order";
    public static final String APENAS_FAVORITOS = "apenas_favoritos";
    public static final String MOSTRAR_VALORES = "mostrar_valores";
    public static final String SEED_FEITO = "seed_feito";

    public static final String ORD_NOME = "nome";
    public static final String ORD_VALOR = "valor";
    public static final String ORD_CATEGORIA = "categoria";

    private Preferencias() {
    }

    public static SharedPreferences get(Context context) {
        return context.getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
    }

}
