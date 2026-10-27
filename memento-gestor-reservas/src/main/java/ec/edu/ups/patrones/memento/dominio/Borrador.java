package ec.edu.ups.patrones.memento.dominio;

import java.time.Instant;

/**
 * Memento (interfaz estrecha): lo único que el Caretaker puede ver de un borrador.
 * El estado de la reserva no es accesible a través de esta interfaz.
 */
public interface Borrador {

    Paso obtenerPaso();

    Instant obtenerFechaCreacion();

    String obtenerEtiqueta();
}
