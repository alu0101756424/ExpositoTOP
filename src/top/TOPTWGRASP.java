
package top;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.security.SecureRandom;

/**
 * @file TOPTWGRASP.java
 * @brief Implementación GRASP para el problema TOPTW.
 * @details Contiene la implementación del constructor voraz aleatorizado (GRASP),
 *          funciones para construir la RCL (Restricted Candidate List), criterios
 *          de selección (aleatoria y difusa) y evaluación/comprobación de inserciones.
 *          Opera sobre una instancia de TOPTWSolution y utiliza estructuras de
 *          tiempos por cliente para evaluación incremental.
 * @author alu0101756424
 * @version 1.0
 * @date 2025-10-31
 */
public class TOPTWGRASP {
    /** Generador aleatorio seguro usado en las selecciones RCL. */
    private static final SecureRandom r = new SecureRandom();

    /**
     * @brief Marca usada para indicar que una solución no ha sido evaluada.
     * @details Valor por defecto que puede usarse para inicializar evaluaciones pendientes.
     */
    public static double NO_EVALUATED = -1.0;

    /** Solución sobre la que opera el GRASP. */
    private TOPTWSolution solution;
    /** Tiempo asociado a la solución (puede representar tiempo de ejecución o similar). */
    private int solutionTime;

    /**
     * @brief Constructor que recibe la solución sobre la que trabajará el GRASP.
     * @param sol Instancia de `TOPTWSolution` usada para construir y evaluar rutas.
     */
    public TOPTWGRASP(TOPTWSolution sol) {
        this.solution = sol;
        this.solutionTime = 0;
    }

    /**
     * @brief Bucle principal del algoritmo GRASP.
     * @param maxIterations Número máximo de iteraciones a ejecutar.
     * @param maxSizeRCL Tamaño máximo permitido para la RCL durante la construcción.
     * @details Para cada iteración construye una solución mediante `computeGreedySolution`,
     *          evalúa su fitness y mantiene estadísticas de media y mejor solución.
     */
    public void GRASP(int maxIterations, int maxSizeRCL) {
        double averageFitness = 0.0;
        double bestSolution = 0.0;
        for (int i = 0; i < maxIterations; i++) {

            this.computeGreedySolution(maxSizeRCL);

            // IMPRIMIR SOLUCION
            double fitness = this.solution.evaluateFitness();
            System.out.println(this.solution.getInfoSolution());
            averageFitness += fitness;
            if (bestSolution < fitness) {
                bestSolution = fitness;
            }

            /******
             *
             * BÚSQUEDA LOCAL (placeholder)
             *
             */
        }
        averageFitness = averageFitness / maxIterations;
        System.out.println(" --> MEDIA: " + averageFitness);
        System.out.println(" --> MEJOR SOLUCION: " + bestSolution);
    }

    /**
     * @brief Selección aleatoria dentro de la RCL.
     * @param maxTRCL Tamaño (límite superior) de la RCL para la selección.
     * @return Índice seleccionado aleatoriamente en el rango \[0, maxTRCL).
     */
    public int aleatorySelectionRCL(int maxTRCL) {
        int low = 0;
        int high = maxTRCL;
        int posSelected = r.nextInt(high - low) + low;
        return posSelected;
    }

    /**
     * @brief Selección difusa que prioriza el mejor criterio FDRCL (minimiza pertenencia).
     * @param rcl Lista de candidatos; cada entrada es un arreglo con información del candidato.
     * @return Posición del candidato seleccionado según la función de pertenencia calculada.
     * @details Usa la puntuación máxima para normalizar la pertenencia: membership = 1 - score/maxScore.
     */
    public int fuzzySelectionBestFDRCL(ArrayList<double[]> rcl) {
        double[] membershipFunction = new double[rcl.size()];
        double maxSc = this.getMaxScore();
        for (int j = 0; j < rcl.size(); j++) {
            membershipFunction[j] = 1 - ((rcl.get(j)[4]) / maxSc);
        }
        double minMemFunc = Double.MAX_VALUE;
        int posSelected = -1;
        for (int i = 0; i < rcl.size(); i++) {
            if (minMemFunc > membershipFunction[i]) {
                minMemFunc = membershipFunction[i];
                posSelected = i;
            }
        }
        return posSelected;
    }

