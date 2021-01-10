package editor.view.controller;

import java.util.logging.Logger;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import editor.observable.EditorModelObservable;
import editor.view.model.ViewType;
import editor.view.sub.SubView;
import editor.workspaceAction.service.ContextMenuService;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javax.swing.*;

public class EditorView extends SubView {

    private static final Logger LOGGER = Logger.getLogger(EditorView.class.getName());

    @FXML
    public StackPane stackPane;

    private SwingNode swingNode;

    private JTextPane textPane;

    private EditorModelObservable editorModelObservable;
    private ContextMenuService contextMenuService;
    @Inject
    EditorView(@Assisted Region root,
               @Assisted ViewType viewType,
               EditorModelObservable editorModelObservable,
               ContextMenuService contextMenuService) {
        super(root, viewType);
        this.editorModelObservable = editorModelObservable;
        this.contextMenuService = contextMenuService;
        init();
    }
    @Override
    protected void init() {
        createTextPane();
        createContextMenu();
    }
    public void createTextPane() {
        swingNode = new SwingNode();
        createSwingContent(swingNode);
        stackPane.getChildren().add(swingNode);
    }

    private void createSwingContent(SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                textPane = new JTextPane();
                JScrollPane editorScrollPane = new JScrollPane(textPane);
                SwingUtilities.invokeLater(() -> textPane.requestFocusInWindow());



                swingNode.setContent(editorScrollPane);
                editorModelObservable.notifyTextPaneInited(textPane);
            }
        });
    }
    public JTextPane getTextPane() {
        return textPane;
    }

    public void createContextMenu() {
        contextMenuService.CreateContextMenu(stackPane);
    }
}
