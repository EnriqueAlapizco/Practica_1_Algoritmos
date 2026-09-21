package juego_21.controlador;

import juego_21.modelo.Juego;
import juego_21.vista.JuegoView;

public class JuegoController {
    private final Juego juego;
    private final JuegoView vista;

    public JuegoController(Juego juego, JuegoView vista) {
        this.juego = juego;
        this.vista = vista;
        vista.getBtnPedir().setOnAction(evento -> {
            juego.pedirCarta();
            vista.actualizar(juego);
        });
        vista.getBtnPlantarse().setOnAction(evento -> {
            juego.plantarse();
            vista.actualizar(juego);
        });
        vista.getBtnNuevaPartida().setOnAction(evento -> nuevaPartida());
        nuevaPartida();
    }

    private void nuevaPartida() {
        int cantidad = vista.getCantidadJugadores().getValue();
        juego.iniciarPartida(cantidad);
        vista.crearPanelesJugadores(cantidad);
        vista.actualizar(juego);
    }
}
