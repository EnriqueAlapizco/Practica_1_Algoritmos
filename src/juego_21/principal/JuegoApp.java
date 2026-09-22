package juego_21.principal;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import juego_21.controlador.JuegoController;
import juego_21.modelo.Juego;
import juego_21.vista.JuegoView;

public class JuegoApp extends Application {
    @Override
    public void start(Stage stage) {
        Juego juego = new Juego(1);
        JuegoView vista = new JuegoView();
        new JuegoController(juego, vista);
        stage.setTitle("Juego de 21");
        stage.setScene(new Scene(vista, 1000, 600));
        stage.setMinWidth(760);
        stage.setMinHeight(500);
        stage.show();
    }

    public static void main(String[] args) { launch(args); }
}
