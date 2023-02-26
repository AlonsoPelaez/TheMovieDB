package com.example.dam2.m08.themoviedb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private RecyclerView rvpeliculas;
    private RequestQueue requestQueue;
    public static final String urlbase ="https://api.themoviedb.org/3";
    public static final String url_img= "https://image.tmdb.org/t/p/original";
    public static final String api_key= "eb1a471df669cd0847bf22aaac49cc9b";

    public static List<Pelicula> listaPeliculas;

    private AdaptadorPelicula adaptadorPelicula;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaPeliculas = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        rvpeliculas = findViewById(R.id.rvpeliculas);
        searchView = findViewById(R.id.searchView);

        String url = construyeUrl("universo marvel");
        cargaPeliculas(url);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busquedaPelicula();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvpeliculas.setLayoutManager(linearLayoutManager);
        adaptadorPelicula = new AdaptadorPelicula(listaPeliculas);
        adaptadorPelicula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Pelicula_info.class);
                intent.putExtra("pelicula",listaPeliculas.get(rvpeliculas.getChildAdapterPosition(view)));
                startActivity(intent);
            }
        });
        rvpeliculas.setAdapter(adaptadorPelicula);

    }
    public void busquedaPelicula(){

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adaptadorPelicula.notifyDataSetChanged();
                String url = construyeUrl("busqueda",query);
                cargaPeliculas(url);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }
    public String construyeUrl(String tipo){


        String url = null;
        if (tipo.equals("universo marvel")) {
            Uri uri= Uri.parse(urlbase)
                    .buildUpon()
                    .appendPath("list")
                    .appendPath("1")
                    .appendQueryParameter("api_key", api_key)
                    .build();
            url = uri.toString();
            return url;
        }
        if (tipo.equals("mas populares")){
            Uri uri= Uri.parse(urlbase)
                    .buildUpon()
                    .appendPath("movie")
                    .appendPath("popular")
                    .appendQueryParameter("api_key",api_key)
                    .appendQueryParameter("page","1")
                    .build();
            url = uri.toString();
            return url;
        }
        if (tipo.equals("mejores valoradas")){
            Uri uri= Uri.parse(urlbase)
                    .buildUpon()
                    .appendPath("movie")
                    .appendPath("top_rated")
                    .appendQueryParameter("api_key",api_key)
                    .appendQueryParameter("page","1")
                    .build();
            url = uri.toString();
            return url;
        }

        
        return url;
    }
    public String construyeUrl(String tipo, String query){
        String url = null;
        if (tipo.equals("busqueda")) {
            Uri uri= Uri.parse(urlbase)
                    .buildUpon()
                    .appendPath("search")
                    .appendPath("movie")
                    .appendQueryParameter("api_key", api_key)
                    .appendQueryParameter("query",query)
                    .build();
            url = uri.toString();
            return url;
        }
        return url;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_menu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem opcionMenu){

        if (opcionMenu.getItemId() == R.id.itemMasPopulares){
            Toast.makeText(getApplicationContext(), "peliculas mas populares",Toast.LENGTH_SHORT).show();
            adaptadorPelicula.notifyDataSetChanged();
            String url = construyeUrl("mas populares");
            cargaPeliculas(url);
            return true;
        }
        if (opcionMenu.getItemId() == R.id.itemMejoresValoradas){
            Toast.makeText(getApplicationContext(), "peliculas mejores valoradas",Toast.LENGTH_SHORT).show();
            adaptadorPelicula.notifyDataSetChanged();
            String url = construyeUrl("mejores valoradas");
            cargaPeliculas(url);
            return true;
        }
        if (opcionMenu.getItemId() == R.id.itemPeliculasUniversoMarvel){
            Toast.makeText(getApplicationContext(), "peliculas Universo Marvel",Toast.LENGTH_SHORT).show();
            adaptadorPelicula.notifyDataSetChanged();
            String url = construyeUrl("universo marvel");
            cargaPeliculas(url);
            return true;
        }
        return  super.onOptionsItemSelected(opcionMenu);
    }

    private void cargaPeliculas(String url){

        listaPeliculas.clear();
        String resultado="items";
        if (!url.equals(construyeUrl("universo marvel"))){
            resultado="results";
        }
        String finalResultado = resultado;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray(finalResultado);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int id = jsonObject.getInt("id");
                                String title = jsonObject.getString("title");
                                String overview = jsonObject.getString("overview");
                                int vote_average = jsonObject.getInt("vote_average");
                                String poster_path = url_img + jsonObject.getString("poster_path");

                                String release_date = jsonObject.getString("release_date");
                                Pelicula pelicula = new Pelicula(id, title, overview, vote_average*10, poster_path, release_date);

                                listaPeliculas.add(pelicula);
                                adaptadorPelicula.notifyItemRangeInserted(listaPeliculas.size(), 1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    private class AdaptadorPelicula extends RecyclerView.Adapter<AdaptadorPelicula.AdaptadorPeliculaHolder> implements View.OnClickListener {

        List<Pelicula> lista;
        private View.OnClickListener listener;

        public AdaptadorPelicula(List<Pelicula> listape) {
            this.lista = listape;
        }

        @NonNull
        @Override
        public AdaptadorPeliculaHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view = getLayoutInflater().inflate(R.layout.layout_target, viewGroup, false);
            view.setOnClickListener(this);

            return new AdaptadorPeliculaHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorPeliculaHolder adaptadorPeliculaHolder, int i) {
            adaptadorPeliculaHolder.imprimir(i);
        }
        

        @Override
        public int getItemCount() {
            return lista.size();
        }
        public void setOnClickListener(View.OnClickListener listener){
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            if (listener!=null){
                listener.onClick(view);
            }
        }

        class AdaptadorPeliculaHolder extends RecyclerView.ViewHolder{

            TextView tvTitulo, tvFecha_estreno, tvPopularidad;
            ImageView ivPoster;


            public AdaptadorPeliculaHolder(@NonNull View itemView) {
                super(itemView);
                tvTitulo = itemView.findViewById(R.id.cvTitulo);
                tvFecha_estreno= itemView.findViewById(R.id.cvFecha_estreno);
                tvPopularidad = itemView.findViewById(R.id.cvPopularidad);

                ivPoster = itemView.findViewById(R.id.cvPoster_pelicula);
            }

            public void imprimir(int i) {

                tvTitulo.setText(" "+ lista.get(i).getTitle());
                tvFecha_estreno.setText("Fecha de Estreno: "+ lista.get(i).getRelease_date());
                tvPopularidad.setText("Popularidad: " + lista.get(i).getVote_average() +"%");
                Picasso.get().load(listaPeliculas.get(i).getPoster_path()).into(ivPoster);
            }
        }

    }
}
