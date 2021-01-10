package editor.searchWord.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import editor.editorSpace.model.EditorModel;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.logging.Logger;

@Singleton
public class WordSearchService {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private static final Color HIGHLIGHT_COLOR = Color.yellow;

    private EditorModel editorModel;
    private MyHighlightPainter myHighlightPainter;

    @Inject
    WordSearchService(EditorModel editorModel) {
        this.editorModel = editorModel;
        myHighlightPainter = new MyHighlightPainter(HIGHLIGHT_COLOR);
    }

    public void search(String pattern) {
        highlight(editorModel.getTextPane(), pattern);
    }
    public void unSearch() {
        removeHighlight(editorModel.getTextPane());
    }

    private void highlight(JTextComponent textComponent, String pattern) {
        removeHighlight(textComponent);

        try {
            Highlighter highlighter = textComponent.getHighlighter();
            Document document = textComponent.getDocument();
            String text = document.getText(0, document.getLength());
            int pos = 0;

            while ((pos = text.toUpperCase().indexOf(pattern.toUpperCase(), pos))>=0){
                highlighter.addHighlight(pos, pos + pattern.length(), myHighlightPainter);
                pos +=pattern.length();
            }
        }
        catch (Exception ex){
        }
    }

    private void removeHighlight(JTextComponent textComponent) {
        Highlighter highlighter = textComponent.getHighlighter();
        Highlighter.Highlight[] highlights = highlighter.getHighlights();
        for(int i=0; i< highlights.length; i++){
            if(highlights[i].getPainter() instanceof MyHighlightPainter){
                highlighter.removeHighlight(highlights[i]);
            }
        }

    }


}
