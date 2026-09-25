package DeckOfCards;

import java.util.ArrayList;
import java.util.Collections;
import practica_2_algoritmos.Pila;

/**
 * Adaptacion con pilas del Mazo de Cecilia Curlango Rosas (2025-2).
 * El tope contiene la siguiente carta que se va a repartir.
 */
public class Mazo {
    private final Pila<CartaInglesa> cartas = new Pila<>(52);

    public Mazo() {
        ArrayList<CartaInglesa> iniciales = new ArrayList<>();
        for (int valor = 2; valor <= 14; valor++) {
            for (Palo palo : Palo.values()) {
                iniciales.add(new CartaInglesa(valor, palo, palo.getColor()));
            }
        }
        Collections.shuffle(iniciales);
        cargarEnOrden(iniciales);
    }

    private void cargarEnOrden(ArrayList<CartaInglesa> orden) {
        cartas.clear();
        for (int i = orden.size() - 1; i >= 0; i--) {
            cartas.push(orden.get(i));
        }
    }

    /** Copia de las cartas en orden de reparto; no expone el almacenamiento. */
    public ArrayList<CartaInglesa> getCartas() {
        ArrayList<CartaInglesa> copia = new ArrayList<>();
        Pila<CartaInglesa> auxiliar = new Pila<>(52);
        while (!cartas.empty()) {
            CartaInglesa carta = cartas.pop();
            copia.add(carta);
            auxiliar.push(carta);
        }
        while (!auxiliar.empty()) {
            cartas.push(auxiliar.pop());
        }
        return copia;
    }

    public CartaInglesa obtenerUnaCarta() {
        return cartas.empty() ? null : cartas.pop();
    }

    /** Restaura las cartas en su orden de reparto, sin crear otra baraja. */
    public void restaurarCartas(ArrayList<CartaInglesa> guardadas) {
        if (guardadas == null || guardadas.size() > 52 || guardadas.contains(null)) {
            throw new IllegalArgumentException("Las cartas del mazo no son validas");
        }
        cargarEnOrden(guardadas);
    }

    public boolean estaVacio() { return cartas.empty(); }

    /** La lista solo es temporal; el almacenamiento sigue siendo una Pila. */
    public void barajar() {
        ArrayList<CartaInglesa> mezcladas = getCartas();
        Collections.shuffle(mezcladas);
        cargarEnOrden(mezcladas);
    }

    public void ordenar() {
        ArrayList<CartaInglesa> ordenadas = getCartas();
        Collections.sort(ordenadas);
        cargarEnOrden(ordenadas);
    }

    @Override
    public String toString() {
        return getCartas().toString();
    }
}
