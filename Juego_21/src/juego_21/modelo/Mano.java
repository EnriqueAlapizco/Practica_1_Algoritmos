package juego_21.modelo;

import DeckOfCards.CartaInglesa;
import java.util.ArrayList;

public class Mano {
    private final ArrayList<CartaInglesa> cartas = new ArrayList<>();

    public void agregarCarta(CartaInglesa carta) {
        if (carta != null) cartas.add(carta);
    }

    public int calcularPuntaje() {
        int total = 0;
        int ases = 0;
        for (CartaInglesa carta : cartas) {
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
        for (CartaInglesa carta : cartas) {
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

    public ArrayList<CartaInglesa> getCartas() { return cartas; }
}
