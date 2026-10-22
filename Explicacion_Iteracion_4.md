# Guía de Estudio y Defensa — Iteración 4: MVC + Patrones Estructurales (Facade, Composite, Decorator)

Este documento resume todos los cambios arquitectónicos y de diseño aplicados en el proyecto **PetCare Plus** para la **Iteración 4**, detallando **qué problema había**, **cómo se solucionó**, **en qué clases está implementado** y **cómo explicarlo en el coloquio/entrega**.

---

## 1. Arquitectura MVC (Modelo - Vista - Controlador)

### ¿Qué problema teníamos antes?
1. **Desorden estructural:** Las clases del **Modelo** (`Mascota`, `Dueño`, `Tratamiento`, `Perro`, `Cirujia`, etc.) estaban sueltas en la raíz `src/Veterinaria/`, mezcladas con el punto de entrada `Main`.
2. **Acoplamiento Vista-Modelo (Violación de MVC y Liskov en la GUI):**
   En `MascotasPanel.java` y `TratamientosPanel.java`, el método `mostrarDetalle()` interrogaba directamente al modelo usando `instanceof` y casteando a las subclases concretas:
   ```java
   // ❌ ANTES (en MascotasPanel.java - La Vista conocía las subclases y hacía lógica del modelo)
   if (mascota instanceof Perro) builder.append("Raza: ").append(((Perro) mascota).getRaza());
   if (mascota instanceof Gato) builder.append("Vacunado: ").append(((Gato) mascota).getEstaVacunado());
   ```
   Además, `verificarAptitud()` llamaba directamente a `mascota.esAptaParaProcedimientoEspecial()` desde la Vista sin pasar por el Controlador.

### ¿Cómo se aplicó MVC completo?
1. **Paquete dedicado `Veterinaria.modelo`:** Agrupa todas las entidades del dominio, enums, excepciones, builders y factories.
2. **Polimorfismo en el Modelo:** Se agregaron `getDescripcionExtra()` y `obtenerDetalle()` tanto en `Mascota` como en `Tratamiento`.
3. **Mediación estricta del Controlador:** Ahora `MascotasPanel` ni siquiera importa las clases `Perro`, `Gato`, `Ave` ni `Exoticos`. Le pide todo a `MascotaController`:
   ```java
   // ✅ AHORA (en MascotasPanel.java)
   private void mostrarDetalle(Mascota mascota) {
       detalle.setText(mascotaController.obtenerDetalle(mascota));
   }

   private void verificarAptitud() {
       Mascota mascota = mascotasList.getSelectedValue();
       boolean apta = mascotaController.verificarAptitud(mascota);
       ...
   }
   ```

> **Frase clave para el profesor:** *"Consolidamos MVC separando físicamente el paquete `modelo` y eliminando los `instanceof` y llamadas directas al dominio que habían quedado en los paneles de Swing. Ahora la Vista solo captura eventos y delega 100% en los Controladores."*

---

## 2. Patrón FACADE (Fachada)

* **Clase implementada:** `src/Veterinaria/facade/ClinicaVeterinariaFacade.java`

### ¿Qué problema resuelve?
Antes, la clase de inicio `PetCarePlusApp.java` estaba acoplada a **8 clases distintas del subsistema**: tenía que instanciar `ClinicaVeterinaria`, `ArchivoClinicaRepository`, `ClinicaVeterinariaService`, `ConsultaVeterinariaService`, `DuenoController`, `MascotaController`, `TratamientoController` y `ConsultaController`.

### ¿Cómo funciona ahora?
`ClinicaVeterinariaFacade` actúa como una **única puerta de entrada simplificada** que encapsula el armado de repositorios, servicios, clínica y controladores:

```java
// En PetCarePlusApp.java
ClinicaVeterinariaFacade facade = new ClinicaVeterinariaFacade();
facade.inicializarSistema();

MainFrame frame = new MainFrame(facade);
frame.setVisible(true);
```

> **Frase clave para el profesor:** *"Aplicamos el patrón Facade en `ClinicaVeterinariaFacade` para ocultar la complejidad de inicializar y coordinar el repositorio CSV, el modelo de la clínica, los servicios y los cuatro controladores, ofreciendo una interfaz única de alto nivel."*

---

## 3. Patrón COMPOSITE (Objeto Compuesto)

