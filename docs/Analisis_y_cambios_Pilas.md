# Práctica 2: etapa Pilas

## Orden de trabajo de la asignación

1. Partir de la práctica 1 en la rama `Pilas`.
2. Analizar la viabilidad de sustituir `ArrayList` en `DrawPile`, `FoundationDeck`, `TableauDeck` y `WastePile`.
3. Implementar con la clase `Pila` propia y comprobar que el juego sigue funcionando, incluida su interfaz gráfica.
4. Guardar un commit final de esta etapa antes de crear `Undo`.
5. En `Undo`, analizar, diseñar y después implementar cómo registrar y deshacer movimientos.

Este documento corresponde a los puntos 2 y 3. La función de deshacer todavía no está implementada.

## Análisis de viabilidad

| Clase | Dictamen | Adaptación necesaria |
|---|---|---|
| `DrawPile` | Viable | Cargar las cartas en sentido inverso para conservar el orden de reparto; adaptar la consulta del extremo contrario al tope. |
| `FoundationDeck` | Muy adecuada | Usar `push`, `pop` y `peek`, conservando las reglas de palo y secuencia; recorrer con una pila auxiliar para mostrar todas las cartas. |
| `WastePile` | Muy adecuada | Agregar y retirar por el tope; al vaciar, recuperar el orden de llegada mediante una pila auxiliar. |
| `TableauDeck` | Viable con mayor adaptación | Consultar el interior y extraer bloques mediante una pila auxiliar, conservando el orden y la visibilidad. |

Las cuatro clases pueden adaptarse. La necesidad de recorrer elementos internos no hace imposible la sustitución, pero obliga a realizar operaciones adicionales. No se propone excluir ninguna clase. El PDF exige justificar cualquier exclusión y obtener la aceptación de la profesora.

### DrawPile

El almacenamiento es una `Pila<CartaInglesa>` de capacidad 52. `getCartas(int)` y `retirarCartas()` retiran mediante `pop()`. La recarga introduce la última carta de la lista primero, dejando arriba la próxima carta por repartir. Las cartas recargadas quedan boca abajo.

El método original `verCarta()` consultaba el último elemento del `ArrayList`, mientras el reparto retiraba el primero. Se conserva esa diferencia: la consulta recorre hasta la base con una pila auxiliar y restaura el montón. No se sustituye por `peek()`, porque eso cambiaría qué carta devuelve.

### FoundationDeck

Utiliza una pila de capacidad 13. Solo acepta el As cuando está vacía; después exige el mismo palo y el siguiente valor bajo. La consulta y extracción trabajan con el tope. `toString()` muestra la secuencia completa desde la base, restaurando la pila después del recorrido.

### WastePile

Utiliza una pila de capacidad 52. Las cartas recibidas se apilan en su orden de llegada, de modo que la última recibida queda disponible. `emptyPile()` usa una pila auxiliar para devolverlas desde la más antigua a la última. Esto conserva el orden cuando se entregan a `DrawPile.recargar()`.

### TableauDeck

Utiliza una pila de capacidad 52. Las consultas internas recorren y restauran el contenido. `removeStartingAt(int)` conserva el filtro exacto del código proporcionado: retira las cartas visibles cuyo valor sea menor o igual al recibido. No presupone que siempre exista una coincidencia exacta o un bloque contiguo.

Los bloques se devuelven desde su primera carta hacia la última y se agregan al destino en ese orden. Así, un bloque Q-J conserva Q debajo de J. Al retirar individualmente la última carta, se voltea la que queda descubierta, igual que en el original. En el traslado de bloques, esa acción sigue correspondiendo al código coordinador del solitario.

## Aplicación al juego de 21

Las cuatro clases anteriores pertenecen al solitario de referencia. Se incluyen adaptadas en el paquete `solitaire` para cubrir las clases indicadas en la asignación, pero no se introducen las reglas del solitario en el juego de 21.

En el juego de 21 se adaptan dos clases:

- `Mazo`: almacenamiento permanente en una pila de 52 cartas. Las listas se usan temporalmente para generar, mezclar u ordenar; `obtenerUnaCarta()` usa `pop()`.
- `Mano`: almacenamiento permanente en una pila de 52 cartas. Recibe cartas con `push()` y genera una lista temporal en orden de llegada para calcular puntos y actualizar las vistas.

La clase `Pila` conserva sus métodos `push`, `pop`, `peek`, `empty`, `isFull`, `size` y `clear`, sin modificaciones. Las listas de jugadores y de paneles siguen siendo listas porque su acceso depende del turno o la posición, no del orden LIFO.

## Compatibilidad y límites

- Las listas devueltas por `Mazo.getCartas()`, `Mano.getCartas()` y `TableauDeck.getCards()` son copias del contenedor. Agregar o eliminar elementos de esas listas ya no modifica el almacenamiento interno.
- Las cartas de esas copias son los mismos objetos: voltearlas mantiene el comportamiento de la interfaz y del dealer. No son copias profundas ni estados preparados para `Undo`.
- Los métodos que antes devolvían `null` al consultar o retirar de un montón vacío conservan ese resultado, comprobando `empty()` antes de usar `peek()` o `pop()`.
- Las capacidades son fijas. Se rechazan lotes demasiado grandes antes de insertarlos parcialmente.
- Se validan cantidades inválidas al configurar o retirar de `DrawPile`, y se exige una carga inicial de 1 a 52 cartas para `TableauDeck`.
- Consultar todo el contenido y mover un bloque requiere recorridos; no todas las operaciones pasan a ser constantes por usar una pila. El reparto individual del mazo sí evita desplazar las cartas restantes como ocurría con `remove(0)`.

## Verificación realizada antes de integrar

- Compilación con el archivo de construcción de NetBeans: correcta.
- 20 casos de prueba: operaciones básicas, capacidad, 52 cartas únicas, orden del mazo, puntuación de ases, blackjack, recarga, reglas de fundaciones, consulta y traslado de bloques, y reglas del dealer.
- Uno de esos casos simula 200 partidas de 1 a 4 jugadores, comprobando finalización, resultados y ausencia de cartas repetidas.
- Interfaz JavaFX original: nueva partida, selección de 1 a 4 jugadores, pedir, plantarse y deshabilitación de botones al terminar; captura visual revisada.
- Compilación del `SolitaireGame` y la interfaz de texto originales contra las cuatro clases adaptadas; ejecución de extracción hasta agotar el mazo y recarga.

La práctica 1 y las fuentes originales descargadas de Solitaire no se modifican. Los cambios se preparan únicamente para `Practica_2_Algoritmos`.

Después de integrar los archivos, se repitieron la compilación de NetBeans, los 20 casos y la prueba de interfaz contra el proyecto real. Todas las comprobaciones pasaron. Se verificó además que el proyecto original de la práctica 1 y la clase Pila permanecen sin cambios.
