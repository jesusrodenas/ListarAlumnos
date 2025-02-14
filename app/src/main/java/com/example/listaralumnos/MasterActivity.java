package com.example.listaralumnos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MasterActivity extends AppCompatActivity {
    private List<Alumno> alumnoList;
    private ListAdapter listAdapter;
    private RecyclerView recyclerView;
    private ProgressBar mProgressBar;

    private final static String URL_SERVICIO = "https://idocentic.website/monroymanagement/services/readStudent.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        recyclerView = findViewById(R.id.listRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressBar = findViewById(R.id.progressBar);
        this.alumnoList = new ArrayList<Alumno>();

        /**
         * Este método relizará estas operaciones:
         * - Invocará al servicio.
         * - Procesará el XML cargando la lista de alumnos (alumnoList).
         * - Cargará con la información (alumnoList) el ListAdapter y la RecyclerView.
         * - Ocultará la progressBar y dará visibilidad a la lista.
         */
        cargaYMuestraListaAlumnos();
    }


    /*
        Método que va a realizar una petición con el endpoint que le hemos pasado. Si recibe una respuesta positiva
        va a ejecutar el método que va a tratar el XML y lo va a mostrar
        en caso de no poder ejecutar con éxito la petición, podremos ver un error en el logcat
         */
    private void cargaYMuestraListaAlumnos() {
        RequestQueue queue = Volley.newRequestQueue(this);

        // Se prepara la petición con la URL del servicio
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_SERVICIO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Obtener el String como ISO y parsear a UTF-8
                        try {
                            byte[] bytes = response.getBytes("ISO-8859-1");
                            String utf8String = new String(bytes, "UTF-8");
                            procesaXMLyCargaLista(utf8String);

                            listAdapter = new ListAdapter(alumnoList,MasterActivity.this);
                            recyclerView.setAdapter(listAdapter);

                            // Actualizamos la vista para ocultar la progressBar y mostrar la lista.
                            mProgressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        } catch (UnsupportedEncodingException e) {
                            Log.e("Error", "Error al parsear a UTF-8: " + e.getMessage());
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        });

        queue.add(stringRequest);
    }

    /**
     * Este método va a recibir el string(xml) recibido por Volley y tratará el XML
     * extrayendo los datos y añadiendo cada uno de los alumnos a la alumnoList
     */
    private void procesaXMLyCargaLista(String response) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new org.xml.sax.InputSource(new StringReader(response)));

            NodeList studentNL = document.getElementsByTagName("student");

            for (int i = 0; i < studentNL.getLength(); i++) {
                Element studentElement = (Element) studentNL.item(i);

                // Obtener los datos del estudiante del XML
                int id= Integer.parseInt(studentElement.getElementsByTagName("studentId").item(0).getTextContent());
                String nombre = studentElement.getElementsByTagName("firstName").item(0).getTextContent();
                String ape1 = studentElement.getElementsByTagName("lastName1").item(0).getTextContent();
                String ape2 = studentElement.getElementsByTagName("lastName2").item(0).getTextContent();
                alumnoList.add(new Alumno(nombre,ape1,ape2,id));
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}