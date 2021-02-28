package editor.markupList.handler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import editor.editorSpace.model.EditorModel;
import javax.swing.text.*;

@Singleton
public class NumberingMarkupListHandler extends MarkupListBaseHandler{

    private static final String NUMBERS_ATTR = "NUMBERS";

    private final EditorModel editorModel;

    @Inject
    public NumberingMarkupListHandler(EditorModel editorModel){
        super(editorModel);
        this.editorModel = editorModel;
    }

    @Override
    public void insertMarkupElement(int elementStartOffset) {
        if (isNumberedElement(elementStartOffset)) {
           return;
        }
        number++;
        insertNumber(elementStartOffset, number);

        int numberedLength = getNumberString(number).length();
        int selectionEnd = editorModel.getTextPane().getSelectionEnd();
        editorModel.getTextPane().setSelectionEnd(selectionEnd + numberedLength);
    }

    @Override
    public void removeMarkupElement(int elementStartOffset) {
        if (!isNumberedElement(elementStartOffset)) {
            return;
        }
        int numberedLength = getNumberLength(elementStartOffset);
        removeMarkup(elementStartOffset,numberedLength );

        int selectionEnd = editorModel.getTextPane().getSelectionEnd();
        editorModel.getTextPane().setSelectionEnd(selectionEnd - numberedLength);
    }

    private void insertNumber(int elementStartOffset, Integer num) {
        try {
            editorModel.getStyledDocument().insertString(elementStartOffset, getNumberString(num), getNumberedAttributes(elementStartOffset, num));
        }
        catch(BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }
    private boolean isNumberedElement(int elementStartOffset) {

        AttributeSet attributeSet = getElementAttributes(elementStartOffset);
        Integer elementNumber = (Integer) attributeSet.getAttribute(NUMBERS_ATTR);

        if ((elementNumber == null) || (!isFirstCharNumber(elementStartOffset))) {
            return false;
        }
        return true;
    }
    private AttributeSet getNumberedAttributes(int elementStartOffset, Integer number) {

        AttributeSet attributeSet = getElementAttributes(elementStartOffset);
        SimpleAttributeSet newSimpleAttribute = new SimpleAttributeSet(attributeSet);
        newSimpleAttribute.addAttribute(NUMBERS_ATTR, number);
        return newSimpleAttribute;
    }
    private AttributeSet getElementAttributes(int pos) {
        
        StyledDocument doc = editorModel.getStyledDocument();
        Element charEle = doc.getCharacterElement(pos);
        return charEle.getAttributes();
    }
    private String getNumberString(Integer nextNumber) {

        return nextNumber.toString() + "." + " ";
    }

    private boolean isFirstCharNumber(int elementStartOffset) {

        if (Character.isDigit(getFirstCharacter(elementStartOffset))) {
            return true;
        }
        return false;
    }
    private char getFirstCharacter(int elementStartOffset) {

        String firstCharacter = "";
        try {
            firstCharacter = editorModel.getTextPane().getText(elementStartOffset, 1);
        }
        catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
        return firstCharacter.charAt(0);
    }

    private int getNumberLength(int elementStartOffset) {

        Integer elementNumber = getElementNumber(elementStartOffset);
        int length = elementNumber.toString().length() + 2; // 2 = dot + space after number
        return length;
    }
    private Integer getElementNumber(int elementStartOffset) {
        AttributeSet attrSet = getElementAttributes(elementStartOffset);
        Integer elementNumber = (Integer) attrSet.getAttribute(NUMBERS_ATTR);
        return elementNumber;
    }
}
