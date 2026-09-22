package practica_2_algoritmos;

/**
 * Pila generica de capacidad fija: el ultimo elemento en entrar
 * es el primero en salir (LIFO).
 * @param <T> tipo de los elementos almacenados
 */
public class Pila<T> {
    private final Object[] elementos;
    private int tope;

    public Pila(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero.");
        }
        elementos = new Object[capacidad];
        tope = -1;
    }

    public void push(T elemento) {
        if (isFull()) {
            throw new IllegalStateException("La pila esta llena.");
        }
        tope++;
        elementos[tope] = elemento;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (empty()) {
            throw new IllegalStateException("La pila esta vacia.");
        }
        T elemento = (T) elementos[tope];
        elementos[tope] = null;
        tope--;
        return elemento;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (empty()) {
            throw new IllegalStateException("La pila esta vacia.");
        }
        return (T) elementos[tope];
    }

    public boolean empty() {
        return tope == -1;
    }

    public boolean isFull() {
        return tope == elementos.length - 1;
    }

    public int size() {
        return tope + 1;
    }

    public void clear() {
        while (!empty()) {
            pop();
        }
    }
}
