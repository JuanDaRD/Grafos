package co.edu.remington.grafos.algoritmos;

import co.edu.remington.grafos.modelo.GrafoCasanare;
import co.edu.remington.grafos.modelo.Via;

import java.util.*;

/**
 * Implementación del algoritmo DFS (Depth-First Search / Búsqueda en Profundidad).
 * Explora tan lejos como sea posible antes de retroceder (backtracking).
 */
public class RecorridoDFS {

    private GrafoCasanare grafo;
    private Set<Integer> visitados;
    private List<Integer> orden;
    private List<String> camino;
    private int paso;

    public RecorridoDFS(GrafoCasanare grafo) {
        this.grafo = grafo;
    }

    /**
     * Ejecuta DFS desde el nodo origen dado (versión recursiva).
     * Muestra el orden de exploración en profundidad.
     *
     * @param origen ID del nodo de inicio
     * @return Lista de nodos en orden DFS
     */
    public List<Integer> ejecutar(int origen) {
        visitados = new HashSet<>();
        orden = new ArrayList<>();
        camino = new ArrayList<>();
        paso = 1;

        System.out.println("\n======= RECORRIDO DFS desde " + grafo.getNombre(origen) + " =======");
        System.out.printf("%-5s %-20s %-30s%n", "Paso", "Municipio", "Camino recorrido");
        System.out.println("-".repeat(60));

        dfsRecursivo(origen, grafo.getNombre(origen));

        System.out.println("-".repeat(60));
        System.out.println("Total municipios visitados: " + orden.size() +
                " de " + grafo.getMunicipios().size());
        System.out.println("Orden completo: " + formatearOrden(orden));

        // Detectar municipios no alcanzables
        for (int id : grafo.getIds()) {
            if (!visitados.contains(id)) {
                System.out.println("  DESCONECTADO: " + grafo.getNombre(id) + " (" + id + ")");
            }
        }
        System.out.println("==========================================\n");
        return orden;
    }

    private void dfsRecursivo(int nodo, String caminoActual) {
        visitados.add(nodo);
        orden.add(nodo);
        camino.add(grafo.getNombre(nodo));

        System.out.printf("%-5d %-20s %-30s%n",
                paso++, grafo.getNombre(nodo), caminoActual);

        // Ordenar vecinos para resultado determinista
        List<Via> vecinos = new ArrayList<>(grafo.getListaAdyacencia().get(nodo));
        vecinos.sort(Comparator.comparingInt(Via::getDestino));

        for (Via v : vecinos) {
            if (!visitados.contains(v.getDestino())) {
                String nuevoCamino = caminoActual + " -> " + grafo.getNombre(v.getDestino());
                dfsRecursivo(v.getDestino(), nuevoCamino);
            }
        }
        camino.remove(camino.size() - 1);
    }

    private String formatearOrden(List<Integer> lista) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lista.size(); i++) {
            sb.append(grafo.getNombre(lista.get(i)));
            if (i < lista.size() - 1) sb.append(" -> ");
        }
        return sb.toString();
    }
}
