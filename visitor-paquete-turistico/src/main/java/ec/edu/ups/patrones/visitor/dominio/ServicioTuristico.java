package ec.edu.ups.patrones.visitor.dominio;

import ec.edu.ups.patrones.visitor.visitantes.VisitanteServicio;

import java.math.BigDecimal;

/**
 * Element del patrón Visitor: todo servicio del paquete acepta un visitante
 * y le indica su tipo concreto (doble despacho).
 */
public interface ServicioTuristico {

    String codigo();

    BigDecimal precioBase();

    void aceptar(VisitanteServicio visitante);
}
