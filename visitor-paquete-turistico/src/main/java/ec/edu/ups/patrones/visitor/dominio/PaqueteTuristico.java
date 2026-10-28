package ec.edu.ups.patrones.visitor.dominio;

import ec.edu.ups.patrones.visitor.visitantes.VisitanteServicio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ObjectStructure del patrón Visitor: agrupa los servicios del paquete y
 * permite que un visitante los recorra sin exponer la colección.
 */
public class PaqueteTuristico {

    private final String codigo;
    private final String titular;
    private final List<ServicioTuristico> servicios = new ArrayList<>();

    public PaqueteTuristico(String codigo, String titular) {
        this.codigo = Objects.requireNonNull(codigo, "codigo");
        this.titular = Objects.requireNonNull(titular, "titular");
    }

    public void agregarServicio(ServicioTuristico servicio) {
        servicios.add(Objects.requireNonNull(servicio, "servicio"));
    }

    /** Recorre los servicios en el orden en que se agregaron. */
    public void aceptar(VisitanteServicio visitante) {
        Objects.requireNonNull(visitante, "visitante");
        for (ServicioTuristico servicio : servicios) {
            servicio.aceptar(visitante);
        }
    }

    /** Operación uniforme para todos los servicios: no necesita un visitante. */
    public BigDecimal obtenerPrecioBase() {
        return servicios.stream()
                .map(ServicioTuristico::precioBase)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String obtenerCodigo() {
        return codigo;
    }

    public String obtenerTitular() {
        return titular;
    }

    public int cantidadServicios() {
        return servicios.size();
    }

    @Override
    public String toString() {
        return "Paquete " + codigo + " de " + titular + " | servicios=" + servicios.size()
                + " | precio base=" + obtenerPrecioBase();
    }
}
