package com.example.paparazziapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class TomarFotoDiscoActivity extends AppCompatActivity {

    private static final String [] PERMISOS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    //La ruta de la imagen.
    public static final String DIRECTORIO_PUBLICO_PROPIO = Environment.getExternalStorageDirectory().getPath() + "/CFTIC";

    private Uri foto_uri;
    private String ruta_foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_foto_disco);

        ActivityCompat.requestPermissions(this, PERMISOS, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            this.foto_uri = crearFicheroImagen();
        Intent intent_foto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Como quiero que lo guarde en el fichero, se lo digo. Tirame la foto y la guardas en foto_uri. Para después, poder pasarselo a la camara de fotos.
        intent_foto.putExtra(MediaStore.EXTRA_OUTPUT, foto_uri);

        //Comenzamos la actividad
        startActivityForResult(intent_foto, 300);
    }catch (Exception e){
            Log.e("MIAPP" , "PEta" + e);
        }

    }

    /**
     * En caso de ser la carpeta pública el destino de nuestra foto, se actuaiza así para que salga la foto tomada
     * este método funciona pq generamos nosotros la URI a partir de una ruta, de forma que es de tipo file:///
     * si te refieres a este método con una uri del tipo content:/// NO ACTUALIZA
     */
    private void actualizarGaleriaTrasCaptura ()
    {
        Log.d("MIAPP", "Ruta captura foto " + ruta_foto);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(ruta_foto);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

        /*File file = new File(ruta_foto);

        MediaScannerConnection.scanFile(this,
                new String[] { file.getPath() }, new String[] { "image/jpeg" },
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode)
        {
            case RESULT_OK:
                //LA FOTO FUE BIEN. Esta guardada en la uri ahora, en una ruta externa.
                ImageView imageView = findViewById(R.id.foto_disco);
                imageView.setImageURI(foto_uri);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                actualizarGaleriaTrasCaptura();
                break;

            case RESULT_CANCELED:
                break;
        }
    }

    /**
     * Si se decide guardar la foto capturada, debo crear antes un fichero y pasar la URI (ruta) del mismo.
     * Para eso vale esta función: para crear el fichero donde será almacenado la foto y su URI
     *
     * @return La URI que identifica al fichero. Null si la cosa fue mal
     */
    private Uri crearFicheroImagen() {
        Uri uri_dest = null;
        String momento_actual = null;
        String nombre_fichero = null;
        File f = null;
        String ruta_captura_foto = null;

        //Para formatear la fecha, que nos de la fecha del dispositivo, del moemnto en el que se ha hecho la foto.
        momento_actual = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); //así nos garantizamos emplear un sufijo aleatorio: el nombre del archivo de la imagen incluirá el momento exacto
        nombre_fichero = "CFTIC" + momento_actual + ".jpg"; //Lo primero es el prefijo que tendrá la foto. Lo segundo es
        //el sufijo, el tipo de foto


        File fdir = new File(DIRECTORIO_PUBLICO_PROPIO);
        //Si existe el directorio entra, si no existe me lo crea y después entra.
        if ((fdir.exists()) || (fdir.mkdir())) {
            //Este string, en comparacion con el string de uri, sirve para refrescar mi galeria, va mejor.
            //Con la ruta interna, no externa (uri).
            ruta_foto = DIRECTORIO_PUBLICO_PROPIO + "/"+nombre_fichero;
            Log.i("MIAPP", "RUTA FOTO = " + ruta_foto);
            //El fichero
            f = new File(ruta_foto);

            try {
                //Aqui crea el fichero. Lo que necesito para invocar a la camara de fotos.
                if (f.createNewFile()) {
                    Log.i("MIAPP", "Fichero creado");
                    //uri_dest es mi lugar de destino.
                    uri_dest = FileProvider.getUriForFile(this, "edu.cftic.imagenapp.fileprovider", f);//Authority tiene que coincidir con el que hay en el Manifest.
                    //uri_dest = Uri.fromFile(f);//métod antiguo, falla aunque se refiera a una ruta pública
                    Log.i("MIAPP", "URI FOTO = " + uri_dest.toString());

                } else {
                    Log.i("MIAPP", "Fichero NO creado (ya existía)");
                }
            } catch (IOException e) {
                Log.e("MIAPP", "Error creando el fichero", e);
            }


        }



        return uri_dest;
    }
}
