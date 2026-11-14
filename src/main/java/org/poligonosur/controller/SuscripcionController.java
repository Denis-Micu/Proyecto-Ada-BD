package org.poligonosur.controller;

import org.poligonosur.dao.CategoriaDAO;
import org.poligonosur.dao.ServicioDAO;
import org.poligonosur.dao.SuscripcionDAO;
import org.poligonosur.exception.DatosException;
import org.poligonosur.exception.ValidacionException;
import org.poligonosur.model.Categoria;
import org.poligonosur.model.Servicio;
import org.poligonosur.model.Suscripcion;
import org.poligonosur.util.Sesion;

import java.math.BigDecimal;
import java.util.List;

// Listado, alta y modificación de suscripciones del usuario conectado.
// Lo usan el panel principal y la pantalla de alta y modificación.
public class SuscripcionController {

    // Valores válidos, los mismos que los CHECK de la tabla suscripcion
    private static final List<String> PERIODICIDADES = List.of("MENSUAL", "TRIMESTRAL", "SEMESTRAL", "ANUAL");
    private static final List<String> ESTADOS = List.of("ACTIVA", "PAUSADA", "CANCELADA");

    private final SuscripcionDAO suscripcionDAO = new SuscripcionDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final ServicioDAO servicioDAO = new ServicioDAO();

    // Devuelve las suscripciones del usuario conectado
    public List<Suscripcion> listarMisSuscripciones() throws ValidacionException, DatosException {
        return suscripcionDAO.listarPorUsuario(idUsuarioActual());
    }

    // Para el desplegable de categorías
    public List<Categoria> listarCategorias() throws DatosException {
        return categoriaDAO.listar();
    }

    // Para el desplegable de servicios
    public List<Servicio> listarServicios() throws DatosException {
        return servicioDAO.listar();
    }

    // Para el desplegable de periodicidad
    public List<String> getPeriodicidades() {
        return PERIODICIDADES;
    }

    // Para el desplegable de estado
    public List<String> getEstados() {
        return ESTADOS;
    }

    // Da de alta una suscripción nueva para el usuario conectado.
    // Si no se indica estado, se crea como ACTIVA.
    public void crear(Suscripcion s) throws ValidacionException, DatosException {
        if (s.getEstado() == null || s.getEstado().isBlank()) {
            s.setEstado("ACTIVA");
        }
        validar(s);
        // El usuario siempre es el de la sesión, no el que venga en el objeto
        s.setIdUsuario(idUsuarioActual());
        suscripcionDAO.insertar(s);
    }

    // Guarda los cambios de una suscripción que ya existe
    public void modificar(Suscripcion s) throws ValidacionException, DatosException {
        if (s.getIdSuscripcion() <= 0) {
            throw new ValidacionException("No se ha indicado qué susc   ripción modificar");
        }
        validar(s);
        s.setIdUsuario(idUsuarioActual());
        suscripcionDAO.modificar(s);
    }

    // Comprueba los datos antes de mandarlos a la base de datos.
    // Así el usuario ve un mensaje claro en vez de un error de MySQL.
    private void validar(Suscripcion s) throws ValidacionException {
        if (s.getIdServicio() <= 0) {
            throw new ValidacionException("Elige un servicio");
        }
        if (s.getImporte() == null) {
            throw new ValidacionException("Escribe el importe");
        }
        if (s.getImporte().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidacionException("El importe no puede ser negativo");
        }
        if (s.getImporte().scale() > 2) {
            throw new ValidacionException("El importe solo puede tener dos decimales");
        }
        if (!PERIODICIDADES.contains(s.getPeriodicidad())) {
            throw new ValidacionException("Elige una periodicidad");
        }
        if (s.getFechaInicio() == null) {
            throw new ValidacionException("Escribe la fecha de inicio");
        }
        if (s.getProximoPago() == null) {
            throw new ValidacionException("Escribe la fecha del próximo pago");
        }
        if (s.getProximoPago().isBefore(s.getFechaInicio())) {
            throw new ValidacionException("El próximo pago no puede ser antes de la fecha de inicio");
        }
        if (!ESTADOS.contains(s.getEstado())) {
            throw new ValidacionException("Elige un estado");
        }
        if (s.getDiasAviso() < 0) {
            throw new ValidacionException("Los días de aviso no pueden ser negativos");
        }

        // Si las notas están vacías se guardan como NULL
        if (s.getNotas() != null && s.getNotas().isBlank()) {
            s.setNotas(null);
        }
        if (s.getNotas() != null && s.getNotas().length() > 500) {
            throw new ValidacionException("Las notas no pueden pasar de 500 caracteres");
        }
    }

    // Devuelve el id del usuario conectado, o lanza un error si nadie ha iniciado sesión
    private int idUsuarioActual() throws ValidacionException {
        if (!Sesion.hayUsuario()) {
            throw new ValidacionException("Tienes que iniciar sesión");
        }
        return Sesion.getUsuario().getIdUsuario();
    }
}
