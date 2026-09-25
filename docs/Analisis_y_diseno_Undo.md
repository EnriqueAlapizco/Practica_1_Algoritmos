# Práctica 2: análisis y diseño de Deshacer

## Punto de partida

La etapa de pilas quedó registrada en el commit `002f9f1` (Modificacion con pilas). La nueva rama creada por el alumno se llama `undo`. Esta etapa conserva el juego de 21 y utiliza la clase propia `Pila<T>` sin modificar sus métodos.

## Análisis de las acciones

Se registra un estado antes de cada solicitud válida de **Pedir**. Deshacer recupera ese estado y revierte sus consecuencias automáticas: avance del turno por pasarse de 21, jugada del dealer, revelación de cartas y asignación de resultados.

La regla acordada con el alumno establece cierres definitivos del historial:

- **Plantarse:** borra todo el historial y no se registra como acción reversible. Esto aplica con uno o varios jugadores, aunque posteriormente se pierda contra el dealer.
- **Alcanzar exactamente 21:** cierra el turno y borra el historial. El regreso a un turno anterior solo se permite cuando terminó por pasarse de 21.
- **Pasarse de 21:** conserva el historial. Se puede recuperar el turno, incluso después de terminar la partida.
- **Pedir sin llegar a 21:** permite deshacer las cartas solicitadas, hasta el último cierre del historial.

Ejemplo con dos jugadores: si el primero se planta, el segundo puede deshacer sus propias solicitudes, pero nunca regresar al primero. Si el primero se pasa de 21, sí puede recuperarse su turno al deshacer. Cuando el siguiente jugador se planta o alcanza 21, ese nuevo cierre también impide regresar a derrotas anteriores. Varias derrotas consecutivas pueden revertirse en orden inverso mientras no exista un cierre entre ellas.

El reparto inicial es el límite original. Nueva partida borra las acciones de la partida anterior. Las solicitudes rechazadas, como pedir al terminar la partida o con un mazo vacío, no generan historial ni cambian los cierres existentes.

Devolver solamente la última carta sería insuficiente: al pasarse de 21 el jugador puede quedar plantado automáticamente, cambiar el turno y finalizar la partida. Guardar el estado completo permite recuperar esos datos juntos. Si la última derrota activa al dealer, se revierte también su jugada, conservando plantados a quienes ya habían cerrado su turno.

## Regla de barajado acordada

Cuando deshacer devuelve cartas al mazo, se barajan todas las cartas disponibles. Las manos recuperan su contenido y orden anteriores. La carta devuelta puede volver a salir por azar; no se fuerza una carta diferente. Esta es una variación deliberada del deshacer exacto, porque el orden del mazo puede cambiar.

Si el dealer tomó cartas como consecuencia de la derrota que se deshace, también se devuelven y se baraja una sola vez. Las cartas de las manos conservadas no participan en la mezcla.

## Estructura del historial

`Juego` contiene una `Pila<EstadoJuego>`. Antes de pedir hace `push`; al deshacer hace `pop`; `empty` determina si el botón está disponible. `clear` inicia un historial nuevo y establece los cierres por Plantarse o por alcanzar 21.

Se usan 52 posiciones: como máximo hay 48 cartas posteriores al reparto mínimo. Es una cota conservadora para una baraja de 52 cartas y de uno a cuatro jugadores. Deshacer libera una posición, por lo que repetir una jugada no acumula estados abandonados.

Cada `EstadoJuego` guarda:

- Cartas del mazo en orden de reparto y cartas del dealer.
- Cartas de cada jugador en orden de llegada.
- Estado plantado y resultado de cada jugador.
- Índice del turno y estado de finalización de la partida.
- Visibilidad de cada carta.

Las cartas se copian como objetos independientes al guardar y al restaurar. Copiar únicamente las listas no sería suficiente: revelar la carta del dealer podría alterar una referencia compartida con el historial. Los puntajes se recalculan a partir de las manos, sin guardar valores redundantes.

Las listas dentro de cada estado describen los datos de una partida; el historial usa nuestra pila. Las manos y el mazo también conservan su almacenamiento con pilas. Para mezclar se emplea una lista temporal con `Collections.shuffle` y después se vuelve a cargar la pila.

## Cambios de clases y métodos

| Clase | Cambio y propósito |
|---|---|
| `EstadoJuego` (nueva) | Captura datos independientes. `copiarCartas` conserva valor, palo, color y visibilidad; `restaurar` recupera las manos y estados, y restaura/baraja el mazo cuando se devolvieron cartas. Sus consultas recuperan turno y finalización. |
| `Juego` | Añade `historial`, `guardarEstado`, `puedeDeshacer` y `deshacer`. `pedirCarta` guarda antes de actuar y cierra el historial si alcanza exactamente 21. `plantarse` lo vacía sin guardar la acción. `iniciarPartida` también lo limpia. El dealer deja de pedir si el mazo se agota. |
| `Mazo` | Añade `restaurarCartas`, `estaVacio` y `barajar`. Restaurar carga las cartas recibidas sin generar una baraja nueva. |
| `Mano` | Añade `restaurarCartas` para reemplazar su contenido en el orden guardado. Acceso limitado al paquete del modelo. |
| `Jugador` | Añade `restaurarEstado` para recuperar plantado y resultado. Conserva nombre e identidad del jugador. |
| `JuegoView` | Añade botón Deshacer y `getBtnDeshacer`. `actualizar` lo habilita según el historial, incluso después de finalizar la partida. |
| `JuegoController` | Conecta el botón con `juego.deshacer()` y actualiza la vista. |

La interfaz de texto conserva su funcionamiento previo; el control nuevo se incorpora en la interfaz gráfica requerida. Las cuatro clases del paquete `solitaire` conservan la adaptación anterior; el historial corresponde al juego de 21.

## Casos de verificación

Las pruebas anteriores que permitían deshacer Plantarse o regresar desde 21 quedan sustituidas por las nuevas reglas. La revisión verifica:

- Pedir y deshacer durante el mismo turno, devolviendo y barajando las cartas.
- Un jugador que pide, se planta y pierde contra el dealer: Deshacer queda bloqueado.
- Un jugador que se pasa de 21: recupera turno, mano y partida activa.
- Dos jugadores: cierre al plantarse el primero; las cartas del segundo siguen siendo reversibles sin cruzar ese cierre.
- Dos jugadores: retorno al primero cuando se pasó de 21, incluso tras deshacer solicitudes posteriores del segundo.
- Un nuevo Plantarse impide volver a derrotas anteriores.
- Alcanzar 21 cierra el historial.
- Derrota del último jugador seguida de la jugada del dealer: revierte sus consecuencias sin reabrir los turnos cerrados.
- Tres jugadores con dos derrotas consecutivas: deshacer en orden inverso.
- Nueva partida limpia el historial.
- 150 ciclos de pedir/deshacer mantienen las cartas y liberan las posiciones de la pila.
- 200 partidas con pedir, plantarse y deshacer respetan los cierres y conservan 52 cartas distintas.

La prueba JavaFX comprueba que el botón se habilite al pedir o pasarse y se deshabilite al plantarse o llegar a 21, con uno, dos, tres y cuatro jugadores. El bloqueo también se verifica directamente en el modelo, sin depender del botón.

Validación completada en el proyecto real de NetBeans: BUILD SUCCESSFUL, 12 casos de las reglas de Undo aprobados, 20 casos de la etapa Pilas aprobados y prueba JavaFX aprobada para uno a cuatro jugadores. Los cambios están en la rama undo y siguen pendientes de commit y push.

