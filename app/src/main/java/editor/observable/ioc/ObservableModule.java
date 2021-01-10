package editor.observable.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import editor.observable.*;
import editor.observable.service.DocumentDispatcher;

public class ObservableModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EditorModelObservable.class).in(Singleton.class);
        bind(FileLoadingObservable.class).in(Singleton.class);
        bind(DocumentInsertUpdateObservable.class).in(Singleton.class);
        bind(DocumentChangeUpdateObservable.class).in(Singleton.class);
        bind(DocumentRemoveUpdateObservable.class).in(Singleton.class);
        bind(DocumentDispatcher.class).in(Singleton.class);
    }
}