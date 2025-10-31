package top;
/**
 * @file TOPTWEvaluator.java
 * @brief Evaluador de soluciones para el problema TOPTW.
 * @details Clase responsable de calcular y asignar el valor de la función objetivo
 *          para una instancia de `TOPTWSolution`. La lógica original calcula el coste
 *          acumulado por ruta (suma acumulada de distancias desde el depósito y entre
 *          sucesores). En la versión actual parte del código está comentado y se debe
 *          adaptar según la interfaz de `TOPTWSolution` y del modelo de problema.
 * @author alu0101756424
 * @version 1.0
 * @date 2025-10-31
 */
public class TOPTWEvaluator {
    /**
     * @brief Marca para indicar que una solución no ha sido evaluada.
     * @details Valor numérico usado para inicializar o detectar soluciones no evaluadas
     *          antes de ejecutar el evaluador.
     */

    public static double NO_EVALUATED = -1.0;
    /**
     * @brief Evalúa la solución indicada y asigna su valor de función objetivo.
     * @param solution Objeto `TOPTWSolution` que contiene la representación de rutas y enlaces
     *                 con el problema (matriz de distancias, índices de depósito, sucesores, etc.).
     * @details La evaluación debe:
     *          - Iterar por cada depósito/vehículo presente en la solución.
     *          - Recorrer la secuencia de nodos partiendo del depósito, acumulando distancias
     *            entre sucesores y desde/hacia el depósito.
     *          - Acumular en la función objetivo los costes acumulados por cada etapa (si esa
     *            es la métrica deseada).
     * @note El cuerpo del método en el repositorio está comentado; restaurar y adaptar la lógica
     *       según la implementación concreta de `TOPTWSolution` y del modelo de problema.
     * @see TOPTWSolution
     */
    public void evaluate(TOPTWSolution solution) {
        /*CumulativeCVRP problem = solution.getProblem();
        double objectiveFunctionValue = 0.0;
        for (int i = 0; i < solution.getIndexDepot().size(); i++) {
            double cumulative = 0;
            int depot = solution.getAnIndexDepot(i);
            int actual = depot;
            actual = solution.getSuccessor(actual);
            cumulative += problem.getDistanceMatrix(0, actual);
            objectiveFunctionValue += problem.getDistanceMatrix(0, actual);
            System.out.println("Desde " + 0 + " a " + actual + " = " + cumulative);
            while (actual != depot) {
                int ant = actual;
                actual = solution.getSuccessor(actual);
                if (actual != depot) {
                    cumulative += problem.getDistanceMatrix(ant, actual);
                    objectiveFunctionValue += cumulative;
                    System.out.println("Desde " + ant + " a " + actual + " = " + cumulative);
                } else {
                    cumulative += problem.getDistanceMatrix(ant, 0);
                    objectiveFunctionValue += cumulative;
                    System.out.println("Desde " + ant + " a " + 0 + " = " + cumulative);
                }
            }
            System.out.println("");
        }
        solution.setObjectiveFunctionValue(objectiveFunctionValue);*/
    }
}
