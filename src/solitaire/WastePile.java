package solitaire;

import DeckOfCards.CartaInglesa;
import java.util.ArrayList;
import practica_2_algoritmos.Pila;

/** Adaptacion con pilas de WastePile de Cecilia Curlango Rosas (2025-2). */
public class WastePile {
    private final Pila<CartaInglesa> cartas = new Pila<>(52);

    public void addCartas(ArrayList<CartaInglesa> nuevas) {
        if (nuevas.size() > 52 - cartas.size()) {
            throw new IllegalArgumentException("El monton admite hasta 52 cartas.");
        }
        for (CartaInglesa carta : nuevas) {
            cartas.push(carta);
        }
    }

    /** Vacia el monton y devuelve las cartas desde la mas antigua a la ultima. */
    public ArrayList<CartaInglesa> emptyPile() {
        ArrayList<CartaInglesa> retiradas = new ArrayList<>();
        Pila<CartaInglesa> auxiliar = new Pila<>(52);
        while (!cartas.empty()) {
            auxiliar.push(cartas.pop());
        }
        while (!auxiliar.empty()) {
            retiradas.add(auxiliar.pop());
        }
        return retiradas;
    }

    public CartaInglesa verCarta() {
        return cartas.empty() ? null : cartas.peek();
    }

    public CartaInglesa getCarta() {
        return cartas.empty() ? null : cartas.pop();
    }

    public boolean hayCartas() {
        return !cartas.empty();
    }

    @Override
    public String toString() {
        if (cartas.empty()) {
            return "---";
        }
        cartas.peek().makeFaceUp();
        return cartas.peek().toString();
    }
}
