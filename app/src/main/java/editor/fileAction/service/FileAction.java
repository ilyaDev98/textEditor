package editor.fileAction.service;

public interface FileAction {

	void onCreateProject();

	String getProjectName();

	void onOpenProject();

	void onSaveProject();

	void onSaveProjectAs();

	void onExitApp();
}
