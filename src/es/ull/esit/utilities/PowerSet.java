package es.ull.esit.utilities;

import java.util.BitSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * @file PowerSet.java
 * @brief Iterador para generar el conjunto potencia (todos los subconjuntos) de un conjunto dado.
 * @param <E> Tipo de los elementos del conjunto.
 * @details
 * Esta clase implementa {@link Iterator} y {@link Iterable} para recorrer todos los
 * subconjuntos de un conjunto de entrada. Internamente convierte el conjunto en
 * un arreglo y utiliza un {@link BitSet} como contador binario: cada bit indica
 * si el elemento correspondiente está presente en el subconjunto actual.
 *
 * Nota: la implementación usa {@link TreeSet} para construir cada subconjunto,
 * por lo que los elementos deberán ser comparables (o cambiar el tipo de Set
 * si no se desea ordenación).
 */
public class PowerSet<E> implements Iterator<Set<E>>, Iterable<Set<E>> {

    /** Arreglo con los elementos originales (orden fijo para iteración). */
    private E[] arr = null;

    /**
     * Contador binario representado con BitSet.
     * - bits 0..(n-1): indican presencia de cada elemento
     * - bit n: marca el fin de la iteración (cuando se pone a 1 ya no hay más subconjuntos)
     */
    private BitSet bset = null;

    /**
     * Constructor.
     * @param set Conjunto de entrada cuyo conjunto potencia se desea generar.
     * @implNote Se convierte el conjunto a un arreglo; el orden resultante depende
     *           de la implementación de {@code set.toArray()}. El constructor
     *           crea un BitSet de longitud {@code arr.length + 1} para marcar el fin.
     */
    @SuppressWarnings("unchecked")
    public PowerSet(Set<E> set) {
        this.arr = (E[]) set.toArray();
        this.bset = new BitSet(this.arr.length + 1);
    }

    /**
     * Comprueba si queda algún subconjunto por generar.
     * @return {@code true} si aún hay subconjuntos pendientes, {@code false} cuando se ha llegado al final.
     */
    @Override
    public boolean hasNext() {
        return !this.bset.get(this.arr.length);
    }

    /**
     * Devuelve el siguiente subconjunto en la iteración.
     * @return Un {@link Set} con los elementos seleccionados según el BitSet actual.
     * @details Construye un {@link TreeSet} con los elementos cuyo bit está activo,
     *          luego incrementa el BitSet como contador (suma 1 en binario).
     * @implNote Si los elementos no son comparables, {@link TreeSet} lanzará
     *           {@link ClassCastException}; cambiar la implementación a {@link java.util.LinkedHashSet}
     *           si se precisa mantener inserción sin requisito de comparabilidad.
     */
    @Override
    public Set<E> next() {
        Set<E> returnSet = new TreeSet<>();
        for (int i = 0; i < this.arr.length; i++) {
            if (this.bset.get(i)) {
                returnSet.add(this.arr[i]);
            }
        }
        // Incrementar el BitSet como un contador binario
        for (int i = 0; i < this.bset.size(); i++) {
            if (!this.bset.get(i)) {
                this.bset.set(i);
                break;
            } else {
                this.bset.clear(i);
            }
        }
        return returnSet;
    }

    /**
     * Operación no soportada en este iterador.
     * @throws UnsupportedOperationException siempre que se invoque.
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not Supported!");
    }

    /**
     * Devuelve el iterador (esta misma instancia).
     * @return {@code this} como {@link Iterator}.
     */
    @Override
    public Iterator<Set<E>> iterator() {
        return this;
    }
}
