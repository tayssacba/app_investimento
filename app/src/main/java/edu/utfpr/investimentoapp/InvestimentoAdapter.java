package edu.utfpr.investimentoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class InvestimentoAdapter extends ArrayAdapter<Investimento> {
    public InvestimentoAdapter(Context context, ArrayList<Investimento> investimentos) {
        super(context, 0, investimentos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Investimento investimento = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_investimento, parent, false);
        }

        TextView tvNome = convertView.findViewById(R.id.tvNomeItem);
        TextView tvCategoria = convertView.findViewById(R.id.tvCategoriaItem);
        TextView tvRisco = convertView.findViewById(R.id.tvRiscoItem);
        TextView tvValor = convertView.findViewById(R.id.tvValorItem);

        tvNome.setText(investimento.getNome());
        tvCategoria.setText("Categoria: " + investimento.getCategoria());
        tvRisco.setText("Risco: " + investimento.getRisco());
        tvValor.setText("Mínimo: " + investimento.getValorMinimo());

        return convertView;
    }
}