    /**
     * @brief Selección difusa mediante alpha-cut sobre la RCL.
     * @param rcl Lista de candidatos; cada entrada contiene métricas del candidato.
     * @param alpha Umbral entre 0.0 y 1.0 para el corte difuso.
     * @return Índice del candidato elegido dentro de la RCL completa.
     * @details Si el alpha-cut deja candidatos, se elige aleatoriamente entre ellos;
     *          si no, se elige aleatoriamente dentro de la RCL original.
     */
    public int fuzzySelectionAlphaCutRCL(ArrayList<double[]> rcl, double alpha) {
        ArrayList<double[]> rclAlphaCut = new ArrayList<double[]>();
        ArrayList<Integer> rclPos = new ArrayList<Integer>();
        double[] membershipFunction = new double[rcl.size()];
        double maxSc = this.getMaxScore();
        for (int j = 0; j < rcl.size(); j++) {
            membershipFunction[j] = 1 - ((rcl.get(j)[4]) / maxSc);
            if (membershipFunction[j] <= alpha) {
                rclAlphaCut.add(rcl.get(j));
                rclPos.add(j);
            }
        }
        int posSelected = -1;
        if (rclAlphaCut.size() > 0) {
            posSelected = rclPos.get(aleatorySelectionRCL(rclAlphaCut.size()));
        } else {
            posSelected = aleatorySelectionRCL(rcl.size());
        }
        return posSelected;
    }

    /**
     * @brief Construcción voraz aleatorizada de la solución.
     * @param maxSizeRCL Tamaño máximo de la RCL durante la construcción.
     * @details Inicializa la solución, las estructuras de tiempos y la lista de clientes,
     *          itera evaluando candidatos, construyendo la RCL y seleccionando inserciones
     *          hasta agotar clientes o no poder insertar más.
     */
    public void computeGreedySolution(int maxSizeRCL) {
        initializeSolution();
        ArrayList<ArrayList<Double>> departureTimesPerClient = initializeDepartureTimes();
        ArrayList<Integer> customers = initializeCustomers();

        ArrayList<double[]> candidates = evaluateAndSortCandidates(customers, departureTimesPerClient);
        int maxTRCL = maxSizeRCL;
        boolean existCandidates = true;

        while (!customers.isEmpty() && existCandidates) {
            if (!candidates.isEmpty()) {
                processCandidateSet(customers, departureTimesPerClient, candidates, maxTRCL);
            } else {
                existCandidates = handleNoCandidates(departureTimesPerClient);
            }
            candidates = reevaluateCandidates(customers, departureTimesPerClient);
        }
    }

    /**
     * @brief Inicializa la solución invocando `initSolution` de `TOPTWSolution`.
     * @details Método auxiliar para separar responsabilidades en la construcción.
     */
    private void initializeSolution() {
        this.solution.initSolution();
    }

    /**
     * @brief Inicializa la estructura de tiempos de salida por cliente y por ruta.
     * @return Estructura lista para su uso por la evaluación incremental.
     * @details Cada ruta tiene una lista de tiempos inicializados a 0.0.
     */
    private ArrayList<ArrayList<Double>> initializeDepartureTimes() {
        ArrayList<ArrayList<Double>> departureTimesPerClient = new ArrayList<>();
        ArrayList<Double> init = new ArrayList<>();
        for (int z = 0; z < this.solution.getProblem().getPOIs() + this.solution.getProblem().getVehicles(); z++) {
            init.add(0.0);
        }
        departureTimesPerClient.add(init);
        return departureTimesPerClient;
    }

    /**
     * @brief Inicializa la lista de clientes disponibles (POIs).
     * @return ArrayList con índices de clientes desde 1 hasta POIs.
     */
    private ArrayList<Integer> initializeCustomers() {
        ArrayList<Integer> customers = new ArrayList<>();
        for (int j = 1; j <= this.solution.getProblem().getPOIs(); j++) {
            customers.add(j);
        }
        return customers;
    }

