-- Datos de ejemplo para probar la aplicación.
-- Se ejecuta después de schema.sql, con las tablas vacías.
-- Las fechas van en función de CURDATE() para que siempre haya avisos de cobro.

USE gestor_suscripciones;

-- Usuarios. La contraseña de los dos es 1234
INSERT INTO usuario (id_usuario, nombre, email, password) VALUES
(1, 'Ana García',  'ana@correo.com',  '1234'),
(2, 'Luis Martín', 'luis@correo.com', '1234');

-- Categorías
INSERT INTO categoria (id_categoria, nombre, color) VALUES
(1, 'Streaming', '#E50914'),
(2, 'Música',    '#1DB954'),
(3, 'Deporte',   '#FF8C00'),
(4, 'Software',  '#0078D4'),
(5, 'Compras',   '#FF9900');

-- Servicios
INSERT INTO servicio (id_servicio, id_categoria, nombre, url) VALUES
(1, 1, 'Netflix',              'https://www.netflix.com'),
(2, 1, 'Disney+',              'https://www.disneyplus.com'),
(3, 1, 'HBO Max',              'https://www.hbomax.com'),
(4, 2, 'Spotify',              'https://www.spotify.com'),
(5, 2, 'Apple Music',          'https://music.apple.com'),
(6, 3, 'Gimnasio',             NULL),
(7, 4, 'Microsoft 365',        'https://www.microsoft.com/microsoft-365'),
(8, 4, 'Adobe Creative Cloud', 'https://www.adobe.com'),
(9, 5, 'Amazon Prime',         'https://www.amazon.es/prime');

-- Suscripciones de Ana
-- 1 y 3 salen en los avisos (la 3 ya está vencida), 2 y 4 no porque falta mucho,
-- 5 está pausada y 6 cancelada, así que no cuentan
INSERT INTO suscripcion (id_suscripcion, id_usuario, id_servicio, importe, periodicidad,
                         fecha_inicio, proximo_pago, estado, dias_aviso, notas) VALUES
(1, 1, 1, 12.99, 'MENSUAL',    CURDATE() - INTERVAL 3 MONTH + INTERVAL 3 DAY,  CURDATE() + INTERVAL 3 DAY,  'ACTIVA',    7, 'Plan estándar'),
(2, 1, 4, 10.99, 'MENSUAL',    CURDATE() - INTERVAL 2 MONTH + INTERVAL 10 DAY, CURDATE() + INTERVAL 10 DAY, 'ACTIVA',    7, NULL),
(3, 1, 6, 90.00, 'TRIMESTRAL', CURDATE() - INTERVAL 3 MONTH - INTERVAL 2 DAY,  CURDATE() - INTERVAL 2 DAY,  'ACTIVA',    5, 'Pago en recepción'),
(4, 1, 9, 49.90, 'ANUAL',      CURDATE() - INTERVAL 1 YEAR + INTERVAL 60 DAY,  CURDATE() + INTERVAL 60 DAY, 'ACTIVA',    7, NULL),
(5, 1, 2,  8.99, 'MENSUAL',    CURDATE() - INTERVAL 1 MONTH + INTERVAL 5 DAY,  CURDATE() + INTERVAL 5 DAY,  'PAUSADA',   7, 'Pausada hasta verano'),
(6, 1, 3,  9.99, 'MENSUAL',    CURDATE() - INTERVAL 1 MONTH + INTERVAL 1 DAY,  CURDATE() + INTERVAL 1 DAY,  'CANCELADA', 7, NULL);

-- Suscripciones de Luis
-- La 7 avisa con 30 días de margen
INSERT INTO suscripcion (id_suscripcion, id_usuario, id_servicio, importe, periodicidad,
                         fecha_inicio, proximo_pago, estado, dias_aviso, notas) VALUES
(7, 2, 7, 99.00, 'ANUAL',     CURDATE() - INTERVAL 1 YEAR + INTERVAL 20 DAY,  CURDATE() + INTERVAL 20 DAY, 'ACTIVA', 30, 'Plan familiar'),
(8, 2, 4, 10.99, 'MENSUAL',   CURDATE() - INTERVAL 1 MONTH + INTERVAL 1 DAY,  CURDATE() + INTERVAL 1 DAY,  'ACTIVA', 7,  NULL),
(9, 2, 8, 60.49, 'MENSUAL',   CURDATE() - INTERVAL 2 MONTH + INTERVAL 15 DAY, CURDATE() + INTERVAL 15 DAY, 'ACTIVA', 7,  NULL);

-- Historial de pagos (los cobros ya hechos antes del próximo pago)
INSERT INTO pago (id_suscripcion, fecha_pago, importe, estado) VALUES
(1, CURDATE() - INTERVAL 3 MONTH + INTERVAL 3 DAY,  12.99, 'PAGADO'),
(1, CURDATE() - INTERVAL 2 MONTH + INTERVAL 3 DAY,  12.99, 'PAGADO'),
(1, CURDATE() - INTERVAL 1 MONTH + INTERVAL 3 DAY,  12.99, 'PAGADO'),
(2, CURDATE() - INTERVAL 2 MONTH + INTERVAL 10 DAY, 10.99, 'PAGADO'),
(2, CURDATE() - INTERVAL 1 MONTH + INTERVAL 10 DAY, 10.99, 'PAGADO'),
(3, CURDATE() - INTERVAL 3 MONTH - INTERVAL 2 DAY,  90.00, 'PAGADO'),
(4, CURDATE() - INTERVAL 1 YEAR + INTERVAL 60 DAY,  49.90, 'PAGADO'),
(5, CURDATE() - INTERVAL 1 MONTH + INTERVAL 5 DAY,   8.99, 'PAGADO'),
(6, CURDATE() - INTERVAL 1 MONTH + INTERVAL 1 DAY,   9.99, 'PAGADO'),
(7, CURDATE() - INTERVAL 1 YEAR + INTERVAL 20 DAY,  99.00, 'PAGADO'),
(8, CURDATE() - INTERVAL 1 MONTH + INTERVAL 1 DAY,  10.99, 'PAGADO'),
(9, CURDATE() - INTERVAL 2 MONTH + INTERVAL 15 DAY, 60.49, 'PAGADO'),
(9, CURDATE() - INTERVAL 1 MONTH + INTERVAL 15 DAY, 60.49, 'PAGADO');
