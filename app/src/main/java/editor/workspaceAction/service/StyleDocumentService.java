package editor.workspaceAction.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import editor.editorSpace.model.EditorModel;
import editor.observable.EditorModelObservable;
import editor.observable.FileLoadingObservable;
import editor.workspaceAction.model.AlignmentType;
import javafx.scene.paint.Color;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.util.logging.Logger;

@Singleton
public class StyleDocumentService {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final EditorModel editorModel;
    private EditorModelObservable editorModelObservable;
    private FileLoadingObservable fileLoadingObservable;

    @Inject
    StyleDocumentService(EditorModel editorModel,
                         EditorModelObservable editorModelObservable,
                         FileLoadingObservable fileLoadingObservable) {
        this.editorModel = editorModel;
        this.editorModelObservable = editorModelObservable;
        this.fileLoadingObservable = fileLoadingObservable;
        setListeners();
    }

    private void setListeners() {
        editorModelObservable.editorModelInited.addListener(this::onEditorModelInited);
        fileLoadingObservable.addListener(this::onfileLoading);


    }
    private void onEditorModelInited() {
        setFont(EditorModel.DEFAULT_FONT);
        setFontSize(EditorModel.DEFAULT_FONT_SIZE);
    }

    private void onfileLoading() {
        setFont(editorModel.getFontName());
        setFontSize(editorModel.getFontSize());
    }

    public void setFont(String fontName ) {
        SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
        StyleConstants.setFontFamily(simpleAttributeSet,  fontName);
        editorModel.setFontName(fontName);
        editorModel.getTextPane().setCharacterAttributes(simpleAttributeSet, false);

    }

    public void setColor(Color color) {
        java.awt.Color awtColor = new java.awt.Color(
                (float) color.getRed(),
                (float) color.getGreen(),
                (float) color.getBlue(),
                (float) color.getOpacity());
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setForeground(attr,  awtColor);
        editorModel.getTextPane().setCharacterAttributes(attr, false);
    }

    public void setFontSize(int fontSize) {
        SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
        StyleConstants.setFontSize(simpleAttributeSet,  fontSize);
        editorModel.setFontSize(fontSize);
        editorModel.getTextPane().setCharacterAttributes(simpleAttributeSet, false);
    }


    public void setAlign(AlignmentType align) {
        SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
        StyleConstants.setAlignment(simpleAttributeSet,  align.getCode());
        StyleConstants.setFontFamily(simpleAttributeSet,  editorModel.getFontName());
        StyleConstants.setFontSize(simpleAttributeSet,  editorModel.getFontSize());
        editorModel.getTextPane().setParagraphAttributes(simpleAttributeSet, false);

    }
}
