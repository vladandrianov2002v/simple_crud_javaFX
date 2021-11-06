package ru.sapteh.controller;

import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.sapteh.model.User;
import ru.sapteh.service.UserService;

public class ListUserController {
    private final ObservableList<User> users = FXCollections.observableArrayList();

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
    private TextField searchTextField;

    @FXML
    private Label countLabel;

    @FXML
    private Button searchButton;

    @FXML
    public void initialize() {
        initData();

        idColumn.setCellValueFactory(u -> new SimpleObjectProperty<>(u.getValue().getId()));

        firstNameColumn.setCellValueFactory(u -> new SimpleObjectProperty<>(u.getValue().getFirstName()));

        lastNameColumn.setCellValueFactory(u -> new SimpleObjectProperty<>(u.getValue().getLastName()));

        ageColumn.setCellValueFactory(u -> new SimpleObjectProperty<>(u.getValue().getAge()));

        userTableView.setItems(users);

        countLabel.setText(String.valueOf(users.size()));
    }

    private void initData() {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        UserService userService = new UserService(factory);
        users.addAll(userService.findAll());
    }
}
