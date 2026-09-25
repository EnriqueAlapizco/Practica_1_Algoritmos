package juego_21.modelo;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Mazo;
import java.util.ArrayList;

/** Datos anteriores a una accion. Cada estado guarda sus propias cartas. */
final class EstadoJuego {
    private final ArrayList<CartaInglesa> cartasMazo;
    private final ArrayList<CartaInglesa> cartasDealer;
    private final ArrayList<ArrayList<CartaInglesa>> manos = new ArrayList<>();
    private final boolean[] plantados;
    private final ResultadoJugador[] resultados;
    private final int turnoActual;
    private final boolean partidaTerminada;

    EstadoJuego(Mazo mazo, ArrayList<Jugador> jugadores, Mano dealer,
            int turnoActual, boolean partidaTerminada) {
        cartasMazo = copiarCartas(mazo.getCartas());
        cartasDealer = copiarCartas(dealer.getCartas());
        plantados = new boolean[jugadores.size()];
        resultados = new ResultadoJugador[jugadores.size()];
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            manos.add(copiarCartas(jugador.getMano().getCartas()));
            plantados[i] = jugador.isPlantado();
            resultados[i] = jugador.getResultado();
        }
        this.turnoActual = turnoActual;
        this.partidaTerminada = partidaTerminada;
    }

    /** Copia tambien la visibilidad; una lista nueva de referencias no basta. */
    private static ArrayList<CartaInglesa> copiarCartas(ArrayList<CartaInglesa> cartas) {
        ArrayList<CartaInglesa> copia = new ArrayList<>();
        for (CartaInglesa carta : cartas) {
            CartaInglesa nueva = new CartaInglesa(carta.getValor(), carta.getPalo(),
                    carta.getColor());
            if (carta.isFaceup()) nueva.makeFaceUp();
            copia.add(nueva);
        }
        return copia;
    }

    void restaurar(Mazo mazo, ArrayList<Jugador> jugadores, Mano dealer) {
        boolean regresanCartas = cartasMazo.size() > mazo.getCartas().size();
        // Restaurar y barajar solo cuando la accion devolvio cartas al mazo.
        if (regresanCartas) {
            mazo.restaurarCartas(copiarCartas(cartasMazo));
            mazo.barajar();
        }
        dealer.restaurarCartas(copiarCartas(cartasDealer));
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jugador = jugadores.get(i);
            jugador.getMano().restaurarCartas(copiarCartas(manos.get(i)));
            jugador.restaurarEstado(plantados[i], resultados[i]);
        }
    }

    int getTurnoActual() { return turnoActual; }
    boolean isPartidaTerminada() { return partidaTerminada; }
}
