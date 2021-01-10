package editor.ui.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import editor.fileAction.model.FileChooserAction;
import editor.fileAction.model.FileChooserExtension;
import editor.view.annotation.MainWindowStage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.Optional;

@Singleton
public class UiDialogUtils {

	private Stage window;


	@Inject
	UiDialogUtils(@MainWindowStage Stage window) {
		this.window = window;
	}

	public File showFileChooser(FileChooserAction action, String title, File initialDirectory, FileChooser.ExtensionFilter extensionFilter) {
		FileChooser projectFileChooser = new FileChooser();
		projectFileChooser.setTitle(title);
		if(initialDirectory != null && initialDirectory.isDirectory()){
			projectFileChooser.setInitialDirectory(initialDirectory);
		}
		projectFileChooser.getExtensionFilters().addAll(extensionFilter, getDefaultFilter());

		if (action == FileChooserAction.SAVE_DIALOG) {
			return projectFileChooser.showSaveDialog(window);
		}
		if (action == FileChooserAction.OPEN_DIALOG) {
			return projectFileChooser.showOpenDialog(window);
		}
		return null;
	}

	public Optional<ButtonType> showConfirmDialog(String title, String header, String content, ButtonType[] buttons) {
		return showDialog(Alert.AlertType.CONFIRMATION, title, header, content, buttons);
	}

	public Optional<ButtonType> showWarningDialog(String title, String header, String content, ButtonType[] buttons) {
		return showDialog(Alert.AlertType.WARNING, title, header, content, buttons);
	}

	public Optional<ButtonType> showErrorDialog(String title, String header, String content) {
		return showDialog(Alert.AlertType.ERROR, title, header, content, null);
	}

	private Optional<ButtonType> showDialog(Alert.AlertType type, String title, String header, String content, ButtonType[] buttons) {
		Alert dialog;
		if (buttons != null) {
			dialog = new Alert(type, content, buttons);
		} else {
			dialog = new Alert(type, content);
		}
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		return dialog.showAndWait();
	}

	private FileChooser.ExtensionFilter getDefaultFilter() {
		return new FileChooser.ExtensionFilter(FileChooserExtension.ALL.getTitle(), FileChooserExtension.ALL.getExtension());
	}

}
