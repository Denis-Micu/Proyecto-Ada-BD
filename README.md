# Gestor de suscripciones

Proyecto de la asignatura **Acceso a Datos (UD2)**. Es una aplicación de escritorio en Java que guarda y lee los datos de una base de datos MySQL usando JDBC.

El usuario apunta sus suscripciones (Netflix, Spotify, el gimnasio...) y la aplicación calcula cuándo es el próximo cobro, avisa de los cobros que están cerca y guarda el historial de pagos. Todos los datos los mete el usuario a mano; no se conecta con bancos ni con otros servicios.

Es un prototipo para clase, así que se ha hecho lo más sencillo posible.

## Tecnologías

- Java 21 y Maven
- MySQL (tablas InnoDB)
- JDBC con `DriverManager`, sin ORM ni frameworks
- JavaFX para la interfaz
- `dotenv-java` para leer los datos de conexión del archivo `.env`

## Cómo ponerlo en marcha

### 1. Crear la base de datos

Ejecutar en MySQL estos tres scripts, en este orden:

```
db/schema.sql           -> crea la base de datos gestor_suscripciones y las tablas
db/procedimientos.sql   -> crea los procedimientos almacenados
db/datos_prueba.sql     -> mete datos de ejemplo (con las tablas vacías)
```

Las fechas de los datos de prueba se calculan con `CURDATE()`, así que siempre salen cobros cercanos sea el día que sea.

### 2. Revisar el `.env`

En la raíz del proyecto está el archivo `.env` con los datos de conexión:

```
DB_HOST=localhost:3306/gestor_suscripciones
DB_USER=root
DB_PASSWORD=1234
```

`DB_HOST` lleva solo `host:puerto/base`; la clase `ConexionBD` le añade `jdbc:mysql://` delante. Si tu MySQL tiene otro usuario o contraseña, cámbialos aquí.

> **Aviso:** el `.env` está subido al repositorio solo para que el profesor pueda probar el proyecto sin configurar nada. En un proyecto real no debería estar en git, porque tiene las credenciales de la base de datos.

### 3. Ejecutar

Con Maven:

```
mvn compile
mvn javafx:run
```

O desde el IDE, ejecutando la clase `org.poligonosur.Main`. `Main` no hereda de `Application`, así que se puede lanzar sin configurar nada de JavaFX.

### Usuarios de prueba

| Email | Contraseña |
|---|---|
| ana@correo.com | 1234 |
| luis@correo.com | 1234 |

Las contraseñas se guardan en texto plano. Es una decisión del proyecto para que sea más sencillo; en una aplicación real se guardarían cifradas.

## Estructura del proyecto

```
Proyecto-Ada-BD/
├── pom.xml
├── .env
├── db/
│   ├── schema.sql
│   ├── procedimientos.sql
│   └── datos_prueba.sql
└── src/main/
    ├── java/org/poligonosur/
    │   ├── Main.java
    │   ├── model/       Usuario, Categoria, Servicio, Suscripcion, Pago, ResumenGasto
    │   ├── dao/         el único sitio donde hay SQL
    │   ├── controller/  reglas y comprobaciones antes de ir a la base de datos
    │   ├── ui/          pantallas JavaFX
    │   ├── util/        ConexionBD y Sesion (usuario conectado)
    │   └── exception/   DatosException y ValidacionException
    └── resources/org/poligonosur/ui/
                         archivos .fxml de las pantallas y estilos.css
```

Se sigue el patrón **Modelo Vista Controlador**. El camino de los datos siempre es:

```
ui  ->  controller  ->  dao  ->  MySQL
```

La interfaz nunca llama directamente a un DAO, y solo los DAO tienen SQL.

## Base de datos

Cinco tablas:

| Tabla | Para qué sirve |
|---|---|
| `usuario` | Personas que usan la aplicación (nombre, email, contraseña) |
| `categoria` | Tipos de servicio (vídeo, música, deporte...) con un color |
| `servicio` | Netflix, Spotify, etc. Puede no tener categoría |
| `suscripcion` | Lo que paga cada usuario: importe, periodicidad, próximo pago, estado y días de aviso |
| `pago` | Historial de pagos de cada suscripción |

Relaciones (todas 1:N):

- un usuario tiene muchas suscripciones
- un servicio aparece en muchas suscripciones
- una categoría tiene muchos servicios
- una suscripción tiene muchos pagos

Valores permitidos:

- `suscripcion.periodicidad`: MENSUAL, TRIMESTRAL, SEMESTRAL, ANUAL
- `suscripcion.estado`: ACTIVA, PAUSADA, CANCELADA
- `pago.estado`: PAGADO, PENDIENTE

### Procedimientos almacenados

- **`sp_resumen_gasto(id_usuario, OUT gasto_mensual, OUT gasto_anual, OUT num_activas)`**: calcula lo que gasta el usuario al mes y al año con sus suscripciones activas. Pasa cada importe a lo que cuesta al año según su periodicidad y el mensual es el anual entre 12.
- **`sp_proximos_cobros(id_usuario)`**: devuelve las suscripciones activas cuyo cobro está dentro de sus días de aviso (también las que ya han vencido), con los días que faltan.

## Requisitos de la asignatura

| Requisito | Dónde se cumple |
|---|---|
| Consultas normales (`Statement`) | `CategoriaDAO.listar()` y `ServicioDAO.listar()`, para rellenar los desplegables |
| Consultas preparadas (`PreparedStatement`) | Inicio de sesión, alta, modificación y listado de suscripciones, historial de pagos |
| Procedimientos almacenados (`CallableStatement`) | `ResumenDAO`: `sp_resumen_gasto` (parámetros OUT) y `sp_proximos_cobros` (devuelve filas) |
| Transacciones | `PagoDAO.registrarPago()`: inserta el pago y avanza el `proximo_pago` de la suscripción, con `commit` o `rollback` si algo falla |
| Modelo Vista Controlador | Paquetes `model`, `ui` y `controller` |
| Interfaz gráfica | Cuatro pantallas en JavaFX |

Además, todas las conexiones, sentencias y resultados se cierran con `try-with-resources`, y los valores nunca se pegan al SQL: siempre se usan `?`.

## Pantallas

Cada pantalla enseña una forma distinta de acceder a la base de datos:

1. **Inicio de sesión**: consulta preparada que comprueba el email y la contraseña.
2. **Panel principal**: listado de suscripciones con `JOIN`, resumen de gasto (procedimiento con OUT) y avisos de los próximos cobros (procedimiento que devuelve filas).
3. **Alta y modificación de suscripción**: `INSERT` y `UPDATE` preparados, con los desplegables de categorías y servicios cargados con consultas normales.
4. **Detalle de suscripción**: historial de pagos y botón para registrar un pago (transacción).

El resto de pantallas de la aplicación completa solo están en los wireframes.
