package editor.view.sub;
import com.google.inject.name.Named;
import editor.view.model.ViewType;
import javafx.scene.layout.Region;

public interface SubViewFactory {
	@Named("EditorView") SubView buildEditorView(Region root, ViewType viewType);
	@Named("MenuView") SubView buildMenuView(Region root, ViewType viewType);
	@Named("PropertiesView") SubView buildPropertiesView(Region root, ViewType viewType);
}
