package editor.observable;
import com.google.inject.Singleton;
import io.github.stasgora.observetree.Observable;

@Singleton
public class FileLoadingObservable extends Observable {

	public void notifyFileLoading(){
		onValueChanged();
		notifyListeners();
	}
}
