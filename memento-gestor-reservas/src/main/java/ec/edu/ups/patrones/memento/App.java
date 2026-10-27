package ec.edu.ups.patrones.memento;

import ec.edu.ups.patrones.memento.asistente.AsistenteReserva;
import ec.edu.ups.patrones.memento.cliente.InterfazReserva;
import ec.edu.ups.patrones.memento.dominio.Asiento;
import ec.edu.ups.patrones.memento.dominio.Extra;
import ec.edu.ups.patrones.memento.dominio.Pasajero;
import ec.edu.ups.patrones.memento.dominio.ReservaViaje;
import ec.edu.ups.patrones.memento.dominio.Tarifa;
import ec.edu.ups.patrones.memento.dominio.Vuelo;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Demostración del patrón Memento sobre el Gestor de Reservas de Viaje. */
public class App {

    public static void main(String[] args) {
        Pasajero ana = new Pasajero("Ana Torres", "0102030405");
        Pasajero luis = new Pasajero("Luis Mora", "0607080910");

        ReservaViaje reserva = new ReservaViaje("RES-001", ana);
        AsistenteReserva asistente = new AsistenteReserva(reserva);
        InterfazReserva interfaz = new InterfazReserva(asistente);

        titulo("1. Se completan los pasos hasta llegar a Extras");
        reserva.agregarPasajero(luis);
        interfaz.alPresionarSiguiente();

        reserva.establecerVuelo(new Vuelo("UIO-GYE-1", "UIO", "GYE", LocalDate.now().plusDays(30), Tarifa.ECONOMICA));
        interfaz.alPresionarSiguiente();

        reserva.asignarAsiento(ana, new Asiento("12A"));
        interfaz.alPresionarSiguiente(); // rechazado: falta el asiento de Luis
        reserva.asignarAsiento(luis, new Asiento("12B"));
        interfaz.alPresionarSiguiente();

        reserva.agregarExtra(new Extra("Equipaje 23 kg", new BigDecimal("25")));
        reserva.agregarExtra(new Extra("Seguro de viaje", new BigDecimal("15")));
        mostrar(reserva);

        titulo("2. El usuario vuelve atrás: se descartan los extras del paso actual");
        interfaz.alPresionarAtras();
        mostrar(reserva);

        titulo("3. Se guarda el borrador 'Plan A' y se prueba otra configuración");
        interfaz.alGuardarBorrador("Plan A");
        reserva.establecerVuelo(new Vuelo("UIO-GYE-2", "UIO", "GYE", LocalDate.now().plusDays(45), Tarifa.PREMIUM));
        mostrar(reserva); // asientos invalidados y precio recalculado

        titulo("4. Se descarta la prueba y se retoma 'Plan A'");
        interfaz.alRetomarBorrador("Plan A");
        mostrar(reserva); // asientos y precio restaurados

        titulo("5. Límites del flujo");
        interfaz.alRetomarBorrador("No existe");
        while (asistente.puedeRetroceder()) {
            interfaz.alPresionarAtras();
        }
        interfaz.alPresionarAtras();
        mostrar(reserva);
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("== " + texto);
    }

    private static void mostrar(ReservaViaje reserva) {
        System.out.println("  " + reserva);
    }
}
