package editor.workspaceAction.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import editor.fileAction.service.FileAction;
import editor.fileAction.service.FileActionFacade;
import editor.fileAction.service.FileExecutor;
import editor.markupList.service.MarkupListKeyService;
import editor.markupList.handler.BulletsMarkupListHandler;
import editor.markupList.handler.NumberingMarkupListHandler;
import editor.markupList.service.MarkupListService;
import editor.searchWord.service.WordSearchService;
import editor.workspaceAction.service.UndoRedoService;
import editor.workspaceAction.service.*;


public class WorkspaceActionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(FileAction.class).to(FileActionFacade.class);
        bind(FileExecutor.class).in(Singleton.class);

        bind(WorkspaceAction.class).to(WorkspaceActionFacade.class);
        bind(UndoRedoService.class).in(Singleton.class);
        bind(ContextMenuService.class).in(Singleton.class);
        bind(StyleDocumentService.class).in(Singleton.class);
        bind(WordSearchService.class).in(Singleton.class);

        bind(MarkupListService.class).in(Singleton.class);
        bind(MarkupListKeyService.class).in(Singleton.class);
        // handlers
        bind(NumberingMarkupListHandler.class).in(Singleton.class);
        bind(BulletsMarkupListHandler.class).in(Singleton.class);
    }
}