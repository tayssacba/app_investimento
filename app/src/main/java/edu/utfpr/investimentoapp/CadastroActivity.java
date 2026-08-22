package edu.utfpr.investimentoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.utfpr.investimentoapp.databinding.ActivityCadastroBinding;

public class CadastroActivity extends AppCompatActivity {

    private ActivityCadastroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configurarSpinner();
        configurarListeners();
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

    private void configurarListeners() {
        binding.btnSalvar.setOnClickListener(v -> processarCadastro());
        binding.btnLimpar.setOnClickListener(v -> limparFormulario());
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

        Intent intentRetorno = new Intent();

        intentRetorno.putExtra("nome", ativo);
        intentRetorno.putExtra("categoria", tipoMovimentacao);
        intentRetorno.putExtra("risco", isAjuste ? "Ajuste" : "Padrão");
        intentRetorno.putExtra("valorMinimo", "R$ " + valorDigitado);

        setResult(RESULT_OK, intentRetorno);
        finish();
    }

    private void limparFormulario() {
        binding.etValor.setText("");
        binding.tilValor.setError(null);
        binding.rgTipoMovimentacao.check(R.id.rbAporte);
        binding.cbAjusteRendimento.setChecked(false);
        binding.spnAtivo.setSelection(0);
        Toast.makeText(this, R.string.msg_sucesso, Toast.LENGTH_SHORT).show();
    }
}
