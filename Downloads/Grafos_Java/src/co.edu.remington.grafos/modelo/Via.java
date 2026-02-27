package co.edu.remington.grafos.modelo;

/**
 * Clase que representa una arista (vía) del grafo de municipios de Casanare.
 * Cada vía conecta dos municipios con una distancia y un estado de la vía.
 */
public class Via {
    private int destino;
    private double distancia; // en kilómetros
    private String estado;    // "Bueno", "Regular", "Malo"

    public Via(int destino, double distancia, String estado) {
        this.destino = destino;
        this.distancia = distancia;
        this.estado = estado;
    }

    public int getDestino() {
        return destino;
    }

    public double getDistancia() {
        return distancia;
    }

    public String getEstado() {
        return estado;
    }

    /**
     * Retorna la distancia penalizada según el estado de la vía.
     * Malo: x1.5 | Regular: x1.2 | Bueno: x1.0
     */
    public double getDistanciaPenalizada() {
        switch (estado) {
            case "Malo":    return distancia * 1.5;
            case "Regular": return distancia * 1.2;
            default:        return distancia;
        }
    }

    @Override
    public String toString() {
        return String.format("-> Destino: %d | %.1f km | Estado: %s", destino, distancia, estado);
    }
}
