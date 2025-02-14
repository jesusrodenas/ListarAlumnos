package com.example.listaralumnos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Clase que necesitamos para controlar los datos y la vista del RecyclerView
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.AlumnoViewHolder> {
    private List<Alumno> alumnoList;
    private Context contextActivity;

    /**
     * Constructor del adaptador en el que seteamos el conjunto de alumnos y el contexto (Activity) sobre
     * el que representaremos.
     *
     * @param listaAlumnos
     * @param contextActivity
     */
    public ListAdapter(List<Alumno> listaAlumnos, Context contextActivity){
        this.alumnoList =listaAlumnos;
        this.contextActivity = contextActivity;
    }

    /**
     * Este método devuelve el número de items que tiene la lista de alumnos
     */
    @Override
    public int getItemCount(){
        return alumnoList.size();
    }

    /**
     * Indicamos que la vista será el xml creado para ello
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @Override
    public ListAdapter.AlumnoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_element, parent, false);
        return new ListAdapter.AlumnoViewHolder(view);
    }

    /**
     * Enlazará los datos de cada uno de los elementos de la lista y asignará el listener de
     * evento
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ListAdapter.AlumnoViewHolder holder, int position){
        int pos = position;
        holder.bindData(alumnoList.get(pos));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idAPasar = alumnoList.get(pos).getId();

                Intent cambiarAct = new Intent (contextActivity, DetailActivity.class );
                cambiarAct.putExtra("DATO_ID", idAPasar);
                contextActivity.startActivity(cambiarAct);
            }
        });
    }

    /**
     * Esta clase maneja los datos que deben ser incluidos en cada uno de los alumnos de la lista.
     */
    public class AlumnoViewHolder extends RecyclerView.ViewHolder{
        ImageView iconImage;
        TextView nombre, apellidos;

        /**
         * En el constructor vamos a recoger los elementos de la vista y se lo vamos a asignar a las
         * variables establecidas en la clase
         *
         * @param itemView
         */
        AlumnoViewHolder(View itemView){
            super(itemView);
            iconImage = itemView.findViewById(R.id.iconImageView);
            nombre = itemView.findViewById(R.id.nombreTV);
            apellidos = itemView.findViewById(R.id.apellidosTV);
        }

        /**
         * Recibe el alumno por parámetro y a través de los getters de la clase Alumno asignamos los valores que deben
         * de mostrar los elementos de la vista.
         *
         * @param item
         */
        void bindData(final Alumno item){
            nombre.setText(item.getNombre());
            apellidos.setText(item.getApellido1() + " " + item.getApellido2());
        }
    }
}
