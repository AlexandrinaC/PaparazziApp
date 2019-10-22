package com.example.paparazziapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;//el menú lateral
    private boolean menu_visible;//para gestionar si está visible o no el menú lateral

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.menu_visible = false;

        //iniciamos el menú
        //Con esta función cojo la barrita de arriba para añardirle la hamburguesa.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //muestra el boton de para atrás (por defecto)
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_luna_negra);//personalizo con el del menu
        //asignamos listener del menú lateral
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navview);
        //El que te va a escuchar cuando le des al boton, va a ser esta clase. Cualquier elemento que recibe e menu,
        //lo va a escuchar esta clase.
        navigationView.setNavigationItemSelectedListener(this); //escucho los eventos de esta clase aquí
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Esto se va a activar cuando toque la hamburguesa
        int id_item = item.getItemId();
        switch (id_item) {


            case android.R.id.home:
                //Si toco y el menu esta desplegado, es false. Entonces lo cerramos.
                if (menu_visible) {
                    drawerLayout.closeDrawers();
                    menu_visible = false;
                    //Si toco y el menu esta cerrado, es true. Entonces lo abrimos.
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                    menu_visible = true;
                }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //Pongo el molde del intent, aqui, ya que lo voy a utilizar en varias partes.
        Intent intent;
        String menu = menuItem.getTitle().toString();
        //Con esto, cojo cada numero de cada icono. En mi caso, el 16,17 y 18
        int npi = menuItem.getOrder();//obtengo el número del punto de interés

        switch (npi){
            case 16: intent = new Intent(this, FotoRAM.class);
            startActivity(intent);
            break;

            case 17: intent = new Intent (this, FotoDisco.class);
            startActivity(intent);
            break;

            case 18: intent = new Intent(this,SeleccionarFoto.class);
            startActivity(intent);
            break;
        }

        Log.d(getClass().getCanonicalName(), "Ha tocado la opción " + menu + " " +npi);
        drawerLayout.closeDrawers();
        menu_visible = false;

        return false;
    }
}
