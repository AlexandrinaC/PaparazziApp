package com.example.paparazziapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.util.List;

public class FotoRAM extends AppCompatActivity {

    private static final String[] PERMISOS = {Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_ram);

        ActivityCompat.requestPermissions(this, PERMISOS, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Suponemos que nos da los permisos
        //Con esto decimos que queremos tomar una foto. es un intent implicito.
        Intent tomar_foto_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (tomar_foto_intent.resolveActivity(getPackageManager()) !=null){
            Log.i("MIAPP", "Al menos hay una app que toma fotos");

            //Si por lo menos hay una app que toma fotos, comienza la funcion de tomar foto.
            startActivityForResult(tomar_foto_intent, 150);
        }

        //Un intent para hacer una lista de las apps que pueden realizar esta accion.
        List<ResolveInfo> lista_paquetes = getPackageManager().queryIntentActivities(tomar_foto_intent, PackageManager.MATCH_DEFAULT_ONLY);
        //Para recorrer la lista
        for (ResolveInfo resolveInfo: lista_paquetes)
        {
            Log.i("MIAPP", "APPS candidatas " + resolveInfo.activityInfo.packageName);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("MIAPP", "Ha venido de tomar a foto");
        switch (resultCode){
            case RESULT_OK: Log.i("MIAPP", "La foto fue bien");
                Bundle saco = data.getExtras();
                Bitmap bitmap = (Bitmap)saco.get("data");

                if (bitmap!=null)
                {
                    ImageView imageView = findViewById(R.id.montana);
                    imageView.setImageBitmap(bitmap);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            break;

            case RESULT_CANCELED: Log.i("MIAPP", "Foto abortada");
            break;
        }
    }

}