    /**
     * @brief Evalúa y ordena la lista de candidatos usando evaluación completa.
     * @param customers Clientes disponibles a evaluar.
     * @param departureTimes Estructura de tiempos por cliente/ruta.
     * @return Lista de candidatos ordenada por coste de inserción.
     */
    private ArrayList<double[]> evaluateAndSortCandidates(ArrayList<Integer> customers,
                                                          ArrayList<ArrayList<Double>> departureTimes) {
        ArrayList<double[]> candidates = this.comprehensiveEvaluation(customers, departureTimes);
        candidates.sort(Comparator.comparingDouble(a -> a[a.length - 2]));
        return candidates;
    }

    /**
     * @brief Procesa el conjunto de candidatos (construye RCL, selecciona e inserta).
     * @param customers Lista de clientes restantes (se actualizará).
     * @param departureTimes Tiempos de salida por cliente y ruta (se actualizará).
     * @param candidates Lista completa de candidatos ordenada.
     * @param maxSizeRCL Tamaño máximo de la RCL a considerar.
     */
    private void processCandidateSet(ArrayList<Integer> customers,
                                     ArrayList<ArrayList<Double>> departureTimes,
                                     ArrayList<double[]> candidates, int maxSizeRCL) {
        int maxTRCL = Math.min(maxSizeRCL, candidates.size());
        ArrayList<double[]> rcl = new ArrayList<>(candidates.subList(0, maxTRCL));

        int posSelected = selectCandidate(rcl, maxTRCL);
        double[] candidateSelected = rcl.get(posSelected);

        customers.removeIf(c -> c == (int) candidateSelected[0]);
        updateSolution(candidateSelected, departureTimes);
    }

    /**
     * @brief Selecciona la estrategia de selección dentro de la RCL.
     * @param rcl Sublista de candidatos consideradas.
     * @param maxTRCL Tamaño efectivo de la RCL.
     * @return Índice seleccionado dentro de la RCL según la estrategia configurada.
     */
    private int selectCandidate(ArrayList<double[]> rcl, int maxTRCL) {
        int selection = 3;
        double alpha = 0.8;

        switch (selection) {
            case 1:
                return this.aleatorySelectionRCL(maxTRCL);
            case 2:
                return this.fuzzySelectionBestFDRCL(rcl);
            case 3:
                return this.fuzzySelectionAlphaCutRCL(rcl, alpha);
            default:
                return this.aleatorySelectionRCL(maxTRCL);
        }
    }

    /**
     * @brief Maneja el caso en que no hay candidatos válidos para inserción.
     * @param departureTimes Estructura de tiempos por ruta/cliente (se puede ampliar).
     * @return true si se ha creado una nueva ruta (si quedan vehículos libres), false en otro caso.
     */
    private boolean handleNoCandidates(ArrayList<ArrayList<Double>> departureTimes) {
        if (this.solution.getCreatedRoutes() < this.solution.getProblem().getVehicles()) {
            this.solution.addRoute();
            ArrayList<Double> initNew = new ArrayList<>();
            for (int z = 0; z < this.solution.getProblem().getPOIs() + this.solution.getProblem().getVehicles(); z++) {
                initNew.add(0.0);
            }
            departureTimes.add(initNew);
            return true;
        }
        return false;
    }

    /**
     * @brief Reevalúa y ordena candidatos tras cambios en la solución.
     * @param customers Clientes disponibles.
     * @param departureTimes Estructura actualizada de tiempos.
     * @return Lista de candidatos re-evaluada y ordenada por coste de inserción.
     */
    private ArrayList<double[]> reevaluateCandidates(ArrayList<Integer> customers,
                                                     ArrayList<ArrayList<Double>> departureTimes) {
        ArrayList<double[]> candidates = this.comprehensiveEvaluation(customers, departureTimes);
        candidates.sort(Comparator.comparingDouble(a -> a[a.length - 2]));
        return candidates;
    }

