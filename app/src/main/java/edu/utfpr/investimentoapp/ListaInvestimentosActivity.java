package edu.utfpr.investimentoapp;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListaInvestimentosActivity extends AppCompatActivity {
    private ArrayList<Investimento> listaInvestimentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_investimentos);

        carregarDados();

        ListView listView = findViewById(R.id.lvInvestimentos);
        InvestimentoAdapter adapter = new InvestimentoAdapter(this, listaInvestimentos);
        listView.setAdapter(adapter);
    }

    private void carregarDados() {
        listaInvestimentos = new ArrayList<>();

        String[] nomes = getResources().getStringArray(R.array.n_ativos);
        String[] categorias = getResources().getStringArray(R.array.c_ativos);
        String[] riscos = getResources().getStringArray(R.array.r_ativos);
        String[] valores = getResources().getStringArray(R.array.v_minimos);

        for (int i = 0; i < nomes.length; i++) {
            Investimento inv = new Investimento(nomes[i], categorias[i], riscos[i], valores[i]);
            listaInvestimentos.add(inv);
        }
    }
}
