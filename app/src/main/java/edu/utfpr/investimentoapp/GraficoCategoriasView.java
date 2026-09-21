package edu.utfpr.investimentoapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class GraficoCategoriasView extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF area = new RectF();
    private double[] valores = new double[0];
    private int[] cores = new int[0];

    public GraficoCategoriasView(Context context) {
        super(context);
        init();
    }

    public GraficoCategoriasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.STROKE);
    }

    public void setDados(double[] valores, int[] cores) {
        this.valores = valores;
        this.cores = cores;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        double total = 0;
        for (double v : valores) {
            total += v;
        }
        if (total <= 0) {
            return;
        }

        float lado = Math.min(getWidth(), getHeight());
        float espessura = lado * 0.18f;
        float margem = espessura / 2f;
        area.set(margem, margem, lado - margem, lado - margem);
        paint.setStrokeWidth(espessura);

        float inicio = -90f;
        for (int i = 0; i < valores.length && i < cores.length; i++) {
            if (valores[i] <= 0) {
                continue;
            }
            float varredura = (float) (valores[i] / total * 360.0);
            paint.setColor(cores[i]);
            canvas.drawArc(area, inicio, varredura, false, paint);
            inicio += varredura;
        }
    }
}
