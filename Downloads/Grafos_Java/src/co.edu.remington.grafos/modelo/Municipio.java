package co.edu.remington.grafos.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un municipio (nodo) del grafo de Casanare.
 */
public class Municipio {
    private int id;
    private String nombre;
    private List<Via> conexiones; // lista de vías adyacentes

    public Municipio(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.conexiones = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Via> getConexiones() {
        return conexiones;
    }

    public void agregarConexion(Via via) {
        conexiones.add(via);
    }

    @Override
    public String toString() {
        return String.format("[%d] %s", id, nombre);
    }
}
