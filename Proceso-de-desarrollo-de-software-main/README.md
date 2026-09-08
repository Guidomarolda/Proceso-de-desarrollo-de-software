# PetCare Plus

PetCare Plus es un sistema de gestion para una clinica veterinaria. Permite registrar duenos, mascotas y tratamientos, calcular costos de atencion, consultar informacion del dominio y conservar los datos entre ejecuciones mediante archivos.

El proyecto esta desarrollado en Java con Programacion Orientada a Objetos. La version actual incorpora una interfaz grafica con Swing y una capa de persistencia en archivos CSV, manteniendo separadas las responsabilidades de dominio, servicios, persistencia e interfaz.

## Objetivo general

El objetivo del programa es administrar la informacion principal de una clinica veterinaria:

- duenos registrados;
- mascotas asociadas a cada dueno;
- tratamientos asociados a cada mascota;
- costos de atencion y tratamiento;
- reglas de aptitud y seguimiento.

La logica de negocio no esta dentro de la interfaz grafica. La GUI solo solicita datos, muestra informacion y ejecuta acciones a traves de servicios.

## Funcionalidades principales

El sistema permite:

- registrar duenos;
- visualizar duenos registrados;
- consultar datos de un dueno;
- registrar mascotas asociadas a un dueno;
- seleccionar el tipo de mascota y cargar los datos especificos;
- visualizar mascotas registradas;
- consultar datos de una mascota;
- calcular y mostrar el costo base de atencion de una mascota;
- comparar mascotas por costo base;
- verificar si una mascota es apta para procedimientos especiales;
- registrar tratamientos asociados a una mascota;
- seleccionar el tipo de tratamiento y cargar sus datos especificos;
- visualizar tratamientos registrados;
- consultar costo, estado y datos especificos de cada tratamiento;
- calcular el costo total de una consulta veterinaria;
- consultar un resumen de tratamientos por mascota;
- detectar tratamientos que requieren seguimiento.

La version actual no incluye modificacion ni eliminacion de entidades desde la interfaz.

## Entidades principales del dominio

Las entidades centrales son:

- `Dueño`: representa a la persona responsable de una o mas mascotas.
- `Mascota`: clase abstracta base para todos los animales atendidos.
- `Tratamiento`: clase abstracta base para los tratamientos veterinarios.

La relacion principal es:

```text
Dueño
 └── Mascotas
      └── Tratamientos
```

Cada mascota pertenece a un unico dueno. Cada tratamiento pertenece a una unica mascota.

## Tipos de mascotas

Todas las mascotas tienen:

- identificador unico;
- nombre;
- edad;
- peso;
- lista de tratamientos.

Los tipos concretos son:

- `Perro`: agrega raza.
- `Gato`: agrega si esta vacunado o no.
- `Ave`: agrega tipo de ave, por ejemplo `canario`, `loro` o `aguila`.
- `Exoticos`: agrega nivel de cuidado especial: `BAJO`, `MEDIO` o `ALTO`.

Cada tipo calcula su costo base de atencion de manera polimorfica.

## Tipos de tratamientos

Todos los tratamientos tienen:

- identificador unico;
- nombre descriptivo;
- fecha de inicio;
- estado.

Los estados disponibles son:

- `encurso`;
- `completado`;
- `suspendido`.

Los tipos concretos son:

- `TratamientoPreventivo`: agrega frecuencia recomendada en meses.
- `TratamientoCurativo`: agrega duracion estimada en dias y diagnostico.
- `Cirujia`: agrega tipo de cirugia y nivel de complejidad.

## Reglas de negocio importantes

Las reglas principales implementadas son:

- Una mascota pertenece a un unico dueno.
- Un tratamiento pertenece a una unica mascota.
- El costo base de atencion depende del tipo de mascota:
  - perro: `500 + edad * 50`;
  - gato: `400 + edad * 30`, con recargo de `200` si no esta vacunado;
  - ave: `300 + edad * 20`;
  - exotico: `800 + edad * 100`.
- El costo del tratamiento depende de su tipo:
  - preventivo: `600`;
  - curativo: `1000 + duracion * 50`;
  - cirugia: `3000`, con adicional de `1500` si es media y `3000` si es alta.
- Una cirugia no puede quedar en estado `encurso`.
- Una mascota es apta para procedimientos especiales si tiene menos de 10 anos y pesa menos de 30 kg.
- Un tratamiento preventivo requiere seguimiento si su frecuencia recomendada es menor a 3 meses.
- Un tratamiento curativo requiere seguimiento si su duracion supera los 30 dias.
- Una cirugia informaria seguimiento si estuviera `encurso`, aunque esa situacion se evita mediante validacion.

## Estructura de paquetes

El codigo esta organizado en capas:

```text
src/Veterinaria
    Clases de dominio, enums, factories y Main.

src/Veterinaria/servicios
    Servicios de aplicacion.

src/Veterinaria/persistencia
    Lectura y escritura de archivos.

src/Veterinaria/gui
    Interfaz grafica Swing.
```

Clases destacadas:

- `Main`: punto de entrada principal. Inicia la aplicacion grafica.
- `ClinicaVeterinariaService`: coordina registros, consultas, calculos y guardado.
- `ArchivoClinicaRepository`: carga y guarda los datos en archivos CSV.
- `MainFrame`: ventana principal de Swing.
- `DuenosPanel`, `MascotasPanel`, `TratamientosPanel`, `ConsultasPanel`: paneles de la interfaz.

## Interfaz grafica

La interfaz esta implementada con Swing porque el proyecto original era Java estandar y no usaba JavaFX.

La ventana principal usa pestanas:

