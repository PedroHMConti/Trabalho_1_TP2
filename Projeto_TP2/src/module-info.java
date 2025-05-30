module ProjetoTP2 {
    requires javafx.controls;
    requires javafx.fxml;

    opens GUIPKG to javafx.fxml;
    exports GUIPKG;
}