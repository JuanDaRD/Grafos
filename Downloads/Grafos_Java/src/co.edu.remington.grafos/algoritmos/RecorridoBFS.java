package co.edu.remington.grafos.algoritmos;

import co.edu.remington.grafos.modelo.GrafoCasanare;
import co.edu.remington.grafos.modelo.Via;

import java.util.*;

/**
 * Implementación del algoritmo BFS (Breadth-First Search / Búsqueda en Anchura).
 * Explora el grafo por niveles usando una cola (Queue).
 */
public class RecorridoBFS {

    private GrafoCasanare grafo;

    public RecorridoBFS(GrafoCasanare grafo) {
        this.grafo = grafo;
    }

    /**
     * Ejecuta BFS desde el nodo origen dado.
     * Muestra el orden de visita y el nivel de cada municipio respecto al origen.
     *
     * @param origen ID del nodo de inicio
     * @return Lista de nodos en orden de visita
     */
    public List<Integer> ejecutar(int origen) {
        Map<Integer, List<Via>> lista = grafo.getListaAdyacencia();
        Set<Integer> visitados = new HashSet<>();
        Queue<Integer> cola = new LinkedList<>();
        Map<Integer, Integer> nivel = new HashMap<>();
        List<Integer> orden = new ArrayList<>();

        cola.add(origen);
        visitados.add(origen);
        nivel.put(origen, 0);

        System.out.println("\n======= RECORRIDO BFS desde " + grafo.getNombre(origen) + " =======");
        System.out.printf("%-5s %-20s %-8s%n", "Paso", "Municipio", "Nivel");
        System.out.println("-".repeat(40));

        int paso = 1;
        while (!cola.isEmpty()) {
            int actual = cola.poll();
            orden.add(actual);
            int nivelActual = nivel.get(actual);

            System.out.printf("%-5d %-20s %-8d%n",
                    paso++, grafo.getNombre(actual), nivelActual);

            // Ordenar vecinos para resultado determinista
            List<Via> vecinos = new ArrayList<>(lista.get(actual));
            vecinos.sort(Comparator.comparingInt(Via::getDestino));

            for (Via v : vecinos) {
                if (!visitados.contains(v.getDestino())) {
                    visitados.add(v.getDestino());
                    cola.add(v.getDestino());
                    nivel.put(v.getDestino(), nivelActual + 1);
                }
            }
        }

        System.out.println("-".repeat(40));
        System.out.println("Total municipios visitados: " + orden.size() +
                " de " + grafo.getMunicipios().size());

        // Detectar municipios no alcanzables
        for (int id : grafo.getIds()) {
            if (!visitados.contains(id)) {
                System.out.println("  DESCONECTADO: " + grafo.getNombre(id) + " (" + id + ")");
            }
        }
        System.out.println("==========================================\n");
        return orden;
    }
}
