package juego_21.modelo;

import DeckOfCards.CartaInglesa;
import java.util.ArrayList;
import practica_2_algoritmos.Pila;

public class Mano {
    private final Pila<CartaInglesa> cartas = new Pila<>(52);

    public void agregarCarta(CartaInglesa carta) {
        if (carta != null) cartas.push(carta);
    }

    void restaurarCartas(ArrayList<CartaInglesa> guardadas) {
        cartas.clear();
        for (CartaInglesa carta : guardadas) cartas.push(carta);
    }

    public int calcularPuntaje() {
        int total = 0;
        int ases = 0;
        for (CartaInglesa carta : getCartas()) {
            int valor = carta.getValor();
            if (valor == 14) {
                total += 11;
                ases++;
            } else if (valor >= 11) {
                total += 10;
            } else {
                total += valor;
            }
        }
        while (total > 21 && ases > 0) {
            total -= 10;
            ases--;
        }
        return total;
    }

    public boolean estaPasada() { return calcularPuntaje() > 21; }
    public boolean tiene21() { return calcularPuntaje() == 21; }

    public boolean esBlackjack() {
        return cartas.size() == 2 && calcularPuntaje() == 21;
    }

    public boolean esSuave() {
        int total = 0;
        int ases = 0;
        for (CartaInglesa carta : getCartas()) {
            int valor = carta.getValor();
            if (valor == 14) {
                total += 11;
                ases++;
            } else if (valor >= 11) {
                total += 10;
            } else {
                total += valor;
            }
        }
        while (total > 21 && ases > 0) {
            total -= 10;
            ases--;
        }
        return ases > 0;
    }

    /** Copia en orden de llegada. La mano conserva todas sus cartas. */
    public ArrayList<CartaInglesa> getCartas() {
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
}
