package ec.edu.ups.patrones.memento.dominio;

import java.math.BigDecimal;
import java.util.Objects;

public record Extra(String descripcion, BigDecimal precio) {

    public Extra {
        Objects.requireNonNull(descripcion, "descripcion");
        Objects.requireNonNull(precio, "precio");
    }
}
