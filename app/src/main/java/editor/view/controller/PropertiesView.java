package editor.view.controller;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import editor.editorSpace.model.EditorModel;
import editor.observable.DocumentChangeUpdateObservable;
import editor.observable.DocumentInsertUpdateObservable;
import editor.view.model.ViewType;
import editor.view.service.WindowManager;
import editor.view.sub.SubView;
import editor.workspaceAction.model.AlignmentType;
import editor.workspaceAction.service.WorkspaceAction;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Logger;

public class PropertiesView extends SubView {

    private static final Logger LOGGER = Logger.getLogger(PropertiesView.class.getName());

    private static final String [] FONT_SIZES  = {"4", "6", "8", "10", "12", "14", "16", "18", "20", "22", "24", "26", "28", "30", "32", "34", "36", "48", "72"};
    @FXML
    public ComboBox<String> editorFonts;
    @FXML
    public ComboBox<String> sizeFonts;
    @FXML
    public Button undoButton;
    @FXML
    public Button redoButton;
    @FXML
    public ColorPicker colorPicker;
    @FXML
    public Button leftAlignButton;
    @FXML
    public Button centerAlignButton;
    @FXML
    public Button rightAlignButton;
    @FXML
    public Button wordSearchButton;

    private WorkspaceAction workspaceAction;
    private EditorModel editorModel;
    private DocumentChangeUpdateObservable documentChangeObservable;
    private DocumentInsertUpdateObservable documentInsertObservable;
    private WindowManager windowManager;
    @Inject
    PropertiesView(@Assisted Region root,
                   @Assisted ViewType viewType,
                   WorkspaceAction workspaceAction,
                   EditorModel editorModel,
                   DocumentChangeUpdateObservable documentChangeObservable,
                   DocumentInsertUpdateObservable documentInsertObservable,
                   WindowManager windowManager) {
        super(root, viewType);
        this.workspaceAction = workspaceAction;
        this.editorModel = editorModel;
        this.documentChangeObservable = documentChangeObservable;
        this.documentInsertObservable = documentInsertObservable;
        this.windowManager = windowManager;
        init();
    }

    @Override
    protected void init() {
        setListeners();
        bindButtons();
        bindColorPicker();
        initEditorFonts();
        initSizeFonts();
        bindAlignButtons();
        bindWordSearchButton();

    }

    private void setListeners() {
        documentInsertObservable.addListener(this::updateProperties);
        documentChangeObservable.addListener(this::updateProperties);

    }
    private void bindButtons() {
        undoButton.setOnAction(event -> workspaceAction.undo());
        redoButton.setOnAction(event -> workspaceAction.redo());
    }
    private void bindAlignButtons() {
        leftAlignButton.setOnAction(event -> workspaceAction.setAlign(AlignmentType.LEFT));
        centerAlignButton.setOnAction(event -> workspaceAction.setAlign(AlignmentType.CENTER));
        rightAlignButton.setOnAction(event -> workspaceAction.setAlign(AlignmentType.RIGHT));
    }

    private void bindWordSearchButton() {

        wordSearchButton.setOnAction(event ->{
            if(windowManager.isWindowOpened(WordSearchWindowView.class)){
                return;
            }
            windowManager.openWordSearchWindow();
        });
    }
    private void initSizeFonts(){
        sizeFonts.setEditable(true);
        sizeFonts.getItems().addAll(Arrays.asList(FONT_SIZES));
        sizeFonts.setValue(String.valueOf(EditorModel.DEFAULT_FONT_SIZE));

        sizeFonts.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String previous, String current) {
                workspaceAction.setFontSize(Integer.parseInt(current));
            }
        });
    }

    private void initEditorFonts(){
        editorFonts.setEditable(true);
        editorFonts.getItems().addAll(getEditorFonts());
        editorFonts.setValue(EditorModel.DEFAULT_FONT);

        editorFonts.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String previous, String current) {
                workspaceAction.setFont(current);
            }
        });
    }
    private void bindColorPicker(){
        editorFonts.setEditable(true);
        colorPicker.setValue(Color.BLACK);
        colorPicker.setOnAction(actionEvent -> workspaceAction.setColor(colorPicker.getValue()));
    }


    private Vector<String> getEditorFonts() {
        String [] availableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        Vector<String> fonts = new Vector<>();
        for (String font : availableFonts) {
            fonts.add(font);
        }
        return fonts;
    }
    private void updateProperties(){

        AttributeSet attributeSet = editorModel.getTextPane().getCharacterAttributes();

        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                editorFonts.setValue(StyleConstants.getFontFamily(attributeSet));
                colorPicker.setValue(awtColorToJavaFX(StyleConstants.getForeground(attributeSet)));
                sizeFonts.setValue(String.valueOf(StyleConstants.getFontSize(attributeSet)));
            }
        });

    }

    private javafx.scene.paint.Color awtColorToJavaFX(java.awt.Color c) {
        return javafx.scene.paint.Color.rgb(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha() / 255.0);
    }
}
