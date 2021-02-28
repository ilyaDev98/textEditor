package editor;

import com.google.inject.Guice;
import com.google.inject.Injector;
import editor.injector.InjectorApp;
import editor.observable.ioc.ObservableModule;
import editor.ui.ioc.UIModule;
import editor.editorSpace.ioc.EditorModule;
import editor.view.controller.MainWindowView;
import editor.view.ioc.WindowModule;
import editor.view.model.ViewType;
import editor.view.sub.SubViewFactory;
import editor.workspaceAction.ioc.WorkspaceActionModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class TextEditor extends Application {

    private MainWindowView mainView;
    private Injector injector;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + ViewType.MAIN_VIEW.fxmlFileName + ".fxml"));
        Parent root = loader.load();
        mainView = loader.getController();

        InitModules(root,stage);
        initViews();

        stage.requestFocus();
        stage.show();
    }

    private void InitModules(Parent root, Stage stage){
        injector = Guice.createInjector(
                new WindowModule(root, stage),
                new WorkspaceActionModule(),
                new EditorModule(),
                new ObservableModule(),
                new UIModule());
        InjectorApp.SetInjector(injector);
    }
    private void initViews() {
        injector.injectMembers(mainView);
        SubViewFactory subViewFactory = injector.getInstance(SubViewFactory.class);
        subViewFactory.buildEditorView(mainView.getEditorView(), ViewType.EDITOR_VIEW);
        subViewFactory.buildMenuView(mainView.getMenuView(), ViewType.MENU_VIEW);
        subViewFactory.buildPropertiesView(mainView.getPropertiesView(), ViewType.PROPERTIES_VIEW);

    }
}