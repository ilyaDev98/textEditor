package editor.workspaceAction.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import editor.editorSpace.model.EditorModel;
import editor.markupList.service.MarkupListKeyService;
import editor.markupList.model.MarkupListType;
import editor.markupList.service.MarkupListService;
import editor.searchWord.service.WordSearchService;
import editor.workspaceAction.model.AlignmentType;
import javafx.scene.paint.Color;
import java.util.logging.Logger;

@Singleton
public class WorkspaceActionFacade implements WorkspaceAction {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final UndoRedoService undoRedoService;
    private final EditorModel editorModel;
    private final StyleDocumentService styleDocumentService;
    private final WordSearchService wordSearchService;
    private final MarkupListService markupListService;
    private final MarkupListKeyService markupListKeyService;

    @Inject
    WorkspaceActionFacade(UndoRedoService undoRedoService,
                          EditorModel editorModel,
                          StyleDocumentService styleDocumentService,
                          WordSearchService wordSearchService,
                          MarkupListService markupListService,
                          MarkupListKeyService markupListKeyService) {
        this.undoRedoService = undoRedoService;
        this.editorModel = editorModel;
        this.styleDocumentService = styleDocumentService;
        this.wordSearchService = wordSearchService;
        this.markupListService = markupListService;
        this.markupListKeyService = markupListKeyService;
    }

    @Override
    public void undo() { undoRedoService.undo(); }

    @Override
    public void redo() {
        undoRedoService.redo();
    }

    @Override
    public void cut() {
        editorModel.getTextPane().cut();
    }
    @Override
    public void copy() {
        editorModel.getTextPane().copy();
    }
    @Override
    public void paste()
    {
        editorModel.getTextPane().paste();
    }

    @Override
    public void setFont(String fontName ) { styleDocumentService.setFont(fontName); }

    @Override
    public void setColor(Color color) { styleDocumentService.setColor(color); }

    @Override
    public void setFontSize(int fontSize) { styleDocumentService.setFontSize(fontSize); }

    @Override
    public void setAlign(AlignmentType align) { styleDocumentService.setAlign(align); }

    @Override
    public void searchWord(String pattern) {
        wordSearchService.search(pattern);
    }

    @Override
    public void unSearchWord() {
        wordSearchService.unSearch();
    }

    @Override
    public void insertMarkupList(MarkupListType markupListType) {
        markupListService.insertMarkupList(markupListType);
    }

    @Override
    public void removeMarkupList() {
        markupListService.removeMarkupList();
    }
}
