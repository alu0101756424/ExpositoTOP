/**
 * @file TOPTW.java
 * @brief Clase que representa una instancia del problema TOPTW (Team Orienteering Problem with Time Windows).
 * @details Provee almacenamiento de nodos, coordenadas, ventanas temporales, tiempos de servicio,
 *          matriz de distancias y utilidades para calcular distancias entre nodos y representar la instancia.
 * @author alu0101756424
 * @version 1.0
 * @date 2025-10-31
 */
package top;

import java.util.ArrayList;
import java.util.Arrays;

import es.ull.esit.utilities.ExpositoUtilities;

/**
 * @class TOPTW
 * @brief Modelo de instancia TOPTW.
 *
 * Representa una colección de puntos de interés (POIs) y un depósito (índice 0),
 * con coordenadas, puntuaciones, ventanas temporales, tiempos de servicio y matriz
 * de distancias. Ofrece utilidades para calcular distancias entre rutas y nodos,
 * así como parámetros relativos a vehículos y rutas.
 */
public class TOPTW {
    /** Número de nodos (POIs) en la instancia. */
    private int nodes;
    /** Coordenadas X de los nodos (incluye índice 0 como depósito). */
    private double[] x;
    /** Coordenadas Y de los nodos (incluye índice 0 como depósito). */
    private double[] y;
    /** Puntuación de cada nodo. */
    private double[] score;
    /** Tiempo de apertura (ready time) de cada nodo. */
    private double[] readyTime;
    /** Tiempo de cierre (due time) de cada nodo. */
    private double[] dueTime;
    /** Tiempo de servicio en cada nodo. */
    private double[] serviceTime;
    /** Número de vehículos disponibles. */
    private int vehicles;
    /** Número de depósitos adicionales (si los hubiera). */
    private int depots;
    /** Tiempo máximo permitido por ruta. */
    private double maxTimePerRoute;
    /** Número máximo de rutas permitidas. */
    private double maxRoutes;
    /** Matriz de distancias simétrica entre nodos. */
    private double[][] distanceMatrix;

    /**
     * @brief Constructor básico.
     * @param nodes Número de nodos (POIs) a modelar.
     * @param routes Número máximo de rutas/vehículos.
     */
    public TOPTW(int nodes, int routes) {
        this.nodes = nodes;
        this.depots = 0;
        this.x = new double[this.nodes + 1];
        this.y = new double[this.nodes + 1];
        this.score = new double[this.nodes + 1];
        this.readyTime = new double[this.nodes + 1];
        this.dueTime = new double[this.nodes + 1];
        this.serviceTime = new double[this.nodes + 1];
        this.distanceMatrix = new double[this.nodes + 1][this.nodes + 1];
        for (int i = 0; i < this.nodes + 1; i++) {
            for (int j = 0; j < this.nodes + 1; j++) {
                this.distanceMatrix[i][j] = 0.0;
            }
        }
        this.maxRoutes = routes;
        this.vehicles = routes;
    }

    /**
     * @brief Comprueba si un índice corresponde a un depósito externo.
     * @param a Índice a comprobar.
     * @return true si el índice es mayor que el número de nodos (se considera depósito externo).
     */
    public boolean isDepot(int a) {
        if(a > this.nodes) {
            return true;
        }
        return false;
    }

    /**
     * @brief Calcula la distancia total de una ruta representada por un arreglo de enteros.
     * @param route Arreglo con la secuencia de nodos de la ruta.
     * @return Distancia total de la ruta.
     */
    public double getDistance(int[] route) {
        double distance = 0.0;
        for (int i = 0; i < route.length - 1; i++) {
            int node1 = route[i];
            int node2 = route[i + 1];
            distance += this.getDistance(node1, node2);
        }
        return distance;
    }

    /**
     * @brief Calcula la distancia total de una ruta representada por un ArrayList de Integer.
     * @param route Lista con la secuencia de nodos de la ruta.
     * @return Distancia total de la ruta.
     */
    public double getDistance(ArrayList<Integer> route) {
        double distance = 0.0;
        for (int i = 0; i < route.size() - 1; i++) {
            int node1 = route.get(i);
            int node2 = route.get(i + 1);
            distance += this.getDistance(node1, node2);
        }
        return distance;
    }

