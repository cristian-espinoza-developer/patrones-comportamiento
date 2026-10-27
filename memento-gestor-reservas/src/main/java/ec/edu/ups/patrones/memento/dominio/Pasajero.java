package ec.edu.ups.patrones.memento.dominio;

import java.util.Objects;

/** Objeto de valor inmutable: puede compartirse entre la reserva y sus mementos sin copiarse. */
public record Pasajero(String nombre, String documento) {

    public Pasajero {
        Objects.requireNonNull(nombre, "nombre");
        Objects.requireNonNull(documento, "documento");
    }
}
