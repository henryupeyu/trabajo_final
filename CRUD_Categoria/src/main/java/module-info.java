module pe.edu.upeu.sysventas {
    requires javafx.controls;
    requires javafx.fxml;
    opens pe.edu.upeu.sysventas to javafx.fxml;
    opens pe.edu.upeu.sysventas.controller to javafx.fxml;
    exports pe.edu.upeu.sysventas;
    exports pe.edu.upeu.sysventas.model;
    opens pe.edu.upeu.sysventas.model to javafx.base;
}
