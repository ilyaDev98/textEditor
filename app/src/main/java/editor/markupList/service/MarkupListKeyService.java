package editor.markupList.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import editor.editorSpace.model.EditorModel;
import editor.markupList.NumbersMarkupKeyListener;
import editor.observable.EditorModelObservable;

@Singleton
public class MarkupListKeyService {

    private final EditorModel editorModel;
    private final EditorModelObservable editorModelObservable;


    @Inject
    MarkupListKeyService(EditorModel editorModel,
                         EditorModelObservable editorModelObservable) {
        this.editorModel = editorModel;
        this.editorModelObservable = editorModelObservable;
        setListeners();

    }
    private void setListeners() {
        editorModelObservable.editorModelInited.addListener(this::onEditorModelInited);


    }
    private void onEditorModelInited() {
        editorModel.getTextPane().addKeyListener(new NumbersMarkupKeyListener(editorModel.getTextPane()));
    }


}
