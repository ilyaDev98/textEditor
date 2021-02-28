package editor.markupList.service;

import com.google.inject.Singleton;
import editor.injector.InjectorApp;
import editor.markupList.handler.MarkupListBaseHandler;
import editor.markupList.model.MarkupListActionType;
import editor.markupList.model.MarkupListType;


@Singleton
public class MarkupListService {


    public void insertMarkupList(MarkupListType markupListType) {
        removeMarkupList();
    MarkupListBaseHandler markupListBaseHandler = (MarkupListBaseHandler) InjectorApp.getInjector().getInstance(markupListType.getClaazz());
        markupListBaseHandler.changeMarkupList(MarkupListActionType.INSERT);
}

    public void removeMarkupList() {
        for (MarkupListType markupListType: MarkupListType.values()) {
            MarkupListBaseHandler markupListBaseHandler = (MarkupListBaseHandler) InjectorApp.getInjector().getInstance(markupListType.getClaazz());
            markupListBaseHandler.changeMarkupList(MarkupListActionType.REMOVE);
        }
    }
}
