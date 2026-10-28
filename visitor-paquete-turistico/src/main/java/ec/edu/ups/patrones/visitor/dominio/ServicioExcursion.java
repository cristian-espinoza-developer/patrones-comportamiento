package ec.edu.ups.patrones.visitor.dominio;

import ec.edu.ups.patrones.visitor.visitantes.VisitanteServicio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/** ConcreteElement: excursión o tour en destino. */
public record ServicioExcursion(String codigo, String nombre, LocalDate fecha, int personas,
                                BigDecimal precioPorPersona) implements ServicioTuristico {

    public ServicioExcursion {
        Objects.requireNonNull(codigo, "codigo");
        Objects.requireNonNull(nombre, "nombre");
        Objects.requireNonNull(fecha, "fecha");
        Objects.requireNonNull(precioPorPersona, "precioPorPersona");
        if (personas <= 0) {
            throw new IllegalArgumentException("La excursión debe tener al menos una persona");
        }
        if (precioPorPersona.signum() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
    }

    @Override
    public BigDecimal precioBase() {
        return precioPorPersona.multiply(BigDecimal.valueOf(personas));
    }

    @Override
    public void aceptar(VisitanteServicio visitante) {
        visitante.visitarExcursion(this);
    }
}
