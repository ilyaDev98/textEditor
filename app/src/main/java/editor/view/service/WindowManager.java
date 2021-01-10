package editor.view.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import editor.InjectorApp;
import editor.view.controller.WordSearchWindowView;
import editor.view.model.ViewType;
import editor.workspaceAction.service.WorkspaceAction;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Singleton
public class WindowManager {

    private static final Logger LOGGER = Logger.getLogger(WindowManager.class.getName());
    private static final String WORD_SEARCH_WINDOW_NAME = "Найти";

    private List<Object> controllers = new ArrayList<>();

    @Inject
    WindowManager(WorkspaceAction workspaceAction) {

    }

    public void openWordSearchWindow() {

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/" + ViewType.WORD_SEARCH_VIEW.fxmlFileName + ".fxml"));
        try {
            Parent root = fxmlLoader.load();
            WordSearchWindowView wordSearchWindowView = fxmlLoader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(WORD_SEARCH_WINDOW_NAME);

            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.initStyle(StageStyle.UTILITY);

            stage.setOnHidden(e -> {
                wordSearchWindowView.onHidden();
                controllers.remove(wordSearchWindowView);
            });
            stage.show();
            InjectorApp.getInjector().injectMembers(wordSearchWindowView);
            controllers.add(wordSearchWindowView);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public <T> boolean isWindowOpened(T clazz) {
        return controllers.stream().filter(c -> c.getClass() == clazz).count() != 0;
    }
}
