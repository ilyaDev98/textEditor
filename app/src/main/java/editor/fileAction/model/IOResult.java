package editor.fileAction.model;

public class IOResult<T> {
    private T data;
    private boolean ok;

    public IOResult(boolean ok, T data){
        this.data = data;
        this.ok = ok;
    }

    public boolean isOk() {
        return ok;
    }
    public boolean hasData(){
        return data != null;
    }
    public T getData() {
        return data;
    }
}

