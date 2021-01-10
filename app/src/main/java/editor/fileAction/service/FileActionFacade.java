package editor.fileAction.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import editor.editorSpace.model.EditorModel;
import editor.fileAction.model.FileChooserAction;
import editor.fileAction.model.FileChooserExtension;
import editor.ui.service.UiDialogUtils;
import javafx.application.Platform;
import java.io.*;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import editor.editorSpace.service.EditorService;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javax.swing.text.BadLocationException;

@Singleton
public class FileActionFacade implements FileAction {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final String DEFAULT_TITLE_WINDOW = "Безымянный";

    private final EditorService editorService;
    private final FileExecutor fileExecutor;
    private final UiDialogUtils dialogUtils;
    private final EditorModel editorModel;

    @Inject
    FileActionFacade(EditorService editorService,
                     UiDialogUtils dialogUtils,
                     FileExecutor fileExecutor,
                     EditorModel editorModel ) {
        this.editorService = editorService;
        this.dialogUtils = dialogUtils;
        this.fileExecutor = fileExecutor;
        this.editorModel =editorModel;
    }

    @Override
    public void onCreateProject() {
        if (!editorService.isModifiedFile()){
            editorService.createEmptyProject();
            return;
        }
        if (!isSaveFile("Сохранение")) {
            return;
        }
        editorService.createEmptyProject();

    }

    @Override
    public String getProjectName() {
        if (!editorModel.HasPath()) {
            return DEFAULT_TITLE_WINDOW;
        }
        return fileExecutor.getProjectFileName(editorModel.getFile());
    }
    @Override
    public void onOpenProject() {
        String openTitle = "Открытие";
        if (!editorService.isModifiedFile()){
            openFile(openTitle);
            return;
        }
        if (!isSaveFile("Сохранение")) {
            return;
        }
        openFile(openTitle);

    }

    @Override
    public void onSaveProject() {

        if(!editorModel.HasPath()){
            saveProjectAsNew();
            return;
        }
        try{
            fileExecutor.saveProject(editorModel.getFile());
            showSuccessfulSaveDialog("Сохранение", getProjectName());
        }
        catch (BadLocationException | IOException ex) {
            logger.log(Level.SEVERE, "Failed save project from '" +  editorModel.getFile().getAbsolutePath() + "'", ex);
            showErrorDialog("Сохранение");
        }

    }
    @Override
    public void onSaveProjectAs() {
        saveProjectAsNew();
    }


    private void saveProjectAsNew(){
        File file = showProjectFileChooser(FileChooserAction.SAVE_DIALOG, "Сохранение");
        if(file== null){
            return;
        }
        try{
            fileExecutor.saveProject(file);
            showSuccessfulSaveDialog("Сохранение", getProjectName());
        }
        catch (BadLocationException | IOException ex) {
            logger.log(Level.SEVERE, "Failed save project from '" +  file.getAbsolutePath() + "'", ex);
            showErrorDialog("Сохранение");
        }
    }

    @Override
    public void onExitApp() {

        Platform.exit();
    }

    private void openFile(String title) {
        File location = showProjectFileChooser(FileChooserAction.OPEN_DIALOG, title);
        if (location == null) {
            return;
        }
        try {
            fileExecutor.openProject(location);
        } catch (IOException | BadLocationException e) {
            logger.log(Level.SEVERE, "Failed loading project from '" + location.getAbsolutePath() + "'", e);
            showErrorDialog(title);
        }
    }
    private File showProjectFileChooser(FileChooserAction action, String title) {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(FileChooserExtension.RTF.getTitle(), FileChooserExtension.RTF.getExtension());
        return dialogUtils.showFileChooser(action, title, new File("./"), filter);
    }
    private boolean isSaveFile(String title) {
        ButtonType saveButton = new ButtonType("Сохранить");
        ButtonType discardButton = new ButtonType("Не сохранять");

        String headerText = "Открытый проект '"+ getProjectName() + "' был изменен";
        String contentText = "Вы хотите сохранить или отменить изменения?";

        ButtonType[] buttonTypes = {saveButton, discardButton, ButtonType.CANCEL};
        Optional<ButtonType> response = dialogUtils.showConfirmDialog(title, headerText, contentText, buttonTypes);
        if (!response.isPresent() || response.get() == ButtonType.CANCEL) {
            return false;
        }
        if (response.get() == saveButton) {
            onSaveProject();
        }
        return true;
    }

    private void showErrorDialog(String titleText) {
        dialogUtils.showErrorDialog(titleText,  "Операция не удалась", "Произошла непредвиденная ошибка");
    }
    private void showSuccessfulSaveDialog(String titleText, String fileName) {
        dialogUtils.showConfirmDialog(titleText,  "Операция успешна", "Файл ' "+ fileName+ " ' успешно сохранен", null);
    }
}
