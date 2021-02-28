package editor.markupList.model;

import editor.markupList.handler.BulletsMarkupListHandler;
import editor.markupList.handler.NumberingMarkupListHandler;

public enum MarkupListType {
    NUMBERING(NumberingMarkupListHandler.class),
    BULLETS(BulletsMarkupListHandler.class);

    private Class claazz;

     MarkupListType(Class clazz){
        this.claazz = clazz;
    }

    public Class getClaazz() {
        return claazz;
    }
}
