package com.example.paparazziapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class SeleccionarFotoActivity extends AppCompatActivity {
    //NO VAAAAAA NU SE POR QUEEEEEEEEE

    private static final String[] PERMISOS = {Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_foto);

        ActivityCompat.requestPermissions(this, PERMISOS, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.i("MIAPP", "QUIERO SELECCIONAR UNA FOTO");
        String accion = null;
        Intent intentpidefoto = new Intent();
        //caso particular. Se ha modificado el acceso a documentos https://developer.android.com/guide/topics/providers/document-provider.html#client
        //Dependiendo de la version que sea el movil, se realizará un intent u otro.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            accion = Intent.ACTION_OPEN_DOCUMENT;
        } else {
            accion = Intent.ACTION_PICK;
        }

        intentpidefoto.setAction(accion);
        //Tipo mime. Acampoña la información y la identifica.
        intentpidefoto.setType("image/*");//Todos los archivos de imagen,

        startActivityForResult(intentpidefoto, 150);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode)
        {
            case RESULT_OK:
                Log.i("MIAPP", "Ha seleccionado foto");
                Uri ruta = data.getData();
                Log.i("MIAPP", "ruta " + ruta.toString());
                //TODO coger el imageView y setearle la Uri.
                ImageView imageView = findViewById(R.id.foto_seleccionada);
                imageView.setImageURI(ruta);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                break;
            case RESULT_CANCELED:
                Log.i("MIAPP", "No ha seleccionado foto");
                break;
        }
    }
}
