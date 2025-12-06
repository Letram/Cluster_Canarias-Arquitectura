# Arquitectura Hexagonal — Principios y organización

## Introducción
La arquitectura hexagonal tiene como objetivo principal mantener el dominio (la lógica de negocio) completamente independiente de detalles técnicos. Esto permite un núcleo de aplicación limpio, testable y fácil de mantener, sin depender de bases de datos, frameworks u otros mecanismos de infraestructura.

## Principio central: dominio libre de detalles técnicos
El dominio engloba todo aquello que no depende de decisiones tecnológicas: reglas de negocio, modelos y servicios que representan la lógica del sistema. En contraste, los "detalles" son la persistencia, el tipo de interfaz de usuario, frameworks, y cualquier aspecto tecnológico cambiable.

## Puertos y adaptadores
- Puertos: son interfaces definidas en el dominio que expresan las operaciones necesarias para interactuar con el exterior (por ejemplo, repositorios o servicios externos). Los puertos no se definen desde la infraestructura; los expone el dominio según sus necesidades.
- Adaptadores: son las implementaciones concretas de esos puertos y residen en la capa de infraestructura. Su función es traducir entre el dominio y el mundo exterior (BD, APIs, etc.).

Los servicios del dominio deben depender únicamente de los puertos (interfaces), no de adaptadores concretos. Esto garantiza inversión de dependencias y facilita el reemplazo de implementaciones.

## Inyección de dependencias y factoría de adaptadores
Para ensamblar la aplicación se crea una factoría o módulo de composición en la infraestructura que:
1. Crea las implementaciones concretas de los puertos (adaptadores).
2. Inyecta esas implementaciones en los servicios del dominio (normalmente a través de constructores).

De este modo, el dominio permanece agnóstico respecto a cómo se cumplen sus contratos; solo conoce las interfaces.

## Modelos y DTOs
- Modelos del dominio: objetos que representan conceptos y reglas de negocio. Pertenecen al dominio y deben ser puros.
- DTOs de dominio (compartidos): cuando una misma estructura de datos se usa tanto en la presentación como en los servicios (por ejemplo, para entrada/salida de casos de uso) y representa conceptos del negocio, tiene sentido que ese DTO resida en el dominio. Estos DTOs son parte del contrato del dominio y permiten que presentación y servicios trabajen con tipos comunes sin implicar detalles de infraestructura.
- DTOs y estructuras de persistencia/transferencia (infraestructura): formatos específicos para la base de datos, APIs externas o serialización pertenecen a la capa de infraestructura. La infraestructura debe mapear entre estos formatos y los modelos/DTOs del dominio.

Responsabilidad de los mapeos:
- El dominio define los contratos (modelos y DTOs compartidos).
- La infraestructura implementa adaptadores que convierten entre los DTOs/modelos del dominio y las representaciones específicas necesarias para persistencia o comunicación externa.

## Resumen
- Mantener el dominio libre de detalles técnicos es la base de la arquitectura hexagonal.
- El dominio define puertos; la infraestructura implementa adaptadores.
- La composición (factoría) arma las implementaciones y las inyecta en los servicios del dominio.
- Los modelos y los DTOs que representan contratos compartidos por presentación y servicio pueden residir en el dominio.
- Los DTOs/estructuras específicas de persistencia o de transporte permanecen en la infraestructura.
---

# Nuevo

El contenido que está en el módulo de "presentación", lo podemos mover al módulo de infraestructura, ya que la presentación es un detalle técnico. De esta forma, el dominio y los servicios no dependen de la presentación directamente, sino que lo hacen a través de puertos definidos en el dominio e implementados en infraestructura.
¿Por qué la presentación es un detalle técnico? Porque puede cambiar con el tiempo (por ejemplo, cambiar de una interfaz web a una API REST o a una aplicación móvil) sin afectar la lógica de negocio. Al mantener la presentación en infraestructura, podemos cambiarla o reemplazarla sin impactar el dominio. Debido a esto, tiene que ir dentro de la definición de infraestructura.
Lo que diferencia a la presentación de la persistencia es la naturaleza de sus ports. Si tuviéramos que diferenciarlas, podríamos considerar que la presentación pertenecería al INPUT (primary) y la persistencia como OUTPUT (secondary). Pero ambas son detalles técnicos y, por lo tanto, deben residir en infraestructura.
También se conocen los INPUTS como "drivers" y los OUTPUTS como "driven". La presentación es un driver porque inicia la interacción con el usuario, mientras que la persistencia es driven porque responde a las solicitudes del dominio para almacenar o recuperar datos.

Los enchufes (o ports) del dominio, a veces se crean usando una capa intermedia entre el dominio y la infraestructura, llamada "aplicación" o "servicios de aplicación". Esta capa puede definir casos de uso específicos que orquestan la lógica del dominio y exponen puertos para la presentación y la persistencia. Sin embargo, la responsabilidad principal sigue siendo que el dominio no dependa de detalles técnicos, y cualquier interacción con la presentación o la persistencia se realice a través de puertos definidos en el dominio o en la capa de aplicación. Por lo que los servicios y su lógica están en la capa de aplicación, mientras que el dominio contiene las reglas de negocio puras.

# Cambios
- handlers -> interfaces en la infraestructura, que acabarán siendo adaptadores de los puertos definidos en el dominio o en la capa de aplicación.
- Crear una capa de aplicación entre el dominio y la infraestructura.
- La aplicación tiene que definir las interfaces de entrada.
- Dominio: puertos de salida (repositorios, servicios externos).
- Aplicación: puertos de entrada (casos de uso).
- La capa de aplicación depende del dominio, pero no al revés.
- La capa de infraestructura depende tanto del dominio como de la capa de aplicación, implementando los adaptadores necesarios para los puertos definidos en ambas capas.

