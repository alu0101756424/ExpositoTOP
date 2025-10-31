// java
package es.ull.esit.utils;

import java.util.Objects;

/**
 * Representa un par inmutable de valores (first, second).
 *
 * @param <F> Tipo del primer elemento.
 * @param <S> Tipo del segundo elemento.
 */
public class Pair<F, S> {
    /** Primer elemento del par (puede ser null). */
    public final F first;
    /** Segundo elemento del par (puede ser null). */
    public final S second;

    /**
     * Crea una instancia de Pair con los valores proporcionados.
     *
     * @param first  Primer elemento.
     * @param second Segundo elemento.
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Comprueba igualdad lógica entre pares.
     * Dos pares son iguales si sus primeros elementos son iguales y sus segundos elementos son iguales.
     *
     * @param o Objeto a comparar.
     * @return {@code true} si {@code o} es un {@code Pair} con componentes iguales; {@code false} en otro caso.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair<?, ?> p = (Pair<?, ?>) o;
        return Objects.equals(p.first, first) && Objects.equals(p.second, second);
    }

    /**
     * Calcula el código hash combinando los hashes de los componentes.
     * Se utiliza una operación XOR para combinar los valores (maneja null).
     *
     * @return Código hash del par.
     */
    @Override
    public int hashCode() {
        return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode());
    }

    /**
     * Método de fábrica conveniente para crear pares sin repetir tipos.
     *
     * @param <A> Tipo del primer elemento.
     * @param <B> Tipo del segundo elemento.
     * @param a   Primer elemento.
     * @param b   Segundo elemento.
     * @return Nuevo {@code Pair<A,B>} con los valores proporcionados.
     */
    public static <A, B> Pair<A, B> create(A a, B b) {
        return new Pair<A, B>(a, b);
    }
}
