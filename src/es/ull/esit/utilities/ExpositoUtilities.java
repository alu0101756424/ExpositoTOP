package es.ull.esit.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Set;

/**
 * @file ExpositoUtilities.java
 * @brief Utilidades generales auxiliares usadas en el proyecto.
 * @details Proporciona funciones de formato de salida, comprobaciones numéricas,
 *          operaciones con matrices, lectura/escritura de ficheros y utilidades
 *          para verificar aciclicidad en matrices de adyacencia/distancias.
 * @author alu0101756424
 * @version 1.0
 * @date 2025-10-31
 */
public class ExpositoUtilities {

    /** Ancho de columna por defecto para formateos. */
    public static final int DEFAULT_COLUMN_WIDTH = 10;
    /** Constante para alineación a la izquierda. */
    public static final int ALIGNMENT_LEFT = 1;
    /** Constante para alineación a la derecha. */
    public static final int ALIGNMENT_RIGHT = 2;

    /**
     * @brief Busca la primera aparición de un elemento en un vector de enteros.
     * @param vector Arreglo donde buscar.
     * @param element Elemento a localizar.
     * @return Índice de la primera aparición o -1 si no se encuentra.
     */
    private static int getFirstAppearance(int[] vector, int element) {
        for (int i = 0; i < vector.length; i++) {
            if (vector[i] == element) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @brief Imprime el contenido de un fichero en la salida estándar.
     * @param file Ruta al fichero a imprimir.
     * @note Los errores de E/S se registran en el logger.
     */
    public static void printFile(String file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(ExpositoUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @brief Normaliza una cadena sustituyendo tabuladores por espacios y reduciendo múltiples espacios a uno.
     * @param string Cadena a simplificar.
     * @return Cadena simplificada y recortada.
     */
    public static String simplifyString(String string) {
        string = string.replace("\t", " ");
        for (int i = 0; i < 50; i++) {
            string = string.replace("  ", " ");
        }
        string = string.trim();
        return string;
    }

    /**
     * @brief Multiplica dos matrices (producto matricial).
     * @param a Matriz A (m x n).
     * @param b Matriz B (n x p).
     * @return Resultado (m x p) o matriz vacía si A está vacía, o null si dimensiones no compatibles.
     */
    public static double[][] multiplyMatrices(double a[][], double b[][]) {
        if (a.length == 0) {
            return new double[0][0];
        }
        if (a[0].length != b.length) {
            return null;
        }
        int n = a[0].length;
        int m = a.length;
        int p = b[0].length;
        double[][] ans = new double[m][p];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < p; j++) {
                for (int k = 0; k < n; k++) {
                    ans[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return ans;
    }

    /**
     * @brief Escribe un texto en un fichero (sobrescribe).
     * @param file Ruta del fichero de salida.
     * @param text Texto a escribir.
     * @throws IOException Si hay error al acceder al fichero.
     * @note Los errores se registran en el logger; la excepción se propaga si ocurre.
     */
    public static void writeTextToFile(String file, String text) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(text);
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(ExpositoUtilities.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

    }

    /**
     * @brief Devuelve una cadena formateada si la entrada es numérica (int/double).
     * @param string Texto a formatear.
     * @return Cadena original o formateada si es double.
     */
    public static String getFormat(String string) {
        if (!ExpositoUtilities.isInteger(string)) {
            if (ExpositoUtilities.isDouble(string)) {
                double value = Double.parseDouble(string);
                string = ExpositoUtilities.getFormat(value);
            }
        }
        return string;
    }

    /**
     * @brief Formatea un número double con 3 decimales y punto como separador.
     * @param value Valor a formatear.
     * @return Cadena con formato "0.000".
     */
    public static String getFormat(double value) {
        DecimalFormat decimalFormatter = new DecimalFormat("0.000");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        decimalFormatter.setDecimalFormatSymbols(symbols);
        return decimalFormatter.format(value);
    }

    /**
     * @brief Formatea un número double con un número variable de ceros decimales.
     * @param value Valor a formatear.
     * @param zeros Cantidad de decimales.
     * @return Cadena formateada.
     */
    public static String getFormat(double value, int zeros) {
        String format = "0.";
        for (int i = 0; i < zeros; i++) {
            format += "0";
        }
        DecimalFormat decimalFormatter = new DecimalFormat(format);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        decimalFormatter.setDecimalFormatSymbols(symbols);
        return decimalFormatter.format(value);
    }

    /**
     * @brief Formatea una cadena dentro de una columna con alineación por defecto (derecha).
     * @param string Texto a formatear.
     * @param width Ancho de la columna.
     * @return Cadena formateada.
     */
    public static String getFormat(String string, int width) {
        return ExpositoUtilities.getFormat(string, width, ExpositoUtilities.ALIGNMENT_RIGHT);
    }

    /**
     * @brief Formatea una cadena con ancho y alineación especificados.
     * @param string Texto a formatear.
     * @param width Ancho de la columna.
     * @param alignment ALIGNMENT_LEFT o ALIGNMENT_RIGHT.
     * @return Cadena formateada según especificaciones.
     */
    public static String getFormat(String string, int width, int alignment) {
        String format = "";
        if (alignment == ExpositoUtilities.ALIGNMENT_LEFT) {
            format = "%-" + width + "s";
        } else {
            format = "%" + 1 + "$" + width + "s";
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        String[] data = new String[]{string};
        return String.format(format, (Object[]) data);
    }

    /**
     * @brief Formatea una lista de cadenas alineadas en columnas de igual ancho.
     * @param strings Lista de cadenas.
     * @param width Ancho de columna.
     * @return Cadena resultante con los elementos formateados.
     */
    public static String getFormat(ArrayList<String> strings, int width) {
        String format = "";
        for (int i = 0; i < strings.size(); i++) {
            format += "%" + (i + 1) + "$" + width + "s";
        }
        String[] data = new String[strings.size()];
        for (int t = 0; t < strings.size(); t++) {
            data[t] = "" + ExpositoUtilities.getFormat(strings.get(t));
        }
        return String.format(format, (Object[]) data);
    }

    /**
     * @brief Formatea una lista de enteros con la anchura por defecto.
     * @param strings Lista de enteros.
     * @return Cadena formateada.
     */
    public static String getFormat(ArrayList<Integer> strings) {
        String format = "";
        for (int i = 0; i < strings.size(); i++) {
            format += "%" + (i + 1) + "$" + DEFAULT_COLUMN_WIDTH + "s";
        }
        Integer[] data = new Integer[strings.size()];
        for (int t = 0; t < strings.size(); t++) {
            data[t] = strings.get(t);
        }
        return String.format(format, (Object[]) data);
    }

    /**
     * @brief Formatea un arreglo de cadenas con un ancho fijo por columna.
     * @param strings Arreglo de cadenas.
     * @param width Ancho por columna.
     * @return Cadena formateada.
     */
    public static String getFormat(String[] strings, int width) {
        int[] alignment = new int[strings.length];
        Arrays.fill(alignment, ExpositoUtilities.ALIGNMENT_RIGHT);
        int[] widths = new int[strings.length];
        Arrays.fill(widths, width);
        return ExpositoUtilities.getFormat(strings, widths, alignment);
    }

    /**
     * @brief Formatea una matriz de cadenas (filas) con ancho fijo por columna.
     * @param matrixStrings Matriz de cadenas por filas.
     * @param width Ancho por columna.
     * @return Resultado con filas separadas por salto de línea.
     */
    public static String getFormat(String[][] matrixStrings, int width) {
        String result = "";
        for (int i = 0; i < matrixStrings.length; i++) {
            String[] strings = matrixStrings[i];
            int[] alignment = new int[strings.length];
            Arrays.fill(alignment, ExpositoUtilities.ALIGNMENT_RIGHT);
            int[] widths = new int[strings.length];
            Arrays.fill(widths, width);
            result += ExpositoUtilities.getFormat(strings, widths, alignment);
            if (i < (matrixStrings.length - 1)) {
                result += "\n";
            }
        }
        return result;
    }

    /**
     * @brief Formatea un arreglo de cadenas con anchura por defecto.
     * @param strings Arreglo de cadenas.
     * @return Cadena formateada.
     */
    public static String getFormat(String[] strings) {
        int[] alignment = new int[strings.length];
        Arrays.fill(alignment, ExpositoUtilities.ALIGNMENT_RIGHT);
        int[] widths = new int[strings.length];
        Arrays.fill(widths, ExpositoUtilities.DEFAULT_COLUMN_WIDTH);
        return ExpositoUtilities.getFormat(strings, widths, alignment);
    }

    /**
     * @brief Formatea un arreglo de cadenas con anchuras individuales.
     * @param strings Arreglo de cadenas.
     * @param width Arreglo de anchos por columna.
     * @return Cadena formateada.
     */
    public static String getFormat(String[] strings, int[] width) {
        int[] alignment = new int[strings.length];
        Arrays.fill(alignment, ExpositoUtilities.ALIGNMENT_RIGHT);
        return ExpositoUtilities.getFormat(strings, width, alignment);
    }

    /**
     * @brief Formatea un arreglo de cadenas con anchuras y alineaciones individualizadas.
     * @param strings Arreglo de cadenas.
     * @param width Arreglo de anchos por columna.
     * @param alignment Arreglo de alineaciones (ALIGNMENT_LEFT/RIGHT).
     * @return Cadena formateada.
     */
    public static String getFormat(String[] strings, int[] width, int[] alignment) {
        String format = "";
        for (int i = 0; i < strings.length; i++) {
            if (alignment[i] == ExpositoUtilities.ALIGNMENT_LEFT) {
                format += "%" + (i + 1) + "$-" + width[i] + "s";
            } else {
                format += "%" + (i + 1) + "$" + width[i] + "s";
            }
        }
        String[] data = new String[strings.length];
        for (int t = 0; t < strings.length; t++) {
            data[t] = "" + ExpositoUtilities.getFormat(strings[t]);
        }
        return String.format(format, (Object[]) data);
    }

    /**
     * @brief Comprueba si una cadena representa un entero válido.
     * @param str Cadena a comprobar.
     * @return true si puede convertirse a Integer, false en caso contrario.
     */
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * @brief Comprueba si una cadena representa un double válido.
     * @param str Cadena a comprobar.
     * @return true si puede convertirse a Double, false en caso contrario.
     */
    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * @brief Comprueba si una matriz de distancias representa un grafo acíclico (considerando nodos reales).
     * @param distanceMatrix Matriz de distancias/adyacencia con Integer.MAX_VALUE como ausencia de arista.
     * @return true si no existe ciclo, false si se detecta un camino que regresa al nodo.
     */
    public static boolean isAcyclic(int[][] distanceMatrix) {
        int numRealTasks = distanceMatrix.length - 2;
        int node = 1;
        boolean acyclic = true;
        while (acyclic && node <= numRealTasks) {
            if (ExpositoUtilities.thereIsPath(distanceMatrix, node)) {
                return false;
            }
            node++;
        }
        return true;
    }

    /**
     * @brief Comprueba si existe un camino que vuelva al nodo indicado (búsqueda por anchura).
     * @param distanceMatrix Matriz de distancias.
     * @param node Nodo de inicio.
     * @return true si existe una ruta de retorno al nodo, false en caso contrario.
     */
    public static boolean thereIsPath(int[][] distanceMatrix, int node) {
        int n = distanceMatrix.length;
        Set<Integer> unvisited = initializeUnvisited(n, node);
        Queue<Integer> queue = new LinkedList<>();
        queue.add(node);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            if (checkConnections(distanceMatrix, node, n, unvisited, queue, current)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @brief Método auxiliar para explorar conexiones desde el nodo actual.
     * @param distanceMatrix Matriz de distancias.
     * @param node Nodo de inicio (comprobación de retorno).
     * @param n Número de nodos.
     * @param unvisited Conjunto de nodos no visitados.
     * @param queue Cola de BFS.
     * @param current Nodo actualmente explorado.
     * @return true si se encuentra un camino de retorno al nodo, false en otro caso.
     */
    private static boolean checkConnections(int[][] distanceMatrix, int node, int n,
                                            Set<Integer> unvisited, Queue<Integer> queue, int current) {
        for (int i = 0; i < n; i++) {
            if (shouldSkip(distanceMatrix, current, i)) continue;
            if (i == node) return true;
            if (unvisited.remove(i)) queue.add(i);
        }
        return false;
    }

    /**
     * @brief Determina si debe omitirse la comprobación entre dos nodos.
     * @param distanceMatrix Matriz de distancias.
     * @param current Nodo origen.
     * @param i Nodo destino.
     * @return true si debe omitirse (misma posición o ausencia de arista), false en otro caso.
     */
    private static boolean shouldSkip(int[][] distanceMatrix, int current, int i) {
        return i == current || distanceMatrix[current][i] == Integer.MAX_VALUE;
    }

    /**
     * @brief Inicializa el conjunto de nodos no visitados para la búsqueda BFS.
     * @param n Número total de nodos.
     * @param node Nodo que no debe incluirse en el conjunto.
     * @return Conjunto de nodos no visitados (excluyendo *node*).
     */
    private static Set<Integer> initializeUnvisited(int n, int node) {
        Set<Integer> unvisited = new HashSet<>();
        for (int i = 0; i < n; i++) {
            if (i != node) unvisited.add(i);
        }
        return unvisited;
    }
}