- En primera instancia, todos los puertos están en el dominio, luego ya se hará otro refactor para sacarlos a la capa de aplicación.
- De forma opcional, se puede dividir la capa de infraestructura en dos subcapas: una para los adaptadores de entrada (presentación) y otra para los adaptadores de salida (persistencia). Esto ayuda a organizar mejor el código y clarificar las responsabilidades de cada parte de la infraestructura.
- De la misma forma que en la infraestructura se pueden tener subcapas, en la capa de aplicación también se pueden tener 2 subcapas para los diferentes tipos de ports (entrada y salida)
- Al final los métodos que van en las interfaces de los ports son los handleGet y handlePost pero con nombres representativos. A veces, la interfaz de la representación puede ser RocketHandler -> RocketUserCases (y handleGet y handlePost como sus métodos a implementar)
- Luego ya la lógica de cada uno de los métodos de la interfaz la implementa el "adapter" o "handler" de la capa de infraestructura. 
- La interfaz de la presentación tiene que ser implementada por el servicio
- El handler tiene que tener dentro un rocketsusecase.
- El servicio implementa el puerto de entrada. De esta forma, el handler usa el EntityUseCases. En el punto de inicio de la aplicación, los handlers se crean inyectándole dentro un EntityUseCasesAdapter

---

# Domain Driven Development (DDD) y Arquitectura Hexagonal
La Arquitectura Hexagonal y el Domain Driven Development (DDD) son dos enfoques complementarios que pueden trabajar juntos para crear aplicaciones robustas y mantenibles. A continuación, se explica cómo se relacionan y cómo se pueden integrar:
1. Enfoque en el Dominio:
   - Ambos enfoques ponen un fuerte énfasis en el dominio y la lógica de negocio. DDD se centra en modelar el dominio de manera precisa, mientras que la Arquitectura Hexagonal busca aislar ese dominio de los detalles técnicos.
   - En DDD, se utilizan conceptos como entidades, agregados, servicios de dominio y repositorios para estructurar el dominio. Estos conceptos pueden ser implementados dentro del núcleo de la Arquitectura Hexagonal.
2. Capas y Separación de Responsabilidades:
   - La Arquitectura Hexagonal define capas claras (dominio, aplicación, infraestructura) que ayudan a separar las responsabilidades. DDD puede beneficiarse de esta separación al ubicar los modelos de dominio y la lógica de negocio en la capa de dominio.
   - Los casos de uso definidos en DDD pueden implementarse en la capa de aplicación de la Arquitectura Hexagonal, actuando como puertos de entrada.
3. Puertos y Adaptadores:
   - En la Arquitectura Hexagonal, los puertos y adaptadores permiten que el dominio interactúe con el mundo exterior sin depender de detalles técnicos. DDD puede aprovechar este mecanismo para definir interfaces claras para la persistencia, la comunicación externa y la presentación.
   - Los repositorios en DDD pueden ser implementados como adaptadores de salida en la capa de infraestructura.
4. Lenguaje Ubicuo:
   - DDD promueve el uso de un lenguaje ubicuo compartido entre desarrolladores y expertos en el dominio. Este lenguaje puede ser reflejado en los nombres de las clases, métodos y estructuras dentro del núcleo de la Arquitectura Hexagonal.
5. Evolución y Mantenimiento:
   - La combinación de DDD y Arquitectura Hexagonal facilita la evolución del sistema. A medida que cambian los requisitos del negocio, el dominio puede adaptarse sin afectar los detalles técnicos, gracias a la separación proporcionada por la Arquitectura Hexagonal.

> En resumen, la Arquitectura Hexagonal proporciona una estructura sólida para implementar los principios de DDD, permitiendo que el dominio se mantenga limpio y enfocado en la lógica de negocio, mientras que DDD ofrece herramientas y conceptos para modelar ese dominio de manera efectiva. Juntos, estos enfoques pueden conducir a aplicaciones más coherentes, flexibles y fáciles de mantener.  

Esta metodología y arquitectura se suele utilizar en sistemas complejos donde la lógica de negocio es crucial y se espera que evolucione con el tiempo. Al combinar DDD con la Arquitectura Hexagonal, los desarrolladores pueden crear sistemas que no solo son técnicamente sólidos, sino también alineados con las necesidades del negocio. Es decir, a proyectos grandes.

Normalmente, en estos proyectos tenemos más de un dominio, por lo que podemos hablar de Supporting Domains y Bounded Contexts. Cada Bounded Context puede implementarse como un módulo o servicio separado dentro de la Arquitectura Hexagonal, manteniendo sus propios modelos de dominio, puertos y adaptadores. Esto permite una mayor modularidad y facilita la gestión de la complejidad en sistemas grandes. Los Supporting Domains son dominios que apoyan al dominio principal, y pueden tener sus propias implementaciones hexagonales, pero siempre subordinadas al dominio principal.

Las diferentes relaciones entre contextos son:
- Relaciones de dependencia: Un contexto puede depender de otro para ciertos servicios o datos.
- Relaciones de colaboración: Contextos que trabajan juntos para lograr un objetivo común.
- Relaciones de integración: Contextos que se integran a través de puertos y adaptadores.

Por otro lado, también tenemos:
- Customer/Supplier: Un contexto actúa como proveedor de servicios o datos para otro contexto.
- Conformist: Un contexto adopta las reglas y modelos de otro contexto sin modificaciones.
- Anticorruption Layer: Un contexto utiliza una capa de anticorrupción para interactuar con otro contexto sin verse afectado por sus modelos o reglas. Estas relaciones ayudan a definir cómo los diferentes contextos interactúan y colaboran dentro de un sistema más grande, manteniendo la integridad y coherencia de cada dominio.

---
