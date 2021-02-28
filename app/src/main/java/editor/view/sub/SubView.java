package editor.view.sub;

import editor.TextEditor;
import editor.view.model.ViewType;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class SubView {

	private static final Logger LOGGER = Logger.getLogger(SubView.class.getName());

	protected final Region root;
	protected ViewType viewType;

	public SubView(Region root, ViewType viewType) {
		this.root = root;
		this.viewType = viewType;
		loadView();
	}

	private void loadView() {
		FXMLLoader loader = new FXMLLoader(TextEditor.class.getResource("/fxml/" + viewType.fxmlFileName + ".fxml"));
		loader.setRoot(root);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Loading " + viewType.fxmlFileName + " component failed", e);
		}
	}

	protected abstract void init();

}
