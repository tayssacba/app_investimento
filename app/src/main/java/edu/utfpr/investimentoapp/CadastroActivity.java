package edu.utfpr.investimentoapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CadastroActivity extends AppCompatActivity {

    public static final String EXTRA_ATIVO = "extra_ativo";
    public static final String EXTRA_MODO = "extra_modo";
    public static final int MODO_INSERIR = 0;
    public static final int MODO_EDITAR = 1;

    private EditText etNomeProduto;
    private EditText etValorInicial;
    private Spinner spinnerCategoria;
    private Spinner spinnerInstituicao;
    private RadioGroup rgTipoRenda;
    private RadioButton rbRendaFixa;
    private RadioButton rbRendaVariavel;
    private CheckBox cbFavorito;
    private EditText etAnotacoes;

    private Ativo ativoEmEdicao = null;
    private int modo = MODO_INSERIR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        bindViews();
        configurarSpinners();

        modo = getIntent().getIntExtra(EXTRA_MODO, MODO_INSERIR);
        if (modo == MODO_EDITAR) {
            ativoEmEdicao = (Ativo) getIntent().getSerializableExtra(EXTRA_ATIVO);
            setTitle(getString(R.string.title_editar));
            preencherFormulario(ativoEmEdicao);
        } else {
            setTitle(getString(R.string.title_cadastro));
        }

        findViewById(R.id.btn_limpar).setOnClickListener(v -> limparFormulario());
        findViewById(R.id.btn_salvar).setOnClickListener(v -> salvar());
    }

    // ── Options Menu ──────────────────────────────────────────────────────────
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        } else if (id == R.id.menu_salvar) {
            salvar();
            return true;
        } else if (id == R.id.menu_limpar) {
            limparFormulario();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ── Lógica do formulário ──────────────────────────────────────────────────
    private void salvar() {
        String nome = etNomeProduto.getText().toString().trim();
        if (TextUtils.isEmpty(nome)) {
            Toast.makeText(getApplicationContext(), R.string.erro_nome_obrigatorio, Toast.LENGTH_SHORT).show();
            etNomeProduto.requestFocus();
            return;
        }

        String valorStr = etValorInicial.getText().toString().replace(",", ".").trim();
        double valor;
        try {
            valor = Double.parseDouble(valorStr);
            if (valor <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), R.string.erro_valor_invalido, Toast.LENGTH_SHORT).show();
            etValorInicial.requestFocus();
            return;
        }

        if (rgTipoRenda.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), R.string.erro_tipo_renda, Toast.LENGTH_SHORT).show();
            return;
        }

        Ativo ativo = (ativoEmEdicao != null) ? ativoEmEdicao : new Ativo();
        ativo.setNomeProduto(nome);
        ativo.setCategoria(spinnerCategoria.getSelectedItemPosition());
        ativo.setInstituicao(spinnerInstituicao.getSelectedItem().toString());
        ativo.setTipoRenda(rbRendaFixa.isChecked() ? Ativo.RENDA_FIXA : Ativo.RENDA_VARIAVEL);
        ativo.setFavorito(cbFavorito.isChecked());
        ativo.setValorInicial(valor);
        ativo.setAnotacoes(etAnotacoes.getText().toString().trim());

        Intent result = new Intent();
        result.putExtra(EXTRA_ATIVO, ativo);
        result.putExtra(EXTRA_MODO, modo);
        setResult(RESULT_OK, result);

        Toast.makeText(getApplicationContext(),
                modo == MODO_EDITAR ? R.string.toast_atualizado : R.string.toast_salvo,
                Toast.LENGTH_SHORT).show();
        finish();
    }

    private void limparFormulario() {
        etNomeProduto.setText("");
        etValorInicial.setText("");
        etAnotacoes.setText("");
        rgTipoRenda.clearCheck();
        cbFavorito.setChecked(false);
        spinnerCategoria.setSelection(0);
        spinnerInstituicao.setSelection(0);
        etNomeProduto.requestFocus();

        Toast.makeText(getApplicationContext(), R.string.toast_limpar, Toast.LENGTH_SHORT).show();
    }

    private void preencherFormulario(Ativo a) {
        if (a == null) return;
        etNomeProduto.setText(a.getNomeProduto());
        etValorInicial.setText(String.valueOf(a.getValorInicial()));
        etAnotacoes.setText(a.getAnotacoes());
        cbFavorito.setChecked(a.isFavorito());

        if (a.getCategoria() >= 0 && a.getCategoria() < spinnerCategoria.getCount()) {
            spinnerCategoria.setSelection(a.getCategoria());
        }

        String[] insts = getResources().getStringArray(R.array.instituicoes);
        for (int i = 0; i < insts.length; i++) {
            if (insts[i].equals(a.getInstituicao())) {
                spinnerInstituicao.setSelection(i);
                break;
            }
        }

        if (a.getTipoRenda() == Ativo.RENDA_FIXA) {
            rbRendaFixa.setChecked(true);
        } else {
            rbRendaVariavel.setChecked(true);
        }
    }

    // ── Setup ─────────────────────────────────────────────────────────────────
    private void bindViews() {
        etNomeProduto = findViewById(R.id.et_nome_produto);
        etValorInicial = findViewById(R.id.et_valor_inicial);
        spinnerCategoria = findViewById(R.id.spinner_categoria);
        spinnerInstituicao = findViewById(R.id.spinner_instituicao);
        rgTipoRenda = findViewById(R.id.rg_tipo_renda);
        rbRendaFixa = findViewById(R.id.rb_renda_fixa);
        rbRendaVariavel = findViewById(R.id.rb_renda_variavel);
        cbFavorito = findViewById(R.id.cb_favorito);
        etAnotacoes = findViewById(R.id.et_anotacoes);
    }

    private void configurarSpinners() {
        ArrayAdapter<CharSequence> adCat = ArrayAdapter.createFromResource(
                this, R.array.categorias, android.R.layout.simple_spinner_item);
        adCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adCat);

        ArrayAdapter<CharSequence> adInst = ArrayAdapter.createFromResource(
                this, R.array.instituicoes, android.R.layout.simple_spinner_item);
        adInst.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInstituicao.setAdapter(adInst);
    }
}
