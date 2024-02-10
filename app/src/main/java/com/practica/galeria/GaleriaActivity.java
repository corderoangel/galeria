package com.practica.galeria;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;

public class GaleriaActivity extends AppCompatActivity {
    String[] archivos;
    RecyclerView rv1;
    AdaptadorFotos adaptadorFotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);

        archivos = fileList();
        rv1 = findViewById(R.id.rv1);
        rv1.setLayoutManager(new GridLayoutManager(this, 3));

       /**
        * @param archivos: matriz de cadenas que contiene nombres de archivos o datos relacionados
        * con las imagenes que se mostrarán en la cuadricula
        * ---------------------------------------------------------------------------------
        * @param this: contexto
        * ---------------------------------------------------------------------------------
        * @param (setData){}: lambda que define el comportamiento que se activará cuando se inicie
        * el action mode
        * ---------------------------------------------------------------------------------
        */
        adaptadorFotos = new AdaptadorFotos(archivos, this, (setData) -> {

           /* Iniciar nuevo actionmode
            * creación de instancia anonima ActionMode.Callback, que es una interfaz para definir
            * el comportamiento del ActionMode
            */
            startSupportActionMode(new ActionMode.Callback() {
                /**
                 * @param mode: modo de acción actual, puede utilizarse para controlar y gestionar
                 * el comportamiento del modo de acción
                 * -------------------------------------------------------------------------------
                 * @param menu: se utilizará para inflar las opciones de menú especificas del modo
                 * de acción
                 * -------------------------------------------------------------------------------
                 */
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    mode.getMenuInflater().inflate(R.menu.menu_galeria, menu);
                   /*
                    * el modo de acción se creó correctamente y se mostrará en la interfaz de
                    * usuario
                    */
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

               /**
                * llamada al metodo cuando se hace clic en una opción del menú del modo acción
                *
                * @param mode : representa el modo de acción actual, puede utilizarse para
                * controlar y gestionar el comportamiento del modo de acción.
                * -----------------------------------------------------------------------------
                * @param item: representa la opción del menú en la que el usuario hizo clic
                * -----------------------------------------------------------------------------
                */
                /*@SuppressLint("NotifyDataSetChanged")
                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    Set<Integer> datosEliminar = new HashSet<>();
                    if (item.getItemId() == R.id.menu_opcion_eliminar) {
                        //Obtener los elementos seleccionados del adaptador
                        Toast.makeText(GaleriaActivity.this, "dato: " + setData, Toast.LENGTH_SHORT).show();
                        for (int position : adaptadorFotos.elementosSeleccionados){
                            String archivosEliminar = archivos[position];
                            GaleriaActivity.this.deleteFile(archivosEliminar);
                            datosEliminar.add(position);
                        }
                        adaptadorFotos.elementosSeleccionados.removeAll(datosEliminar);
                        adaptadorFotos.notifyDataSetChanged();

                        rv1.setAdapter(adaptadorFotos);

                        mode.finish();
                    }
                    return true;
                }*/

               @Override
               public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                   if (item.getItemId() == R.id.menu_opcion_eliminar) {
                       // Llama al método para eliminar elementos en el adaptador
                       adaptadorFotos.eliminarElementosSeleccionados();
                       mode.finish();
                       return true;
                   }
                   return false;
               }


                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    setData.clear();
                    adaptadorFotos.elementosSeleccionados.clear();
                    Toast.makeText(GaleriaActivity.this, "setData: " + setData, Toast.LENGTH_SHORT).show();
                    adaptadorFotos.notifyDataSetChanged();
                }


            });
            return false;
        });

        rv1.setAdapter(adaptadorFotos);
    }
}