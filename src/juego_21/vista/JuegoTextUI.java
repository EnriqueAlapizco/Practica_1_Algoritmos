package juego_21.vista;

import DeckOfCards.CartaInglesa;
import java.util.Scanner;
import juego_21.modelo.Juego;
import juego_21.modelo.Jugador;
import juego_21.modelo.Mano;

public class JuegoTextUI {
    private final Scanner teclado = new Scanner(System.in);
    private Juego juego;

    public static void main(String[] args) {
        new JuegoTextUI().jugar();
    }

    public void jugar() {
        juego = new Juego(leerCantidadJugadores());
        while (!juego.isPartidaTerminada()) {
            mostrarEstado();
            System.out.println("Turno de " + juego.getJugadorActual().getNombre());
            System.out.print("P) Pedir carta   S) Plantarse: ");
            String opcion = teclado.nextLine().trim().toUpperCase();
            if (opcion.equals("P")) juego.pedirCarta();
            else if (opcion.equals("S")) juego.plantarse();
            else System.out.println("Opcion no valida.");
        }
        mostrarEstado();
        System.out.println("Partida terminada.");
    }

    private int leerCantidadJugadores() {
        int cantidad = 0;
        while (cantidad < 1 || cantidad > 4) {
            System.out.print("Cantidad de jugadores (1-4): ");
            try {
                cantidad = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException ex) {
                cantidad = 0;
            }
        }
        return cantidad;
    }

    private void mostrarEstado() {
        System.out.println("\nDEALER");
        mostrarMano(juego.getManoDealer(), juego.isPartidaTerminada());
        for (Jugador jugador : juego.getJugadores()) {
            System.out.println("\n" + jugador.getNombre());
            mostrarMano(jugador.getMano(), true);
            if (juego.isPartidaTerminada()) {
                System.out.println("Resultado: " + jugador.getResultado());
            }
        }
        System.out.println();
    }

    private void mostrarMano(Mano mano, boolean mostrarPuntaje) {
        for (CartaInglesa carta : mano.getCartas()) System.out.print(carta + " ");
        System.out.println(mostrarPuntaje ? " Puntos: " + mano.calcularPuntaje() : " Puntos: ?");
    }
}
