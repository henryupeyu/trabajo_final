package pe.edu.upeu.sysventas.controller;

import java.util.LinkedHashMap;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pe.edu.upeu.sysventas.components.ColumnInfo;
import pe.edu.upeu.sysventas.components.TableViewHelper;
import pe.edu.upeu.sysventas.model.Categoria;
import pe.edu.upeu.sysventas.service.ICategoriaService;

// CRUD independiente de categoría.
public class CategoriaController {
    private final ICategoriaService servicio;
    private Long idEdicion = null;
    @FXML private TextField txtNombre, txtBuscar;
    @FXML private Label lblEstado;
    @FXML private TableView<Categoria> tabla;

    public CategoriaController(ICategoriaService servicio) { this.servicio = servicio; }

    @FXML public void initialize() {
        LinkedHashMap<String, ColumnInfo> columnas = new LinkedHashMap<>();
        columnas.put("Código", new ColumnInfo("idCategoria", 80.0));
        columnas.put("Nombre", new ColumnInfo("nombre", 330.0));
        new TableViewHelper<Categoria>().addColumnsInOrderWithSize(tabla, columnas, this::editar, this::eliminar);
        tabla.setPlaceholder(new Label("No hay registros con esa búsqueda."));
        txtBuscar.textProperty().addListener((obs, antes, ahora) -> listar());
        nuevo(); listar();
    }
    @FXML public void listar() {
        var resultado = FXCollections.<Categoria>observableArrayList();
        String busqueda = txtBuscar.getText().trim().toLowerCase(Locale.ROOT);
        for (Categoria entidad : servicio.findAll()) {
            if ((entidad.getIdCategoria() + " " + entidad.getNombre()).toLowerCase(Locale.ROOT).contains(busqueda))
                resultado.add(entidad);
        }
        tabla.setItems(resultado);
    }
    @FXML public void guardar() {
        try {
            Categoria entidad = new Categoria(idEdicion, txtNombre.getText());
            boolean editando = idEdicion != null;
            if (editando) servicio.update(idEdicion, entidad);
            else servicio.save(entidad);
            nuevo(); txtBuscar.clear(); listar();
            lblEstado.setText(editando ? "Registro actualizado correctamente." : "Registro creado correctamente.");
        } catch (RuntimeException ex) { lblEstado.setText(ex.getMessage()); }
    }
    public void editar(Categoria entidad) {
        idEdicion = entidad.getIdCategoria();
        txtNombre.setText(entidad.getNombre());
        lblEstado.setText("Editando código " + idEdicion + ". Pulsa Guardar para actualizar.");
        txtNombre.requestFocus();
    }
    @FXML public void editarSeleccionado() {
        Categoria entidad = tabla.getSelectionModel().getSelectedItem();
        if (entidad == null) lblEstado.setText("Selecciona un registro de la tabla.");
        else editar(entidad);
    }
    public void eliminar(Categoria entidad) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Eliminar " + entidad.getNombre() + "?", ButtonType.YES, ButtonType.NO);
        alerta.setHeaderText("Eliminar categoría");
        alerta.initOwner(tabla.getScene().getWindow());
        if (alerta.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) return;
        try {
            servicio.delete(entidad.getIdCategoria());
            if (entidad.getIdCategoria().equals(idEdicion)) nuevo();
            listar(); lblEstado.setText("Registro eliminado correctamente.");
        } catch (RuntimeException ex) { lblEstado.setText(ex.getMessage()); }
    }
    @FXML public void eliminarSeleccionado() {
        Categoria entidad = tabla.getSelectionModel().getSelectedItem();
        if (entidad == null) lblEstado.setText("Selecciona un registro de la tabla.");
        else eliminar(entidad);
    }
    @FXML public void nuevo() {
        idEdicion = null; txtNombre.clear(); tabla.getSelectionModel().clearSelection();
        lblEstado.setText("Nuevo registro. Escribe un nombre y pulsa Guardar.");
    }
}
