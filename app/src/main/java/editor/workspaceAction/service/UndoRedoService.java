package editor.workspaceAction.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import editor.editorSpace.model.EditorModel;
import editor.observable.FileLoadingObservable;
import javax.swing.undo.UndoManager;

@Singleton
public class UndoRedoService {

	private FileLoadingObservable fileLoadingObservable;
	private UndoManager undoManager;
	private EditorModel editorModel;

	@Inject
	UndoRedoService(FileLoadingObservable fileLoadingObservable, EditorModel editorModel) {
		this.fileLoadingObservable = fileLoadingObservable;
		this.editorModel = editorModel;
		undoManager = new UndoManager();
		setListeners();
	}
	public void undo() {
		if (!undoManager.canUndo()) {
			return;
		}
		undoManager.undo();
	}

	public void redo()  {
		if (! undoManager.canRedo()) {
			return;
		}
		undoManager.redo();
	}
	public boolean canUndo(){
		return undoManager.canUndo();
	}

	private void setListeners() {

		fileLoadingObservable.addListener(this::setUndoableEditListener);
	}
	private void setUndoableEditListener() {
		undoManager.discardAllEdits();
		editorModel.getStyledDocument().addUndoableEditListener(undoManager);
	}
}
