package editor.fileAction.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import editor.editorSpace.model.EditorModel;
import editor.observable.FileLoadingObservable;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
import javax.swing.text.rtf.RTFEditorKit;
import java.io.*;

@Singleton
public class FileExecutor {

	private FileLoadingObservable fileLoadingObservable;
	private EditorModel editorModel;

	@Inject
	FileExecutor(FileLoadingObservable fileLoadingObservable, EditorModel editorModel) {
		this.editorModel = editorModel;
		this.fileLoadingObservable = fileLoadingObservable;
	}

	public void openProject(File file) throws IOException, BadLocationException {
		FileInputStream stream = new FileInputStream(file);
		RTFEditorKit kit = new RTFEditorKit();
		Document doc = kit.createDefaultDocument();
		kit.read(stream, doc, 0);
		editorModel.setStyledDocument((DefaultStyledDocument) doc);
		editorModel.setFile(file);
		fileLoadingObservable.notifyFileLoading();
	}

	public void saveProject(File file) throws IOException, BadLocationException {
		FileOutputStream fos = new FileOutputStream(file);
		RTFEditorKit rtfKit = new RTFEditorKit();
		StyledDocument doc = editorModel.getStyledDocument();
		rtfKit.write(fos, doc, 0, doc.getLength());
		fos.flush();
		fos.close();
		editorModel.setFile(file);
		fileLoadingObservable.notifyFileLoading();
	}

	public String getProjectFileName(File projectFile) {
		return projectFile.getName();
	}
}
