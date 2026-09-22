package juego_21.modelo;

public enum ResultadoJugador {
    PENDIENTE("Pendiente"), GANA("Gana"), PIERDE("Pierde"), EMPATA("Empata");

    private final String texto;

    ResultadoJugador(String texto) { this.texto = texto; }

    @Override
    public String toString() { return texto; }
}
