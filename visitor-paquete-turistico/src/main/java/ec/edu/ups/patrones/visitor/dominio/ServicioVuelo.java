package ec.edu.ups.patrones.visitor.dominio;

import ec.edu.ups.patrones.visitor.visitantes.VisitanteServicio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/** ConcreteElement: tramo aéreo del paquete. */
public record ServicioVuelo(String codigo, String origen, String destino, LocalDate fecha,
                            boolean internacional, BigDecimal precioBase) implements ServicioTuristico {

    public ServicioVuelo {
        Objects.requireNonNull(codigo, "codigo");
        Objects.requireNonNull(origen, "origen");
        Objects.requireNonNull(destino, "destino");
        Objects.requireNonNull(fecha, "fecha");
        Objects.requireNonNull(precioBase, "precioBase");
        if (precioBase.signum() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
    }

    @Override
    public void aceptar(VisitanteServicio visitante) {
        visitante.visitarVuelo(this);
    }
}
