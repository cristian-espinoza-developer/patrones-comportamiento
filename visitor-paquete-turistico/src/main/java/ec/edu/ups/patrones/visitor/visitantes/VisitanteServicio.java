package ec.edu.ups.patrones.visitor.visitantes;

import ec.edu.ups.patrones.visitor.dominio.ServicioExcursion;
import ec.edu.ups.patrones.visitor.dominio.ServicioHotel;
import ec.edu.ups.patrones.visitor.dominio.ServicioSeguro;
import ec.edu.ups.patrones.visitor.dominio.ServicioTraslado;
import ec.edu.ups.patrones.visitor.dominio.ServicioVuelo;

/**
 * Visitor del patrón: declara una operación por cada tipo concreto de servicio.
 * Cada operación nueva sobre el paquete es una implementación de esta interfaz.
 */
public interface VisitanteServicio {

    void visitarVuelo(ServicioVuelo vuelo);

    void visitarHotel(ServicioHotel hotel);

    void visitarTraslado(ServicioTraslado traslado);

    void visitarExcursion(ServicioExcursion excursion);

    void visitarSeguro(ServicioSeguro seguro);
}
