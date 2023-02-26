package com.example.dam2.m08.themoviedb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.RequestQueue;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Pelicula_info extends AppCompatActivity {

    private ImageView ivPortada_foto;
    TextView piTitulo, piFecha_estreno, piPopularidad, piSinopsis;


    List<Pelicula> listaPelicula= MainActivity.listaPeliculas;
    ImageButton btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pelicula_info);

        piTitulo = findViewById(R.id.piTitulo);
        piFecha_estreno = findViewById(R.id.piFecha_estreno);
        piPopularidad = findViewById(R.id.piPopularidad);
        piSinopsis = findViewById(R.id.piSinopsis);
        ivPortada_foto = findViewById(R.id.piPortada_pelicula);

        Pelicula pelicula = getIntent().getParcelableExtra("pelicula");

        muestraPelicula(pelicula);


        btn = findViewById(R.id.boton_atras);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Pelicula_info.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void muestraPelicula(Pelicula pelicula){
        piTitulo.setText(pelicula.getTitle());
        piFecha_estreno.setText("Fecha de Estreno: " +pelicula.getRelease_date());
        piPopularidad.setText("Popularidad:" + pelicula.getVote_average()+"%");
        piSinopsis.setText(pelicula.getOverview());

        Picasso.get().load(pelicula.getPoster_path()).into(ivPortada_foto);
    }
}
