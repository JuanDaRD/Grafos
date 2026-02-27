package co.edu.remington.grafos.principal;

import co.edu.remington.grafos.algoritmos.Dijkstra;
import co.edu.remington.grafos.algoritmos.RecorridoBFS;
import co.edu.remington.grafos.algoritmos.RecorridoDFS;
import co.edu.remington.grafos.modelo.GrafoCasanare;
import co.edu.remington.grafos.utilidades.MatrizAdyacencia;

import java.util.List;
import java.util.Scanner;

/**
 * ============================================================
 *  Sistema de Rutas - Red Vial de Casanare
 *  Universidad Remington — Ingeniería de Sistemas
 *  Taller de Grafos en Java — 2026-I
 * ============================================================
 */
public class Main {

    static GrafoCasanare grafo = new GrafoCasanare();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE RUTAS - RED VIAL DE CASANARE           ║");
        System.out.println("║   Universidad Remington — Estructuras de Datos       ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        // Inicializar datos del taller
        grafo.inicializarDatosCasanare();
        System.out.println("\n✓ Red vial de Casanare cargada correctamente.\n");

        // Demostración automática de todas las partes del taller
        ejecutarDemostracion();

        // Menú interactivo
        menuPrincipal();
    }

    // ===========================================================
    //  DEMOSTRACIÓN AUTOMÁTICA (muestra resultados de todas las partes)
    // ===========================================================

    static void ejecutarDemostracion() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("   PARTE A — MODELADO DEL GRAFO");
        System.out.println("=".repeat(60));

        grafo.mostrarListaAdyacencia();
        grafo.mostrarMatrizAdyacencia();

        MatrizAdyacencia mAdj = new MatrizAdyacencia(grafo);
        mAdj.mostrarConGrados();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("   PARTE B — ALGORITMOS DE RECORRIDO");
        System.out.println("=".repeat(60));

        RecorridoBFS bfs = new RecorridoBFS(grafo);
        bfs.ejecutar(0); // desde Yopal

        RecorridoDFS dfs = new RecorridoDFS(grafo);
        dfs.ejecutar(0); // desde Yopal

        // Respuesta a la pregunta del taller
        System.out.println("ANÁLISIS DE CONECTIVIDAD:");
        System.out.println("  ¿El grafo es conexo? " + (grafo.esConexo() ? "SÍ" : "NO"));
        System.out.println("  Todos los 10 municipios son accesibles desde Yopal.");
        System.out.println("  No existen municipios desconectados de la red.\n");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("   PARTE C — RUTAS MÁS CORTAS (DIJKSTRA)");
        System.out.println("=".repeat(60));

        Dijkstra dijk = new Dijkstra(grafo);

        // Todas las rutas desde Yopal sin penalización
        dijk.mostrarTodasLasRutas(0, false);

        // Rutas específicas solicitadas en el taller
        System.out.println("\n--- Rutas específicas del taller (sin penalización) ---");
        dijk.mostrarRutaEspecifica(0, 6, false); // Yopal -> Monterrey
        dijk.mostrarRutaEspecifica(0, 4, false); // Yopal -> Orocué
        dijk.mostrarRutaEspecifica(9, 5, false); // Hato Corozal -> Villanueva

        // Comparación con penalización
        System.out.println("\n--- COMPARACIÓN con penalización por estado de vía ---");
        dijk.compararRutas(0, 6); // Yopal -> Monterrey
        dijk.compararRutas(0, 4); // Yopal -> Orocué
        dijk.compararRutas(9, 5); // Hato Corozal -> Villanueva

        System.out.println("\n" + "=".repeat(60));
        System.out.println("   PARTE D — FUNCIONALIDADES ADICIONALES");
        System.out.println("=".repeat(60));

        // Conectividad
        System.out.println("\n[Conectividad del grafo]");
        boolean conexo = grafo.esConexo();
        System.out.println("  El grafo es " + (conexo ? "CONEXO" : "NO CONEXO") +
                ": todos los municipios están conectados entre sí.");

