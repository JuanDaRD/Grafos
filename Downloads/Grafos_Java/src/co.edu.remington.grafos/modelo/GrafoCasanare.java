package co.edu.remington.grafos.modelo;

import java.util.*;

/**
 * Clase que representa el grafo completo de la red vial de Casanare.
 * Implementa tanto lista de adyacencia como matriz de adyacencia.
 */
public class GrafoCasanare {

    private Map<Integer, List<Via>> listaAdyacencia;
    private Map<Integer, String> municipios;
    private int numNodos;

    public GrafoCasanare() {
        listaAdyacencia = new HashMap<>();
        municipios = new HashMap<>();
        numNodos = 0;
    }

    // =============================================
    //  MÉTODOS DE CONSTRUCCIÓN DEL GRAFO
    // =============================================

    public void agregarMunicipio(int id, String nombre) {
        municipios.put(id, nombre);
        listaAdyacencia.putIfAbsent(id, new ArrayList<>());
        numNodos = municipios.size();
    }

    public void agregarVia(int origen, int destino, double distancia, String estado) {
        if (!listaAdyacencia.containsKey(origen) || !listaAdyacencia.containsKey(destino)) {
            System.out.println("Error: uno de los municipios no existe.");
            return;
        }
        // Grafo no dirigido: se agrega en ambas direcciones
        listaAdyacencia.get(origen).add(new Via(destino, distancia, estado));
        listaAdyacencia.get(destino).add(new Via(origen, distancia, estado));
    }

    // =============================================
    //  GETTERS
    // =============================================

    public Map<Integer, List<Via>> getListaAdyacencia() {
        return listaAdyacencia;
    }

    public Map<Integer, String> getMunicipios() {
        return municipios;
    }

    public int getNumNodos() {
        return numNodos;
    }

    public String getNombre(int id) {
        return municipios.getOrDefault(id, "Desconocido");
    }

    public List<Integer> getIds() {
        List<Integer> ids = new ArrayList<>(municipios.keySet());
        Collections.sort(ids);
        return ids;
    }

    // =============================================
    //  MOSTRAR LISTA DE ADYACENCIA
    // =============================================

    public void mostrarListaAdyacencia() {
        System.out.println("\n======= LISTA DE ADYACENCIA =======");
        for (int id : getIds()) {
            System.out.print(getNombre(id) + " (" + id + "): ");
            List<Via> vias = listaAdyacencia.get(id);
            if (vias.isEmpty()) {
                System.out.println("Sin conexiones");
            } else {
                for (Via v : vias) {
                    System.out.printf("  %s (%s) [%.1f km, %s]\n",
                            getNombre(v.getDestino()), v.getDestino(),
                            v.getDistancia(), v.getEstado());
                }
            }
        }
        System.out.println("====================================\n");
    }

    // =============================================
    //  MATRIZ DE ADYACENCIA
    // =============================================

    /**
     * Construye y retorna la matriz de adyacencia con distancias reales.
     * 0 = sin conexión directa, valor > 0 = distancia en km.
     */
    public double[][] obtenerMatrizAdyacencia() {
        int n = numNodos;
        double[][] matriz = new double[n][n];
        for (int i = 0; i < n; i++) Arrays.fill(matriz[i], 0);

        for (int id : getIds()) {
            for (Via v : listaAdyacencia.get(id)) {
                matriz[id][v.getDestino()] = v.getDistancia();
            }
        }
        return matriz;
    }

    public void mostrarMatrizAdyacencia() {
        System.out.println("\n======= MATRIZ DE ADYACENCIA (distancias en km) =======");
        double[][] m = obtenerMatrizAdyacencia();
        int n = numNodos;

        // Encabezado
        System.out.printf("%18s", "");
        for (int i = 0; i < n; i++) {
            System.out.printf("%8s", getNombre(i).substring(0, Math.min(7, getNombre(i).length())));
        }
        System.out.println();

        // Filas
        for (int i = 0; i < n; i++) {
            System.out.printf("%18s", getNombre(i).substring(0, Math.min(17, getNombre(i).length())));
            for (int j = 0; j < n; j++) {
                if (m[i][j] == 0) System.out.printf("%8s", "-");
                else System.out.printf("%8.1f", m[i][j]);
            }
            System.out.println();
        }
        System.out.println("=========================================================\n");
    }

