package editor.markupList.handler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import editor.editorSpace.model.EditorModel;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;

@Singleton
public class BulletsMarkupListHandler extends MarkupListBaseHandler {

    private static final char BULLET_CHAR = '\u2022';
    private static final String BULLET_STR = new String(new char [] {BULLET_CHAR});
    private static final String BULLET_STR_WITH_SPACE = BULLET_STR + " ";
    private static final int BULLET_LENGTH = BULLET_STR_WITH_SPACE.length();

    private final EditorModel editorModel;

    @Inject
    public BulletsMarkupListHandler(EditorModel editorModel){
        super(editorModel);
        this.editorModel = editorModel;
    }

    @Override
    public void insertMarkupElement(int elementStartOffset) {
        if (isBulletedPara(elementStartOffset)) {
           return;
        }
        insertBullet(elementStartOffset);
        int selectionEnd = editorModel.getTextPane().getSelectionEnd();
        editorModel.getTextPane().setSelectionEnd(selectionEnd + BULLET_LENGTH);
    }

    @Override
    public void removeMarkupElement(int elementStartOffset) {
        if (!isBulletedPara(elementStartOffset)) {
            return;
        }
        removeMarkup(elementStartOffset, BULLET_LENGTH);
        int selectionEnd = editorModel.getTextPane().getSelectionEnd();
        editorModel.getTextPane().setSelectionEnd(selectionEnd - BULLET_LENGTH);
    }

    private void insertBullet(int insertPos) {

        try {
            editorModel.getStyledDocument().insertString(insertPos, BULLET_STR_WITH_SPACE, getParaStartAttributes(insertPos));
        }
        catch(BadLocationException ex) {

            throw new RuntimeException(ex);
        }
    }

    private AttributeSet getParaStartAttributes(int pos) {

        StyledDocument doc = editorModel.getStyledDocument();
        Element charEle = doc.getCharacterElement(pos);
        return charEle.getAttributes();
    }
    private boolean isBulletedPara(int paraEleStart) {

        if (getParaFirstCharacter(paraEleStart) == BULLET_CHAR) {
            return true;
        }
        return false;
    }
    private char getParaFirstCharacter(int paraEleStart) {

        String firstChar = "";
        try {
            firstChar = editorModel.getTextPane().getText(paraEleStart, 1);
        }
        catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
        return firstChar.charAt(0);
    }
}
