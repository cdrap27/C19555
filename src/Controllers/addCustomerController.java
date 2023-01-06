package Controllers;

import DAO.DBCountries;
import DAO.DBCustomers;
import DAO.DBDivision;
import Model.Countries;
import Model.Customers;
import Model.Division;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class addCustomerController {
    public TextField addCustomerID;
    public TextField addCustomerName;
    public TextField addCustomerAddress;
    public TextField addCustomerPost;
    public TextField addCustomerPhone;
    public Button save;
    public Button cancel;
    public ChoiceBox addCustomerCountry;
    public ChoiceBox addCustomerDivision;
    public TextField addCustomerDivisionID;

    /**
     * initializes the add customer screen
     */
    public void initialize(){
        addCustomerID.setText(String.valueOf(DBCustomers.customerSize +1));
        addCustomerCountry.setItems(Countries.countryNames(DBCountries.getCountryList()));
    }

    /**
     * on save, checks if data entered is correct, adds data if it is, and returns to the dashboard
     * @param actionEvent action event
     * @throws IOException io exception
     */
    public void onSave(ActionEvent actionEvent) throws IOException {
        if(checkData() == true){
            addCustomerForm();
            DBCustomers.getCustomerList().add(addCustomerForm());
            DBCustomers.addCustomerSQL(addCustomerForm());
            DBCustomers.customerSize = DBCustomers.customerSize+1;
            Parent root = FXMLLoader.load(getClass().getResource("../Views/dashboard.fxml"));
            Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1055, 699);
            stage.setTitle("Dashboard");
            stage.setScene(scene);
            stage.getScene().getWindow().centerOnScreen();
            stage.show();

        }
    }

    /**
     * returns to the dashboard upon hitting cancel.
     * @param actionEvent action event
     * @throws IOException io exception
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel?");
        alert.setContentText("Are you sure you want to cancel adding customer?");
        ButtonType CANCEL = new ButtonType("Cancel");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("../Views/dashboard.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1055, 699);
            stage.setTitle("Dashboard");
            stage.setScene(scene);
            stage.getScene().getWindow().centerOnScreen();
            stage.show();
        }
    }

    /**
     * sets divsion box upon selecting a country
     * @param event event
     */
    public void countrySelected(ActionEvent event) {
        addCustomerDivisionID.clear();
        addCustomerDivision.setItems(Division.divisionNames((String)addCustomerCountry.getSelectionModel().getSelectedItem()));
    }

    /**
     * sets division id upon selecting a division
     * @param actionEvent action event
     */
    public void divisionSelected(ActionEvent actionEvent) {
        addCustomerDivisionID.clear();
        if(addCustomerDivision.getValue() != null)
        addCustomerDivisionID.setText(String.valueOf(Division.getDivisionID((String)addCustomerDivision.getSelectionModel().getSelectedItem())));
    }

    /**
     * checks data for errors
     * @return returns boolean
     */
    public Boolean checkData(){
        Boolean proceed = true;
        Boolean check = true;
        while(proceed == true){
            if(Customers.checkName(addCustomerName) == false){
                check = false;
                break;
            }
            if(Customers.checkAddress(addCustomerAddress) == false){
                check = false;
                break;
            }
            if(Customers.checkPost(addCustomerPost) == false){
                check = false;
                break;
            }
            if(Customers.checkPhone(addCustomerPhone) == false){
                check = false;
                break;
            }
            if(Customers.checkCountry(addCustomerCountry) == false){
                check = false;
                break;
            }
            if(Customers.checkDivision(addCustomerDivision) == false){
                check = false;
                break;
            }
            proceed = false;
        }
        return check;
    }

    /**
     * creates a customer with the data entered
     * @return customer
     */
    public Customers addCustomerForm(){
        String name = addCustomerName.getText();
        String address = addCustomerAddress.getText();
        String post = addCustomerPost.getText();
        String phone = addCustomerPhone.getText();
        int divisionID = DBCustomers.customerSize +1;
        String country = (String)addCustomerCountry.getSelectionModel().getSelectedItem();
        String division =(String) addCustomerDivision.getSelectionModel().getSelectedItem();
        Customers c = new Customers(DBCustomers.customerSize+1, name, address, post,
                phone, Division.getDivisionID((String)addCustomerDivision.getSelectionModel().getSelectedItem()), division, country);

        return c;
    }


}