    /**
     * @brief Calcula la distancia total de varias rutas.
     * @param routes Arreglo de rutas, cada una como ArrayList\<Integer\>.
     * @return Distancia total combinada de todas las rutas.
     */
    public double getDistance(ArrayList<Integer>[] routes) {
        double distance = 0.0;
        for (ArrayList<Integer> route : routes) {
            distance += this.getDistance(route);
        }
        return distance;
    }


    /**
     * @brief Rellena la matriz de distancias euclídeas entre todos los nodos.
     * @note Asume que las coordenadas X/Y están inicializadas.
     */
    public void calculateDistanceMatrix() {
        for (int i = 0; i < this.nodes + 1; i++) {
            for (int j = 0; j < this.nodes + 1; j++) {
                if (i != j) {
                    double diffXs = this.x[i] - this.x[j];
                    double diffYs = this.y[i] - this.y[j];
                    this.distanceMatrix[i][j] = Math.sqrt(diffXs * diffXs + diffYs * diffYs);
                    this.distanceMatrix[j][i] = this.distanceMatrix[i][j];
                } else {
                    this.distanceMatrix[i][j] = 0.0;
                }
            }
        }
    }

    /**
     * @brief Obtiene el tiempo máximo permitido por ruta.
     * @return Tiempo máximo por ruta.
     */
    public double getMaxTimePerRoute() {
        return maxTimePerRoute;
    }

    /**
     * @brief Establece el tiempo máximo por ruta.
     * @param maxTimePerRoute Tiempo máximo por ruta.
     */
    public void setMaxTimePerRoute(double maxTimePerRoute) {
        this.maxTimePerRoute = maxTimePerRoute;
    }

    /**
     * @brief Obtiene el número máximo de rutas permitidas.
     * @return Número máximo de rutas.
     */
    public double getMaxRoutes() {
        return maxRoutes;
    }

    /**
     * @brief Establece el número máximo de rutas permitidas.
     * @param maxRoutes Número máximo de rutas.
     */
    public void setMaxRoutes(double maxRoutes) {
        this.maxRoutes = maxRoutes;
    }

    /**
     * @brief Obtiene el número de puntos de interés (POIs).
     * @return Número de POIs.
     */
    public int getPOIs() {
        return this.nodes;
    }

    /**
     * @brief Devuelve la distancia entre dos nodos (gestiona índices de depósito).
     * @param i Índice del primer nodo.
     * @param j Índice del segundo nodo.
     * @return Distancia entre i y j.
     */
    public double getDistance(int i, int j) {
        if(this.isDepot(i)) { i=0; }
        if(this.isDepot(j)) { j=0; }
        return this.distanceMatrix[i][j];
    }

    /**
     * @brief Obtiene el tiempo de viaje entre dos nodos (actualmente coincide con la distancia).
     * @param i Índice del primer nodo.
     * @param j Índice del segundo nodo.
     * @return Tiempo de viaje entre i y j.
     */
    public double getTime(int i, int j) {
        if(this.isDepot(i)) { i=0; }
        if(this.isDepot(j)) { j=0; }
        return this.distanceMatrix[i][j];
    }

    /**
     * @brief Obtiene el número de nodos (POIs).
     * @return Número de nodos.
     */
    public int getNodes() {
        return this.nodes;
    }

    /**
     * @brief Establece el número de nodos (POIs).
     * @param nodes Nuevo número de nodos.
     */
    public void setNodes(int nodes) {
        this.nodes = nodes;
    }

    /**
     * @brief Obtiene la coordenada X de un índice (gestiona depósitos).
     * @param index Índice del nodo.
     * @return Coordenada X.
     */
    public double getX(int index) {
        if(this.isDepot(index)) { index=0; }
        return this.x[index];
    }

    /**
     * @brief Establece la coordenada X de un nodo.
     * @param index Índice del nodo.
     * @param x Valor de X.
     */
    public void setX(int index, double x) {
        this.x[index] = x;
    }

    /**
     * @brief Obtiene la coordenada Y de un índice (gestiona depósitos).
     * @param index Índice del nodo.
     * @return Coordenada Y.
     */
    public double getY(int index) {
        if(this.isDepot(index)) { index=0; }
        return this.y[index];
    }

    /**
     * @brief Establece la coordenada Y de un nodo.
     * @param index Índice del nodo.
     * @param y Valor de Y.
     */
    public void setY(int index, double y) {
        this.y[index] = y;
    }

