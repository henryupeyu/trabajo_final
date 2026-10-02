package pe.edu.upeu.sysventas.controller;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import pe.edu.upeu.sysventas.config.AppContext;

public class MainguiController {
    @FXML private TabPane tabPane;
    private CategoriaController modulo;
    @FXML public void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main_categoria.fxml"));
            loader.setControllerFactory(AppContext.getInstance()::getBean);
            Parent vista = loader.load();
            modulo = loader.getController();
            ScrollPane scroll = new ScrollPane(vista);
            scroll.setFitToWidth(true); scroll.setFitToHeight(true);
            scroll.getStyleClass().add("fondo-transparente");
            tabPane.getTabs().setAll(new Tab("CRUD Categoría", scroll));
        } catch (IOException ex) { throw new RuntimeException("No se pudo abrir el CRUD.", ex); }
    }
    @FXML private void actualizar() { modulo.listar(); }
    @FXML private void nuevo() { modulo.nuevo(); }
    @FXML private void ayuda() {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION,
            "Crear: escribe los datos y pulsa Guardar.\nConsultar: utiliza la tabla y la búsqueda.\n"
            + "Actualizar: selecciona una fila, pulsa Editar y después Guardar.\n"
            + "Eliminar: selecciona una fila, pulsa Eliminar y confirma.\n\n"
            + "Este CRUD funciona de forma independiente. Los cambios se guardan durante la sesión.");
        alerta.setHeaderText("CRUD Categoría"); alerta.showAndWait();
    }
    public boolean confirmarSalida() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION,
            "¿Salir? Los datos de esta sesión no se conservarán.", ButtonType.YES, ButtonType.NO);
        alerta.setHeaderText("Cerrar CRUD");
        return alerta.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }
    @FXML private void salir() { if (confirmarSalida()) Platform.exit(); }
}
