package juego_21.vista;

import DeckOfCards.CartaInglesa;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import juego_21.modelo.Mano;

public class PanelMano extends VBox {
    private final Label titulo = new Label();
    private final HBox cartas = new HBox(5);
    private final Label puntos = new Label();
    private final Label estado = new Label();

    public PanelMano() {
        setSpacing(8);
        setPadding(new Insets(10));
        setMinWidth(145);
        setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(this, Priority.ALWAYS);
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        puntos.setStyle("-fx-text-fill: white;");
        estado.setStyle("-fx-font-weight: bold; -fx-text-fill: #ffe66d;");
        getChildren().addAll(titulo, cartas, puntos, estado);
    }

    public void actualizar(String nombre, Mano mano, boolean mostrarPuntaje,
            boolean turnoActivo, String textoEstado) {
        titulo.setText(nombre);
        cartas.getChildren().clear();
        for (CartaInglesa carta : mano.getCartas()) {
            String textoCarta = carta.toString()
                    .replace("\uFE0E", "")
                    .replace("\uFE0F", "");
            Label vistaCarta = new Label(textoCarta);
            String color = textoCarta.equals("@") ? "#17324d"
                    : carta.getColor().equals("rojo") ? "#d71920" : "black";
            vistaCarta.setStyle("-fx-background-color: white; -fx-text-fill: " + color
                    + "; -fx-border-color: #333333; -fx-padding: 14 8;"
                    + "-fx-font-size: 17px; -fx-min-width: 45px; -fx-alignment: center;");
            cartas.getChildren().add(vistaCarta);
        }
        puntos.setText(mostrarPuntaje ? "Puntos: " + mano.calcularPuntaje() : "Puntos: ?");
        estado.setText(textoEstado);
        setStyle(turnoActivo
                ? "-fx-border-color: #ffe66d; -fx-border-width: 3; -fx-background-color: #168c32;"
                : "-fx-border-color: white; -fx-border-width: 1; -fx-background-color: #0f7629;");
    }
}