- `Duenos`: permite registrar y visualizar duenos.
- `Mascotas`: permite registrar mascotas para un dueno, consultar datos, ver costo base, comparar mascotas y verificar aptitud.
- `Tratamientos`: permite registrar tratamientos para una mascota y consultar costo, estado y seguimiento.
- `Consultas`: permite calcular el costo total de una consulta, ver resumen de tratamientos por mascota y listar tratamientos que requieren seguimiento.

Los botones y formularios no contienen reglas de negocio. Cuando el usuario ejecuta una accion, la interfaz llama a `ClinicaVeterinariaService`, y el servicio usa las clases del dominio y la persistencia.

## Persistencia mediante archivos

La persistencia se realiza mediante archivos CSV ubicados en la carpeta:

```text
data/
```

Los archivos son:

```text
data/duenos.csv
data/mascotas.csv
data/tratamientos.csv
```

La aplicacion carga los archivos al iniciar y guarda automaticamente despues de registrar un dueno, una mascota o un tratamiento.

La interfaz grafica no lee ni escribe archivos directamente. Esa responsabilidad esta concentrada en `ArchivoClinicaRepository`.

## Datos guardados y formato

El formato elegido es CSV con `;` como separador. Se eligio por ser simple, legible, facil de mantener y no requerir dependencias externas.

`duenos.csv` guarda:

```csv
id;nombre;apellido;telefono;correo
```

`mascotas.csv` guarda:

```csv
id;duenoId;tipo;nombre;edad;peso;extra
```

El campo `extra` depende del tipo de mascota:

- perro: raza;
- gato: `true` o `false` segun este vacunado;
- ave: tipo de ave;
- exotico: nivel de cuidado.

`tratamientos.csv` guarda:

```csv
id;mascotaId;tipo;nombre;fechaInicio;estado;extra1;extra2
```

Los campos `extra1` y `extra2` dependen del tipo de tratamiento:

- preventivo: frecuencia recomendada en `extra1`;
- curativo: duracion en `extra1` y diagnostico en `extra2`;
- cirugia: tipo de cirugia en `extra1` y complejidad en `extra2`.

## Reconstruccion de relaciones al iniciar

Al iniciar la aplicacion, `ArchivoClinicaRepository` carga los archivos en este orden:

1. Duenos.
2. Mascotas.
3. Tratamientos.

Primero se crean los duenos y se guardan en memoria por `id`. Luego se cargan las mascotas y cada una se asocia al dueno correspondiente mediante `duenoId`. Finalmente se cargan los tratamientos y cada uno se asocia a la mascota correspondiente mediante `mascotaId`.

De esta forma se evita crear copias inconsistentes de los mismos objetos.

## Validaciones principales

El sistema valida:

- campos obligatorios en duenos;
- nombre obligatorio en mascotas y tratamientos;
- peso mayor a cero;
- edad no negativa;
- frecuencia recomendada mayor a cero;
- duracion mayor a cero;
- estado obligatorio;
- tipo de mascota y tratamiento reconocido;
- que no se registre una cirugia en estado `encurso`;
- que exista el dueno al asociar una mascota;
- que exista la mascota al asociar un tratamiento.

Si una operacion no es valida desde la GUI, se muestra un mensaje de error y no se guarda un estado inconsistente.

## Como ejecutar el programa

Desde IntelliJ IDEA se puede ejecutar la clase:

```text
Veterinaria.Main
```

Desde consola, compilar:

```powershell
javac -encoding UTF-8 -d out\production\FinalPOONicolascasais1185300 (Get-ChildItem -Recurse -Filter *.java src).FullName
```

Ejecutar:

```powershell
java -cp out\production\FinalPOONicolascasais1185300 Veterinaria.Main
```

Al ejecutarse, se abre la interfaz grafica de PetCare Plus.

## Dependencias

No se agregaron dependencias externas.

El proyecto utiliza solamente:

- Java;
- Swing, incluido en la biblioteca estandar de Java;
- clases estandar de `java.nio.file`, `java.util` y `java.io`.

## Patrones y decisiones arquitectonicas

Se mantienen y usan decisiones orientadas a objetos:

- Herencia y polimorfismo en `Mascota` y sus subclases.
- Herencia y polimorfismo en `Tratamiento` y sus subclases.
- `Comparable<Mascota>` para comparar mascotas por costo base.
- Factories de mascotas ya existentes en el proyecto original.
- Separacion por capas:
  - dominio;
  - servicios;
  - persistencia;
  - interfaz grafica.
- Repository para aislar el acceso a archivos en `ArchivoClinicaRepository`.
- Service Layer en `ClinicaVeterinariaService` para evitar que la GUI use directamente archivos o distribuya reglas de aplicacion.

Esta organizacion deja el proyecto preparado para futuras mejoras con principios SOLID, por ejemplo separar repositorios por entidad, agregar interfaces de repositorio o incorporar nuevos tipos de mascotas/tratamientos sin modificar la interfaz de forma invasiva.

## Cambios respecto del proyecto original

El proyecto original tenia la mayor parte del dominio y ejemplos de uso en `Main`, pero no tenia interfaz grafica ni persistencia.

Los cambios principales fueron:

- se agrego una interfaz Swing;
- se agrego persistencia en CSV;
- se agregaron identificadores unicos para duenos, mascotas y tratamientos;
- `Dueño` paso de tener una sola mascota a manejar una lista de mascotas;
- `Mascota` paso de tener un solo tratamiento a manejar una lista de tratamientos;
- se corrigio la validacion de `Cirujia` para comparar enums correctamente;
- se corrigio el calculo de costo de cirugia segun complejidad;
- se agrego una capa de servicios para concentrar operaciones de aplicacion;
- se mantuvieron metodos compatibles con el modelo anterior, como `getMascota()` y `getTratamiento()`, para no romper de forma abrupta el codigo existente.
