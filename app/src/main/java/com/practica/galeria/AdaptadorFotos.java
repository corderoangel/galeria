package com.practica.galeria;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class AdaptadorFotos extends RecyclerView.Adapter<AdaptadorFotos.AdaptadorFotosHolder>{
    private String archivos[];
    private Context context;
    mostrarMenu mostrarMenu;
    public Set<Integer> elementosSeleccionados = new HashSet<>();
    public AdaptadorFotos(String[] archivos, Context context, mostrarMenu mostrarMenu) {
        this.archivos = archivos;
        this.context = context;
        this.mostrarMenu = mostrarMenu;
    }

    public void eliminarElementosSeleccionados() {
        List<Integer> elementosSeleccionadosList = new ArrayList<>(elementosSeleccionados);
        Collections.sort(elementosSeleccionadosList, Collections.reverseOrder()); // Ordena en orden descendente para evitar problemas con los índices

        for (Integer position : elementosSeleccionadosList) {
            String archivoEliminar = archivos[position];
            this.context.deleteFile(archivoEliminar); // Elimina el archivo del almacenamiento interno
            archivos[position] = null; // Establece la posición en null
        }

        elementosSeleccionados.clear();
        archivos = Arrays.stream(archivos).filter(Objects::nonNull).toArray(String[]::new); // Elimina elementos null
        notifyDataSetChanged(); // Notifica al adaptador que los datos han cambiado
    }

    /*
    * 1- volver archivos clases
    * 2- que tenga el bitmap ahí
    * 3- el bitmap de esa clase se pase al bitmap del dialogo
    * */

    @NonNull
    @Override
    public AdaptadorFotos.AdaptadorFotosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_foto, parent, false);
        return new AdaptadorFotosHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull AdaptadorFotos.AdaptadorFotosHolder holder, int position) {
        /*if(elementosSeleccionados.isEmpty()){
            holder.imprimir(position);
            holder.itemView.setBackgroundResource(android.R.color.transparent);
        }*/
        holder.imprimir(position);

        holder.itemView.setBackgroundResource(elementosSeleccionados.contains(position) ? android.R.color.holo_red_dark : android.R.color.transparent);

        /*
        * seleccionar itemViews de la GaleriaActivity
        * */
        holder.checkBox.setOnCheckedChangeListener(null); // Para evitar que el cambio de estado del CheckBox afecte a otros elementos
        holder.checkBox.setChecked(elementosSeleccionados.contains(position));
        holder.checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (elementosSeleccionados.isEmpty()) {
                    // Iniciar el modo de acción solo si no hay elementos seleccionados previamente.
                    mostrarMenu.iniciarActionMode(elementosSeleccionados);
                }

                toggleElementoSeleccionado(position);
                holder.itemView.setBackgroundResource(elementosSeleccionados.contains(position) ? android.R.color.holo_red_dark : android.R.color.transparent);

                if(holder.checkBox.isChecked()){
                    holder.itemView.setBackgroundResource(android.R.color.holo_red_dark);
                    Toast.makeText(context, "seleccionada foto #" + position, Toast.LENGTH_SHORT).show();
                }
                if(!holder.checkBox.isChecked()){
                    holder.itemView.setBackgroundResource(android.R.color.transparent);
                    Toast.makeText(context, "deseleccionada foto #" + position, Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void toggleElementoSeleccionado(int position) {
       /*
        * si elemento seleccionado contiene la posición
        */
        if (elementosSeleccionados.contains(position)) {
           /*
            * Deseleccionar el elemento si ya estaba seleccionado
            */
            elementosSeleccionados.remove(position);
        } else {
           /*
            * Seleccionar el elemento si no estaba seleccionado
            */
            elementosSeleccionados.add(position);
        }
        //Notificar cambios en los elementos para que se actualice la apariencia
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return archivos.length;
    }

    public class AdaptadorFotosHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        ImageView iv1;

        public AdaptadorFotosHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            iv1 = itemView.findViewById(R.id.ivFoto);
        }

        public void imprimir(int position){
            try {
                FileInputStream fileInputStream = context.openFileInput(archivos[position]);
                Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                iv1.setImageBitmap(bitmap);
                fileInputStream.close();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
