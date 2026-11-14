package org.poligonosur.exception;

// Error en los datos que ha escrito el usuario (importe negativo, campo vacío...).
// La lanzan los controladores antes de llamar al DAO.
public class ValidacionException extends Exception {

    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
