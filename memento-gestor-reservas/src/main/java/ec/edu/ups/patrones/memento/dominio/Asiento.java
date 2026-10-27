package ec.edu.ups.patrones.memento.dominio;

import java.util.Objects;

public record Asiento(String codigo) {

    public Asiento {
        Objects.requireNonNull(codigo, "codigo");
    }
}
