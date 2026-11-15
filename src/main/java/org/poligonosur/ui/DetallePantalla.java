package org.poligonosur.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.poligonosur.controller.PagoController;
import org.poligonosur.controller.SuscripcionController;
import org.poligonosur.exception.DatosException;
import org.poligonosur.exception.ValidacionException;
import org.poligonosur.model.Pago;
import org.poligonosur.model.Suscripcion;

import java.time.LocalDate;

// Detalle de una suscripción (Detalle.fxml): datos, historial de pagos y botones para pagar, modificar, pausar y cancelar
public class DetallePantalla {

    @FXML
    private Label lblServicio;
    @FXML
    private Label lblEstado;
    @FXML
    private Label lblImporte;
    @FXML
    private Label lblPeriodicidad;
    @FXML
    private Label lblProximoCobro;
    @FXML
    private Button btnPausar;
    @FXML
    private TableView<Pago> tablaPagos;
    @FXML
    private TableColumn<Pago, String> colFecha;
    @FXML
    private TableColumn<Pago, String> colImporte;
    @FXML
    private TableColumn<Pago, String> colEstado;

    private final PagoController pagoController = new PagoController();
    private final SuscripcionController suscripcionController = new SuscripcionController();

    private Suscripcion suscripcion;

    @FXML
    private void initialize() {
        // Qué texto se enseña en cada columna
        colFecha.setCellValueFactory(c -> new SimpleStringProperty(Formato.fecha(c.getValue().getFechaPago())));
        colImporte.setCellValueFactory(c -> new SimpleStringProperty(Formato.importe(c.getValue().getImporte())));
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(Formato.texto(c.getValue().getEstado())));
    }

    // Lo llama la pantalla anterior para decir qué suscripción se enseña
    public void cargar(Suscripcion s) {
        suscripcion = s;
        mostrarDatos();
        cargarHistorial();
    }

    private void mostrarDatos() {
        lblServicio.setText(suscripcion.getNombreServicio());
        lblEstado.setText("Estado: " + Formato.texto(suscripcion.getEstado()));
        lblImporte.setText(Formato.importe(suscripcion.getImporte()));
        lblPeriodicidad.setText(Formato.texto(suscripcion.getPeriodicidad()));
        lblProximoCobro.setText(Formato.fecha(suscripcion.getProximoPago()));
        // Si está pausada el mismo botón sirve para volver a activarla
        btnPausar.setText("PAUSADA".equals(suscripcion.getEstado()) ? "Reactivar" : "Pausar");
    }

    private void cargarHistorial() {
        try {
            tablaPagos.getItems().setAll(pagoController.listarHistorial(suscripcion.getIdSuscripcion()));
        } catch (ValidacionException | DatosException e) {
            Navegacion.mostrarError(e.getMessage());
        }
    }

    @FXML
    private void registrarPago() {
        try {
            LocalDate nuevoProximoPago = pagoController.registrarPago(suscripcion.getIdSuscripcion());
            suscripcion.setProximoPago(nuevoProximoPago);
            mostrarDatos();
            cargarHistorial();
            Navegacion.mostrarInfo("Pago registrado. Próximo cobro: " + Formato.fecha(nuevoProximoPago));
        } catch (ValidacionException | DatosException e) {
            Navegacion.mostrarError(e.getMessage());
        }
    }

    @FXML
    private void modificar() {
        FormularioPantalla formulario = Navegacion.ir("Formulario.fxml");
        formulario.cargar(suscripcion);
    }

    @FXML
    private void pausar() {
        cambiarEstado("PAUSADA".equals(suscripcion.getEstado()) ? "ACTIVA" : "PAUSADA");
    }

    @FXML
    private void cancelar() {
        if (Navegacion.confirmar("¿Seguro que quieres cancelar " + suscripcion.getNombreServicio() + "?")) {
            cambiarEstado("CANCELADA");
        }
    }

    // Guarda el nuevo estado. Si falla, se deja el que tenía.
    private void cambiarEstado(String nuevoEstado) {
        String estadoAnterior = suscripcion.getEstado();
        suscripcion.setEstado(nuevoEstado);
        try {
            suscripcionController.modificar(suscripcion);
        } catch (ValidacionException | DatosException e) {
            suscripcion.setEstado(estadoAnterior);
            Navegacion.mostrarError(e.getMessage());
        }
        mostrarDatos();
    }

    @FXML
    private void volver() {
        Navegacion.ir("Panel.fxml");
    }
}
