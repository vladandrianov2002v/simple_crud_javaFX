package ru.sapteh.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.sapteh.model.User;
import ru.sapteh.service.UserService;

public class ListUserController {

    private final UserService userService;
    private final ObservableList<User> users = FXCollections.observableArrayList();
    @FXML
    public TextField searchTxt;
    @FXML
    public Label countLbl;

    public ListUserController() {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        userService = new UserService(factory);
    }


    @FXML
    private Label labelId;

    @FXML
    private TextField textFieldFirstName;

    @FXML
    private TextField textFieldLastName;

    @FXML
    private TextField textFieldAge;

    @FXML
    private TableView<User>
            userTableView;

    @FXML
    private TableColumn<User, Integer>
            idColumn;

    @FXML
    private TableColumn<User, String>
            firstNameColumn;


    @FXML
    private TableColumn<User, String>
            lastNameColumn;


    @FXML
    private TableColumn<User, Integer>
            ageColumn;


    @FXML
    public void initialize() {
        initData();
        searchByFirstName();
        idColumn.setCellValueFactory(u -> new SimpleObjectProperty<>(u.getValue().getId()));

        firstNameColumn.setCellValueFactory(u -> new SimpleObjectProperty<>(u.getValue().getFirstName()));
        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameColumn.setOnEditCommit(event -> {
            User user = event.getTableView().getItems().get(event.getTablePosition().getRow());
            user.setFirstName(event.getNewValue());
            userService.update(user);
        });

        lastNameColumn.setCellValueFactory(u -> new SimpleObjectProperty<>(u.getValue().getLastName()));
        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameColumn.setOnEditCommit(event -> {
            User user = event.getTableView().getItems().get(event.getTablePosition().getRow());
            user.setLastName(event.getNewValue());
            userService.update(user);
        });

        ageColumn.setCellValueFactory(u -> new SimpleObjectProperty<>(u.getValue().getAge()));
        ageColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<>() {
            @Override
            public String toString(Integer age) {
                return String.valueOf(age);
            }

            @Override
            public Integer fromString(String age) {
                return Integer.parseInt(age);
            }
        }));

        userTableView.setItems(users);
        userTableView.setEditable(true);
        listenerTabUserDetails(null);
        userTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            listenerTabUserDetails(newValue);
        });
        ageColumn.setOnEditCommit(event -> {
            User user = event.getTableView().getItems().get(event.getTablePosition().getRow());
            user.setAge(event.getNewValue());
            userService.update(user);
        });
        countLbl.setText(String.valueOf(userTableView.getItems().size()));
    }


    private void initData() {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        UserService userService = new UserService(factory);
        users.addAll(userService.findAll());
    }

    @FXML
    public void updateUserButton(ActionEvent actionEvent) {
        int selectedIndex = userTableView.getSelectionModel().getSelectedIndex();
        final User user = userTableView.getSelectionModel().getSelectedItem();
        user.setFirstName(textFieldFirstName.getText());
        user.setLastName(textFieldLastName.getText());
        user.setAge(Integer.parseInt(textFieldAge.getText()));
        userTableView.getItems().set(selectedIndex, user);
        userService.update(user);
        cleanTextField();
        countLbl.setText(String.valueOf(userTableView.getItems().size()));
    }

    @FXML
    public void deleteUserButton(ActionEvent actionEvent) {
        final User user = userTableView.getSelectionModel().getSelectedItem();
        userService.delete(user);
        userTableView.getItems().remove(user);
        cleanTextField();
        System.out.println("Delete user: " + user);
        countLbl.setText(String.valueOf(userTableView.getItems().size()));
    }

    @FXML
    public void saveUserButton(ActionEvent actionEvent) {
        User user = new User(textFieldFirstName.getText(), textFieldLastName.getText(), Integer.parseInt(textFieldAge.getText()));
        userService.save(user);

        userTableView.getItems().add(user);
        cleanTextField();
        countLbl.setText(String.valueOf(userTableView.getItems().size()));
    }

    private void listenerTabUserDetails(User user) {
        if (user != null) {
            labelId.setText(String.valueOf(user.getId()));
            textFieldFirstName.setText(user.getFirstName());
            textFieldLastName.setText(user.getLastName());
            textFieldAge.setText(String.valueOf(user.getAge()));
        } else {
            labelId.setText("");
            textFieldFirstName.setText("");
            textFieldLastName.setText("");
            textFieldAge.setText("");
        }
    }

    private void cleanTextField() {
        textFieldFirstName.clear();
        textFieldLastName.clear();
        textFieldAge.clear();
        labelId.setText("");
    }

    private void searchByFirstName() {
        searchTxt.textProperty().addListener((obs, old, newValue) -> {
            FilteredList<User> userFilteredList = new FilteredList<>(users,
                    s -> s.getFirstName().toLowerCase().contains(newValue.toLowerCase().trim()));
            userTableView.setItems(userFilteredList);
            countLbl.setText(String.valueOf(userFilteredList.size()));
        });
    }
}
