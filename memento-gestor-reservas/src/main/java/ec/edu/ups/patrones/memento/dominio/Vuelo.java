package ec.edu.ups.patrones.memento.dominio;

import java.time.LocalDate;
import java.util.Objects;

public record Vuelo(String codigo, String origen, String destino, LocalDate fecha, Tarifa tarifa) {

    public Vuelo {
        Objects.requireNonNull(codigo, "codigo");
        Objects.requireNonNull(origen, "origen");
        Objects.requireNonNull(destino, "destino");
        Objects.requireNonNull(fecha, "fecha");
        Objects.requireNonNull(tarifa, "tarifa");
    }
}
