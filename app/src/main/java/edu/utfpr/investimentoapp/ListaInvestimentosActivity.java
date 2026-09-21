package edu.utfpr.investimentoapp;

import static androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class ListaInvestimentosActivity extends AppCompatActivity {

    private AppDatabase db;
    private AtivoAdapter adapter;
    private final List<Ativo> ativos = new ArrayList<>();

    private ListView listView;
    private TextView tvPatrimonio;
    private TextView tvQtd;
    private View blocoGrafico;
    private GraficoCategoriasView grafico;
    private GridLayout gridLegenda;

    @SuppressWarnings("deprecation")
    private final ActivityResultLauncher<Intent> cadastroLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    Ativo ativo = (Ativo) data.getSerializableExtra(CadastroActivity.EXTRA_ATIVO);
                    int modo = data.getIntExtra(CadastroActivity.EXTRA_MODO, CadastroActivity.MODO_INSERIR);

                    if (ativo == null) {
                        return;
                    }

                    if (modo == CadastroActivity.MODO_EDITAR) {
                        db.ativoDAO().atualizar(ativo);
                    } else {
                        db.ativoDAO().inserir(ativo);
                    }
                    recarregarLista();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInsatnceState) {
        super.onCreate(savedInsatnceState);
        setContentView(R.layout.activity_lista_investimentos);
        setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setTitle(getString(R.string.title_listagem));

        db = AppDatabase.getInstance(this);
        carregarDadosIniciais();

        listView = findViewById(R.id.list_view_ativos);
        tvPatrimonio = findViewById(R.id.tv_patrimonio);
        tvQtd = findViewById(R.id.tv_qtd);
        blocoGrafico = findViewById(R.id.bloco_grafico);
        grafico = findViewById(R.id.grafico);
        gridLegenda = findViewById(R.id.grid_legenda);

        adapter = new AtivoAdapter(this, ativos);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.tv_vazio));

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Ativo a = ativos.get(position);
            String info = a.getNomeProduto()
                    + " · " + AtivoAdapter.nomeDaCategoria(this, a.getCategoria())
                    + " · " + Formato.moeda(a.getValorInicial());
            Log.d("INVESTIMENTO", "Item clicado");
            Toast.makeText(getApplicationContext(), getString(R.string.toast_item_clicado, info),
                    Toast.LENGTH_SHORT).show();
        });

        configurarMenuContextual();
        recarregarLista();
    }

    @Override
    protected void onResume() {
        super.onResume();
        recarregarLista();
    }

    private void carregarDadosIniciais() {
        SharedPreferences prefs = Preferencias.get(this);
        if (prefs.getBoolean(Preferencias.SEED_FEITO, false)) {
            return;
        }
        if (db.ativoDAO().contar() == 0) {
            String[] nomes = getResources().getStringArray(R.array.ativos_nomes);
            int[] categorias = getResources().getIntArray(R.array.ativos_categorias);
            String[] instituicoes = getResources().getStringArray(R.array.ativos_instituicoes);
            int[] tipos = getResources().getIntArray(R.array.ativos_tipos_renda);
            String[] valores = getResources().getStringArray(R.array.ativos_valores);
            String[] anotacoes = getResources().getStringArray(R.array.ativos_anotacoes);
            String[] favoritos = getResources().getStringArray(R.array.ativos_favoritos);

            for (int i = 0; i < nomes.length; i++) {
                db.ativoDAO().inserir(new Ativo(
                        nomes[i], categorias[i], instituicoes[i], tipos[i],
                        Boolean.parseBoolean(favoritos[i]),
                        Double.parseDouble(valores[i]),
                        anotacoes[i]));
            }
        }
        // Grava a flag: se o usuário apagar tudo, os dados de exemplo não voltam
        prefs.edit().putBoolean(Preferencias.SEED_FEITO, true).apply();
    }

    private void recarregarLista() {
        SharedPreferences prefs = Preferencias.get(this);
        String ordem = prefs.getString(Preferencias.ORDENACAO, Preferencias.ORD_NOME);
        int apenasFavoritos = prefs.getBoolean(Preferencias.APENAS_FAVORITOS, false) ? 1 : 0;
        boolean mostrarValores = prefs.getBoolean(Preferencias.MOSTRAR_VALORES, true);

        List<Ativo> carregados;
        if (Preferencias.ORD_VALOR.equals(ordem)) {
            carregados = db.ativoDAO().listarPorValor(apenasFavoritos);
        } else if (Preferencias.ORD_CATEGORIA.equals(ordem)) {
            carregados = db.ativoDAO().listarPorCategoria(apenasFavoritos);
        } else {
            carregados = db.ativoDAO().listarPorNome(apenasFavoritos);
        }

        ativos.clear();
        ativos.addAll(carregados);
        adapter.setMostrarValores(mostrarValores);
        adapter.notifyDataSetChanged();

        tvQtd.setText(getResources().getQuantityString(
                R.plurals.qtd_ativos, ativos.size(), ativos.size()));
        tvQtd.setVisibility(ativos.isEmpty() ? View.GONE : View.VISIBLE);

        atualizarResumo();
    }

    @SuppressLint("MissingInflatedId")
    private void atualizarResumo() {
        List<Ativo> todos = db.ativoDAO().listarPorNome(0);
        String[] nomesCategorias = getResources().getStringArray(R.array.categorias);
        double[] porCategoria = new double[nomesCategorias.length];
        int[] cores = new int[nomesCategorias.length];

        double total = 0;
        for (Ativo a : todos) {
            total += a.getValorInicial();
            int c = a.getCategoria();
            if (c >= 0 && c < porCategoria.length) {
                porCategoria[c] += a.getValorInicial();
            }
        }
        tvPatrimonio.setText(Formato.moeda(total));

        gridLegenda.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < porCategoria.length; i++) {
            cores[i] = AtivoAdapter.corCategoria(this, i);
            if (porCategoria[i] > 0) {
                View item = inflater.inflate(R.layout.item_legenda, gridLegenda, false);
                item.findViewById(R.id.view_dot_legenda).getBackground().mutate()
                        .setTint(cores[i]);
                ((TextView) item.findViewById(R.id.tv_legenda)).setText(nomesCategorias[i]);
                gridLegenda.addView(item);
            }
        }
        grafico.setDados(porCategoria, cores);
        blocoGrafico.setVisibility(total > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_listagem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_adicionar) {
            abrirCadastro(null);
            return true;
        } else if (id == R.id.menu_configuracoes) {
            startActivity(new Intent(this, ConfiguracoesActivity.class));
            return true;
        } else if (id == R.id.menu_sobre) {
            startActivity(new Intent(this, AutoriaActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void abrirCadastro(Ativo ativo) {
        Intent intent = new Intent(this, CadastroActivity.class);
        if (ativo != null) {
            intent.putExtra(CadastroActivity.EXTRA_ATIVO, ativo);
            intent.putExtra(CadastroActivity.EXTRA_MODO, CadastroActivity.MODO_EDITAR);
        } else {
            intent.putExtra(CadastroActivity.EXTRA_MODO, CadastroActivity.MODO_INSERIR);
        }
        cadastroLauncher.launch(intent);
    }

    private void configurarMenuContextual() {
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id,
                                                  boolean checked) {
                int qtd = listView.getCheckedItemCount();
                mode.setTitle(getResources().getQuantityString(R.plurals.selecionados, qtd, qtd));
                mode.invalidate(); // reavalia se "Editar" aparece
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_contextual, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                MenuItem editar = menu.findItem(R.id.ctx_editar);
                if (editar != null) {
                    editar.setVisible(listView.getCheckedItemCount() == 1);
                }
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                List<Ativo> selecionados = obterSelecionados();
                if (item.getItemId() == R.id.ctx_editar) {
                    if (selecionados.size() == 1) {
                        abrirCadastro(selecionados.get(0));
                    }
                    mode.finish();
                    return true;
                } else if (item.getItemId() == R.id.ctx_excluir) {
                    if (!selecionados.isEmpty()) {
                        confirmarExclusao(selecionados, mode);
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        });
    }

    private List<Ativo> obterSelecionados() {
        List<Ativo> selecionados = new ArrayList<>();
        SparseBooleanArray marcados = listView.getCheckedItemPositions();
        for (int i = 0; i < marcados.size(); i++) {
            int posicao = marcados.keyAt(i);
            if (marcados.valueAt(i) && posicao < ativos.size()) {
                selecionados.add(ativos.get(posicao));
            }
        }
        return selecionados;
    }

    // AlertDialog: só exclui do banco se o usuário confirmar
    private void confirmarExclusao(List<Ativo> selecionados, ActionMode mode) {
        int qtd = selecionados.size();
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.confirm_excluir_titulo)
                .setMessage(getResources().getQuantityString(
                        R.plurals.confirm_excluir_msg, qtd, qtd))
                .setPositiveButton(R.string.confirm_sim, (dialog, which) -> {
                    db.ativoDAO().excluirVarios(selecionados);
                    mode.finish();
                    recarregarLista();
                    Toast.makeText(getApplicationContext(), getResources().getQuantityString(
                            R.plurals.toast_excluido, qtd, qtd), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.confirm_nao, null)
                .show();
    }
}