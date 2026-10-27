package ec.edu.ups.patrones.memento.dominio;

import java.util.Objects;

/** Vincula un pasajero con el asiento que ocupa. */
public record AsignacionAsiento(Pasajero pasajero, Asiento asiento) {

    public AsignacionAsiento {
        Objects.requireNonNull(pasajero, "pasajero");
        Objects.requireNonNull(asiento, "asiento");
    }
}
