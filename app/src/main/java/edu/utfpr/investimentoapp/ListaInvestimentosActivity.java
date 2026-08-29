package edu.utfpr.investimentoapp;

import static androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListaInvestimentosActivity extends AppCompatActivity {
    private ArrayList<Investimento> listaInvestimentos;
    private InvestimentoAdapter adapter;
    private ActivityResultLauncher<Intent> cadastroLauncher;
    private RadioGroup rgOrdenacao;
    private CheckBox cbApenasFavoritos;
    private CheckBox cbMostrarValores;

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
        setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setTitle(getString(R.string.title_listagem));

        listaInvestimentos = new ArrayList<>();
        ListView listView = findViewById(R.id.lvInvestimentos);
        adapter = new InvestimentoAdapter(this, listaInvestimentos);
        listView.setAdapter(adapter);

        android.widget.TextView tvVazio = findViewById(R.id.tvVazio);
        listView.setEmptyView(tvVazio);

        configurarLauncher();
        configurarMenuContextual(listView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        aplicarConfiguracoes();
    }

    private void aplicarConfiguracoes() {
        if (listaInvestimentos == null || listaInvestimentos.isEmpty()) return;

        SharedPreferences prefs = getSharedPreferences("invst_prefs", MODE_PRIVATE);
        String sort = prefs.getString("sort_order", "nome");

        java.util.Collections.sort(listaInvestimentos, (a, b) -> {
            if ("valor".equals(sort)) {
                double valA = 0.0;
                double valB = 0.0;
                try {
                    valA = Double.parseDouble(a.getValorMinimo().replace("R$ ", "").replace(".", "").replace(",", "."));
                    valB = Double.parseDouble(b.getValorMinimo().replace("R$ ", "").replace(".", "").replace(",", "."));
                } catch (Exception ignored) {
                }

                return Double.compare(valB, valA);

            } else if ("categoria".equals(sort)) {
                return a.getCategoria().compareToIgnoreCase(b.getCategoria());
            } else {
                return a.getNome().compareToIgnoreCase(b.getNome());
            }
        });

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_adicionar) {
            Intent intent = new Intent(this, CadastroActivity.class);
            cadastroLauncher.launch(intent);
            return true;
        } else if (item.getItemId() == R.id.menu_sobre) {
            startActivity(new Intent(this, AutoriaActivity.class));
            return true;
        } else if (item.getItemId() == R.id.menu_configuracoes) {
            startActivity(new Intent(this, ConfiguracoesActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void configurarLauncher() {
        cadastroLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        String nome = data.getStringExtra("NOME");
                        String categoria = data.getStringExtra("CATEGORIA");
                        String risco = data.getStringExtra("RISCO");
                        String valor = data.getStringExtra("VALOR");
                        int posicao = data.getIntExtra("POSICAO", -1);

                        Investimento inv = new Investimento(nome, categoria, risco, valor);

                        if (posicao == -1) {
                            // É uma inclusão
                            listaInvestimentos.add(inv);
                        } else {
                            // É uma edição
                            listaInvestimentos.set(posicao, inv);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
        );
    }

    private void configurarMenuContextual(final ListView listView) {
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            private int posicaoSelecionada;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Guarda a posição do item que foi segurado
                if (checked) {
                    posicaoSelecionada = position;
                    mode.setTitle(getString(R.string.toast_item_clicado));
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.list_context_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.menu_editar) {
                    Investimento invEdit = listaInvestimentos.get(posicaoSelecionada);
                    Intent intent = new Intent(ListaInvestimentosActivity.this, CadastroActivity.class);
                    intent.putExtra("NOME", invEdit.getNome());
                    intent.putExtra("CATEGORIA", invEdit.getCategoria());
                    intent.putExtra("RISCO", invEdit.getRisco());
                    intent.putExtra("VALOR", invEdit.getValorMinimo());
                    intent.putExtra("POSICAO", posicaoSelecionada);

                    cadastroLauncher.launch(intent);
                    mode.finish();
                    return true;

                } else if (item.getItemId() == R.id.menu_excluir) {
                    int pos = posicaoSelecionada;
                    listView.clearChoices();
                    listaInvestimentos.remove(pos);
                    adapter.notifyDataSetChanged();
                    mode.finish();

                    Toast.makeText(getApplicationContext(), getString(R.string.toast_excluido), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        });
    }
}