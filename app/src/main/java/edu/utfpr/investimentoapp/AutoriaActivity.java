package edu.utfpr.investimentoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class AutoriaActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autoria);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sobre");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        LinearLayout llLinkedin = findViewById(R.id.ll_linkedin);
        llLinkedin.setOnClickListener(v -> {
            String url = "https://www.linkedin.com/in/tayssa-asakawa/";

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
