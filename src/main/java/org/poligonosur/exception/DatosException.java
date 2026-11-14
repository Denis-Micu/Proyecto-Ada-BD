package org.poligonosur.exception;

// Error al leer o guardar en la base de datos.
// Los DAO envuelven la SQLException en esta excepción con un mensaje que se pueda enseñar al usuario.
public class DatosException extends Exception {

    // Solo con mensaje
    public DatosException(String mensaje) {
        super(mensaje);
    }

    // Con mensaje y el error original (normalmente una SQLException), para no perder el detalle
    public DatosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
