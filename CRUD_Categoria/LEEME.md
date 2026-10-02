# CRUD de Categoría

Proyecto independiente para exponer la parte de categoría.

## Abrir

1. Descomprime el ZIP.
2. Abre la carpeta CRUD_Categoria en IntelliJ como proyecto Maven.
3. Selecciona JDK 17 o 21 en Project Structure > Project > SDK y en Modules > Dependencies > Module SDK.
4. Ejecuta ABRIR-TIENDA.bat, o Maven > Plugins > javafx > javafx:run.

PowerShell, dentro del proyecto: `.\mvnw.cmd clean javafx:run`.

Internet es necesario para descargar Maven y JavaFX la primera vez. El BAT reconoce el JDK ms-21.0.12 de .jdks si está instalado, o usa JAVA_HOME.

## Operaciones

Crear: completa el formulario y pulsa Guardar.
Leer: utiliza la tabla y su búsqueda.
Actualizar: selecciona una fila, pulsa Editar y después Guardar.
Eliminar: selecciona una fila, pulsa Eliminar y confirma.
Nuevo / Limpiar permite salir del modo de edición.

## Tus archivos principales

Dentro de src/main/java/pe/edu/upeu/sysventas:

- model/Categoria.java
- repository/CategoriaRepository.java
- service/ICategoriaService.java
- service/impl/CategoriaServiceImp.java
- controller/CategoriaController.java

Vista: src/main/resources/view/main_categoria.fxml.
Estilos: src/main/resources/css/style.css.
AppContext.java conecta las clases y MainguiController.java abre la pantalla.

## Dónde se guardan los datos

Este proyecto tiene una sola pantalla CRUD y no comparte sus datos con los otros ZIP.
Las categorías se guardan automáticamente en un archivo local del equipo:

`%USERPROFILE%\\.crud_categoria\\categorias.csv`

En Windows, puedes abrir esa ruta escribiéndola en la barra del Explorador de archivos.
Los registros permanecen después de cerrar y volver a abrir la aplicación. No necesita
instalar ni configurar una base de datos. Si borras ese archivo, eliminarás los registros guardados.

Como este proyecto es independiente, permite administrar su catálogo sin dependencias de productos de otro ZIP. En la aplicación integrada sí se protege la eliminación de registros que estén en uso.

Se verificaron la lógica CRUD, la sintaxis Java y las conexiones FXML. La apertura visual con JavaFX debe comprobarse en un equipo con JDK e Internet; el entorno de preparación no puede descargar las dependencias de Maven Central.
