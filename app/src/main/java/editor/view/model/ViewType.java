package editor.view.model;

public enum ViewType {

	MAIN_VIEW("mainWindow", "mainWindowView"),
	EDITOR_VIEW("editor", "editorView"),
	MENU_VIEW("menu", "menuView"),
	PROPERTIES_VIEW("properties", "propertiesView"),
	WORD_SEARCH_VIEW("wordSearchWindow", "wordSearchWindowView");

	ViewType(String langPrefix, String fxmlFileName) {
		this.langPrefix = langPrefix;
		this.fxmlFileName = fxmlFileName;
	}

	public final String langPrefix;
	public final String fxmlFileName;

}
