package editor.editorSpace.service;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import editor.observable.FileLoadingObservable;
import editor.observable.EditorModelObservable;
import editor.editorSpace.model.EditorModel;
import editor.observable.service.DocumentDispatcher;
import editor.workspaceAction.service.UndoRedoService;
import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import java.util.logging.Logger;

@Singleton
public class EditorService {

    private static final Logger logger = Logger.getLogger(EditorService.class.getName());

    private EditorModelObservable editorModelObservable;
    private EditorModel editorModel;
    private FileLoadingObservable fileLoadingObservable;
    private UndoRedoService undoRedoService;

    @Inject
    EditorService(EditorModelObservable editorModelObservable, EditorModel editorModel, FileLoadingObservable fileLoadingObservable, UndoRedoService undoRedoService, DocumentDispatcher documentDispatcher) {
        this.editorModelObservable = editorModelObservable;
        this.editorModel = editorModel;
        this.fileLoadingObservable = fileLoadingObservable;
        this.undoRedoService = undoRedoService;
        setListeners();
    }

    private void setListeners() {
        editorModelObservable.textPaneInited.addListener(this::onTextPaneInited);
    }
    private void onTextPaneInited() {
        createEmptyProject();

    }
    public void createEmptyProject(){
        JTextPane pane = editorModelObservable.textPaneInited.get();
        pane.setText("");
        pane.setDocument(new DefaultStyledDocument());
        editorModel.setTextPane(pane);
        editorModel.setFile(null);
        fileLoadingObservable.notifyFileLoading();
        editorModelObservable.notifyEditorModelInited(editorModel);

    }

    public boolean isModifiedFile(){
       return undoRedoService.canUndo();
    }
}