    /**
     * @brief Obtiene la puntuación de un nodo.
     * @param index Índice del nodo.
     * @return Puntuación del nodo.
     */
    public double getScore(int index) {
        if(this.isDepot(index)) { index=0; }
        return this.score[index];
    }

    /**
     * @brief Devuelve el arreglo completo de puntuaciones.
     * @return Arreglo de puntuaciones.
     */
    public double[] getScore() {
        return this.score;
    }

    /**
     * @brief Establece la puntuación de un nodo.
     * @param index Índice del nodo.
     * @param score Valor de la puntuación.
     */
    public void setScore(int index, double score) {
        this.score[index] = score;
    }

    /**
     * @brief Obtiene el tiempo de apertura (ready time) de un nodo.
     * @param index Índice del nodo.
     * @return Ready time del nodo.
     */
    public double getReadyTime(int index) {
        if(this.isDepot(index)) { index=0; }
        return this.readyTime[index];
    }

    /**
     * @brief Establece el ready time de un nodo.
     * @param index Índice del nodo.
     * @param readyTime Tiempo de apertura.
     */
    public void setReadyTime(int index, double readyTime) {
        this.readyTime[index] = readyTime;
    }

    /**
     * @brief Obtiene la due time (tiempo límite) de un nodo.
     * @param index Índice del nodo.
     * @return Due time del nodo.
     */
    public double getDueTime(int index) {
        if(this.isDepot(index)) { index=0; }
        return this.dueTime[index];
    }

    /**
     * @brief Establece la due time de un nodo.
     * @param index Índice del nodo.
     * @param dueTime Tiempo límite.
     */
    public void setDueTime(int index, double dueTime) {
        this.dueTime[index] = dueTime;
    }

    /**
     * @brief Obtiene el tiempo de servicio de un nodo.
     * @param index Índice del nodo.
     * @return Tiempo de servicio.
     */
    public double getServiceTime(int index) {
        if(this.isDepot(index)) { index=0; }
        return this.serviceTime[index];
    }

    /**
     * @brief Establece el tiempo de servicio de un nodo.
     * @param index Índice del nodo.
     * @param serviceTime Tiempo de servicio.
     */
    public void setServiceTime(int index, double serviceTime) {
        this.serviceTime[index] = serviceTime;
    }

    /**
     * @brief Obtiene el número de vehículos disponibles.
     * @return Número de vehículos.
     */
    public int getVehicles() {
        return this.vehicles;
    }

    /**
     * @brief Representación en texto de la instancia.
     * @return Cadena con información tabulada de nodos y vehículos.
     */
    @Override
    public String toString() {
        final int COLUMN_WIDTH = 15;
        String text = "Nodes: " + this.nodes + "\n";
        String[] strings = new String[]{"CUST NO.", "XCOORD.", "YCOORD.", "SCORE", "READY TIME", "DUE DATE", "SERVICE TIME"};
        int[] width = new int[strings.length];
        Arrays.fill(width, COLUMN_WIDTH);
        text += ExpositoUtilities.getFormat(strings, width) + "\n";
        for (int i = 0; i < this.nodes; i++) {
            strings = new String[strings.length];
            int index = 0;
            //strings[index++] = Integer.toString("" + i);
            strings[index++] = Integer.toString(i);
            strings[index++] = "" + this.x[i];
            strings[index++] = "" + this.y[i];
            strings[index++] = "" + this.score[i];
            strings[index++] = "" + this.readyTime[i];
            strings[index++] = "" + this.dueTime[i];
            strings[index++] = "" + this.serviceTime[i];
            text += ExpositoUtilities.getFormat(strings, width);
            text += "\n";
        }
        text += "Vehicles: " + this.vehicles + "\n";
        strings = new String[]{"VEHICLE", "CAPACITY"};
        width = new int[strings.length];
        Arrays.fill(width, COLUMN_WIDTH);
        text += ExpositoUtilities.getFormat(strings, width) + "\n";
        return text;
    }

    /**
     * @brief Incrementa y devuelve el contador de nodos (añade un nodo).
     * @return Nuevo número de nodos.
     */
    public int addNode() {
        this.nodes++;
        return this.nodes;
    }

    /**
     * @brief Incrementa y devuelve el contador de depósitos.
     * @return Nuevo número de depósitos.
     */
    public int addNodeDepot() {
        this.depots++;
        return this.depots;
    }
}
