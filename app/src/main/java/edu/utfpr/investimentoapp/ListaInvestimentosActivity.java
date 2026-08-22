package edu.utfpr.investimentoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListaInvestimentosActivity extends AppCompatActivity {
    private ArrayList<Investimento> listaInvestimentos;
    private InvestimentoAdapter adapter;

    private final ActivityResultLauncher<Intent> launcherNovoInvestimento = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();

                    String nome = data.getStringExtra("nome");
                    String categoria = data.getStringExtra("categoria");
                    String risco = data.getStringExtra("risco");
                    String valor = data.getStringExtra("valorMinimo");

                    listaInvestimentos.add(new Investimento(nome, categoria, risco, valor));

                    adapter.notifyDataSetChanged();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_investimentos);

        listaInvestimentos = new ArrayList<>();

        ListView listView = findViewById(R.id.lvInvestimentos);
        adapter = new InvestimentoAdapter(this, listaInvestimentos);
        listView.setAdapter(adapter);

        Button btnAdicionar = findViewById(R.id.btnAdicionar);
        Button btnSobre = findViewById(R.id.btnSobreLista);

        btnAdicionar.setOnClickListener(v -> {

            Intent intent = new Intent(this, CadastroActivity.class);
            launcherNovoInvestimento.launch(intent);
        });

        btnSobre.setOnClickListener(v -> {
            startActivity(new Intent(this, AutoriaActivity.class));
        });
    }
}