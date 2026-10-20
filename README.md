# PetCare Plus

PetCare Plus es un sistema de gestion para una clinica veterinaria. Permite registrar duenos, mascotas y tratamientos, calcular costos de atencion, consultar informacion del dominio y conservar los datos entre ejecuciones mediante archivos.

El proyecto esta desarrollado en Java con Programacion Orientada a Objetos. La version actual incorpora arquitectura **MVC**, principios **SOLID**, principios **GRASP** y los patrones de diseno **Facade**, **Composite**, **Decorator**, **Factory** y **Builder**, junto con una interfaz grafica Swing y persistencia en archivos CSV.

## Objetivo general

El objetivo del programa es administrar la informacion principal de una clinica veterinaria:

- duenos registrados;
- mascotas asociadas a cada dueno;
- tratamientos (simples, planes compuestos y decorados) asociados a cada mascota;
- costos de atencion y tratamiento;
- reglas de aptitud y seguimiento.

La GUI (Vista) solicita datos, muestra informacion y envia las operaciones exclusivamente a los Controladores, que delegan en los servicios y entidades del Modelo.

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
- registrar tratamientos asociados a una mascota (Preventivo, Curativo, Cirujia o Plan Compuesto);
- aplicar adicionales dinamicos (Decoradores: Guardia de Urgencia, Medicacion Especial, Internacion);
- visualizar tratamientos registrados;
- consultar costo, estado y datos especificos de cada tratamiento;
- calcular el costo total de una consulta veterinaria;
- consultar un resumen de tratamientos por mascota;
- detectar tratamientos que requieren seguimiento.

## Entidades principales del dominio

Las entidades centrales (en `Veterinaria.modelo`) son:

- `Dueño`: representa a la persona responsable de una o mas mascotas.
- `Mascota`: clase abstracta base para todos los animales atendidos.
- `Tratamiento`: clase abstracta base (Component) para los tratamientos veterinarios.

La relacion principal es:

```text
Dueño
 └── Mascotas
      └── Tratamientos (Hojas simples, PlanTratamiento Composite o TratamientoDecorator)
```

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

Cada tipo calcula su costo base de atencion y genera su descripcion de detalle de manera polimorfica.

## Tipos de tratamientos, Planes Compuestos y Decoradores

Todos los tratamientos tienen:

- identificador unico;
- nombre descriptivo;
- fecha de inicio;
- estado (`encurso`, `completado`, `suspendido`).

### Hojas simples (Leaf):
- `TratamientoPreventivo`: agrega frecuencia recomendada en meses.
- `TratamientoCurativo`: agrega duracion estimada en dias y diagnostico.
- `Cirujia`: agrega tipo de cirugia y nivel de complejidad.

### Compuesto (Patron Composite):
- `PlanTratamiento`: agrupa multiples `Tratamiento` hijos y calcula de forma recursiva su `costoFijo()` total y si `requiereAtencionUrgente()`.

### Decoradores dinamicos (Patron Decorator en `Veterinaria.modelo.decoradores`):
- `TratamientoDecorator`: clase abstracta base que envuelve cualquier `Tratamiento`.
- `UrgenciaGuardiaDecorator`: suma un recargo de `+$800.0` y marca el tratamiento como de seguimiento urgente.
- `MedicacionEspecialDecorator`: suma un recargo de `+$500.0` por insumos especiales.
- `InternacionDecorator`: suma `$400.0` por cada dia de internacion y marca seguimiento urgente si supera los 3 dias.

## Estructura de paquetes (MVC + Capas)

El codigo esta organizado siguiendo MVC y separacion en capas:

```text
src/Veterinaria
    Main.java (Punto de entrada).

src/Veterinaria/modelo
    Entidades del dominio (Mascota, Dueño, Tratamiento, PlanTratamiento, subclases, enums, factories y builders).

src/Veterinaria/modelo/decoradores
    Decoradores de tratamientos (TratamientoDecorator, UrgenciaGuardiaDecorator, MedicacionEspecialDecorator, InternacionDecorator).

src/Veterinaria/facade
    ClinicaVeterinariaFacade (Fachada unificada de acceso al subsistema).

src/Veterinaria/controladores
    Controladores MVC/GRASP (DuenoController, MascotaController, TratamientoController, ConsultaController).

src/Veterinaria/gui
    Vistas en Swing (MainFrame, DuenosPanel, MascotasPanel, TratamientosPanel, ConsultasPanel, PetCarePlusApp).

src/Veterinaria/servicios
    Servicios de aplicacion e interfaces segregadas (ISP).

src/Veterinaria/persistencia
    Repositorio y mappers CSV.
```

## Como ejecutar el programa

Desde consola o mediante el script incluido:

```powershell
.\run-app.bat
```

O compilando manualmente:

```powershell
javac -encoding UTF-8 -d bin (Get-ChildItem -Recurse -Filter *.java src).FullName
java -cp bin Veterinaria.Main
```

## Patrones y decisiones arquitectonicas

- **MVC (Modelo - Vista - Controlador)**: Separacion estricta entre `Veterinaria.modelo`, `Veterinaria.gui` y `Veterinaria.controladores`. Las vistas no realizan `instanceof` ni invocan reglas de negocio directamente sobre las entidades.
- **Facade (`ClinicaVeterinariaFacade`)**: Provee un punto de acceso unico que encapsula la instanciacion y coordinacion de repositorios, servicios, clinica y controladores.
- **Composite (`PlanTratamiento`)**: Permite componer varios tratamientos dentro de un plan integral tratandolo polimorficamente como un `Tratamiento` mas.
- **Decorator (`TratamientoDecorator` y subclases)**: Permite agregar responsabilidades y costos dinamicos (urgencia, medicacion, internacion) a cualquier tratamiento sin modificar su clase ni generar explosion de subclases.
- **Principios SOLID y GRASP**: Aplicados en servicios segregados, inversion de dependencias en `ClinicaRepository`, polimorfismo Liskov y alta cohesion.
