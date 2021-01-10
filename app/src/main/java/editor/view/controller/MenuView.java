package editor.view.controller;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import editor.view.model.ViewType;
import editor.view.service.WindowManager;
import editor.view.sub.SubView;
import editor.fileAction.service.FileAction;
import editor.workspaceAction.service.WorkspaceAction;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Region;

import java.util.logging.Logger;

public class MenuView extends SubView {

    private static final Logger LOGGER = Logger.getLogger(MenuView.class.getName());

    @FXML
    private MenuItem createProjectMenuItem;
    @FXML
    private MenuItem openProjectMenuItem;
    @FXML
    private MenuItem saveProjectMenuItem;
    @FXML
    private MenuItem saveProjectAsMenuItem;
    @FXML
    private MenuItem exitProjectMenuItem;
    @FXML
    private MenuItem viewHelpMenuItem;
    @FXML
    private MenuItem sendReviewMenuItem;
    @FXML
    private MenuItem aboutTheProgramMenuItem;
    @FXML
    private MenuItem undoMenuItem;
    @FXML
    private MenuItem redoMenuItem;
    @FXML
    private MenuItem cutMenuItem;
    @FXML
    private MenuItem copyMenuItem;
    @FXML
    private MenuItem pasteMenuItem;
    @FXML
    private MenuItem searchMenuItem;

    private FileAction fileAction;
    private WorkspaceAction workspaceAction;
    private WindowManager windowManager;
    @Inject
    MenuView(@Assisted Region root, @Assisted ViewType viewType, FileAction fileAction, WorkspaceAction workspaceAction, WindowManager windowManager) {
        super(root, viewType);
        this.fileAction = fileAction;
        this.workspaceAction = workspaceAction;
        this.windowManager = windowManager;
        init();
    }

    @Override
    protected void init() {

        bindMenuItems();
        bindWordSearchMenuItem();
    }
    private void bindMenuItems() {
        createProjectMenuItem.setOnAction(event -> fileAction.onCreateProject());
        openProjectMenuItem.setOnAction(event -> fileAction.onOpenProject());
        saveProjectMenuItem.setOnAction(event -> fileAction.onSaveProject());
        saveProjectAsMenuItem.setOnAction(event -> fileAction.onSaveProjectAs());
        exitProjectMenuItem.setOnAction(event -> fileAction.onExitApp());
        undoMenuItem.setOnAction(event -> workspaceAction.undo());
        redoMenuItem.setOnAction(event -> workspaceAction.redo());
        cutMenuItem.setOnAction(actionEvent -> workspaceAction.cut());
        copyMenuItem.setOnAction(actionEvent -> workspaceAction.copy());
        pasteMenuItem.setOnAction(event -> workspaceAction.paste());
    }

    private void bindWordSearchMenuItem() {

        searchMenuItem.setOnAction(event ->{
            if(windowManager.isWindowOpened(WordSearchWindowView.class)){
                return;
            }
            windowManager.openWordSearchWindow();
        });
    }
}
