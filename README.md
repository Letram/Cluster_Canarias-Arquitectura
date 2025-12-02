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
