# OrderMaster: Aplicación Web

## 💡 Sobre el Proyecto
**OrderMaster** es la evolución a entorno web del Sistema de Gestión para el asadero "**ShamanBlack**" en la ciudad de Loja. Este proyecto tiene como objetivo principal automatizar y optimizar los procesos críticos durante temporadas de alta demanda, resolviendo problemas de comunicación y demoras en la atención al cliente.

## ✨ Funcionalidades Principales
El sistema se centra en mejorar la eficiencia operativa a través de:
* **Registro y gestión de pedidos:** permite enviar, editar y visualizar el estado de las órdenes en tiempo real.
* **Cálculo automático:** determina el valor total del pedido instantáneamente.
* **Facturación:** genera comprobantes de venta detallados para los clientes.
* **Control de disponibilidad:** permite visualizar el menú actualizado para evitar errores en la toma de pedidos.

## 🛠️ Tecnologías Utilizadas
Para el desarrollo de esta aplicación web, se emplean tecnologías de grado empresarial bajo el paradigma de **Programación Orientada a Objetos (POO)**:

* **Frontend:** Jakarta Faces (JSF), HTML5 y CSS3.
* **Vistas:** Maquetación mediante archivos `.xhtml` (Facelets).
* **Backend:** Jakarta EE - Core.
* **Controladores:** Managed Beans utilizando la anotación `@Named`.
* **Persistencia:** PostgreSQL o MySQL como sistemas de base de datos.

## 👥 Equipo y Organización
El equipo se divide en áreas especializadas para asegurar la calidad y el cumplimiento de los objetivos del proyecto:

### Estructura por Capas
* **Frontend:** Alexander Gallo y Henry Romero.
* **Backend:** Franz Ludeña y Francisco Chamba.

### Roles Específicos
| Rol | Responsable(s) | Tarea Principal |
| :--- | :--- | :--- |
| **Diseñadores de Interfaz** | Henry Romero, Alexander Gallo | Maquetación y diseño de las vistas web (`.xhtml`). |
| **Lógica de Vista (Controlador)** | Franz Ludeña | Implementación de Managed Beans (`@Named`) para la interacción modelo-vista. |
| **Coordinador de Pruebas** | Francisco Chamba | Validación del flujo operativo, usabilidad y control de errores. |

## 📋 Estrategia de Commits
Para mantener un historial de versiones limpio y profesional, el equipo sigue una convención de mensajes descriptivos con los siguientes prefijos:

* **`feat:`** Nueva funcionalidad.
* **`fix:`** Corrección de errores.
* **`docs:`** Cambios en documentación.
* **`style:`** Formato y diseño (espacios, CSS, etc.).
* **`refactor:`** Cambios en el código que no afectan la funcionalidad.
* **`test:`** Adición o modificación de pruebas.
* **`chore:`** Tareas de mantenimiento (ej. `.gitignore`).
