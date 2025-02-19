package com.example.etlap;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EtlapController {
    @FXML private TableView<Etlap> etlapTable;
    @FXML private TableColumn<Etlap, String> colNev;
    @FXML private TableColumn<Etlap, String> colKategoria;
    @FXML private TableColumn<Etlap, Integer> colAr;
    @FXML private Spinner<Integer> percentSpinner;
    @FXML private Spinner<Integer> fixSpinner;

    private ObservableList<Etlap> etlapLista = FXCollections.observableArrayList();
    private EtlapService etlapService;

    @FXML
    public void initialize() {
        colNev.setCellValueFactory(new PropertyValueFactory<>("nev"));
        colKategoria.setCellValueFactory(new PropertyValueFactory<>("kategoria"));
        colAr.setCellValueFactory(new PropertyValueFactory<>("ar"));
        etlapTable.setItems(etlapLista);

        try {
            etlapService = new EtlapService();
            loadEtlap();
        } catch (SQLException e) {
            showAlert("Hiba", "Nem sikerült csatlakozni az adatbázishoz!");
        }
    }

    public void loadEtlap() {
        try {
            List<Etlap> etelek = etlapService.getAll();
            etlapLista.setAll(etelek);
        } catch (SQLException e) {
            showAlert("Hiba", "Nem sikerült betölteni az ételeket az adatbázisból!");
        }
    }

    @FXML
    private void handleUjEtel() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("etlapadd-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Új étel hozzáadása");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Hiba", "Nem sikerült betölteni az új étel hozzáadása ablakot!");
        }
    }

    @FXML
    private void handleTorles() {
        Etlap selectedEtel = etlapTable.getSelectionModel().getSelectedItem();
        if (selectedEtel != null) {
            try {
                if (etlapService.delete(selectedEtel.getId())) {
                    etlapLista.remove(selectedEtel);
                    showAlert("Siker", "Sikeresen törölve!");
                }
            } catch (SQLException e) {
                showAlert("Hiba", "Nem sikerült törölni az elemet!");
            }
        } else {
            showAlert("Figyelmeztetés", "Nincs kiválasztva elem!");
        }
    }

    @FXML
    private void handleAremelesSzazalek() {
        Integer szazalek = percentSpinner.getValue();
        if (szazalek == null || szazalek <= 0) {
            showAlert("Hiba", "Adj meg egy érvényes százalékos értéket!");
            return;
        }
        applyPriceIncrease(szazalek, true);
    }

    @FXML
    private void handleAremelesFix() {
        Integer fix = fixSpinner.getValue();
        if (fix == null || fix <= 0) {
            showAlert("Hiba", "Adj meg egy érvényes fix értéket!");
            return;
        }
        applyPriceIncrease(fix, false);
    }

    private void applyPriceIncrease(int value, boolean isPercentage) {
        Etlap selectedEtel = etlapTable.getSelectionModel().getSelectedItem();
        if (selectedEtel == null) {
            showAlert("Figyelmeztetés", "Nincs kiválasztva elem!");
            return;
        }
        try {
            etlapService.updatePrice(selectedEtel.getId(), value, isPercentage);
            loadEtlap();
            showAlert("Siker", "Áremelés sikeresen végrehajtva!");
        } catch (SQLException e) {
            showAlert("Hiba", "Nem sikerült végrehajtani az áremelést!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
