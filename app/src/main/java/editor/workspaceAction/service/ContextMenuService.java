package editor.workspaceAction.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;

import java.util.logging.Logger;

@Singleton
public class ContextMenuService {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private WorkspaceAction workspaceAction;

    @Inject
    ContextMenuService(WorkspaceAction workspaceAction) {
        this.workspaceAction = workspaceAction;
    }

    public void CreateContextMenu(Node control){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem cutMenuItem = new MenuItem("Вырезать");
        MenuItem copyMenuItem = new MenuItem("Копировать");
        MenuItem pasteMenuItem = new MenuItem("Вставить");
        contextMenu.getItems().addAll(cutMenuItem,copyMenuItem,pasteMenuItem);

        control.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> {
            contextMenu.show(control.getScene().getWindow(), event.getScreenX(), event.getScreenY());
            event.consume();
        });
        cutMenuItem.setOnAction((event) -> { workspaceAction.cut();});
        copyMenuItem.setOnAction((event) -> {workspaceAction.copy();});
        pasteMenuItem.setOnAction((event) -> { workspaceAction.paste();});
    }
}
