package co.edu.remington.grafos.algoritmos;

import co.edu.remington.grafos.modelo.GrafoCasanare;
import co.edu.remington.grafos.modelo.Via;

import java.util.*;

/**
 * Implementación del algoritmo de Dijkstra para encontrar rutas más cortas
 * en grafos ponderados con pesos no negativos.
 *
 * Soporta dos modos:
 *  - Distancia real (kilómetros sin penalización)
 *  - Distancia penalizada según el estado de la vía
 */
public class Dijkstra {

    private GrafoCasanare grafo;

    public Dijkstra(GrafoCasanare grafo) {
        this.grafo = grafo;
    }

    // =============================================
    //  RESULTADO DE DIJKSTRA
    // =============================================

    public static class Resultado {
        public double[] dist;
        public int[] anterior;

        public Resultado(double[] dist, int[] anterior) {
            this.dist = dist;
            this.anterior = anterior;
        }
    }

    // =============================================
    //  ALGORITMO PRINCIPAL
    // =============================================

    /**
     * Ejecuta Dijkstra desde el nodo origen.
     *
     * @param origen      ID del nodo de inicio
     * @param penalizar   true = usar distancias penalizadas por estado de vía
     * @return Resultado con arreglos de distancias mínimas y nodos anteriores
     */
    public Resultado calcular(int origen, boolean penalizar) {
        int n = grafo.getNumNodos();
        double[] dist = new double[n];
        int[] anterior = new int[n];
        Arrays.fill(dist, Double.MAX_VALUE);
        Arrays.fill(anterior, -1);
        dist[origen] = 0;

        // PriorityQueue: {nodo, distancia_acumulada}
        PriorityQueue<double[]> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a[1]));
        pq.offer(new double[]{origen, 0});

        while (!pq.isEmpty()) {
            double[] actual = pq.poll();
            int u = (int) actual[0];
            double dU = actual[1];

            // Si encontramos un camino ya procesado con mejor distancia, ignorar
            if (dU > dist[u]) continue;

            for (Via v : grafo.getListaAdyacencia().get(u)) {
                int w = v.getDestino();
                double peso = penalizar ? v.getDistanciaPenalizada() : v.getDistancia();
                double nuevaDist = dist[u] + peso;

                if (nuevaDist < dist[w]) {
                    dist[w] = nuevaDist;
                    anterior[w] = u;
                    pq.offer(new double[]{w, nuevaDist});
                }
            }
        }
        return new Resultado(dist, anterior);
    }

    // =============================================
    //  RECONSTRUCCIÓN DEL CAMINO
    // =============================================

    /**
     * Reconstruye el camino desde origen hasta destino usando el arreglo anterior.
     */
    public List<Integer> reconstruirCamino(int[] anterior, int destino) {
        List<Integer> camino = new ArrayList<>();
        for (int v = destino; v != -1; v = anterior[v]) {
            camino.add(0, v);
        }
        return camino;
    }

    public String formatearCamino(List<Integer> camino) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < camino.size(); i++) {
            sb.append(grafo.getNombre(camino.get(i)));
            if (i < camino.size() - 1) sb.append(" -> ");
        }
        return sb.toString();
    }

    // =============================================
    //  MOSTRAR RESULTADOS DESDE UN ORIGEN
    // =============================================

    /**
     * Muestra la tabla completa de rutas mínimas desde el nodo origen.
     */
    public void mostrarTodasLasRutas(int origen, boolean penalizar) {
        Resultado res = calcular(origen, penalizar);
        String modo = penalizar ? "PENALIZADA (estado vía)" : "REAL (km)";

        System.out.println("\n======= DIJKSTRA desde " + grafo.getNombre(origen) +
                " [Distancia " + modo + "] =======");
        System.out.printf("%-20s %-12s %-45s%n", "Destino", "Dist (km)", "Ruta óptima");
        System.out.println("-".repeat(80));

        for (int i : grafo.getIds()) {
            if (i == origen) continue;
            String distStr = res.dist[i] == Double.MAX_VALUE ? "Inalcanzable" :
                    String.format("%.2f", res.dist[i]);
            List<Integer> camino = reconstruirCamino(res.anterior, i);
            String rutaStr = res.dist[i] == Double.MAX_VALUE ? "-" : formatearCamino(camino);
            System.out.printf("%-20s %-12s %-45s%n",
                    grafo.getNombre(i), distStr, rutaStr);
        }
        System.out.println("=".repeat(80) + "\n");
    }

    // =============================================
    //  MOSTRAR RUTA ESPECÍFICA
    // =============================================

    /**
     * Muestra la ruta óptima entre dos municipios específicos.
     */
    public void mostrarRutaEspecifica(int origen, int destino, boolean penalizar) {
        Resultado res = calcular(origen, penalizar);
        String modo = penalizar ? "penalizada" : "real";

        System.out.printf("\n--- Ruta %s -> %s [distancia %s] ---\n",
                grafo.getNombre(origen), grafo.getNombre(destino), modo);

        if (res.dist[destino] == Double.MAX_VALUE) {
            System.out.println("No existe ruta entre estos municipios.");
        } else {
            List<Integer> camino = reconstruirCamino(res.anterior, destino);
            System.out.println("  Camino: " + formatearCamino(camino));
            System.out.printf("  Distancia: %.2f km%n", res.dist[destino]);
        }
    }

    // =============================================
    //  COMPARAR RUTAS CON Y SIN PENALIZACIÓN
    // =============================================

    /**
     * Compara la ruta óptima real vs penalizada entre dos municipios.
     */
    public void compararRutas(int origen, int destino) {
        Resultado sinPen = calcular(origen, false);
        Resultado conPen  = calcular(origen, true);

        System.out.println("\n======= COMPARACIÓN: " + grafo.getNombre(origen) +
                " -> " + grafo.getNombre(destino) + " =======");

        // Sin penalización
        System.out.println("  [Sin penalización]");
        if (sinPen.dist[destino] == Double.MAX_VALUE) {
            System.out.println("    Sin ruta");
        } else {
            List<Integer> c1 = reconstruirCamino(sinPen.anterior, destino);
            System.out.printf("    Camino: %s%n", formatearCamino(c1));
            System.out.printf("    Distancia real: %.2f km%n", sinPen.dist[destino]);
        }

        // Con penalización
        System.out.println("  [Con penalización por estado de vía]");
        if (conPen.dist[destino] == Double.MAX_VALUE) {
            System.out.println("    Sin ruta");
        } else {
            List<Integer> c2 = reconstruirCamino(conPen.anterior, destino);
            System.out.printf("    Camino: %s%n", formatearCamino(c2));
            System.out.printf("    Distancia ajustada: %.2f km%n", conPen.dist[destino]);
        }
        System.out.println("=".repeat(50));
    }
}
