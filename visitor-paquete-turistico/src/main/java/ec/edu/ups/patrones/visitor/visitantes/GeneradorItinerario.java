package ec.edu.ups.patrones.visitor.visitantes;

import ec.edu.ups.patrones.visitor.dominio.ServicioExcursion;
import ec.edu.ups.patrones.visitor.dominio.ServicioHotel;
import ec.edu.ups.patrones.visitor.dominio.ServicioSeguro;
import ec.edu.ups.patrones.visitor.dominio.ServicioTraslado;
import ec.edu.ups.patrones.visitor.dominio.ServicioVuelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * ConcreteVisitor: arma el itinerario del viajero ordenado por fecha,
 * sin importar el orden en que se agregaron los servicios.
 */
public class GeneradorItinerario implements VisitanteServicio {

    private record Entrada(LocalDate fecha, String descripcion) {
    }

    private final List<Entrada> entradas = new ArrayList<>();

    @Override
    public void visitarVuelo(ServicioVuelo vuelo) {
        agregar(vuelo.fecha(), "Vuelo " + vuelo.origen() + " -> " + vuelo.destino() + " (" + vuelo.codigo() + ")");
    }

    @Override
    public void visitarHotel(ServicioHotel hotel) {
        agregar(hotel.fechaIngreso(), "Check-in " + hotel.nombre() + ", " + hotel.ciudad()
                + " (" + hotel.noches() + " noches)");
        agregar(hotel.fechaSalida(), "Check-out " + hotel.nombre());
    }

    @Override
    public void visitarTraslado(ServicioTraslado traslado) {
        agregar(traslado.fecha(), "Traslado " + traslado.origen() + " -> " + traslado.destino());
    }

    @Override
    public void visitarExcursion(ServicioExcursion excursion) {
        agregar(excursion.fecha(), "Excursión " + excursion.nombre() + " (" + excursion.personas() + " personas)");
    }

    @Override
    public void visitarSeguro(ServicioSeguro seguro) {
        agregar(seguro.fechaInicio(), "Inicio del seguro " + seguro.cobertura() + " (" + seguro.dias() + " días)");
    }

    /** Devuelve el itinerario ordenado por fecha; el orden de llegada se conserva en empates. */
    public List<String> obtenerItinerario() {
        return entradas.stream()
                .sorted(Comparator.comparing(Entrada::fecha))
                .map(e -> e.fecha() + "  " + e.descripcion())
                .toList();
    }

    private void agregar(LocalDate fecha, String descripcion) {
        entradas.add(new Entrada(fecha, descripcion));
    }
}
