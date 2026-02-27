package co.edu.remington.grafos.utilidades;

import co.edu.remington.grafos.modelo.GrafoCasanare;

/**
 * Utilidad para trabajar con la representación en matriz de adyacencia del grafo.
 * Complementa la lista de adyacencia implementada en GrafoCasanare.
 */
public class MatrizAdyacencia {

    private GrafoCasanare grafo;
    private double[][] matriz;
    private int n;

    public MatrizAdyacencia(GrafoCasanare grafo) {
        this.grafo = grafo;
        this.n = grafo.getNumNodos();
        this.matriz = grafo.obtenerMatrizAdyacencia();
    }

    /**
     * Actualiza la matriz desde el grafo (útil después de agregar municipios/vías).
     */
    public void actualizar() {
        this.n = grafo.getNumNodos();
        this.matriz = grafo.obtenerMatrizAdyacencia();
    }

    /**
     * Retorna true si existe una arista directa entre origen y destino.
     */
    public boolean existeConexion(int origen, int destino) {
        if (origen >= n || destino >= n) return false;
        return matriz[origen][destino] > 0;
    }

    /**
     * Retorna la distancia directa entre dos nodos (0 si no hay conexión).
     */
    public double getDistancia(int origen, int destino) {
        if (origen >= n || destino >= n) return 0;
        return matriz[origen][destino];
    }

    /**
     * Retorna el grado (número de conexiones directas) de un nodo.
     */
    public int getGrado(int nodo) {
        if (nodo >= n) return 0;
        int grado = 0;
        for (int j = 0; j < n; j++) {
            if (matriz[nodo][j] > 0) grado++;
        }
        return grado;
    }

    /**
     * Muestra la matriz con anotaciones adicionales de grado.
     */
    public void mostrarConGrados() {
        grafo.mostrarMatrizAdyacencia();

        System.out.println("Grados de cada municipio:");
        for (int id : grafo.getIds()) {
            System.out.printf("  %-20s grado = %d%n",
                    grafo.getNombre(id), getGrado(id));
        }
        System.out.println();
    }

    public double[][] getMatriz() {
        return matriz;
    }
}
