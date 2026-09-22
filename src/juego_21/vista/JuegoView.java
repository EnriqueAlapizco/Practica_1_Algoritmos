package juego_21.vista;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import juego_21.modelo.Juego;
import juego_21.modelo.Jugador;

public class JuegoView extends BorderPane {
    private final ComboBox<Integer> cantidadJugadores = new ComboBox<>();
    private final Button btnNuevaPartida = new Button("Nueva partida");
    private final Button btnPedir = new Button("Pedir");
    private final Button btnPlantarse = new Button("Plantarse");
    private final Label mensaje = new Label();
    private final PanelMano panelDealer = new PanelMano();
    private final HBox zonaJugadores = new HBox(8);
    private final ArrayList<PanelMano> panelesJugadores = new ArrayList<>();

    public JuegoView() {
        setPadding(new Insets(12));
        setStyle("-fx-background-color: #075d22;");
        cantidadJugadores.getItems().addAll(1, 2, 3, 4);
        cantidadJugadores.setValue(1);

        Label titulo = new Label("JUEGO DE 21");
        Label etiquetaCantidad = new Label("Jugadores:");
        titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
        etiquetaCantidad.setStyle("-fx-text-fill: white;");
        HBox encabezado = new HBox(12, titulo, etiquetaCantidad, cantidadJugadores, btnNuevaPartida);
        encabezado.setAlignment(Pos.CENTER_LEFT);
        encabezado.setPadding(new Insets(0, 0, 12, 0));
        setTop(encabezado);

        zonaJugadores.setAlignment(Pos.TOP_CENTER);
        setCenter(new VBox(12, panelDealer, zonaJugadores));

        mensaje.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
        HBox acciones = new HBox(10, btnPedir, btnPlantarse);
        acciones.setAlignment(Pos.CENTER);
        VBox inferior = new VBox(8, mensaje, acciones);
        inferior.setAlignment(Pos.CENTER);
        inferior.setPadding(new Insets(12, 0, 0, 0));
        setBottom(inferior);
    }

    public void crearPanelesJugadores(int cantidad) {
        panelesJugadores.clear();
        zonaJugadores.getChildren().clear();
        for (int i = 0; i < cantidad; i++) {
            PanelMano panel = new PanelMano();
            panelesJugadores.add(panel);
            zonaJugadores.getChildren().add(panel);
            HBox.setHgrow(panel, Priority.ALWAYS);
        }
    }

    public void actualizar(Juego juego) {
        boolean terminada = juego.isPartidaTerminada();
        panelDealer.actualizar("DEALER", juego.getManoDealer(), terminada, false, "");
        Jugador actual = juego.getJugadorActual();

        for (int i = 0; i < juego.getJugadores().size(); i++) {
            Jugador jugador = juego.getJugadores().get(i);
            String estado = terminada && jugador.getMano().esBlackjack()
                    ? "Blackjack - " + jugador.getResultado()
                    : terminada ? jugador.getResultado().toString()
                    : jugador.isPlantado() ? "Plantado" : jugador == actual ? "Tu turno" : "";
            panelesJugadores.get(i).actualizar(jugador.getNombre(), jugador.getMano(),
                    true, jugador == actual, estado);
        }

        if (!terminada) mensaje.setText("Turno de " + actual.getNombre());
        else if (juego.getManoDealer().estaPasada()) mensaje.setText("El dealer se paso: ganan las manos validas");
        else if (juego.getManoDealer().esBlackjack()) mensaje.setText("Blackjack del dealer");
        else mensaje.setText("Partida terminada");
        btnPedir.setDisable(terminada);
        btnPlantarse.setDisable(terminada);
    }

    public ComboBox<Integer> getCantidadJugadores() { return cantidadJugadores; }
    public Button getBtnNuevaPartida() { return btnNuevaPartida; }
    public Button getBtnPedir() { return btnPedir; }
    public Button getBtnPlantarse() { return btnPlantarse; }
}
