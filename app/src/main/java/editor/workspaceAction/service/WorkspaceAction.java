package editor.workspaceAction.service;

import editor.markupList.model.MarkupListType;
import editor.workspaceAction.model.AlignmentType;
import javafx.scene.paint.Color;

public interface WorkspaceAction {

	void undo();
	void redo();

	void cut();
	void copy();
	void paste();

	void setFont(String font);
	void setColor(Color color);
	void setFontSize(int fontSize);
	void setAlign(AlignmentType align);
	void searchWord(String pattern);
	void unSearchWord();
	void insertMarkupList(MarkupListType markupListType);
	void removeMarkupList();
}
