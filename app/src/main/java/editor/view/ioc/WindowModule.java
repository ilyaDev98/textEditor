package editor.view.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import editor.editorSpace.service.EditorService;
import editor.view.controller.EditorView;
import editor.view.annotation.MainWindowRoot;
import editor.view.annotation.MainWindowStage;
import editor.view.controller.MenuView;
import editor.view.controller.PropertiesView;
import editor.view.service.WindowManager;
import editor.view.sub.SubView;
import editor.view.sub.SubViewFactory;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class WindowModule extends AbstractModule {

    private final Parent mainWindowRoot;
    private final Stage mainWindowStage;

    public WindowModule(Parent mainWindowRoot, Stage mainWindowStage) {
        this.mainWindowRoot = mainWindowRoot;
        this.mainWindowStage = mainWindowStage;
    }

    @Override
    protected void configure() {
        bind(WindowManager.class).in(Singleton.class);
        install(new FactoryModuleBuilder()
                .implement(SubView.class, Names.named("EditorView"), EditorView.class)
                .implement(SubView.class, Names.named("MenuView"), MenuView.class)
                .implement(SubView.class, Names.named("PropertiesView"), PropertiesView.class)
                .build(SubViewFactory.class));
}


    @MainWindowRoot
    @Provides @Singleton
    Parent mainWindowRoot() {
        return mainWindowRoot;
    }

    @MainWindowStage
    @Provides @Singleton
    Stage mainWindowStage() {
        return mainWindowStage;
    }

}
