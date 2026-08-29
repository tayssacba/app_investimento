package edu.utfpr.investimentoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import edu.utfpr.investimentoapp.databinding.ActivityCadastroBinding;

public class CadastroActivity extends AppCompatActivity {

    private ActivityCadastroBinding binding;
    private int posicaoEdicao = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        configurarSpinner();
        verificarModoEdicao();
    }

    private void verificarModoEdicao() {
        Intent it = getIntent();
        if (it.hasExtra("POSICAO")) {
            posicaoEdicao = it.getIntExtra("POSICAO", -1);
            setTitle("Editar Investimento");
        }

        String valorString = it.getStringExtra("VALOR");
        if (valorString != null) {
            binding.etValor.setText(valorString.replace("R$ ", ""));
        }

        String categoria = it.getStringExtra("CATEGORIA");
        if (categoria != null) {
            if (categoria.contains("Aporte")) {
                binding.rgTipoMovimentacao.check(R.id.rbAporte);
            } else if (categoria.contains("Resgate")) {
                binding.rgTipoMovimentacao.check(R.id.rbResgate);
            }
        }

        String risco = it.getStringExtra("RISCO");
        if (risco != null && risco.contains("Ajuste Contábil")) {
            binding.cbAjusteRendimento.setChecked(true);
        }

        String nomeAtivo = it.getStringExtra("NOME");
        if (nomeAtivo != null) {
            for (int i = 0; i < binding.spnAtivo.getCount(); i++) {
                if (binding.spnAtivo.getItemAtPosition(i).toString().equals(nomeAtivo)) {
                    binding.spnAtivo.setSelection(i);
                    break;
                }
            }
        } else {
            setTitle("Registrar Movimentação");
        }
    }

    private void configurarSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.n_ativos,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnAtivo.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cadastro_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_salvar) {
            processarCadastro();
            return true;
        } else if (id == R.id.menu_limpar) {
            limparFormulario();
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void processarCadastro() {
        String valorDigitado = binding.etValor.getText().toString().trim();

        if (valorDigitado.isEmpty()) {
            binding.tilValor.setError(getString(R.string.erro_valor_vazio));
            return;
        }

        try {
            double valor = Double.parseDouble(valorDigitado);
            if (valor <= 0) {
                binding.tilValor.setError(getString(R.string.erro_valor_invalido));
                return;
            }
        } catch (NumberFormatException e) {
            binding.tilValor.setError(getString(R.string.erro_valor_invalido));
            return;
        }

        binding.tilValor.setError(null);

        String ativo = binding.spnAtivo.getSelectedItem().toString();
        int idRadioSelecionado = binding.rgTipoMovimentacao.getCheckedRadioButtonId();
        String tipoMovimentacao = (idRadioSelecionado == R.id.rbAporte) ?
                getString(R.string.label_aporte) : getString(R.string.label_resgate);
        boolean isAjuste = binding.cbAjusteRendimento.isChecked();

        String riscoFormatado = isAjuste ? "Ajuste Contábil" : "Risco Padrão";
        String valorFormatado = "R$ " + valorDigitado;

        Intent intentRetorno = new Intent();
        intentRetorno.putExtra("NOME", ativo);
        intentRetorno.putExtra("CATEGORIA", tipoMovimentacao);
        intentRetorno.putExtra("RISCO", riscoFormatado);
        intentRetorno.putExtra("VALOR", valorFormatado);
        intentRetorno.putExtra("POSICAO", posicaoEdicao);

        setResult(RESULT_OK, intentRetorno);
        finish();
    }

    private void limparFormulario() {
        binding.etValor.setText("");
        binding.tilValor.setError(null);
        binding.rgTipoMovimentacao.check(R.id.rbAporte);
        binding.cbAjusteRendimento.setChecked(false);
        binding.spnAtivo.setSelection(0);
        Toast.makeText(getApplicationContext(), R.string.msg_sucesso, Toast.LENGTH_SHORT).show();
    }
}
