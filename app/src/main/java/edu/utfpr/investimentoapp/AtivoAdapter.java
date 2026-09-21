package edu.utfpr.investimentoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

public class AtivoAdapter extends ArrayAdapter<Ativo> {

    private static final int[] CORES = {
            R.color.cat_renda_fixa,
            R.color.cat_acoes,
            R.color.cat_fiis,
            R.color.cat_cripto,
            R.color.cat_internacional,
            R.color.cat_outros
    };

    private final String[] tiposRenda;
    private boolean mostrarValores = true;

    public AtivoAdapter(@NonNull Context context, @NonNull List<Ativo> ativos) {
        super(context, 0, ativos);
        tiposRenda = new String[]{
                context.getString(R.string.radio_renda_fixa),
                context.getString(R.string.radio_renda_variavel)
        };
    }

    public void setMostrarValores(boolean mostrarValores) {
        this.mostrarValores = mostrarValores;
    }

    public static int corCategoria(Context context, int categoria) {
        int i = (categoria >= 0 && categoria < CORES.length) ? categoria : CORES.length - 1;
        return ContextCompat.getColor(context, CORES[i]);
    }

    public static String nomeDaCategoria(Context context, int categoria) {
        String[] nomes = context.getResources().getStringArray(R.array.categorias);
        int i = (categoria >= 0 && categoria < nomes.length) ? categoria : nomes.length - 1;
        return nomes[i];
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ativo, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Ativo ativo = getItem(position);
        if (ativo == null) {
            return convertView;
        }

        holder.tvNome.setText(ativo.getNomeProduto());

        int tipo = ativo.getTipoRenda() == Ativo.RENDA_VARIAVEL ? 1 : 0;
        holder.tvInfo.setText(ativo.getInstituicao() + " · " + tiposRenda[tipo]);

        if (mostrarValores) {
            holder.tvValor.setText(Formato.moeda(ativo.getValorInicial()));
            holder.tvValor.setVisibility(View.VISIBLE);
        } else {
            holder.tvValor.setVisibility(View.GONE);
        }

        holder.viewDot.getBackground().mutate()
                .setTint(corCategoria(getContext(), ativo.getCategoria()));

        holder.ivFavorito.setVisibility(ativo.isFavorito() ? View.VISIBLE : View.GONE);

        return convertView;
    }

    private static class ViewHolder {
        final View viewDot;
        final TextView tvNome;
        final TextView tvInfo;
        final TextView tvValor;
        final ImageView ivFavorito;

        ViewHolder(View v) {
            viewDot = v.findViewById(R.id.view_categoria_dot);
            tvNome = v.findViewById(R.id.tv_nome_produto);
            tvInfo = v.findViewById(R.id.tv_instituicao_tipo);
            tvValor = v.findViewById(R.id.tv_valor);
            ivFavorito = v.findViewById(R.id.iv_favorito);
        }
    }
}
