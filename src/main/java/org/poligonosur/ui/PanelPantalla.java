package org.poligonosur.ui;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.poligonosur.controller.ResumenController;
import org.poligonosur.controller.SuscripcionController;
import org.poligonosur.controller.UsuarioController;
import org.poligonosur.exception.DatosException;
import org.poligonosur.exception.ValidacionException;
import org.poligonosur.model.Categoria;
import org.poligonosur.model.ResumenGasto;
import org.poligonosur.model.Servicio;
import org.poligonosur.model.Suscripcion;
import org.poligonosur.util.Sesion;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Panel principal (Panel.fxml): resumen de gasto, filtros, tarjetas de suscripciones y avisos
public class PanelPantalla {

    @FXML
    private Label lblFecha;
    @FXML
    private Label lblSaludo;
    @FXML
    private Label lblGastoMensual;
    @FXML
    private Label lblGastoAnual;
    @FXML
    private ComboBox<Categoria> cmbCategoria;
    @FXML
    private ComboBox<String> cmbEstado;
    @FXML
    private ScrollPane scrollTarjetas;
    @FXML
    private HBox cajaTarjetas;
    @FXML
    private VBox cajaAvisos;

    private final SuscripcionController suscripcionController = new SuscripcionController();
    private final ResumenController resumenController = new ResumenController();
    private final UsuarioController usuarioController = new UsuarioController();

    private List<Suscripcion> suscripciones;
    // Para el filtro: id del servicio -> id de su categoría
    private final Map<Integer, Integer> categoriaDeServicio = new HashMap<>();

    // Lo llama JavaFX al cargar el FXML
    @FXML
    private void initialize() {
        lblFecha.setText(Formato.fecha(LocalDate.now()));
        lblSaludo.setText("Hola, " + Sesion.getUsuario().getNombre());

        try {
            ResumenGasto resumen = resumenController.obtenerResumen();
            lblGastoMensual.setText("Gasto mensual: " + Formato.importe(resumen.getGastoMensual()));
            lblGastoAnual.setText("Gasto anual: " + Formato.importe(resumen.getGastoAnual()));

            // Filtros: la primera opción es "Todas" / "Todos"
            cmbCategoria.getItems().add(new Categoria(0, "Todas", null));
            cmbCategoria.getItems().addAll(suscripcionController.listarCategorias());
            cmbCategoria.getSelectionModel().selectFirst();

            cmbEstado.getItems().add("Todos");
            for (String estado : suscripcionController.getEstados()) {
                cmbEstado.getItems().add(Formato.texto(estado));
            }
            cmbEstado.getSelectionModel().selectFirst();

            for (Servicio servicio : suscripcionController.listarServicios()) {
                categoriaDeServicio.put(servicio.getIdServicio(), servicio.getIdCategoria());
            }

            suscripciones = suscripcionController.listarMisSuscripciones();
            mostrarTarjetas();

            for (Suscripcion s : resumenController.listarProximosCobros()) {
                Label aviso = new Label(resumenController.textoAviso(s));
                aviso.getStyleClass().add("aviso");
                aviso.setMaxWidth(Double.MAX_VALUE);
                cajaAvisos.getChildren().add(aviso);
            }
        } catch (ValidacionException | DatosException e) {
            Navegacion.mostrarError(e.getMessage());
        }
    }

    // Se llama al cambiar cualquiera de los dos filtros
    @FXML
    private void filtrar() {
        // Al rellenar los filtros en initialize() todavía no hay suscripciones cargadas
        if (suscripciones != null) {
            mostrarTarjetas();
        }
    }

    // Pinta una tarjeta por cada suscripción que cumple los filtros
    private void mostrarTarjetas() {
        cajaTarjetas.getChildren().clear();
        Categoria categoria = cmbCategoria.getValue();
        String estado = cmbEstado.getValue();

        for (Suscripcion s : suscripciones) {
            boolean vale = true;
            if (categoria != null && categoria.getIdCategoria() != 0) {
                Integer idCategoria = categoriaDeServicio.get(s.getIdServicio());
                vale = idCategoria != null && idCategoria == categoria.getIdCategoria();
            }
            if (estado != null && !estado.equals("Todos")) {
                vale = vale && estado.equalsIgnoreCase(s.getEstado());
            }
            if (vale) {
                cajaTarjetas.getChildren().add(crearTarjeta(s));
            }
        }
    }

    // Tarjeta de una suscripción, como en el diseño. Al pulsarla se abre el detalle.
    private VBox crearTarjeta(Suscripcion s) {
        Label nombre = new Label(s.getNombreServicio());
        nombre.getStyleClass().add("subtitulo");

        Label importe = new Label(Formato.importe(s.getImporte()));
        importe.getStyleClass().add("tarjeta-importe");
        Label periodicidad = new Label(Formato.texto(s.getPeriodicidad()));
        periodicidad.getStyleClass().add("secundario");

        Label textoProximo = new Label("Próximo pago");
        textoProximo.getStyleClass().add("secundario");
        Label proximo = new Label(Formato.fecha(s.getProximoPago()));
        proximo.getStyleClass().add("dato");

        Label textoEstado = new Label("Estado");
        textoEstado.getStyleClass().add("secundario");
        Label estado = new Label(Formato.texto(s.getEstado()));
        estado.getStyleClass().add("etiqueta-estado");
        HBox filaEstado = new HBox(8, textoEstado, estado);
        filaEstado.setAlignment(Pos.CENTER_LEFT);

        VBox tarjeta = new VBox(6,
                nombre,
                new VBox(2, importe, periodicidad),
                new VBox(2, textoProximo, proximo),
                filaEstado);
        tarjeta.getStyleClass().addAll("caja", "tarjeta");
        tarjeta.setPrefSize(218, 218);
        tarjeta.setMinWidth(218);
        tarjeta.setOnMouseClicked(e -> {
            DetallePantalla detalle = Navegacion.ir("Detalle.fxml");
            detalle.cargar(s);
        });
        return tarjeta;
    }

    @FXML
    private void anterior() {
        scrollTarjetas.setHvalue(Math.max(0, scrollTarjetas.getHvalue() - 0.25));
    }

    @FXML
    private void siguiente() {
        scrollTarjetas.setHvalue(Math.min(1, scrollTarjetas.getHvalue() + 0.25));
    }

    @FXML
    private void nuevaSuscripcion() {
        Navegacion.ir("Formulario.fxml");
    }

    // La pantalla de servicios y categorías solo está en el diseño
    @FXML
    private void servicios() {
        Navegacion.mostrarInfo("La gestión de servicios y categorías todavía no está disponible");
    }

    @FXML
    private void salir() {
        usuarioController.cerrarSesion();
        Navegacion.ir("Login.fxml");
    }
}
