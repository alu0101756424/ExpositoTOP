package top;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import es.ull.esit.utilities.ExpositoUtilities;

/**
 * @file TOPTWReader.java
 * @brief Lector de ficheros para instancias TOPTW.
 * @details Proporciona utilidades para parsear un fichero de instancia y crear
 *          un objeto {@link TOPTW} inicializado con coordenadas, tiempos de servicio,
 *          ventanas temporales y puntuaciones. Asume un formato concreto en el fichero
 *          de entrada (línea de cabecera con número de rutas y POIs, seguido de filas
 *          por cada nodo incluyendo depósito en la primera posición).
 * @author alu0101756424
 * @version 1.0
 * @date 2025-10-31
 */
public class TOPTWReader {

    /**
     * @brief Lee una instancia TOPTW desde el fichero indicado.
     * @param filePath Ruta al fichero de instancia.
     * @return Instancia {@link TOPTW} construida con los datos del fichero.
     * @details El método:
     *          - Lee la primera línea para obtener el número de vehículos y POIs.
     *          - Crea un objeto {@link TOPTW} con esos valores.
     *          - Salta la línea de cabecera siguiente y lee una línea por cada nodo
     *            (incluyendo el depósito en la primera iteración).
     *          - Usa {@link ExpositoUtilities#simplifyString(String)} para normalizar
     *            espacios y separar campos.
     *          - Mapea campos a coordenadas, tiempos de servicio, score y ventanas
     *            temporales (ready/due) según la posición del nodo en el fichero.
     *          - Calcula la matriz de distancias llamando a {@link TOPTW#calculateDistanceMatrix()}.
     * @note En caso de error de E/S se escribe el error por stderr y se finaliza la JVM.
     */
    public static TOPTW readProblem(String filePath) {
        TOPTW problem = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            line = ExpositoUtilities.simplifyString(line);
            String[] parts = line.split(" ");
            problem = new TOPTW(Integer.parseInt(parts[2]), Integer.parseInt(parts[1]));

            // Saltamos la siguiente línea
            reader.readLine();

            for (int i = 0; i < problem.getPOIs() + 1; i++) {
                line = reader.readLine();
                line = ExpositoUtilities.simplifyString(line);
                parts = line.split(" ");

                problem.setX(i, Double.parseDouble(parts[1]));
                problem.setY(i, Double.parseDouble(parts[2]));
                problem.setServiceTime(i, Double.parseDouble(parts[3]));
                problem.setScore(i, Double.parseDouble(parts[4]));

                if (i == 0) {
                    // Para el depósito algunas columnas de TW están en posiciones distintas
                    problem.setReadyTime(i, Double.parseDouble(parts[7]));
                    problem.setDueTime(i, Double.parseDouble(parts[8]));
                } else {
                    problem.setReadyTime(i, Double.parseDouble(parts[8]));
                    problem.setDueTime(i, Double.parseDouble(parts[9]));
                }
            }

            problem.calculateDistanceMatrix();

        } catch (IOException e) {
            System.err.println(e);
            System.exit(0);
        }

        problem.setMaxTimePerRoute(problem.getDueTime(0));
        return problem;
    }


}
