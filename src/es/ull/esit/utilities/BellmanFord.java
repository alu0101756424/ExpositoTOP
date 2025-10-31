
package es.ull.esit.utilities;

import java.util.ArrayList;

/**
 * @file BellmanFord.java
 * @brief Implementa el algoritmo de Bellman-Ford para cálculo de caminos mínimos.
 * @details Dado una matriz de distancias y un número de nodos, calcula la distancia
 *          mínima desde el nodo 0 al nodo nodes-1, reconstruyendo además el camino.
 *          Se considera INFINITY para inicializaciones y se almacenan las aristas
 *          explícitamente para las relajaciones.
 */
public class BellmanFord {

    /** Valor de infinito usado para inicializaciones. */
    private static final int INFINITY = 999999;

    /** Matriz de distancias del grafo. */
    private final int[][] distanceMatrix;

    /** Lista paralela con origen de cada arista (u). */
    private ArrayList<Integer> edges1 = null;

    /** Lista paralela con destino de cada arista (v). */
    private ArrayList<Integer> edges2 = null;

    /** Número de nodos del grafo. */
    private final int nodes;

    /** Lista donde se guardará el camino (de destino hacia origen tras reconstruir). */
    private final ArrayList<Integer> path;

    /** Distancias mínimas calculadas desde el nodo 0 a todos los nodos. */
    private int[] distances = null;

    /** Valor resultado asociado al camino calculado (se usa negativo de la distancia final). */
    private int value;

    /**
     * @brief Constructor.
     * @param distanceMatrix Matriz de distancias entre nodos.
     * @param nodes Número de nodos del grafo.
     * @param path Lista donde se almacenará el camino reconstruido.
     */
    public BellmanFord(int[][] distanceMatrix, int nodes, ArrayList<Integer> path) {
        this.distanceMatrix = distanceMatrix;
        this.nodes = nodes;
        this.path = path;
        this.calculateEdges();
        this.value = BellmanFord.INFINITY;
    }

    /**
     * @brief Construye las listas de aristas (origen, destino) a partir de la matriz.
     * @details Se consideran como aristas todas las entradas que no sean Integer.MAX_VALUE.
     */
    private void calculateEdges() {
        this.edges1 = new ArrayList<>();
        this.edges2 = new ArrayList<>();
        for (int i = 0; i < this.nodes; i++) {
            for (int j = 0; j < this.nodes; j++) {
                if (this.distanceMatrix[i][j] != Integer.MAX_VALUE) {
                    this.edges1.add(i);
                    this.edges2.add(j);
                }
            }
        }
    }

    /**
     * @brief Obtiene el arreglo de distancias mínimas calculadas.
     * @return Arreglo de enteros con la distancia mínima desde el nodo 0 a cada nodo.
     *         Puede ser null hasta que no se invoque {@link #solve()}.
     */
    public int[] getDistances() {
        return this.distances;
    }

    /**
     * @brief Obtiene el valor asociado al resultado.
     * @return Entero con el valor calculado; en esta implementación se asigna
     *         como el negativo de la distancia al nodo destino (nodes-1) tras resolver.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * @brief Ejecuta el algoritmo de Bellman-Ford para calcular distancias y camino.
     * @details Inicializa distancias y predecesores, relaja aristas (nodes-1) veces,
     *          reconstruye el camino desde el nodo destino (nodes-1) hacia el origen
     *          y ajusta el atributo value como negativo de la distancia final.
     */
    public void solve() {
        int numEdges = this.edges1.size();
        int[] predecessor = new int[this.nodes];
        this.distances = new int[this.nodes];
        for (int i = 0; i < this.nodes; i++) {
            this.distances[i] = BellmanFord.INFINITY;
            predecessor[i] = -1;
        }
        this.distances[0] = 0;
        for (int i = 0; i < (this.nodes - 1); i++) {
            for (int j = 0; j < numEdges; j++) {
                int u = this.edges1.get(j);
                int v = this.edges2.get(j);
                if (this.distances[v] > this.distances[u] + this.distanceMatrix[u][v]) {
                    this.distances[v] = this.distances[u] + this.distanceMatrix[u][v];
                    predecessor[v] = u;
                }
            }
        }
        // Reconstruir camino desde el nodo destino (nodes-1) hacia el origen
        this.path.add(this.nodes - 1);
        int pred = predecessor[this.nodes - 1];
        while (pred != -1) {
            this.path.add(pred);
            pred = predecessor[pred];
        }
        // En esta clase se utiliza value como negativo de la distancia al destino
        this.value = -this.distances[this.nodes - 1];
    }
}