        // Municipios puente
        System.out.println("\n[Municipios Puente (puntos críticos de articulación)]");
        List<Integer> puentes = grafo.encontrarMunicipiosPuente();
        if (puentes.isEmpty()) {
            System.out.println("  No se encontraron municipios puente.");
        } else {
            System.out.println("  Los siguientes municipios son críticos:");
            for (int id : puentes) {
                System.out.println("    ► " + grafo.getNombre(id) +
                        " (ID: " + id + ") — su eliminación desconectaría la red.");
            }
        }
        System.out.println();
    }

    // ===========================================================
    //  MENÚ INTERACTIVO
    // ===========================================================

    static void menuPrincipal() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n╔══════════════════════════════════════════════╗");
            System.out.println("║        MENÚ PRINCIPAL - SISTEMA CASANARE    ║");
            System.out.println("╠══════════════════════════════════════════════╣");
            System.out.println("║  1. Ver lista de municipios                  ║");
            System.out.println("║  2. Ver matriz de adyacencia                 ║");
            System.out.println("║  3. Recorrido BFS desde un municipio         ║");
            System.out.println("║  4. Recorrido DFS desde un municipio         ║");
            System.out.println("║  5. Ruta más corta entre dos municipios      ║");
            System.out.println("║  6. Todas las rutas desde un municipio       ║");
            System.out.println("║  7. Comparar rutas (normal vs penalizada)    ║");
            System.out.println("║  8. Verificar conectividad del grafo         ║");
            System.out.println("║  9. Identificar municipios puente            ║");
            System.out.println("║ 10. Agregar nuevo municipio                  ║");
            System.out.println("║ 11. Agregar nueva vía                        ║");
            System.out.println("║  0. Salir                                    ║");
            System.out.println("╚══════════════════════════════════════════════╝");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1:  mostrarMunicipios();              break;
                case 2:  grafo.mostrarMatrizAdyacencia();  break;
                case 3:  menuBFS();                        break;
                case 4:  menuDFS();                        break;
                case 5:  menuRutaEspecifica();             break;
                case 6:  menuTodasLasRutas();              break;
                case 7:  menuCompararRutas();              break;
                case 8:  verificarConectividad();          break;
                case 9:  mostrarPuentes();                 break;
                case 10: agregarMunicipio();               break;
                case 11: agregarVia();                     break;
                case 0:
                    System.out.println("\n¡Hasta luego! Sistema de Rutas Casanare finalizado.");
                    break;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
        }
    }

    // ---- Opción 1 ----
    static void mostrarMunicipios() {
        System.out.println("\n=== MUNICIPIOS REGISTRADOS ===");
        for (int id : grafo.getIds()) {
            System.out.printf("  [%2d] %s%n", id, grafo.getNombre(id));
        }
        System.out.println("Total: " + grafo.getMunicipios().size() + " municipios\n");
    }

    // ---- Opción 3 ----
    static void menuBFS() {
        mostrarMunicipios();
        System.out.print("Ingrese el ID del municipio de inicio: ");
        int origen = leerEntero();
        if (!grafo.getMunicipios().containsKey(origen)) {
            System.out.println("ID inválido.");
            return;
        }
        new RecorridoBFS(grafo).ejecutar(origen);
    }

    // ---- Opción 4 ----
    static void menuDFS() {
        mostrarMunicipios();
        System.out.print("Ingrese el ID del municipio de inicio: ");
        int origen = leerEntero();
        if (!grafo.getMunicipios().containsKey(origen)) {
            System.out.println("ID inválido.");
            return;
        }
        new RecorridoDFS(grafo).ejecutar(origen);
    }

    // ---- Opción 5 ----
    static void menuRutaEspecifica() {
        mostrarMunicipios();
        System.out.print("ID del municipio origen: ");
        int origen = leerEntero();
        System.out.print("ID del municipio destino: ");
        int destino = leerEntero();

        if (!grafo.getMunicipios().containsKey(origen) || !grafo.getMunicipios().containsKey(destino)) {
            System.out.println("ID inválido.");
            return;
        }

        System.out.print("¿Aplicar penalización por estado de vías? (s/n): ");
        boolean pen = sc.nextLine().trim().equalsIgnoreCase("s");
        new Dijkstra(grafo).mostrarRutaEspecifica(origen, destino, pen);
    }

    // ---- Opción 6 ----
    static void menuTodasLasRutas() {
        mostrarMunicipios();
        System.out.print("ID del municipio origen: ");
        int origen = leerEntero();
        if (!grafo.getMunicipios().containsKey(origen)) {
            System.out.println("ID inválido.");
            return;
        }
        System.out.print("¿Aplicar penalización por estado de vías? (s/n): ");
        boolean pen = sc.nextLine().trim().equalsIgnoreCase("s");
        new Dijkstra(grafo).mostrarTodasLasRutas(origen, pen);
    }

    // ---- Opción 7 ----
    static void menuCompararRutas() {
        mostrarMunicipios();
        System.out.print("ID del municipio origen: ");
        int origen = leerEntero();
        System.out.print("ID del municipio destino: ");
        int destino = leerEntero();

        if (!grafo.getMunicipios().containsKey(origen) || !grafo.getMunicipios().containsKey(destino)) {
            System.out.println("ID inválido.");
            return;
        }
        new Dijkstra(grafo).compararRutas(origen, destino);
    }

    // ---- Opción 8 ----
    static void verificarConectividad() {
        boolean conexo = grafo.esConexo();
        System.out.println("\n[Conectividad]");
        if (conexo) {
            System.out.println("  ✓ El grafo ES CONEXO. Todos los municipios están interconectados.");
        } else {
            System.out.println("  ✗ El grafo NO es conexo. Existen municipios sin conexión.");
        }
    }

    // ---- Opción 9 ----
    static void mostrarPuentes() {
        List<Integer> puentes = grafo.encontrarMunicipiosPuente();
        System.out.println("\n[Municipios Puente]");
        if (puentes.isEmpty()) {
            System.out.println("  No se encontraron puntos críticos de articulación.");
        } else {
            for (int id : puentes) {
                System.out.println("  ► " + grafo.getNombre(id) + " (ID: " + id + ")");
            }
        }
    }

    // ---- Opción 10 ----
    static void agregarMunicipio() {
        System.out.print("Ingrese el ID del nuevo municipio: ");
        int id = leerEntero();
        if (grafo.getMunicipios().containsKey(id)) {
            System.out.println("Ya existe un municipio con ese ID.");
            return;
        }
        System.out.print("Ingrese el nombre del municipio: ");
        String nombre = sc.nextLine().trim();
        grafo.agregarMunicipio(id, nombre);
        System.out.println("✓ Municipio '" + nombre + "' agregado con ID " + id);
    }

    // ---- Opción 11 ----
    static void agregarVia() {
        mostrarMunicipios();
        System.out.print("ID del municipio origen: ");
        int origen = leerEntero();
        System.out.print("ID del municipio destino: ");
        int destino = leerEntero();
        System.out.print("Distancia en km: ");
        double dist;
        try {
            dist = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Distancia inválida.");
            return;
        }
        System.out.print("Estado de la vía (Bueno/Regular/Malo): ");
        String estado = sc.nextLine().trim();
        if (!estado.equals("Bueno") && !estado.equals("Regular") && !estado.equals("Malo")) {
            System.out.println("Estado inválido. Use: Bueno, Regular o Malo.");
            return;
        }
        grafo.agregarVia(origen, destino, dist, estado);
        System.out.printf("✓ Vía agregada: %s <-> %s (%.1f km, %s)%n",
                grafo.getNombre(origen), grafo.getNombre(destino), dist, estado);
    }

    // ---- Utilidad ----
    static int leerEntero() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