* **Clases participantes:**
  * **Component (Componente abstracto):** `Veterinaria.modelo.Tratamiento`
  * **Leaf (Hojas simples):** `TratamientoPreventivo`, `TratamientoCurativo`, `Cirujia`
  * **Composite (Compuesto):** `Veterinaria.modelo.PlanTratamiento`

### ¿Qué problema del negocio resuelve?
En una veterinaria, una mascota muchas veces no recibe un tratamiento aislado, sino un **Plan Integral de Tratamiento** (por ejemplo, un *Plan Post-Quirúrgico* formado por una `Cirujia` + un `TratamientoCurativo` con antibióticos + un `TratamientoPreventivo` de control).  
Necesitábamos que el sistema pudiera tratar a un **tratamiento individual** y a un **plan compuesto por varios tratamientos** de manera **uniforme** (sin que `Mascota` o `ConsultaVeterinariaService` tengan que diferenciar si es un tratamiento simple o una lista de tratamientos).

### Implementación clave en `PlanTratamiento.java`:
```java
@Override
public Double costoFijo() {
    double total = 0.0;
    for (Tratamiento sub : subTratamientos) {
        total += sub.costoFijo(); // Suma recursivamente hojas u otros planes
    }
    return total;
}

@Override
public Boolean requiereAtencionUrgente() {
    for (Tratamiento sub : subTratamientos) {
        if (Boolean.TRUE.equals(sub.requiereAtencionUrgente())) {
            return true; // Si al menos una etapa es urgente, todo el plan requiere seguimiento
        }
    }
    return false;
}
```

> **Frase clave para el profesor:** *"Aplicamos Composite con la clase `PlanTratamiento`, que hereda de `Tratamiento` y contiene una lista de objetos `Tratamiento`. Calcula su `costoFijo()` sumando recursivamente el costo de sus hijos y determina si `requiereAtencionUrgente()` si alguno de sus componentes lo requiere."*

---

## 4. Patrón DECORATOR (Decorador)

* **Clases participantes (en `Veterinaria.modelo.decoradores`):**
  * **Component:** `Tratamiento`
  * **Base Decorator:** `TratamientoDecorator`
  * **Concrete Decorators:**
    1. `UrgenciaGuardiaDecorator`: Suma un recargo fijo de guardia (`+$800.0`) y hace que `requiereAtencionUrgente()` sea `true`.
    2. `MedicacionEspecialDecorator`: Suma el costo de medicación importada/especial (`+$500.0`).
    3. `InternacionDecorator`: Recibe los días de internación, suma `$400.0 * dias` al costo y, si supera los 3 días, activa `requiereAtencionUrgente() = true`.

### ¿Qué problema del negocio resuelve?
Si quisiéramos agregar adicionales como *Guardia de Urgencia*, *Medicación Especial* o *Internación* usando herencia tradicional, tendríamos una **explosión combinatoria de subclases** (`CirujiaConInternacion`, `CirujiaConUrgenciaYMedicacion`, `CurativoConInternacionYUrgencia`, etc.).  
Con **Decorator**, envolvemos dinámicamente cualquier `Tratamiento` (incluso un `PlanTratamiento` compuesto) en tiempo de ejecución, apilando solo los adicionales que el cliente marque.

### Ejemplo de cómo se apilan los decoradores en `TratamientoFactory.java`:
```java
public static Tratamiento decorarTratamiento(Tratamiento base, boolean urgenciaGuardia, boolean medicacionEspecial, int diasInternacion) {
    Tratamiento resultado = base;
    if (urgenciaGuardia) {
        resultado = new UrgenciaGuardiaDecorator(resultado);
    }
    if (medicacionEspecial) {
        resultado = new MedicacionEspecialDecorator(resultado);
    }
    if (diasInternacion > 0) {
        resultado = new InternacionDecorator(resultado, diasInternacion);
    }
    return resultado;
}
```

> **Integración en la GUI y Persistencia:**
> Tanto el **Composite** (*Plan Compuesto*) como los **Decorators** (*Guardia de Urgencia*, *Medicación Especial*, *Internación*) están disponibles directamente en la pestaña **Tratamientos** de la interfaz gráfica (`TratamientosPanel.java`) y se guardan/reconstruyen automáticamente en `data/tratamientos.csv` mediante `TratamientoCsvMapper.java`.
