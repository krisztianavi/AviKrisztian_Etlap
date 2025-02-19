package com.example.etlap;

import com.example.etlap.Etlap;
import com.example.etlap.EtlapService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.List;

public class EtlapAddController {
    @FXML private TextField nameField;
    @FXML private TextArea descriptionField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private Spinner<Integer> priceSpinner;

    private EtlapService etlapService;

    public void initialize() {
        try {
            etlapService = new EtlapService();
            List<String> categories = etlapService.getCategories();
            if (categories.isEmpty()) {
                System.out.println("Nincsenek kategóriák az adatbázisban!");
            } else {
                categoryComboBox.getItems().setAll(categories);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Hiba történt a kategóriák betöltése közben!");
        }
    }

    @FXML
    private void onAddFood() {
        String nev = nameField.getText();
        String leiras = descriptionField.getText();
        String kategoria = categoryComboBox.getValue();
        int ar = priceSpinner.getValue();

        if (nev.isEmpty() || leiras.isEmpty() || kategoria == null || ar <= 0) {
            System.out.println("Minden mezőt ki kell tölteni!");
            return;
        }

        Etlap etel = new Etlap(0, nev, kategoria, ar, leiras);
        try {
            if (etlapService.create(etel)) {
                System.out.println("Étel sikeresen hozzáadva!");
            } else {
                System.out.println("Hiba történt az étel hozzáadása közben!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}