package editor.fileAction.model;

public enum FileChooserExtension {
    TEXT("Текстовые документы (*.txt)","*.txt"),
    RTF("RTF документы (*.rtf)","*.rtf"),
    ALL("Все файлы","*.*");

    private final String title;
    private final String extension;

    FileChooserExtension(String title, String extension) {
        this.title = title;
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public String getTitle() {
        return title;
    }
}