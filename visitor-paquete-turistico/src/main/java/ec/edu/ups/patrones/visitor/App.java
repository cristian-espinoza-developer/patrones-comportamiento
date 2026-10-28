package ec.edu.ups.patrones.visitor;

import ec.edu.ups.patrones.visitor.dominio.PaqueteTuristico;
import ec.edu.ups.patrones.visitor.dominio.ServicioExcursion;
import ec.edu.ups.patrones.visitor.dominio.ServicioHotel;
import ec.edu.ups.patrones.visitor.dominio.ServicioSeguro;
import ec.edu.ups.patrones.visitor.dominio.ServicioTraslado;
import ec.edu.ups.patrones.visitor.dominio.ServicioVuelo;
import ec.edu.ups.patrones.visitor.visitantes.CalculadoraImpuestos;
import ec.edu.ups.patrones.visitor.visitantes.CalculadoraPuntosFidelidad;
import ec.edu.ups.patrones.visitor.visitantes.GeneradorItinerario;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Client: demostración del patrón Visitor sobre un Paquete Turístico. */
public class App {

    public static void main(String[] args) {
        LocalDate salida = LocalDate.of(2026, 12, 10);

        PaqueteTuristico paquete = new PaqueteTuristico("PAQ-001", "Ana Torres");
        // Se agregan a propósito fuera de orden: el itinerario los ordena por fecha.
        paquete.agregarServicio(new ServicioSeguro("SEG-01", "Asistencia médica", salida, 7, new BigDecimal("6.00")));
        paquete.agregarServicio(new ServicioHotel("HOT-01", "Hotel Caribe", "Cancún", salida, 5, new BigDecimal("120.00")));
        paquete.agregarServicio(new ServicioVuelo("VUE-01", "UIO", "CUN", salida, true, new BigDecimal("650.00")));
        paquete.agregarServicio(new ServicioTraslado("TRA-01", "Aeropuerto CUN", "Hotel Caribe", salida, new BigDecimal("35.00")));
        paquete.agregarServicio(new ServicioExcursion("EXC-01", "Chichén Itzá", salida.plusDays(2), 2, new BigDecimal("90.00")));
        paquete.agregarServicio(new ServicioVuelo("VUE-02", "CUN", "UIO", salida.plusDays(5), true, new BigDecimal("620.00")));
        paquete.agregarServicio(new ServicioVuelo("VUE-03", "UIO", "GYE", salida.plusDays(6), false, new BigDecimal("80.00")));

        titulo("Paquete");
        System.out.println("  " + paquete);

        titulo("1. Operación: impuestos (CalculadoraImpuestos)");
        CalculadoraImpuestos impuestos = new CalculadoraImpuestos();
        paquete.aceptar(impuestos);
        impuestos.obtenerDetalle().forEach(linea -> System.out.println("  " + linea));
        System.out.println("  Total impuestos: " + impuestos.obtenerTotal());

        titulo("2. Operación: puntos de fidelidad (CalculadoraPuntosFidelidad)");
        CalculadoraPuntosFidelidad puntos = new CalculadoraPuntosFidelidad();
        paquete.aceptar(puntos);
        puntos.obtenerDetalle().forEach(linea -> System.out.println("  " + linea));
        System.out.println("  Total puntos: " + puntos.obtenerTotalPuntos());

        titulo("3. Operación: itinerario (GeneradorItinerario)");
        GeneradorItinerario itinerario = new GeneradorItinerario();
        paquete.aceptar(itinerario);
        itinerario.obtenerItinerario().forEach(linea -> System.out.println("  " + linea));

        titulo("Resumen");
        BigDecimal totalPagar = paquete.obtenerPrecioBase().add(impuestos.obtenerTotal());
        System.out.println("  Precio base: " + paquete.obtenerPrecioBase()
                + " | Impuestos: " + impuestos.obtenerTotal()
                + " | Total a pagar: " + totalPagar);
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("== " + texto);
    }
}