    /**
     * @brief Inserta el candidato seleccionado en la solución y actualiza tiempos.
     * @param candidateSelected Arreglo con información del candidato \[cliente, ruta, predecesor, coste, score\].
     * @param departureTimes Estructura de tiempos por ruta/cliente que será actualizada.
     * @details Actualiza predecesores/sucesores en la solución y recalcula los tiempos
     *          a partir del punto de inserción hasta el depósito.
     */
    public void updateSolution(double[] candidateSelected, ArrayList<ArrayList<Double>> departureTimes) {
        // Inserción del cliente en la ruta  return: cliente, ruta, predecesor, coste
        this.solution.setPredecessor((int) candidateSelected[0], (int) candidateSelected[2]);
        this.solution.setSuccessor((int) candidateSelected[0], this.solution.getSuccessor((int) candidateSelected[2]));
        this.solution.setSuccessor((int) candidateSelected[2], (int) candidateSelected[0]);
        this.solution.setPredecessor(this.solution.getSuccessor((int) candidateSelected[0]), (int) candidateSelected[0]);

        // Actualización de las estructuras de datos y conteo a partir de la posición a insertar
        double costInsertionPre = departureTimes.get((int) candidateSelected[1]).get((int) candidateSelected[2]);
        ArrayList<Double> route = departureTimes.get((int) candidateSelected[1]);
        int pre = (int) candidateSelected[2], suc = -1;
        int depot = this.solution.getIndexRoute((int) candidateSelected[1]);
        do {
            suc = this.solution.getSuccessor(pre);
            costInsertionPre += this.solution.getDistance(pre, suc);

            if (costInsertionPre < this.solution.getProblem().getReadyTime(suc)) {
                costInsertionPre = this.solution.getProblem().getReadyTime(suc);
            }
            costInsertionPre += this.solution.getProblem().getServiceTime(suc);

            if (!this.solution.isDepot(suc))
                route.set(suc, costInsertionPre);
            pre = suc;
        } while ((suc != depot));

        // Actualiza tiempos
        departureTimes.set((int) candidateSelected[1], route);
    }

