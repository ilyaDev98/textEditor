package editor.markupList;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class NumbersMarkupKeyListener implements KeyListener {

    private static final char BULLET_CHAR = '\u2022';
    private static final String NUMBERS_ATTR = "NUMBERS";

    private static final String BULLET_STR = new String(new char [] {BULLET_CHAR});
    private static final String BULLET_STR_WITH_SPACE = BULLET_STR + " ";
    private static final int BULLET_LENGTH = BULLET_STR_WITH_SPACE.length();

    private JTextPane textPane;

    private boolean numberedPara_;
    private boolean startPosPlusNum__;
    private String prevParaText_;
    private int prevParaEleStart_;

    public NumbersMarkupKeyListener(JTextPane textPane) {

        this.textPane = textPane;
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        String selectedText = textPane.getSelectedText();

        if ((selectedText == null) || (selectedText.trim().isEmpty())) {

            // continue, processing key press without any selected text
        } else {
            // text is selected within numbered para and a key is pressed
            doReplaceSelectionRoutine();
            return;
        }

        numberedPara_ = false;
        int pos = textPane.getCaretPosition();

        if (!isNumberedParaForPos(pos)) {

            return;
        }

        Element paraEle = textPane.getStyledDocument().getParagraphElement(pos);
        int paraEleStart = paraEle.getStartOffset();

        switch (e.getKeyCode()) {

            case KeyEvent.VK_LEFT: // same as that of VK_KP_LEFT
            case KeyEvent.VK_KP_LEFT:
                int newPos = pos -
                        (getNumberLength(paraEleStart) + 1);
                doLeftArrowKeyRoutine(newPos, startPosPlusNum__);
                break;
            case KeyEvent.VK_DELETE:
                doDeleteKeyRoutine(paraEle, pos);
                break;
            case KeyEvent.VK_BACK_SPACE:
                doBackspaceKeyRoutine(paraEle);
                break;
            case KeyEvent.VK_ENTER:
                getPrevParaDetails(pos);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (! numberedPara_) {

            return;
        }

        switch (e.getKeyCode()) {

            case KeyEvent.VK_ENTER: doEnterKeyRoutine();
                break;
        }
    }
    private void doEnterKeyRoutine() {

        String prevParaText = prevParaText_;
        int prevParaEleStart = prevParaEleStart_;
        int len = getNumberLength(prevParaEleStart) + 1; // +1 for CR

        // Check if prev para with numbers has text
        if (prevParaText.length() == len) {

            // Para has numbers and no text, remove number from para
            removeNumber(prevParaEleStart, len);
            textPane.setCaretPosition(prevParaEleStart);
            return;
        }
        // Prev para with number and text,
        // insert number for new para (current position)
        Integer num = getParaNumber(prevParaEleStart);
        num++;
        insertNumber(textPane.getCaretPosition(), prevParaEleStart, num);

        // After insert, check for numbered paras following the newly
        // inserted numberd para; and re-number those paras.

        // Get newly inserted number para details
        StyledDocument doc = textPane.getStyledDocument();
        Element newParaEle = doc.getParagraphElement(textPane.getCaretPosition());
        int newParaEleEnd = newParaEle.getEndOffset();

        if (newParaEleEnd > doc.getLength()) {

            return; // no next para, end of document text
        }

        // Get next para (following the newly inserted one) and
        // re-number para only if already numered.
        Element nextParaEle = doc.getParagraphElement(newParaEleEnd + 1);
        int nextParaEleStart = nextParaEle.getStartOffset();

        if (isNumberedPara(nextParaEleStart)) {

            doNewNumbers(nextParaEleStart, num);
        }

    } // doEnterKeyRoutine()

    private void insertNumber(int insertPos, int attributesPos, Integer num) {

        try {
            textPane.getStyledDocument().insertString(insertPos,
                    getNumberString(num),
                    getNumbersAttributes(attributesPos, num));
        }
        catch(BadLocationException ex) {

            throw new RuntimeException(ex);
        }
    }
    private void getPrevParaDetails(int pos) {

        pos =  pos - 1;

        if (isNumberedParaForPos(pos)) {

            numberedPara_ = true;
            Element paraEle = textPane.getStyledDocument().getParagraphElement(pos);
            prevParaEleStart_ = paraEle.getStartOffset();
            prevParaText_ = getPrevParaText(prevParaEleStart_, paraEle.getEndOffset());
        }
    }
    private void doBackspaceKeyRoutine(Element paraEle) {

        // In case the position of cursor at the backspace is just after
        // the number: remove the number and re-number the following ones.
        if (startPosPlusNum__) {

            int startOffset = paraEle.getStartOffset();
            removeNumber(startOffset, getNumberLength(startOffset));
            doReNumberingForBackspaceKey(paraEle, startOffset);
            startPosPlusNum__ = false;
        }
    }

    private void doReNumberingForBackspaceKey(Element paraEle, int paraEleStart) {

        // Get bottom para element and check if numbered.
        StyledDocument doc = textPane.getStyledDocument();
        Element bottomParaEle = doc.getParagraphElement(paraEle.getEndOffset() + 1);
        int bottomParaEleStart = bottomParaEle.getStartOffset();

        if (! isNumberedPara(bottomParaEleStart)) {

            return; // there are no numbers following this para, and
            // no re-numbering required.
        }

        // Get top para element and number

        Integer numTop = null;

        if (paraEleStart == 0) {

            // beginning of document, no top para exists
            // before the document start; numTop = null
        }
        else {
            Element topParaEle = doc.getParagraphElement(paraEleStart - 1);
            numTop = getParaNumber(topParaEle.getStartOffset());
        }

        if (numTop == null) {

            // There are no numbered items above the removed para, and
            // there are numbered items following the removed para;
            // bottom numbers start from 1.
            doNewNumbers(bottomParaEleStart, 0);
        }
        else {
            // numTop != null
            // There are numbered items above the removed para, and
            // there are numbered items following the removed para;
            // bottom numbers start from numTop + 1.
            doNewNumbers(bottomParaEleStart, numTop);
        }
    }
    private void removeNumber(int removePos, int length) {

        try {
            textPane.getStyledDocument().remove(removePos, length);
        }
        catch(BadLocationException ex) {

            throw new RuntimeException(ex);
        }
    }
    private String getPrevParaText(int prevParaEleStart, int prevParaEleEnd) {

        String prevParaText = "";

        try {
            prevParaText = textPane.getStyledDocument().getText(prevParaEleStart,
                    (prevParaEleEnd -  prevParaEleStart));
        }
        catch(BadLocationException ex) {

            throw new RuntimeException(ex);
        }

        return prevParaText;
    }
    private void doDeleteKeyRoutine(Element paraEle, int pos) {

        int paraEleEnd = paraEle.getEndOffset();

        if (paraEleEnd > textPane.getStyledDocument().getLength()) {

            return; // no next para, end of document text
        }

        if (pos == (paraEleEnd - 1)) { // last char of para; -1 is for CR

            if (isBulletedParaForPos(paraEleEnd + 1)) {

                // following para is bulleted, remove
                removeBullet(pos, BULLET_LENGTH);
            }
            // else, not a bulleted para
            // delete happens normally (one char)
        }
    }
    private void removeBullet(int removePos, int length) {

        try {
            textPane.getStyledDocument().remove(removePos, length);
        }
        catch(BadLocationException ex) {

            throw new RuntimeException(ex);
        }
    }
    private boolean isBulletedParaForPos(int caretPos) {

        Element paraEle = textPane.getStyledDocument().getParagraphElement(caretPos);

        if (isBulletedPara(paraEle.getStartOffset())) {

            return true;
        }

        return false;
    }
    private boolean isBulletedPara(int paraEleStart) {

        if (getParaFirstCharacter(paraEleStart) == BULLET_CHAR) {

            return true;
        }

        return false;
    }
    private void doLeftArrowKeyRoutine(int pos, boolean startTextPos) {

        if (! startTextPos) {

            return;
        }

        // Check if this is start of document
        Element paraEle =
                textPane.getStyledDocument().getParagraphElement(textPane.getCaretPosition());
        int newPos = (paraEle.getStartOffset() == 0) ? 0 : pos;

        // Position the caret in an EDT, otherwise the caret is
        // positioned at one less position than intended.
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                textPane.setCaretPosition(newPos);
            }
        });
    }
    private int getNumberLength(int paraEleStart) {

        Integer num = getParaNumber(paraEleStart);
        int len = num.toString().length() + 2; // 2 = dot + space after number
        return len;
    }
    private boolean isNumberedParaForPos(int caretPos) {

        Element paraEle = textPane.getStyledDocument().getParagraphElement(caretPos);

        if (isNumberedPara(paraEle.getStartOffset())) {

            return true;
        }

        return false;
    }
    private void doReplaceSelectionRoutine() {

        // Get selection start and end para details.
        // Check if there are numbered paras at top and bottom
        // of the selection. Re-number if needed i.e., when selection
        // is replaced in the middle of numbered paras or at the top
        // items of the numbered paras.

        StyledDocument doc =textPane.getStyledDocument();
        Element topParaEle = doc.getParagraphElement(textPane.getSelectionStart());
        Element bottomParaEle = doc.getParagraphElement(textPane.getSelectionEnd());

        int bottomParaEleStart = bottomParaEle.getStartOffset();
        int bottomParaEleEnd = bottomParaEle.getEndOffset();

        // No numbered text at bottom, no processing required -or-
        // no next para after selection end (end of document text).
        if ((! isNumberedPara(bottomParaEleStart)) ||
                (bottomParaEleEnd > doc.getLength())) {

            return;
        }

        // Check if para following the selection end is numbered or not.
        Element paraEle = doc.getParagraphElement(bottomParaEleEnd + 1);
        int paraEleStart = paraEle.getStartOffset();

        if (! isNumberedPara(paraEleStart)) {
            return;
        }

        // Process re-numbering

        Integer numTop = getParaNumber(topParaEle.getStartOffset());

        if (numTop != null) {

            // There are numbered items above the removed para, and
            // there are numbered items following the removed para;
            // bottom numbers start from numTop + 1.
            doNewNumbers(paraEleStart, numTop);
        }
        else {
            // numTop == null
            // There are no numbered items above the removed para, and
            // there are numbered items following the removed para;
            // bottom numbers start from 1.
            doNewNumbers(paraEleStart, 0);
        }

    } // doReplaceSelectionRoutine()
    private void doNewNumbers(int nextParaEleStart, Integer newNum) {

        StyledDocument doc = textPane.getStyledDocument();
        Element nextParaEle = doc.getParagraphElement(nextParaEleStart);
        boolean nextParaIsNumbered = true;

        NUMBERED_PARA_LOOP:
        while (nextParaIsNumbered) {

            Integer oldNum = getParaNumber(nextParaEleStart);
            newNum++;
            replaceNumbers(nextParaEleStart, oldNum, newNum);

            nextParaIsNumbered = false;

            // Get following para details after number is replaced for a para

            int nextParaEleEnd = nextParaEle.getEndOffset();
            int nextParaPos = nextParaEleEnd + 1;

            if (nextParaPos > doc.getLength()) {

                break NUMBERED_PARA_LOOP; // no next para, end of document text
            }

            nextParaEle = doc.getParagraphElement(nextParaPos);
            nextParaEleStart = nextParaEle.getStartOffset();
            nextParaIsNumbered = isNumberedPara(nextParaEleStart);
        }
        // NUMBERED_PARA_LOOP

    } // doNewNumbers()
    private boolean isNumberedPara(int paraEleStart) {

        AttributeSet attrSet = getParaStartAttributes(paraEleStart);
        Integer paraNum = (Integer) attrSet.getAttribute(NUMBERS_ATTR);

        if ((paraNum == null) || (! isFirstCharNumber(paraEleStart))) {

            return false;
        }

        return true;
    }
    private void replaceNumbers(int nextParaEleStart, Integer prevNum,
                                Integer newNum) {

        try {
            ((DefaultStyledDocument) textPane.getStyledDocument()).replace(
                    nextParaEleStart,
                    getNumberString(prevNum).length(),
                    getNumberString(newNum),
                    getNumbersAttributes(nextParaEleStart, newNum));
        }
        catch(BadLocationException ex) {

            throw new RuntimeException(ex);
        }
    }
    private AttributeSet getNumbersAttributes(int paraEleStart, Integer number) {

        AttributeSet attrs1 = getParaStartAttributes(paraEleStart);
        SimpleAttributeSet attrs2 = new SimpleAttributeSet(attrs1);
        attrs2.addAttribute(NUMBERS_ATTR, number);
        return attrs2;
    }
    private String getNumberString(Integer nextNumber) {

        return new String(nextNumber.toString() + "." + " ");
    }
    private AttributeSet getParaStartAttributes(int pos) {

        StyledDocument doc =  textPane.getStyledDocument();
        Element	charEle = doc.getCharacterElement(pos);
        return charEle.getAttributes();
    }
    private Integer getParaNumber(int paraEleStart) {

        AttributeSet attrSet = getParaStartAttributes(paraEleStart);
        Integer paraNum = (Integer) attrSet.getAttribute(NUMBERS_ATTR);
        return paraNum;
    }
    private boolean isFirstCharNumber(int paraEleStart) {

        if (Character.isDigit(getParaFirstCharacter(paraEleStart))) {
            return true;
        }
        return false;
    }
    private char getParaFirstCharacter(int paraEleStart) {

        String firstChar = "";
        try {
            firstChar = textPane.getText(paraEleStart, 1);
        }
        catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
        return firstChar.charAt(0);
    }
}
