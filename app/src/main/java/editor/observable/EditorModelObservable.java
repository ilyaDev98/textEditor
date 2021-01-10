package editor.observable;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import editor.editorSpace.model.EditorModel;
import io.github.stasgora.observetree.Observable;
import io.github.stasgora.observetree.SettableProperty;

import javax.swing.*;

@Singleton
public class EditorModelObservable extends Observable {

	public final SettableProperty<EditorModel> editorModelInited = new SettableProperty<>();
	public final SettableProperty<JTextPane> textPaneInited = new SettableProperty<>();


	@Inject
	public EditorModelObservable() {
		addSubObservable(editorModelInited);
		addSubObservable(textPaneInited);
	}

	/*public void notifyEditorModelChanged(EditorModel editorModel ){
		editorModelChanged.set(editorModel);
		onValueChanged();
		editorModelChanged.notifyListeners();
	}*/
	public void notifyEditorModelInited(EditorModel editorModel){
		editorModelInited.set(editorModel);
		editorModelInited.notifyListeners();
	}
	public void notifyTextPaneInited(JTextPane textPane){
		textPaneInited.set(textPane);
		textPaneInited.notifyListeners();
	}
}
