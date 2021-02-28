package editor.markupList.handler;

import editor.editorSpace.model.EditorModel;
import editor.markupList.model.MarkupListActionType;
import javax.swing.*;
import javax.swing.text.*;


public abstract class MarkupListBaseHandler {

    private final EditorModel editorModel;

    protected int number;

    MarkupListBaseHandler(EditorModel editorModel) {
        this.editorModel = editorModel;
    }

    public void changeMarkupList(MarkupListActionType markupListActionType) {


        JTextPane textPane = editorModel.getTextPane();
        StyledDocument doc = editorModel.getStyledDocument();

        String selectedText = textPane.getSelectedText();

        if ((selectedText == null) || (selectedText.trim().isEmpty())) {
            return;
        }

        int selectionEnd = textPane.getSelectionEnd();

        Element element = doc.getParagraphElement(textPane.getSelectionStart());
        int elementStartOffset = element.getStartOffset();
        int elementEndOffset;

        int deltaElementEndOffset;
        number = 0;

        do {
            element = doc.getParagraphElement(elementStartOffset);
            elementEndOffset = element.getEndOffset();
            deltaElementEndOffset = elementEndOffset;

            if ((elementEndOffset - elementStartOffset) <= 1) { // empty line

                elementStartOffset = elementEndOffset;

                continue;
            }
            applyMarkupAction(markupListActionType, elementStartOffset);

            // Get the updated para element details after numbering
            element = doc.getParagraphElement(elementStartOffset);
            elementEndOffset = element.getEndOffset();
            elementStartOffset = elementEndOffset;

            deltaElementEndOffset = elementEndOffset - deltaElementEndOffset;
            selectionEnd += deltaElementEndOffset;

        } while (elementEndOffset <= selectionEnd);
    }

    private void applyMarkupAction(MarkupListActionType markupListActionType, int elementStartOffset){

        switch (markupListActionType) {
            case INSERT:
                insertMarkupElement(elementStartOffset);
                break;
            case REMOVE:
                removeMarkupElement(elementStartOffset);
        }
    }

    protected void removeMarkup(int removePos, int length) {

        try {
            editorModel.getStyledDocument().remove(removePos, length);
        }
        catch(BadLocationException ex) {

            throw new RuntimeException(ex);
        }
    }
    protected abstract void insertMarkupElement(int elementStartOffset);

    protected abstract void removeMarkupElement(int elementStartOffset);

}