    /**
     * @brief Evaluación comprensiva de posibles inserciones para todos los clientes y rutas creadas.
     * @param customers Lista de clientes candidatos a insertar.
     * @param departureTimes Tiempos actuales de salida por ruta/cliente.
     * @return Lista de candidatos válidos. Cada entrada es un arreglo double\[5\] con:
     *         \[0\]=cliente, \[1\]=ruta, \[2\]=predecesor, \[3\]=coste de inserción, \[4\]=score del cliente
     * @details Verifica ventanas temporales hacia delante para cada inserción y descarta las que violen
     *          due time, ready time o maxTimePerRoute.
     */
    public ArrayList< double[] > comprehensiveEvaluation(ArrayList<Integer> customers, ArrayList< ArrayList< Double > > departureTimes) {
        ArrayList< double[] > candidatesList = new ArrayList< double[] >();
        double[] infoCandidate = new double[5];
        boolean validFinalInsertion = true;
        infoCandidate[0] = -1;
        infoCandidate[1] = -1;
        infoCandidate[2] = -1;
        infoCandidate[3] = Double.MAX_VALUE;
        infoCandidate[4] = -1;

        for(int c = 0; c < customers.size(); c++) { // clientes disponibles
            for(int k = 0; k < this.solution.getCreatedRoutes(); k++) { // rutas creadas
                validFinalInsertion = true;
                int depot = this.solution.getIndexRoute(k);
                int pre=-1, suc=-1;
                double costInsertion = 0;
                pre = depot;
                int candidate = customers.get(c);
                do {                                                // recorremos la ruta
                    validFinalInsertion = true;
                    suc = this.solution.getSuccessor(pre);
                    double timesUntilPre = departureTimes.get(k).get(pre) + this.solution.getDistance(pre, candidate);
                    if(timesUntilPre < (this.solution.getProblem().getDueTime(candidate))) {
                        double costCand = 0;
                        if(timesUntilPre < this.solution.getProblem().getReadyTime(candidate)) {
                            costCand = this.solution.getProblem().getReadyTime(candidate);
                        } else { costCand = timesUntilPre; }
                        costCand +=  this.solution.getProblem().getServiceTime(candidate);
                        if(costCand > this.solution.getProblem().getMaxTimePerRoute()) { validFinalInsertion = false; }

                        // Comprobar TW desde candidate hasta sucesor
                        double timesUntilSuc = costCand + this.solution.getDistance(candidate, suc);
                        if(timesUntilSuc < (this.solution.getProblem().getDueTime(suc))) {
                            double costSuc = 0;
                            if(timesUntilSuc < this.solution.getProblem().getReadyTime(suc)) {
                                costSuc = this.solution.getProblem().getReadyTime(suc);
                            } else { costSuc = timesUntilSuc; }
                            costSuc +=  this.solution.getProblem().getServiceTime(suc);
                            costInsertion = costSuc;
                            if(costSuc > this.solution.getProblem().getMaxTimePerRoute()) { validFinalInsertion = false;}

                            int pre2=suc, suc2 = -1;
                            if(suc != depot)
                                do {
                                    suc2 = this.solution.getSuccessor(pre2);
                                    double timesUntilSuc2 = costInsertion + this.solution.getDistance(pre2, suc2);
                                    if(timesUntilSuc2 < (this.solution.getProblem().getDueTime(suc2))) {
                                        if(timesUntilSuc2 < this.solution.getProblem().getReadyTime(suc2)) {
                                            costInsertion = this.solution.getProblem().getReadyTime(suc2);
                                        } else { costInsertion = timesUntilSuc2; }
                                        costInsertion += this.solution.getProblem().getServiceTime(suc2);
                                        if(costInsertion > this.solution.getProblem().getMaxTimePerRoute()) { validFinalInsertion = false; }
                                    } else { validFinalInsertion = false; }
                                    pre2 = suc2;
                                } while((suc2 != depot) && validFinalInsertion);
                        } else { validFinalInsertion = false; }
                    } else { validFinalInsertion = false; }

                    if(validFinalInsertion==true) { // cliente, ruta, predecesor, coste
                        if(costInsertion < infoCandidate[3]) {
                            infoCandidate[0] = candidate; infoCandidate[1] = k; infoCandidate[2] = pre; infoCandidate[3] = costInsertion; infoCandidate[4] = this.solution.getProblem().getScore(candidate); // cliente, ruta, predecesor, coste, score
                        }
                    }

                    pre = suc;
                } while(suc != depot);
            } //rutas creadas

            // almacenamos en la lista de candidatos la mejor posición de inserción para el cliente
            if(infoCandidate[0]!=-1 && infoCandidate[1]!=-1 && infoCandidate[2]!=-1 && infoCandidate[3] != Double.MAX_VALUE && infoCandidate[4]!=-1) {
                double[] infoCandidate2 = new double[5];
                infoCandidate2[0] = infoCandidate[0];  infoCandidate2[1] = infoCandidate[1];
                infoCandidate2[2] = infoCandidate[2];  infoCandidate2[3] = infoCandidate[3];
                infoCandidate2[4] = infoCandidate[4];
                candidatesList.add(infoCandidate2);
            }
            validFinalInsertion = true;
            infoCandidate[0] = -1;  infoCandidate[1] = -1;
            infoCandidate[2] = -1;  infoCandidate[3] = Double.MAX_VALUE;
            infoCandidate[4] = -1;
        } // cliente

        return candidatesList;
    }


    /**
     * @brief Obtiene la solución actual del GRASP.
     * @return Instancia de `TOPTWSolution` usada por el GRASP.
     */
    public TOPTWSolution getSolution() {
        return solution;
    }

    /**
     * @brief Establece la solución sobre la que operará el GRASP.
     * @param solution Nueva instancia de `TOPTWSolution`.
     */
    public void setSolution(TOPTWSolution solution) {
        this.solution = solution;
    }

    /**
     * @brief Obtiene el tiempo asociado a la solución.
     * @return Valor entero con el tiempo de la solución.
     */
    public int getSolutionTime() {
        return solutionTime;
    }

    /**
     * @brief Establece el tiempo asociado a la solución.
     * @param solutionTime Tiempo a asignar.
     */
    public void setSolutionTime(int solutionTime) {
        this.solutionTime = solutionTime;
    }

    /**
     * @brief Obtiene la puntuación máxima entre todos los POIs del problema.
     * @return Puntuación máxima encontrada en el arreglo de scores del problema.
     * @details Utilizado por las funciones de selección difusa para normalizar pertenencias.
     */
    public double getMaxScore() {
        double maxSc = -1.0;
        for (int i = 0; i < this.solution.getProblem().getScore().length; i++) {
            if (this.solution.getProblem().getScore(i) > maxSc)
                maxSc = this.solution.getProblem().getScore(i);
        }
        return maxSc;
    }

}
