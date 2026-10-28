package ec.edu.ups.patrones.visitor.visitantes;

import ec.edu.ups.patrones.visitor.dominio.ServicioExcursion;
import ec.edu.ups.patrones.visitor.dominio.ServicioHotel;
import ec.edu.ups.patrones.visitor.dominio.ServicioSeguro;
import ec.edu.ups.patrones.visitor.dominio.ServicioTraslado;
import ec.edu.ups.patrones.visitor.dominio.ServicioVuelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * ConcreteVisitor: calcula los impuestos del paquete. Cada tipo de servicio
 * tributa distinto. Las tasas son simplificadas para la demostración.
 */
public class CalculadoraImpuestos implements VisitanteServicio {

    static final BigDecimal IVA = new BigDecimal("0.15");
    static final BigDecimal TASA_AEROPORTUARIA = new BigDecimal("40.00");
    static final BigDecimal TASA_TURISTICA_POR_NOCHE = new BigDecimal("2.00");
    static final BigDecimal CONTRIBUCION_SEGURO = new BigDecimal("0.035");

    private BigDecimal total = BigDecimal.ZERO;
    private final List<String> detalle = new ArrayList<>();

    @Override
    public void visitarVuelo(ServicioVuelo vuelo) {
        if (vuelo.internacional()) {
            registrar(vuelo.codigo(), "Tasa aeroportuaria (exento de IVA)", TASA_AEROPORTUARIA);
        } else {
            registrar(vuelo.codigo(), "IVA vuelo nacional", vuelo.precioBase().multiply(IVA));
        }
    }

    @Override
    public void visitarHotel(ServicioHotel hotel) {
        BigDecimal tasaTuristica = TASA_TURISTICA_POR_NOCHE.multiply(BigDecimal.valueOf(hotel.noches()));
        registrar(hotel.codigo(), "IVA + tasa turística por noche",
                hotel.precioBase().multiply(IVA).add(tasaTuristica));
    }

    @Override
    public void visitarTraslado(ServicioTraslado traslado) {
        registrar(traslado.codigo(), "IVA traslado", traslado.precioBase().multiply(IVA));
    }

    @Override
    public void visitarExcursion(ServicioExcursion excursion) {
        registrar(excursion.codigo(), "IVA excursión", excursion.precioBase().multiply(IVA));
    }

    @Override
    public void visitarSeguro(ServicioSeguro seguro) {
        registrar(seguro.codigo(), "Contribución de seguros", seguro.precioBase().multiply(CONTRIBUCION_SEGURO));
    }

    public BigDecimal obtenerTotal() {
        return total;
    }

    public List<String> obtenerDetalle() {
        return List.copyOf(detalle);
    }

    private void registrar(String codigo, String concepto, BigDecimal monto) {
        BigDecimal redondeado = monto.setScale(2, RoundingMode.HALF_UP);
        total = total.add(redondeado);
        detalle.add(String.format("%-8s %-36s %10s", codigo, concepto, redondeado));
    }
}
