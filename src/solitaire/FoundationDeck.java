package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;
import practica_2_algoritmos.Pila;

/** Adaptacion con pilas de FoundationDeck de Cecilia M. Curlango (2025). */
public class FoundationDeck {
    private final Palo palo;
    private final Pila<CartaInglesa> cartas = new Pila<>(13);

    public FoundationDeck(Palo palo) {
        this.palo = palo;
    }

    public FoundationDeck(CartaInglesa carta) {
        this(carta.getPalo());
        agregarCarta(carta);
    }

    public boolean agregarCarta(CartaInglesa carta) {
        if (carta == null || !carta.tieneElMismoPalo(palo) || cartas.isFull()) {
            return false;
        }
        int siguiente = cartas.empty() ? 1 : cartas.peek().getValorBajo() + 1;
        if (carta.getValorBajo() != siguiente) {
            return false;
        }
        cartas.push(carta);
        return true;
    }

    CartaInglesa removerUltimaCarta() {
        return cartas.empty() ? null : cartas.pop();
    }

    public boolean estaVacio() {
        return cartas.empty();
    }

    public CartaInglesa getUltimaCarta() {
        return cartas.empty() ? null : cartas.peek();
    }

    @Override
    public String toString() {
        if (cartas.empty()) {
            return "---";
        }
        StringBuilder texto = new StringBuilder();
        Pila<CartaInglesa> auxiliar = new Pila<>(13);
        while (!cartas.empty()) {
            auxiliar.push(cartas.pop());
        }
        while (!auxiliar.empty()) {
            CartaInglesa carta = auxiliar.pop();
            texto.append(carta);
            cartas.push(carta);
        }
        return texto.toString();
    }
}
