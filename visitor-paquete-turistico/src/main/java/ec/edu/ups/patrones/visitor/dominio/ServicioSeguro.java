package ec.edu.ups.patrones.visitor.dominio;

import ec.edu.ups.patrones.visitor.visitantes.VisitanteServicio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/** ConcreteElement: seguro de viaje. */
public record ServicioSeguro(String codigo, String cobertura, LocalDate fechaInicio, int dias,
                             BigDecimal precioPorDia) implements ServicioTuristico {

    public ServicioSeguro {
        Objects.requireNonNull(codigo, "codigo");
        Objects.requireNonNull(cobertura, "cobertura");
        Objects.requireNonNull(fechaInicio, "fechaInicio");
        Objects.requireNonNull(precioPorDia, "precioPorDia");
        if (dias <= 0) {
            throw new IllegalArgumentException("El seguro debe cubrir al menos un día");
        }
        if (precioPorDia.signum() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
    }

    @Override
    public BigDecimal precioBase() {
        return precioPorDia.multiply(BigDecimal.valueOf(dias));
    }

    @Override
    public void aceptar(VisitanteServicio visitante) {
        visitante.visitarSeguro(this);
    }
}
