-- Procedimientos.
-- Se ejecuta después de schema.sql.

USE gestor_suscripciones;

DROP PROCEDURE IF EXISTS sp_resumen_gasto;
DROP PROCEDURE IF EXISTS sp_proximos_cobros;

DELIMITER //

-- Calcula lo que gasta un usuario al mes y al año con sus suscripciones activas.
-- Devuelve los resultados en parámetros OUT.
CREATE PROCEDURE sp_resumen_gasto(
    IN  p_id_usuario    INT,
    OUT p_gasto_mensual DECIMAL(10,2),
    OUT p_gasto_anual   DECIMAL(10,2),
    OUT p_num_activas   INT
)
BEGIN
    -- Pasamos cada importe a lo que cuesta al año según su periodicidad
    SELECT COALESCE(SUM(
               CASE periodicidad
                   WHEN 'MENSUAL'    THEN importe * 12
                   WHEN 'TRIMESTRAL' THEN importe * 4
                   WHEN 'SEMESTRAL'  THEN importe * 2
                   WHEN 'ANUAL'      THEN importe
               END), 0),
           COUNT(*)
      INTO p_gasto_anual, p_num_activas
      FROM suscripcion
     WHERE id_usuario = p_id_usuario
       AND estado = 'ACTIVA';

    -- El gasto mensual es el anual repartido entre 12 meses
    SET p_gasto_mensual = ROUND(p_gasto_anual / 12, 2);
END //

-- Devuelve las suscripciones activas de un usuario cuyo cobro está cerca.
-- Cada suscripción usa sus propios dias_aviso. También salen las ya vencidas.
CREATE PROCEDURE sp_proximos_cobros(
    IN p_id_usuario INT
)
BEGIN
    SELECT su.id_suscripcion,
           se.nombre                            AS servicio,
           su.importe,
           su.proximo_pago,
           DATEDIFF(su.proximo_pago, CURDATE()) AS dias_restantes
      FROM suscripcion su
      JOIN servicio se ON se.id_servicio = su.id_servicio
     WHERE su.id_usuario = p_id_usuario
       AND su.estado = 'ACTIVA'
       AND su.proximo_pago <= DATE_ADD(CURDATE(), INTERVAL su.dias_aviso DAY)
     ORDER BY su.proximo_pago;
END //

DELIMITER ;
