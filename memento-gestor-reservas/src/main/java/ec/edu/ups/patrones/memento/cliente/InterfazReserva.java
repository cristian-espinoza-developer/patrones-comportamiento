package ec.edu.ups.patrones.memento.cliente;

import ec.edu.ups.patrones.memento.asistente.AsistenteReserva;

import java.util.Objects;

/** Client: traduce las acciones del usuario en llamadas al asistente y muestra los errores de flujo. */
public class InterfazReserva {

    private final AsistenteReserva asistente;

    public InterfazReserva(AsistenteReserva asistente) {
        this.asistente = Objects.requireNonNull(asistente, "asistente");
    }

    public void alPresionarSiguiente() {
        try {
            asistente.avanzar();
            System.out.println("  [UI] Siguiente -> OK");
        } catch (IllegalStateException e) {
            System.out.println("  [UI] Siguiente -> rechazado: " + e.getMessage());
        }
    }

    public void alPresionarAtras() {
        try {
            asistente.retroceder();
            System.out.println("  [UI] Atrás -> OK");
        } catch (IllegalStateException e) {
            System.out.println("  [UI] Atrás -> rechazado: " + e.getMessage());
        }
    }

    public void alGuardarBorrador(String etiqueta) {
        asistente.guardarBorradorNombrado(etiqueta);
        System.out.println("  [UI] Borrador guardado: " + etiqueta);
    }

    public void alRetomarBorrador(String etiqueta) {
        try {
            asistente.retomarBorrador(etiqueta);
            System.out.println("  [UI] Borrador retomado: " + etiqueta);
        } catch (IllegalArgumentException e) {
            System.out.println("  [UI] Retomar -> rechazado: " + e.getMessage());
        }
    }
}
