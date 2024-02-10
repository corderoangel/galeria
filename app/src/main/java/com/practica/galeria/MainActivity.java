package com.practica.galeria;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ImageView ivFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivFoto = findViewById(R.id.ivFotoMain);
    }

    final int CAPTURA_IMAGEN = 1;

    public void tomarFoto(View v){
        //capturar foto utilizando la cámara del dispositivo
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


       /*
        * esta linea de codigo se encuentra relacionada con el metodo
        * onActivityResult, donde el requestCode es CAPTURA_IMAGEN
        */
        startActivityForResult(intent, CAPTURA_IMAGEN);
    }

    /**
     * @param requestCode: código de solicitud que identifica la solicitud especifica
     * ---------------------------------------------------------------------------------
     * @param resultCode: indica si la actividad es RESULT_OK(exitosa) o RESULT_CANCELED
     * (ocurrió un error)
     * ---------------------------------------------------------------------------------
     * @param data: datos adicionales de la actividad que ha finalizado (en este caso
     * la imagen capturada)
     * ---------------------------------------------------------------------------------
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURA_IMAGEN && resultCode == RESULT_OK){
            /*
             * obtener extras del objeto data, el cual en este caso se espera contenga
             * una miniatura de una foto
             */
            Bundle extras = data.getExtras();

            /*
             * se extrae la miniatura de la imagen capturada del conjunto de extras con la key
             * data, la cual almacena un objeto Bitmap
             */
            Bitmap bitmap1 = (Bitmap) extras.get("data");

            //enviar la miniatura de la imagen a un imageView
            ivFoto.setImageBitmap(bitmap1);

            try {
               /*
                * FileOutputStream: flujo de salida que se utiliza para escribir datos en el sistema
                * de archivos del dispositivo
                * ----------------------------------------------------------------------------------
                * crearNombreArchivoJPG(): genera el nombre
                * ----------------------------------------------------------------------------------
                * Context.MODE_PRIVATE: modo en que se abrirá el archivo, en este caso solo lo podrá
                * hacer la aplicación presente
                * ----------------------------------------------------------------------------------
                */
                FileOutputStream fos = openFileOutput(crearNombreArchivoJPG(), Context.MODE_PRIVATE);

               /*
                * la miniatura de la imagen Bitmap se comprime en JPEG con máxima calidad al 100% y
                * se guarda en el archivo de salida fos
                */
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                //se cierra el flujo de salida
                fos.close();
            }catch (Exception e){

            }
        }
    }

    private String crearNombreArchivoJPG(){
        String fecha = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return fecha + ".jpg";
    }

    public void verTodasLasFotos(View v){
        Intent intent = new Intent(this, GaleriaActivity.class);
        startActivity(intent);
    }

}