package library.controllers.admin;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import library.entities.Book;
import library.entities.User;
import library.services.api.BookService;
import library.services.api.UserService;
import library.utilities.BookServiceInstance;
import library.utilities.ConfirmBox;
import library.utilities.LoaderProvider;
import library.utilities.UserServiceInstance;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ViewAllUsersAdminController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label errorLabel;
    @FXML
    private TableView<User> table;

    private User user;
    private UserService userService;
    private BookService bookService;

    @FXML
    void moreInfoClicked() {

    }

    @FXML
    void deleteButtonClicked() {
        User selectedItem = this.table.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Boolean confirmation = ConfirmBox.display("DeleteUser", "Would you like to delete this user?");
            if (confirmation) {
                this.table.getItems().remove(selectedItem);
                List<Book> books = this.bookService.getBooksByUserId(selectedItem.getId());
                this.userService.deleteUserById(selectedItem, books);
            }
        } else {
            this.errorLabel.setText("Please select a user.");
        }
    }

    @FXML
    void backToMainMenuClicked() throws IOException {
        FXMLLoader fxmlLoader = LoaderProvider.get();
        fxmlLoader.setLocation(getClass().getResource("/FXML/admin/adminMenu.fxml"));
        AnchorPane root = fxmlLoader.load();
        AdminMenuController controller = fxmlLoader.<AdminMenuController>getController();
        controller.initData(this.user);
        this.anchorPane.getChildren().setAll(root);
    }

    public void initData(User user) {
        this.user = user;
        List<User> users = this.userService.getAllUsers().stream()
                .filter(x -> !x.getRole().getName().equals("ROLE_ADMIN")).collect(Collectors.toList());
        ObservableList<User> observableList = FXCollections.observableList(users);
        this.table.setItems(observableList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.userService = UserServiceInstance.getInstance();
        this.bookService = BookServiceInstance.getInstance();
    }
}