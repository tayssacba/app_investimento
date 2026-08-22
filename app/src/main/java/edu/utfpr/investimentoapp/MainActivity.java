package edu.utfpr.investimentoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_HOME = "home";
    private static final String TAG_CORRETORAS = "corretoras";
    private static final String TAG_ATIVOS = "ativos";
    private static final String TAG_TRANSACOES = "transacoes";
    private static final String TAG_SOBRE = "sobre";

    private android.widget.TextView tvPageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvPageTitle = findViewById(R.id.tv_page_title);
        BottomNavigationView nav = findViewById(R.id.bottom_nav);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), TAG_HOME, "Carteira");
        }

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                loadFragment(new HomeFragment(), TAG_HOME, "Carteira");
            } else if (id == R.id.nav_corretoras) {
                loadFragment(new CorretorasFragment(), TAG_CORRETORAS, "Corretoras");
            } else if (id == R.id.nav_ativos) {
                loadFragment(new AtivosFragment(), TAG_ATIVOS, "Ativos");
            } else if (id == R.id.nav_transacoes) {
                loadFragment(new TransacoesFragment(), TAG_TRANSACOES, "Movimentações");
            } else if(id ==R.id.nav_sobre) {
                loadFragment(new AutoriaFragment(), TAG_SOBRE, "Sobre");
            }
            return true;
        });

        Button btnSobre = findViewById(R.id.btn_sobre);
        if (btnSobre != null) {
            btnSobre.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AutoriaActivity.class);
                startActivity(intent);
            });
        }
    }

    private void loadFragment(Fragment fragment, String tag, String title) {
        tvPageTitle.setText(title);
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, tag)
                    .commit();
        }
    }
}