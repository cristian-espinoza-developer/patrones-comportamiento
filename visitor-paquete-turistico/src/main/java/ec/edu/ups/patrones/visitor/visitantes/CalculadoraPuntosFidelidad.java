package ec.edu.ups.patrones.visitor.visitantes;

import ec.edu.ups.patrones.visitor.dominio.ServicioExcursion;
import ec.edu.ups.patrones.visitor.dominio.ServicioHotel;
import ec.edu.ups.patrones.visitor.dominio.ServicioSeguro;
import ec.edu.ups.patrones.visitor.dominio.ServicioTraslado;
import ec.edu.ups.patrones.visitor.dominio.ServicioVuelo;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * ConcreteVisitor: calcula los puntos del programa de fidelidad.
 * Cada tipo de servicio acumula con una regla distinta.
 */
public class CalculadoraPuntosFidelidad implements VisitanteServicio {

    static final int PUNTOS_POR_NOCHE = 10;
    static final int PUNTOS_POR_PERSONA_EXCURSION = 5;

    private int totalPuntos;
    private final List<String> detalle = new ArrayList<>();

    @Override
    public void visitarVuelo(ServicioVuelo vuelo) {
        int puntos = vuelo.precioBase().setScale(0, RoundingMode.DOWN).intValueExact();
        if (vuelo.internacional()) {
            puntos *= 2;
        }
        registrar(vuelo.codigo(), vuelo.internacional() ? "2 puntos por dólar (internacional)" : "1 punto por dólar", puntos);
    }

    @Override
    public void visitarHotel(ServicioHotel hotel) {
        registrar(hotel.codigo(), PUNTOS_POR_NOCHE + " puntos por noche", PUNTOS_POR_NOCHE * hotel.noches());
    }

    @Override
    public void visitarTraslado(ServicioTraslado traslado) {
        registrar(traslado.codigo(), "No acumula", 0);
    }

    @Override
    public void visitarExcursion(ServicioExcursion excursion) {
        registrar(excursion.codigo(), PUNTOS_POR_PERSONA_EXCURSION + " puntos por persona",
                PUNTOS_POR_PERSONA_EXCURSION * excursion.personas());
    }

    @Override
    public void visitarSeguro(ServicioSeguro seguro) {
        registrar(seguro.codigo(), "No acumula", 0);
    }

    public int obtenerTotalPuntos() {
        return totalPuntos;
    }

    public List<String> obtenerDetalle() {
        return List.copyOf(detalle);
    }

    private void registrar(String codigo, String regla, int puntos) {
        totalPuntos += puntos;
        detalle.add(String.format("%-8s %-36s %10d", codigo, regla, puntos));
    }
}
