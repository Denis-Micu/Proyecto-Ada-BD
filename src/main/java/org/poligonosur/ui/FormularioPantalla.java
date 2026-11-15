package org.poligonosur.ui;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.poligonosur.controller.SuscripcionController;
import org.poligonosur.exception.DatosException;
import org.poligonosur.exception.ValidacionException;
import org.poligonosur.model.Servicio;
import org.poligonosur.model.Suscripcion;

import java.math.BigDecimal;
import java.time.LocalDate;

// Alta y modificación de una suscripción (Formulario.fxml).
// Si se llama a cargar() es una modificación; si no, es un alta.
public class FormularioPantalla {

    @FXML
    private Label lblTitulo;
    @FXML
    private ComboBox<Servicio> cmbServicio;
    @FXML
    private TextField txtImporte;
    @FXML
    private ComboBox<String> cmbPeriodicidad;
    @FXML
    private DatePicker dateInicio;
    @FXML
    private TextField txtDiasAviso;
    @FXML
    private TextArea txtNotas;

    private final SuscripcionController suscripcionController = new SuscripcionController();

    // La suscripción que se está modificando, o null si es nueva
    private Suscripcion suscripcion;

    @FXML
    private void initialize() {
        lblTitulo.setText("Nueva suscripción");
        cmbPeriodicidad.getItems().addAll(suscripcionController.getPeriodicidades());
        cmbPeriodicidad.setValue("MENSUAL");
        dateInicio.setValue(LocalDate.now());
        // 7 días es el valor por defecto de la tabla
        txtDiasAviso.setText("7");

        try {
            cmbServicio.getItems().addAll(suscripcionController.listarServicios());
        } catch (DatosException e) {
            Navegacion.mostrarError(e.getMessage());
        }
    }

    // Rellena el formulario con una suscripción que ya existe
    public void cargar(Suscripcion s) {
        suscripcion = s;
        lblTitulo.setText("Modificar suscripción");
        for (Servicio servicio : cmbServicio.getItems()) {
            if (servicio.getIdServicio() == s.getIdServicio()) {
                cmbServicio.setValue(servicio);
            }
        }
        txtImporte.setText(s.getImporte().toPlainString());
        cmbPeriodicidad.setValue(s.getPeriodicidad());
        dateInicio.setValue(s.getFechaInicio());
        txtDiasAviso.setText(String.valueOf(s.getDiasAviso()));
        txtNotas.setText(s.getNotas());
    }

    @FXML
    private void guardar() {
        // Se trabaja sobre un objeto nuevo para no cambiar la original si al final no se guarda.
        // Al modificar se copian los datos que no salen en el formulario.
        Suscripcion s = new Suscripcion();
        if (suscripcion != null) {
            s.setIdSuscripcion(suscripcion.getIdSuscripcion());
            s.setEstado(suscripcion.getEstado());
            s.setProximoPago(suscripcion.getProximoPago());
        }

        Servicio servicio = cmbServicio.getValue();
        if (servicio != null) {
            s.setIdServicio(servicio.getIdServicio());
            s.setNombreServicio(servicio.getNombre());
        }
        try {
            s.setImporte(new BigDecimal(txtImporte.getText().trim().replace(',', '.')));
        } catch (NumberFormatException e) {
            Navegacion.mostrarError("El importe tiene que ser un número, por ejemplo 9,99");
            return;
        }
        try {
            s.setDiasAviso(Integer.parseInt(txtDiasAviso.getText().trim()));
        } catch (NumberFormatException e) {
            Navegacion.mostrarError("Los días de aviso tienen que ser un número entero");
            return;
        }
        s.setPeriodicidad(cmbPeriodicidad.getValue());
        s.setFechaInicio(dateInicio.getValue());
        s.setNotas(txtNotas.getText());

        try {
            if (suscripcion == null) {
                // En el alta el primer cobro es el día de inicio
                s.setProximoPago(s.getFechaInicio());
                suscripcionController.crear(s);
                Navegacion.ir("Panel.fxml");
            } else {
                suscripcionController.modificar(s);
                DetallePantalla detalle = Navegacion.ir("Detalle.fxml");
                detalle.cargar(s);
            }
        } catch (ValidacionException | DatosException e) {
            Navegacion.mostrarError(e.getMessage());
        }
    }

    // Vuelve sin guardar: al detalle si se estaba modificando, al panel si era nueva
    @FXML
    private void cancelar() {
        if (suscripcion == null) {
            Navegacion.ir("Panel.fxml");
        } else {
            DetallePantalla detalle = Navegacion.ir("Detalle.fxml");
            detalle.cargar(suscripcion);
        }
    }

    // La pantalla de servicios solo está en el diseño
    @FXML
    private void nuevoServicio() {
        Navegacion.mostrarInfo("La gestión de servicios todavía no está disponible");
    }
}
