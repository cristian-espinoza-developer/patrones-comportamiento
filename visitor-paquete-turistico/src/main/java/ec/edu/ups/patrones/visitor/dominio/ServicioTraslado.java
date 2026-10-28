package ec.edu.ups.patrones.visitor.dominio;

import ec.edu.ups.patrones.visitor.visitantes.VisitanteServicio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/** ConcreteElement: traslado terrestre (aeropuerto, hotel, etc.). */
public record ServicioTraslado(String codigo, String origen, String destino, LocalDate fecha,
                               BigDecimal precioBase) implements ServicioTuristico {

    public ServicioTraslado {
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
        visitante.visitarTraslado(this);
    }
}
