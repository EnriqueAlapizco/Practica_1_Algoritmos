package juego_21.modelo;

public class Jugador {
    private final String nombre;
    private final Mano mano = new Mano();
    private boolean plantado;
    private ResultadoJugador resultado = ResultadoJugador.PENDIENTE;

    public Jugador(String nombre) { this.nombre = nombre; }
    public void plantarse() { plantado = true; }
    void restaurarEstado(boolean plantado, ResultadoJugador resultado) {
        this.plantado = plantado;
        this.resultado = resultado;
    }
    public boolean puedeJugar() { return !plantado && !mano.estaPasada(); }
    public String getNombre() { return nombre; }
    public Mano getMano() { return mano; }
    public boolean isPlantado() { return plantado; }
    public ResultadoJugador getResultado() { return resultado; }
    public void setResultado(ResultadoJugador resultado) { this.resultado = resultado; }
}
