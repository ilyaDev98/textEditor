package editor.ui.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import editor.ui.service.UiDialogUtils;

public class UIModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(UiDialogUtils.class).in(Singleton.class);
    }
}
