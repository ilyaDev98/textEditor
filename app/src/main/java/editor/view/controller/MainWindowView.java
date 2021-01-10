package editor.view.controller;
import java.util.logging.Logger;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import editor.observable.FileLoadingObservable;
import editor.view.annotation.MainWindowRoot;
import editor.view.annotation.MainWindowStage;
import editor.fileAction.service.FileAction;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;


@Singleton
public class MainWindowView {

    private static final Logger LOGGER = Logger.getLogger(MainWindowView.class.getName());

    private static final String TEXT_EDITOR_NAME = "Текстовый редактор";

    @FXML
    private StackPane editorView;
    @FXML
    private MenuBar menuView;
    @FXML
    private Pane propertiesView;

    private Stage stage;
    private FileLoadingObservable fileLoadingObservable;
    private FileAction workspaceAction;

    @Inject
    void init(@MainWindowStage Stage stage, @MainWindowRoot Parent windowRoot, FileAction workspaceAction, FileLoadingObservable fileLoadingObservable) {
        this.stage = stage;
        this.workspaceAction = workspaceAction;
        this.fileLoadingObservable = fileLoadingObservable;
        setWindowTitle();
        setListeners();
        createWindowScene(windowRoot);
    }

    private void setListeners() {

        fileLoadingObservable.addListener(this::setWindowTitle);
    }
    private void setWindowTitle() {

        String title = workspaceAction.getProjectName() + " - " + TEXT_EDITOR_NAME;
        stage.setTitle(title);
    }

    private void createWindowScene(Parent windowRoot) {
        Scene scene = new Scene(windowRoot);
        stage.setScene(scene);
        stage.setOnHidden(e -> Platform.exit());

    }
    public StackPane getEditorView(){
        return editorView;
    }
    public MenuBar getMenuView(){
        return menuView;
    }

    public Pane getPropertiesView(){
        return propertiesView;
    }
}
