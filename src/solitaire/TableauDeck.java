package solitaire;

import DeckOfCards.CartaInglesa;
import java.util.ArrayList;
import practica_2_algoritmos.Pila;

/** Adaptacion con pilas de TableauDeck de Cecilia M. Curlango (2025). */
public class TableauDeck {
    private final Pila<CartaInglesa> cartas = new Pila<>(52);

    public void inicializar(ArrayList<CartaInglesa> iniciales) {
        if (iniciales.isEmpty() || iniciales.size() > 52) {
            throw new IllegalArgumentException("Se requieren entre 1 y 52 cartas.");
        }
        cartas.clear();
        for (CartaInglesa carta : iniciales) {
            cartas.push(carta);
        }
        cartas.peek().makeFaceUp();
    }

    /** Conserva el filtro original: cartas visibles con valor menor o igual. */
    public ArrayList<CartaInglesa> removeStartingAt(int value) {
        ArrayList<CartaInglesa> retiradas = new ArrayList<>();
        Pila<CartaInglesa> auxiliar = new Pila<>(52);
        while (!cartas.empty()) {
            auxiliar.push(cartas.pop());
        }
        while (!auxiliar.empty()) {
            CartaInglesa carta = auxiliar.pop();
            if (carta.isFaceup() && carta.getValor() <= value) {
                retiradas.add(carta);
            } else {
                cartas.push(carta);
            }
        }
        return retiradas;
    }

    public CartaInglesa viewCardStartingAt(int value) {
        CartaInglesa encontrada = null;
        Pila<CartaInglesa> auxiliar = new Pila<>(52);
        while (!cartas.empty()) {
            auxiliar.push(cartas.pop());
        }
        while (!auxiliar.empty()) {
            CartaInglesa carta = auxiliar.pop();
            if (encontrada == null && carta.isFaceup() && carta.getValor() <= value) {
                encontrada = carta;
            }
            cartas.push(carta);
        }
        return encontrada;
    }

    public boolean agregarCarta(CartaInglesa carta) {
        if (cartas.isFull() || !sePuedeAgregarCarta(carta)) {
            return false;
        }
        carta.makeFaceUp();
        cartas.push(carta);
        return true;
    }

    CartaInglesa verUltimaCarta() {
        return getUltimaCarta();
    }

    CartaInglesa removerUltimaCarta() {
        if (cartas.empty()) {
            return null;
        }
        CartaInglesa retirada = cartas.pop();
        if (!cartas.empty()) {
            cartas.peek().makeFaceUp();
        }
        return retirada;
    }

    public boolean agregarBloqueDeCartas(ArrayList<CartaInglesa> bloque) {
        if (bloque.isEmpty() || bloque.size() > 52 - cartas.size()
                || !sePuedeAgregarCarta(bloque.getFirst())) {
            return false;
        }
        for (CartaInglesa carta : bloque) {
            cartas.push(carta);
        }
        return true;
    }

    public boolean isEmpty() {
        return cartas.empty();
    }

    public boolean sePuedeAgregarCarta(CartaInglesa carta) {
        if (carta == null) {
            return false;
        }
        if (cartas.empty()) {
            return carta.getValor() == 13;
        }
        CartaInglesa ultima = cartas.peek();
        return !ultima.getColor().equals(carta.getColor())
                && ultima.getValor() == carta.getValor() + 1;
    }

    public CartaInglesa getUltimaCarta() {
        return cartas.empty() ? null : cartas.peek();
    }

    /** Copia de base a tope; modificar la lista no modifica esta columna. */
    public ArrayList<CartaInglesa> getCards() {
        ArrayList<CartaInglesa> copia = new ArrayList<>();
        Pila<CartaInglesa> auxiliar = new Pila<>(52);
        while (!cartas.empty()) {
            auxiliar.push(cartas.pop());
        }
        while (!auxiliar.empty()) {
            CartaInglesa carta = auxiliar.pop();
            copia.add(carta);
            cartas.push(carta);
        }
        return copia;
    }

    @Override
    public String toString() {
        if (cartas.empty()) {
            return "---";
        }
        StringBuilder texto = new StringBuilder();
        for (CartaInglesa carta : getCards()) {
            texto.append(carta);
        }
        return texto.toString();
    }
}
