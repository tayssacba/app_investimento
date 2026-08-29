package edu.utfpr.investimentoapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConfiguracoesActivity extends AppCompatActivity {

    private static final String PREF_NOME = "invst_prefs";
    private static final String PREF_SORT = "sort_order";
    private static final String PREF_FAV = "apenas_favoritos";
    private static final String PREF_VALS = "mostrar_valores";

    private RadioGroup rgOrdenacao;
    private CheckBox cbApenasFavoritos;
    private CheckBox cbMostrarValores;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        setTitle(getString(R.string.title_configuracoes));

        // Botão Up
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        prefs = getSharedPreferences(PREF_NOME, MODE_PRIVATE);

        rgOrdenacao = findViewById(R.id.rg_ordenacao);
        cbApenasFavoritos = findViewById(R.id.cb_apenas_favoritos);
        cbMostrarValores = findViewById(R.id.cb_mostrar_valores);

        // Restaura preferências salvas
        String sort = prefs.getString(PREF_SORT, "nome");
        switch (sort) {
            case "valor":
                rgOrdenacao.check(R.id.rb_ordenar_valor);
                break;
            case "categoria":
                rgOrdenacao.check(R.id.rb_ordenar_categoria);
                break;
            default:
                rgOrdenacao.check(R.id.rb_ordenar_nome);
                break;
        }
        cbApenasFavoritos.setChecked(prefs.getBoolean(PREF_FAV, false));
        cbMostrarValores.setChecked(prefs.getBoolean(PREF_VALS, true));

        // Listeners — persiste mudanças via SharedPreferences imediatamente
        rgOrdenacao.setOnCheckedChangeListener((group, checkedId) -> {
            String novo;
            if (checkedId == R.id.rb_ordenar_valor) novo = "valor";
            else if (checkedId == R.id.rb_ordenar_categoria) novo = "categoria";
            else novo = "nome";
            prefs.edit().putString(PREF_SORT, novo).apply();
            Toast.makeText(getApplicationContext(), R.string.config_salva, Toast.LENGTH_SHORT).show();
        });

        cbApenasFavoritos.setOnCheckedChangeListener((btn, checked) -> {
            prefs.edit().putBoolean(PREF_FAV, checked).apply();
            Toast.makeText(getApplicationContext(), R.string.config_salva, Toast.LENGTH_SHORT).show();
        });

        cbMostrarValores.setOnCheckedChangeListener((btn, checked) -> {
            prefs.edit().putBoolean(PREF_VALS, checked).apply();
            Toast.makeText(getApplicationContext(), R.string.config_salva, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}