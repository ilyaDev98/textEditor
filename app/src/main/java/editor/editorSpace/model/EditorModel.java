package editor.editorSpace.model;

import com.google.inject.Singleton;
import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.io.File;

@Singleton
public class EditorModel {

    public static final String DEFAULT_FONT = "SansSerif";
    public static final int DEFAULT_FONT_SIZE = 18;

    private File file;

    private JTextPane textPane;

    private String fontName;
    private int fontSize;

    public JTextPane getTextPane() {
        return textPane;
    }

    public void setTextPane(JTextPane textPane) {
       this.textPane = textPane;
    }
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public StyledDocument getStyledDocument() {
        return textPane.getStyledDocument();
    }
    public void setStyledDocument(StyledDocument doc){
        textPane.setDocument(doc);
    }
    public boolean HasPath(){
        return file != null;
    }

    public String getFontName() {
        return fontName;
    }
    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }
}
