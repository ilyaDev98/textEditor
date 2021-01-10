package editor.editorSpace.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import editor.observable.DocumentChangeUpdateObservable;
import editor.observable.DocumentInsertUpdateObservable;
import editor.observable.FileLoadingObservable;
import editor.observable.EditorModelObservable;
import editor.editorSpace.model.EditorModel;
import editor.editorSpace.service.EditorService;
import editor.observable.service.DocumentDispatcher;

public class EditorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EditorService.class).in(Singleton.class);
        bind(EditorModel.class).in(Singleton.class);
    }
}
