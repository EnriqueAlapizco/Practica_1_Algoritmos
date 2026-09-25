package juego_21.modelo;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Mazo;
import java.util.ArrayList;
import practica_2_algoritmos.Pila;

public class Juego {
    private final Pila<EstadoJuego> historial = new Pila<>(52);
    private Mazo mazo;
    private final ArrayList<Jugador> jugadores = new ArrayList<>();
    private Mano manoDealer;
    private int turnoActual;
    private boolean partidaTerminada;

    public Juego(int cantidadJugadores) { iniciarPartida(cantidadJugadores); }

    public final void iniciarPartida(int cantidadJugadores) {
        if (cantidadJugadores < 1 || cantidadJugadores > 4) {
            throw new IllegalArgumentException("La cantidad de jugadores debe estar entre 1 y 4");
        }
        historial.clear();
        mazo = new Mazo();
        jugadores.clear();
        manoDealer = new Mano();
        turnoActual = 0;
        partidaTerminada = false;

        for (int i = 1; i <= cantidadJugadores; i++) {
            jugadores.add(new Jugador("Jugador " + i));
        }
        for (int ronda = 0; ronda < 2; ronda++) {
            for (Jugador jugador : jugadores) {
                jugador.getMano().agregarCarta(sacarCartaVisible());
            }
            CartaInglesa cartaDealer = mazo.obtenerUnaCarta();
            if (ronda == 1 && cartaDealer != null) cartaDealer.makeFaceUp();
            manoDealer.agregarCarta(cartaDealer);
        }

        if (manoDealer.esBlackjack()) {
            terminarPartida();
            return;
        }

        for (Jugador jugador : jugadores) {
            if (jugador.getMano().esBlackjack()) jugador.plantarse();
        }
        buscarSiguienteJugador();
    }

    public void pedirCarta() {
        if (partidaTerminada || mazo.estaVacio()) return;
        guardarEstado();
        Jugador jugador = getJugadorActual();
        jugador.getMano().agregarCarta(sacarCartaVisible());
        if (jugador.getMano().calcularPuntaje() >= 21) {
            // Solo se puede volver a un turno anterior si termino por pasarse.
            if (jugador.getMano().tiene21()) historial.clear();
            jugador.plantarse();
            avanzarTurno();
        }
    }

    public void plantarse() {
        if (partidaTerminada) return;
        historial.clear();
        getJugadorActual().plantarse();
        avanzarTurno();
    }

    private void guardarEstado() {
        historial.push(new EstadoJuego(mazo, jugadores, manoDealer, turnoActual,
                partidaTerminada));
    }

    public boolean puedeDeshacer() { return !historial.empty(); }

    public void deshacer() {
        if (!puedeDeshacer()) return;
        EstadoJuego anterior = historial.pop();
        anterior.restaurar(mazo, jugadores, manoDealer);
        turnoActual = anterior.getTurnoActual();
        partidaTerminada = anterior.isPartidaTerminada();
    }

    private CartaInglesa sacarCartaVisible() {
        CartaInglesa carta = mazo.obtenerUnaCarta();
        if (carta != null) carta.makeFaceUp();
        return carta;
    }

    private void avanzarTurno() {
        turnoActual++;
        buscarSiguienteJugador();
    }

    private void buscarSiguienteJugador() {
        while (turnoActual < jugadores.size() && !jugadores.get(turnoActual).puedeJugar()) {
            turnoActual++;
        }
        if (turnoActual >= jugadores.size()) {
            terminarPartida();
        }
    }

    private void terminarPartida() {
        if (!partidaTerminada) {
            jugarTurnoDealer();
            determinarResultados();
            partidaTerminada = true;
        }
    }

    private void jugarTurnoDealer() {
        for (CartaInglesa carta : manoDealer.getCartas()) carta.makeFaceUp();

        boolean hayJugadorSinPasarse = false;
        for (Jugador jugador : jugadores) {
            if (!jugador.getMano().estaPasada()) hayJugadorSinPasarse = true;
        }
        if (!hayJugadorSinPasarse || manoDealer.esBlackjack()) return;

        while (manoDealer.calcularPuntaje() < 17
                || manoDealer.calcularPuntaje() == 17 && manoDealer.esSuave()) {
            CartaInglesa carta = sacarCartaVisible();
            if (carta == null) break;
            manoDealer.agregarCarta(carta);
        }
    }

    private void determinarResultados() {
        int puntosDealer = manoDealer.calcularPuntaje();
        for (Jugador jugador : jugadores) {
            int puntosJugador = jugador.getMano().calcularPuntaje();
            if (puntosJugador > 21) jugador.setResultado(ResultadoJugador.PIERDE);
            else if (manoDealer.esBlackjack() && jugador.getMano().esBlackjack()) jugador.setResultado(ResultadoJugador.EMPATA);
            else if (manoDealer.esBlackjack()) jugador.setResultado(ResultadoJugador.PIERDE);
            else if (jugador.getMano().esBlackjack()) jugador.setResultado(ResultadoJugador.GANA);
            else if (puntosDealer > 21 || puntosJugador > puntosDealer) jugador.setResultado(ResultadoJugador.GANA);
            else if (puntosJugador < puntosDealer) jugador.setResultado(ResultadoJugador.PIERDE);
            else jugador.setResultado(ResultadoJugador.EMPATA);
        }
    }

    public Jugador getJugadorActual() {
        if (partidaTerminada || turnoActual >= jugadores.size()) return null;
        return jugadores.get(turnoActual);
    }

    public ArrayList<Jugador> getJugadores() { return jugadores; }
    public Mano getManoDealer() { return manoDealer; }
    public boolean isPartidaTerminada() { return partidaTerminada; }
}
