package editor.observable.service;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import editor.editorSpace.model.EditorModel;
import editor.observable.DocumentChangeUpdateObservable;
import editor.observable.DocumentInsertUpdateObservable;
import editor.observable.DocumentRemoveUpdateObservable;
import editor.observable.FileLoadingObservable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@Singleton
public class DocumentDispatcher {

    private EditorModel editorModel;
    private FileLoadingObservable fileLoadingObservable;
    private DocumentChangeUpdateObservable documentChangeUpdateObservable;
    private DocumentInsertUpdateObservable documentInsertUpdateObservable;
    private DocumentRemoveUpdateObservable documentRemoveUpdateObservable;
    @Inject
    DocumentDispatcher(EditorModel editorModel,
                       FileLoadingObservable fileLoadingObservable,
                       DocumentChangeUpdateObservable documentChangeUpdateObservable,
                       DocumentInsertUpdateObservable documentInsertUpdateObservable,
                       DocumentRemoveUpdateObservable documentRemoveUpdateObservable) {
        this.editorModel = editorModel;
        this.fileLoadingObservable = fileLoadingObservable;
        this.documentChangeUpdateObservable = documentChangeUpdateObservable;
        this.documentInsertUpdateObservable = documentInsertUpdateObservable;
        this.documentRemoveUpdateObservable = documentRemoveUpdateObservable;
        setListeners();
    }
    private void setListeners() {
        fileLoadingObservable.addListener(this::setDocumentListener);
    }
    private void setDocumentListener() {
        editorModel.getStyledDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                documentInsertUpdateObservable.notifyDocumentInsertUpdate();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                documentRemoveUpdateObservable.notifyDocumentRemoveUpdate();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                documentChangeUpdateObservable.notifyDocumentChangeUpdate();
            }
        });
    }
}
