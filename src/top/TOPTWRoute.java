package top;

/**
 * @file TOPTWRoute.java
 * @brief Representa un enlace (predecesor/sucesor) en una ruta del problema TOPTW.
 * @details Clase ligera que almacena el índice del predecesor, el sucesor y el identificador
 *          de la ruta. Proporciona constructores, getters y setters bien documentados.
 */
public class TOPTWRoute {
    /** Índice del nodo predecesor en la ruta. */
    private int predecessor;
    /** Índice del nodo sucesor en la ruta. */
    private int successor;
    /** Identificador de la ruta. */
    private int id;

    /**
     * @brief Constructor por defecto.
     * @details Inicializa campos a 0.
     */
    public TOPTWRoute() {
        this.predecessor = 0;
        this.successor = 0;
        this.id = 0;
    }

    /**
     * @brief Constructor con parámetros.
     * @param pre Índice del predecesor.
     * @param succ Índice del sucesor.
     * @param id Identificador de la ruta.
     */
    public TOPTWRoute(int pre, int succ, int id) {
        this.predecessor = pre;
        this.successor = succ;
        this.id = id;
    }

    /**
     * @brief Obtiene el índice del predecesor.
     * @return Índice del predecesor.
     */
    public int getPredecessor() {
        return this.predecessor;
    }

    /**
     * @brief Obtiene el índice del sucesor.
     * @return Índice del sucesor.
     */
    public int getSuccessor() {
        return this.successor;
    }

    /**
     * @brief Obtiene el identificador de la ruta.
     * @return Identificador de la ruta.
     */
    public int getId() {
        return this.id;
    }

    /**
     * @brief Establece el índice del predecesor.
     * @param pre Nuevo índice del predecesor.
     */
    public void setPredecessor(int pre) {
        this.predecessor = pre;
    }

    /**
     * @brief Establece el índice del sucesor.
     * @param suc Nuevo índice del sucesor.
     */
    public void setSuccessor(int suc) {
        this.successor = suc;
    }

    /**
     * @brief Establece el identificador de la ruta.
     * @param id Nuevo identificador.
     */
    public void setId(int id) {
        this.id = id;
    }

    /* Métodos alias para mantener compatibilidad con el código existente
       que usa nombres con errores tipográficos. */
    /**
     * @deprecated Use {@link #getPredecessor()}.
     */
    @Deprecated
    public int getPredeccesor() {
        return getPredecessor();
    }

    /**
     * @deprecated Use {@link #getSuccessor()}.
     */
    @Deprecated
    public int getSuccesor() {
        return getSuccessor();
    }

    /**
     * @deprecated Use {@link #setPredecessor(int)}.
     */
    @Deprecated
    public void setPredeccesor(int pre) {
        setPredecessor(pre);
    }

    /**
     * @deprecated Use {@link #setSuccessor(int)}.
     */
    @Deprecated
    public void setSuccesor(int suc) {
        setSuccessor(suc);
    }
}
