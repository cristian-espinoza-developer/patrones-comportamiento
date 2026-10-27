package ec.edu.ups.patrones.memento.dominio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Originator: reserva de viaje en construcción.
 * Es la única clase que conoce su estado interno y la única que puede crear y leer sus mementos.
 */
public class ReservaViaje {

    private static final int PASAJEROS_PARA_DESCUENTO = 4;
    private static final BigDecimal DESCUENTO_GRUPAL = new BigDecimal("0.10");

    private final String id;
    private Paso pasoActual;
    private BigDecimal precioTotal; // derivado: se recalcula, nunca se asigna desde fuera
    private final List<Pasajero> pasajeros = new ArrayList<>();
    private Vuelo vuelo;
    private final List<AsignacionAsiento> asignaciones = new ArrayList<>();
    private final List<Extra> extras = new ArrayList<>();

    public ReservaViaje(String id, Pasajero titular) {
        this.id = Objects.requireNonNull(id, "id");
        this.pasajeros.add(Objects.requireNonNull(titular, "titular"));
        this.pasoActual = Paso.PASAJERO;
        recalcularPrecio();
    }

    public void agregarPasajero(Pasajero pasajero) {
        Objects.requireNonNull(pasajero, "pasajero");
        if (pasajeros.contains(pasajero)) {
            throw new IllegalArgumentException("El pasajero ya está en la reserva: " + pasajero.nombre());
        }
        pasajeros.add(pasajero);
        recalcularPrecio();
        validarInvariantes();
    }

    /** Cambiar de vuelo invalida los asientos y extras elegidos, que dependen del vuelo y la tarifa. */
    public void establecerVuelo(Vuelo vuelo) {
        Objects.requireNonNull(vuelo, "vuelo");
        if (!vuelo.fecha().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha del vuelo debe ser futura: " + vuelo.fecha());
        }
        if (!vuelo.equals(this.vuelo)) {
            asignaciones.clear();
            extras.clear();
        }
        this.vuelo = vuelo;
        recalcularPrecio();
        validarInvariantes();
    }

    public void asignarAsiento(Pasajero pasajero, Asiento asiento) {
        Objects.requireNonNull(pasajero, "pasajero");
        Objects.requireNonNull(asiento, "asiento");
        if (vuelo == null) {
            throw new IllegalStateException("Debe elegir un vuelo antes de asignar asientos");
        }
        if (!pasajeros.contains(pasajero)) {
            throw new IllegalArgumentException("El pasajero no pertenece a la reserva: " + pasajero.nombre());
        }
        boolean ocupado = asignaciones.stream()
                .anyMatch(a -> a.asiento().equals(asiento) && !a.pasajero().equals(pasajero));
        if (ocupado) {
            throw new IllegalArgumentException("El asiento ya está asignado: " + asiento.codigo());
        }
        asignaciones.removeIf(a -> a.pasajero().equals(pasajero)); // un asiento por pasajero
        asignaciones.add(new AsignacionAsiento(pasajero, asiento));
        validarInvariantes();
    }

    public void agregarExtra(Extra extra) {
        Objects.requireNonNull(extra, "extra");
        extras.add(extra);
        recalcularPrecio();
    }

    public Paso obtenerPasoActual() {
        return pasoActual;
    }

    public BigDecimal obtenerPrecioTotal() {
        return precioTotal;
    }

    /** Avanza al siguiente paso si el actual está completo. */
    public void siguientePaso() {
        switch (pasoActual) {
            case PASAJERO, EXTRAS -> { /* siempre hay un titular y los extras son opcionales */ }
            case VUELO -> {
                if (vuelo == null) {
                    throw new IllegalStateException("Debe elegir un vuelo para continuar");
                }
            }
            case ASIENTOS -> {
                if (asignaciones.size() != pasajeros.size()) {
                    throw new IllegalStateException("Todos los pasajeros deben tener asiento para continuar");
                }
            }
            case PAGO -> throw new IllegalStateException("La reserva ya está en el último paso");
        }
        pasoActual = Paso.values()[pasoActual.ordinal() + 1];
    }

    /** Crea un memento con una copia del estado actual. */
    public Borrador guardarBorrador(String etiqueta) {
        return new BorradorReserva(this, etiqueta);
    }