    // =============================================
    //  CONECTIVIDAD
    // =============================================

    /**
     * Determina si el grafo es conexo usando BFS desde el nodo 0.
     */
    public boolean esConexo() {
        if (municipios.isEmpty()) return true;
        Set<Integer> visitados = new HashSet<>();
        Queue<Integer> cola = new LinkedList<>();
        int inicio = getIds().get(0);
        cola.add(inicio);
        visitados.add(inicio);

        while (!cola.isEmpty()) {
            int actual = cola.poll();
            for (Via v : listaAdyacencia.get(actual)) {
                if (!visitados.contains(v.getDestino())) {
                    visitados.add(v.getDestino());
                    cola.add(v.getDestino());
                }
            }
        }
        return visitados.size() == municipios.size();
    }

    // =============================================
    //  MUNICIPIOS PUENTE (Algoritmo de Tarjan)
    // =============================================

    private int timerPuente;

    /**
     * Identifica los municipios (nodos) cuya eliminación desconectaría el grafo.
     * Usa el algoritmo DFS de Tarjan para puntos de articulación.
     */
    public List<Integer> encontrarMunicipiosPuente() {
        int n = numNodos;
        boolean[] visitado = new boolean[n];
        int[] disc = new int[n];      // tiempo de descubrimiento
        int[] low  = new int[n];      // menor tiempo alcanzable
        int[] padre = new int[n];
        boolean[] esPuente = new boolean[n];
        Arrays.fill(padre, -1);
        timerPuente = 0;

        for (int i : getIds()) {
            if (!visitado[i]) {
                dfsPuente(i, visitado, disc, low, padre, esPuente);
            }
        }

        List<Integer> puentes = new ArrayList<>();
        for (int i : getIds()) {
            if (esPuente[i]) puentes.add(i);
        }
        return puentes;
    }

    private void dfsPuente(int u, boolean[] visitado, int[] disc, int[] low,
                            int[] padre, boolean[] esPuente) {
        visitado[u] = true;
        disc[u] = low[u] = timerPuente++;
        int hijosRaiz = 0;

        for (Via v : listaAdyacencia.get(u)) {
            int w = v.getDestino();
            if (!visitado[w]) {
                hijosRaiz++;
                padre[w] = u;
                dfsPuente(w, visitado, disc, low, padre, esPuente);
                low[u] = Math.min(low[u], low[w]);

                // u es punto de articulación si:
                // 1) es raíz y tiene más de 1 hijo, o
                // 2) no es raíz y low[w] >= disc[u]
                if (padre[u] == -1 && hijosRaiz > 1) esPuente[u] = true;
                if (padre[u] != -1 && low[w] >= disc[u])  esPuente[u] = true;

            } else if (w != padre[u]) {
                low[u] = Math.min(low[u], disc[w]);
            }
        }
    }

    // =============================================
    //  INICIALIZACIÓN DE DATOS DEL TALLER
    // =============================================

    /**
     * Carga los municipios y vías del caso práctico del taller.
     */
    public void inicializarDatosCasanare() {
        agregarMunicipio(0, "Yopal");
        agregarMunicipio(1, "Aguazul");
        agregarMunicipio(2, "Tauramena");
        agregarMunicipio(3, "Mani");
        agregarMunicipio(4, "Orocue");
        agregarMunicipio(5, "Villanueva");
        agregarMunicipio(6, "Monterrey");
        agregarMunicipio(7, "Paz de Ariporo");
        agregarMunicipio(8, "Trinidad");
        agregarMunicipio(9, "Hato Corozal");

        agregarVia(0, 1, 28,  "Bueno");
        agregarVia(0, 7, 92,  "Regular");
        agregarVia(0, 3, 65,  "Bueno");
        agregarVia(1, 2, 35,  "Bueno");
        agregarVia(1, 3, 42,  "Regular");
        agregarVia(2, 5, 48,  "Bueno");
        agregarVia(2, 6, 55,  "Malo");
        agregarVia(3, 4, 78,  "Regular");
        agregarVia(5, 6, 22,  "Bueno");
        agregarVia(7, 8, 45,  "Regular");
        agregarVia(7, 9, 38,  "Bueno");
        agregarVia(8, 4, 95,  "Malo");
        agregarVia(9, 8, 52,  "Regular");
    }
}
