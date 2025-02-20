package com.example.listaralumnos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.listaralumnos.utilidades.AlmacenCadenas;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DetailActivity extends AppCompatActivity {
    private TextView txtNombre;
    private TextView txtApellidos;
    private TextView txtCard;
    private TextView txtFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Instanciamos los elementos de la vista encontrandolos con la id
        txtNombre = findViewById(R.id.txtNombre);
        txtApellidos = findViewById(R.id.txtApellidos);
        txtCard = findViewById(R.id.txtCard);
        txtFecha = findViewById(R.id.txtFecha);

        //Se recoge el dato que se ha pasado por el intent al clicar en el RecyclerView en la activity anterior
        Intent intent = getIntent();
        int datoRecibido = intent.getIntExtra(AlmacenCadenas.ID_ALUMNO,0);
        // Añadimos al endpoint el parametro que hemos rescatado de la selección de la lista
        String url = AlmacenCadenas.SERVICIO_LEER_ALUMNO + "?studentId=" + datoRecibido;

        // Llamar a la función para realizar la solicitud Volley
        requestAlumno(url);
    }

    /*
    Método que va a realizar una petición con el endpoint que le hemos pasado. Si recibe una respuesta positiva
    va a ejecutar el método que va a tratar el XML y lo va a mostrar
    en caso de no poder ejecutar con éxito la petición, podremos ver un error en el logcat
     */
    private void requestAlumno(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Parsear el String a UTF-8
                        try {
                            byte[] bytes = response.getBytes("ISO-8859-1");
                            String utf8String = new String(bytes, "UTF-8");
                            leerXMLyMostrar(utf8String);
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

        // Añadir la solicitud a la cola
        queue.add(stringRequest);
    }
/*
    Este método va a recibir el string(xml) recibido por Volley y va a tratar dicho XML para extraer los datos y
    terminar mostrando esos datos en los textviews construidos en el layout
 */
    private void leerXMLyMostrar(String xmlString) {
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new org.xml.sax.InputSource(new StringReader(xmlString)));

            NodeList studentNL = document.getElementsByTagName("student");
            for (int i = 0; i < studentNL.getLength(); i++) {
                Element studentElement = (Element) studentNL.item(i);

                // Obtener los datos del estudiante del XML
                String nombre = studentElement.getElementsByTagName("firstName").item(0).getTextContent();
                String ape1 = studentElement.getElementsByTagName("lastName1").item(0).getTextContent();
                String ape2 = studentElement.getElementsByTagName("lastName2").item(0).getTextContent();
                String idCard = studentElement.getElementsByTagName("idCard").item(0).getTextContent();
                String fecha = studentElement.getElementsByTagName("birthDate").item(0).getTextContent();

                // Mostrar cada dato en el TextView deseado
                txtNombre.setText(nombre);
                txtApellidos.setText(ape1+ " " + ape2);
                txtCard.setText(idCard);
                txtFecha.setText(fecha);


            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}