    /**
     * Restaura el estado desde un memento. El precio no se restaura: se recalcula a partir de
     * los datos restaurados, así los datos derivados no pueden quedar inconsistentes.
     */
    public void restaurar(Borrador borrador) {
        if (!(borrador instanceof BorradorReserva b)) {
            throw new IllegalArgumentException("El borrador no fue creado por una ReservaViaje");
        }
        this.pasoActual = b.pasoActual;
        this.vuelo = b.vuelo;
        this.pasajeros.clear();
        this.pasajeros.addAll(b.pasajeros);
        this.asignaciones.clear();
        this.asignaciones.addAll(b.asignaciones);
        this.extras.clear();
        this.extras.addAll(b.extras);
        recalcularPrecio();
    }

    private void recalcularPrecio() {
        BigDecimal total = BigDecimal.ZERO;
        if (vuelo != null) {
            BigDecimal tarifaBase = switch (vuelo.tarifa()) {
                case ECONOMICA -> new BigDecimal("100");
                case FLEXIBLE -> new BigDecimal("150");
                case PREMIUM -> new BigDecimal("300");
            };
            total = tarifaBase.multiply(BigDecimal.valueOf(pasajeros.size()));
        }
        for (Extra extra : extras) {
            total = total.add(extra.precio());
        }
        if (pasajeros.size() >= PASAJEROS_PARA_DESCUENTO) {
            total = total.subtract(total.multiply(DESCUENTO_GRUPAL));
        }
        this.precioTotal = total.setScale(2, RoundingMode.HALF_UP);
    }

    private void validarInvariantes() {
        if (pasajeros.isEmpty()) {
            throw new IllegalStateException("La reserva debe tener al menos un pasajero");
        }
        long pasajerosConAsiento = asignaciones.stream().map(AsignacionAsiento::pasajero).distinct().count();
        long asientosDistintos = asignaciones.stream().map(AsignacionAsiento::asiento).distinct().count();
        boolean pasajerosValidos = pasajeros.containsAll(
                asignaciones.stream().map(AsignacionAsiento::pasajero).toList());
        if (pasajerosConAsiento != asignaciones.size() || asientosDistintos != asignaciones.size()
                || !pasajerosValidos) {
            throw new IllegalStateException("Asignación de asientos inconsistente");
        }
    }

    @Override
    public String toString() {
        String asientos = asignaciones.isEmpty() ? "-" : asignaciones.stream()
                .map(a -> a.pasajero().nombre() + "=" + a.asiento().codigo())
                .collect(Collectors.joining(", "));
        String listaExtras = extras.isEmpty() ? "-" : extras.stream()
                .map(Extra::descripcion)
                .collect(Collectors.joining(", "));
        String descripcionVuelo = vuelo == null ? "-"
                : vuelo.codigo() + " " + vuelo.origen() + "->" + vuelo.destino() + " " + vuelo.tarifa();
        return "Reserva " + id + " [paso=" + pasoActual
                + ", pasajeros=" + pasajeros.stream().map(Pasajero::nombre).collect(Collectors.joining(", "))
                + ", vuelo=" + descripcionVuelo
                + ", asientos=" + asientos
                + ", extras=" + listaExtras
                + ", total=" + precioTotal + "]";
    }

    /**
     * Memento (interfaz amplia): copia inmutable del estado de la reserva.
     * Es privado: fuera de {@link ReservaViaje} solo se conoce como {@link Borrador}.
     * Los elementos son objetos de valor inmutables, por lo que copiar las colecciones basta.
     */
    private static final class BorradorReserva implements Borrador {

        private final Paso pasoActual;
        private final Instant fechaCreacion;
        private final String etiqueta;
        private final List<Pasajero> pasajeros;
        private final Vuelo vuelo;
        private final List<AsignacionAsiento> asignaciones;
        private final List<Extra> extras;

        BorradorReserva(ReservaViaje reserva, String etiqueta) {
            this.pasoActual = reserva.pasoActual;
            this.fechaCreacion = Instant.now();
            this.etiqueta = etiqueta;
            this.pasajeros = List.copyOf(reserva.pasajeros);
            this.vuelo = reserva.vuelo;
            this.asignaciones = List.copyOf(reserva.asignaciones);
            this.extras = List.copyOf(reserva.extras);
        }

        @Override
        public Paso obtenerPaso() {
            return pasoActual;
        }

        @Override
        public Instant obtenerFechaCreacion() {
            return fechaCreacion;
        }

        @Override
        public String obtenerEtiqueta() {
            return etiqueta;
        }
    }
}
