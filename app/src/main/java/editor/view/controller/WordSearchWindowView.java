package editor.view.controller;

import com.google.inject.Inject;
import editor.workspaceAction.service.WorkspaceAction;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WordSearchWindowView {

    @FXML
    private Button wordSearchButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField wordSearchField;

    private WorkspaceAction workspaceAction;

    @Inject
    void init( WorkspaceAction workspaceAction) {
        this.workspaceAction = workspaceAction;
        bindButtons();
    }
    public void onHidden() {
        workspaceAction.unSearchWord();
    }

    private void bindButtons() {
        wordSearchButton.setOnAction(event -> workspaceAction.searchWord(wordSearchField.getText()));
        cancelButton.setOnAction(event ->close());
    }
    private void close(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

}
