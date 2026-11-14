CREATE DATABASE IF NOT EXISTS gestor_suscripciones
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gestor_suscripciones;

CREATE TABLE usuario (
                         id_usuario     INT          NOT NULL AUTO_INCREMENT,
                         nombre         VARCHAR(100) NOT NULL,
                         email          VARCHAR(255) NOT NULL,
                         password       VARCHAR(255) NOT NULL,
                         fecha_registro DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (id_usuario),
                         CONSTRAINT uq_usuario_email UNIQUE (email)
) ENGINE=InnoDB;

CREATE TABLE categoria (
                           id_categoria INT         NOT NULL AUTO_INCREMENT,
                           nombre       VARCHAR(50) NOT NULL,
                           color        CHAR(7)     NOT NULL DEFAULT '#808080',
                           PRIMARY KEY (id_categoria),
                           CONSTRAINT uq_categoria_nombre UNIQUE (nombre),
                           CONSTRAINT ck_categoria_color
                               CHECK (color REGEXP '^#[0-9A-Fa-f]{6}$')
    ) ENGINE=InnoDB;

CREATE TABLE servicio (
                          id_servicio  INT          NOT NULL AUTO_INCREMENT,
                          id_categoria INT          NULL,
                          nombre       VARCHAR(100) NOT NULL,
                          url          VARCHAR(255) NULL,
                          logo         VARCHAR(255) NULL,
                          PRIMARY KEY (id_servicio),
                          CONSTRAINT uq_servicio_nombre UNIQUE (nombre),
                          CONSTRAINT fk_servicio_categoria FOREIGN KEY (id_categoria)
                              REFERENCES categoria (id_categoria)
                              ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE suscripcion (
                             id_suscripcion INT           NOT NULL AUTO_INCREMENT,
                             id_usuario     INT           NOT NULL,
                             id_servicio    INT           NOT NULL,
                             importe        DECIMAL(10,2) NOT NULL,
                             periodicidad   VARCHAR(15)   NOT NULL,
                             fecha_inicio   DATE          NOT NULL,
                             proximo_pago   DATE          NOT NULL,
                             estado         VARCHAR(10)   NOT NULL DEFAULT 'ACTIVA',
                             dias_aviso     INT           NOT NULL DEFAULT 7,
                             notas          VARCHAR(500)  NULL,
                             PRIMARY KEY (id_suscripcion),
                             CONSTRAINT ck_suscripcion_importe CHECK (importe >= 0),
                             CONSTRAINT ck_suscripcion_periodicidad CHECK (periodicidad IN
                                                                           ('MENSUAL', 'TRIMESTRAL', 'SEMESTRAL', 'ANUAL')),
                             CONSTRAINT ck_suscripcion_estado CHECK (estado IN
                                                                     ('ACTIVA', 'PAUSADA', 'CANCELADA')),
                             CONSTRAINT ck_suscripcion_dias_aviso CHECK (dias_aviso >= 0),
                             CONSTRAINT fk_suscripcion_usuario FOREIGN KEY (id_usuario)
                                 REFERENCES usuario (id_usuario)
                                 ON DELETE CASCADE ON UPDATE CASCADE,
                             CONSTRAINT fk_suscripcion_servicio FOREIGN KEY (id_servicio)
                                 REFERENCES servicio (id_servicio)
                                 ON DELETE RESTRICT ON UPDATE CASCADE,
                             INDEX idx_suscripcion_usuario (id_usuario),
                             INDEX idx_suscripcion_proximo_pago (proximo_pago)
) ENGINE=InnoDB;

CREATE TABLE pago (
                      id_pago        INT           NOT NULL AUTO_INCREMENT,
                      id_suscripcion INT           NOT NULL,
                      fecha_pago     DATE          NOT NULL,
                      importe        DECIMAL(10,2) NOT NULL,
                      estado         VARCHAR(10)   NOT NULL DEFAULT 'PAGADO',
                      PRIMARY KEY (id_pago),
                      CONSTRAINT ck_pago_importe CHECK (importe >= 0),
                      CONSTRAINT ck_pago_estado CHECK (estado IN ('PAGADO', 'PENDIENTE')),
                      CONSTRAINT fk_pago_suscripcion FOREIGN KEY (id_suscripcion)
                          REFERENCES suscripcion (id_suscripcion)
                          ON DELETE CASCADE ON UPDATE CASCADE,
                      INDEX idx_pago_suscripcion_fecha (id_suscripcion, fecha_pago)
) ENGINE=InnoDB;
