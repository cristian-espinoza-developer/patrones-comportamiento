package ec.edu.ups.patrones.memento.asistente;

import ec.edu.ups.patrones.memento.dominio.Borrador;
import ec.edu.ups.patrones.memento.dominio.ReservaViaje;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Caretaker: decide cuándo capturar y cuándo restaurar el estado de la reserva y conserva los
 * borradores. Solo los maneja como {@link Borrador}; nunca accede a su contenido.
 */
public class AsistenteReserva {

    private final int limiteHistorial = 5;
    private final ReservaViaje reserva;
    private final Deque<Borrador> historial = new ArrayDeque<>();
    private final Map<String, Borrador> borradoresNombrados = new HashMap<>();

    public AsistenteReserva(ReservaViaje reserva) {
        this.reserva = Objects.requireNonNull(reserva, "reserva");
    }

    /** Captura el estado y avanza. Si el paso actual está incompleto, no se guarda nada. */
    public void avanzar() {
        Borrador borrador = reserva.guardarBorrador("antes de " + reserva.obtenerPasoActual());
        reserva.siguientePaso();
        historial.push(borrador);
        if (historial.size() > limiteHistorial) {
            historial.removeLast(); // se descarta el borrador más antiguo
        }
    }

    /** Vuelve al estado previo al último avance; descarta los cambios hechos en el paso actual. */
    public void retroceder() {
        if (!puedeRetroceder()) {
            throw new IllegalStateException("No hay pasos anteriores");
        }
        reserva.restaurar(historial.pop());
    }

    public boolean puedeRetroceder() {
        return !historial.isEmpty();
    }

    public void guardarBorradorNombrado(String etiqueta) {
        Objects.requireNonNull(etiqueta, "etiqueta");
        borradoresNombrados.put(etiqueta, reserva.guardarBorrador(etiqueta));
    }

    /** Retoma un borrador con nombre. El historial previo pertenece a otra línea de tiempo y se descarta. */
    public void retomarBorrador(String etiqueta) {
        Borrador borrador = borradoresNombrados.get(etiqueta);
        if (borrador == null) {
            throw new IllegalArgumentException("No existe el borrador: " + etiqueta);
        }
        reserva.restaurar(borrador);
        historial.clear();
    }
}
