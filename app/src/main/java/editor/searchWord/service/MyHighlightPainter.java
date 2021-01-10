package editor.searchWord.service;

import javax.swing.text.DefaultHighlighter;
import java.awt.*;

public class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
    /**
     * Constructs a new highlight painter. If <code>c</code> is null,
     * the JTextComponent will be queried for its selection color.
     *
     * @param c the color for the highlight
     */
    public MyHighlightPainter(Color c) {
        super(c);
    }
}
