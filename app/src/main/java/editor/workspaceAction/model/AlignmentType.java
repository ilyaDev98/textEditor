package editor.workspaceAction.model;

public enum AlignmentType {
    LEFT(0),
    CENTER(1),
    RIGHT(2);

    private final int code;

    AlignmentType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
