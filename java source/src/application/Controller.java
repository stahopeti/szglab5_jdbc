package application;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import dal.ActionResult;
import dal.DataAccessLayer;
import dal.exceptions.CouldNotConnectException;
import dal.exceptions.EntityNotFoundException;
import dal.exceptions.NotConnectedException;
import dal.exceptions.ValidationException;
import dal.impl.SzorakDal;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import model.Place;
import model.Person;
import model.PersonData;
import model.DiscoPeople;

/**
 * Class for implementing the logic.
 */
public class Controller implements Initializable {
	private DataAccessLayer<Place, PersonData, DiscoPeople> dal;

	@FXML
	TextField usernameField;
	@FXML
	TextField passwordField;
	@FXML
	TextField searchByNameTextField;
	@FXML
	Button connectButton;
	@FXML
	Label connectionStateLabel;
	@FXML
	ComboBox<ComboBoxItem<String>> sampleCombo;

	@FXML
	TableColumn<Place, String> nameColumn;

	@FXML
	TableColumn<Place, String> addressColumn;

	@FXML
	TableColumn<Place, String> phoneColumn;
	/*
	@FXML
	TextField searchTextField;
	@FXML
	TableColumn<Person, String> nameColumn;
	@FXML
	TableColumn<Person, String> identityNumberColumn;
	*/
	@FXML
	TableView<Place> searchTable;

	@FXML
	TextField newPersonID;

	@FXML
	TextField newPersonName;

	@FXML
	TextField newPersonAddress;

	@FXML
	TextField newPersonPhone;

	@FXML
	TextField newPersonIncome;

	@FXML
	TextField newPersonHobby;

	@FXML
	TextField newPersonFavouriteMovie;

	public Controller() {
		dal = new SzorakDal();
	}

	@FXML
	public void connectEventHandler(ActionEvent event) {
		//Getting the input from the UI.
		String username = usernameField.getText();
		String password = passwordField.getText();

		try {
			//Connect to the database, and update the UI
			dal.connect(username, password);
			connectionStateLabel.setText("Connection created!");
			connectionStateLabel.setTextFill(Paint.valueOf("green"));
		} catch (ClassNotFoundException e) {
			//Driver is not found
			connectionStateLabel.setText("JDBC driver not found!");
			connectionStateLabel.setTextFill(Paint.valueOf("red"));
		} catch (CouldNotConnectException e) {
			//Could not connect, e.g. invalid username or password
			connectionStateLabel.setText("Could not connect to the server!");
			connectionStateLabel.setTextFill(Paint.valueOf("red"));
		}
	}

	@FXML
	public void listEventHandler() {
		try {
			List<Place> places = dal.search("");
			searchTable.setItems(FXCollections.observableArrayList(places));
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void searchEventHandler(){
		try {
			List<Place> places = dal.search(searchByNameTextField.getText());
			searchTable.setItems(FXCollections.observableArrayList(places));
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void addPersonEventHandler(){
		PersonData personData = new PersonData();
		try {
			personData.parsePerson_id(newPersonID.getText());
			personData.parseName(newPersonName.getText());
			personData.parseAddress(newPersonAddress.getText());
			personData.parsePhone(newPersonPhone.getText());
			personData.parseIncome(newPersonIncome.getText());
			personData.parseHobby(newPersonHobby.getText());
			personData.parseFavourite_movie(newPersonFavouriteMovie.getText());

			ActionResult actionResult = dal.insertOrUpdate(personData, 0);

			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Info");
			alert.setHeaderText(null);
			switch (actionResult) {
				case InsertOccurred:
					alert.setContentText("Person inserted successfully!");
					alert.showAndWait();
					break;
				case UpdateOccurred:
					alert.setContentText("Person updated successfully!");
					alert.showAndWait();
					break;
				case ErrorOccurred:
					alert.setAlertType(Alert.AlertType.ERROR);
					alert.setContentText("Error occurred!");
					alert.showAndWait();
					break;
			}
		} catch (ValidationException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void commitEventHandler() {
		//TODO: handle the click of the commit button.
	}

	@FXML
	public void editEventHandler() {
		//TODO: handle the click of the edit button
	}

	@FXML
	public void statisticsEventHandler() {
		//TODO: handle the click of the statistics button
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO: initalize property-value factories
		//EXAMPLE: sampleCombo stores two strings.
		sampleCombo.getItems().add(new ComboBoxItem<String>("Value A", "a"));
		sampleCombo.getItems().add(new ComboBoxItem<String>("Value B", "b"));
		
		//EXAMPLE: this is how we bind the private variables to a data cell
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
		phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
		//identityNumberColumn.setCellValueFactory(new PropertyValueFactory<>("identityNumber"));
	}

	public void disconnect() {
		dal.disconnect();
	}

}
