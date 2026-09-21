package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Mazo;
import java.util.ArrayList;
import practica_2_algoritmos.Pila;

/** Adaptacion con pilas de DrawPile de Cecilia Curlango (2025). */
public class DrawPile {
    private final Pila<CartaInglesa> cartas = new Pila<>(52);
    private int cuantasCartasSeEntregan = 3;

    public DrawPile() {
        recargar(new Mazo().getCartas());
    }

    public void setCuantasCartasSeEntregan(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        cuantasCartasSeEntregan = cantidad;
    }

    public int getCuantasCartasSeEntregan() {
        return cuantasCartasSeEntregan;
    }

    public ArrayList<CartaInglesa> getCartas(int cantidad) {
        if (cantidad < 0 || cantidad > cartas.size()) {
            throw new IllegalArgumentException("Cantidad de cartas no disponible.");
        }
        ArrayList<CartaInglesa> retiradas = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            retiradas.add(cartas.pop());
        }
        return retiradas;
    }

    public ArrayList<CartaInglesa> retirarCartas() {
        int cantidad = Math.min(cartas.size(), cuantasCartasSeEntregan);
        ArrayList<CartaInglesa> retiradas = getCartas(cantidad);
        for (CartaInglesa carta : retiradas) {
            carta.makeFaceUp();
        }
        return retiradas;
    }

    public boolean hayCartas() {
        return !cartas.empty();
    }

    /**
     * Conserva la consulta original: la ultima carta del orden de reparto.
     * Como ahora es la base de la pila, se recorre y se restaura el monton.
     */
    public CartaInglesa verCarta() {
        Pila<CartaInglesa> auxiliar = new Pila<>(52);
        CartaInglesa ultima = null;
        while (!cartas.empty()) {
            ultima = cartas.pop();
            auxiliar.push(ultima);
        }
        while (!auxiliar.empty()) {
            cartas.push(auxiliar.pop());
        }
        return ultima;
    }

    public void recargar(ArrayList<CartaInglesa> nuevas) {
        if (nuevas.size() > 52) {
            throw new IllegalArgumentException("El mazo admite hasta 52 cartas.");
        }
        cartas.clear();
        for (int i = nuevas.size() - 1; i >= 0; i--) {
            CartaInglesa carta = nuevas.get(i);
            carta.makeFaceDown();
            cartas.push(carta);
        }
    }

    @Override
    public String toString() {
        return cartas.empty() ? "-E-" : "@";
    }
}
