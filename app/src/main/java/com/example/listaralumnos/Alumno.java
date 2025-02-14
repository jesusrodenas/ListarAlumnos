package com.example.listaralumnos;
/*
    Clase utilizada para construir objetos de tipo Alumno que serán almacenados en
    una lista y que se puedam visualizar en el RecyclerView. Se definen los atributos,
    se crean los constructores necesarios y los métodos getters y setters.
 */
public class Alumno {
    public int id;
    public String nombre;
    public String apellido1;
    public String apellido2;
    public String fechaNac;
    public String idCard;

    public Alumno(String nombre, String apellido1, String apellido2, int id) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.id=id;
    }

    public Alumno(String nombre, String idCard, int id){
        this.nombre = nombre;
        this.idCard=idCard;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
}
