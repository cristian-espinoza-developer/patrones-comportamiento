package ec.edu.ups.patrones.visitor.dominio;

import ec.edu.ups.patrones.visitor.visitantes.VisitanteServicio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/** ConcreteElement: estadía en un hotel. */
public record ServicioHotel(String codigo, String nombre, String ciudad, LocalDate fechaIngreso,
                            int noches, BigDecimal precioNoche) implements ServicioTuristico {

    public ServicioHotel {
        Objects.requireNonNull(codigo, "codigo");
        Objects.requireNonNull(nombre, "nombre");
        Objects.requireNonNull(ciudad, "ciudad");
        Objects.requireNonNull(fechaIngreso, "fechaIngreso");
        Objects.requireNonNull(precioNoche, "precioNoche");
        if (noches <= 0) {
            throw new IllegalArgumentException("La estadía debe tener al menos una noche");
        }
        if (precioNoche.signum() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
    }

    public LocalDate fechaSalida() {
        return fechaIngreso.plusDays(noches);
    }

    @Override
    public BigDecimal precioBase() {
        return precioNoche.multiply(BigDecimal.valueOf(noches));
    }

    @Override
    public void aceptar(VisitanteServicio visitante) {
        visitante.visitarHotel(this);
    }
}
