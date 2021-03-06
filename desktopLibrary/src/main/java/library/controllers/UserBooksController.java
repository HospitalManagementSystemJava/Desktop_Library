package library.controllers;

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
import library.utilities.BookServiceInstance;
import library.utilities.ConfirmBox;
import library.utilities.LoaderProvider;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserBooksController implements Initializable {

    @FXML
    private TableView<Book> table;
    @FXML
    private Label errorLabel;
    @FXML
    private AnchorPane rootPane;

    private User user;
    private BookService bookService;


    @FXML
    void deleteButtonClicked() {
        Book selectedItem = this.table.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Boolean confirmation = ConfirmBox.display("DeleteBook", "Would you like to delete this book?");
            if (confirmation) {
                this.table.getItems().remove(selectedItem);
                this.bookService.deleteBookById(selectedItem);
            }
        } else {
            this.errorLabel.setText("Please select a book!");
        }
    }

    @FXML
    void editButtonClicked() throws IOException {
        Book selectedItem = this.table.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/editBook.fxml"));

            AnchorPane root = fxmlLoader.load();
            EditBookController controller = fxmlLoader.<EditBookController>getController();
            controller.initData(this.user, selectedItem);

            this.rootPane.getChildren().setAll(root);
        } else {
            this.errorLabel.setText("Please select a book!");
        }
    }

    @FXML
    void backToMainMenuClicked() throws IOException {
        FXMLLoader fxmlLoader = LoaderProvider.get();
        fxmlLoader.setLocation(getClass().getResource("/FXML/menu.fxml"));
        AnchorPane root = fxmlLoader.load();
        MenuController controller = fxmlLoader.<MenuController>getController();
        controller.initData(this.user);
        this.rootPane.getChildren().setAll(root);
    }

    @FXML
    void descriptionButtonClicked() throws IOException {
        Book selectedItem = this.table.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            this.errorLabel.setText("Please select a book!");
            return;
        }

        FXMLLoader fxmlLoader = LoaderProvider.get();
        fxmlLoader.setLocation(getClass().getResource("/FXML/singleBookView.fxml"));
        AnchorPane root = fxmlLoader.load();
        SingleBookViewController controller = fxmlLoader.<SingleBookViewController>getController();
        controller.initData(this.user, selectedItem);
        this.rootPane.getChildren().setAll(root);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bookService = BookServiceInstance.getInstance();
    }

    public void initData(User user) {
        this.user = user;
        List<Book> booksByUser = this.bookService.getBooksByUserId(user.getId());
        ObservableList<Book> observableList = FXCollections.observableList(booksByUser);
        this.table.setItems(observableList);
    }
}
