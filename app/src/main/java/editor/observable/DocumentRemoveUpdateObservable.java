package editor.observable;

import com.google.inject.Singleton;
import io.github.stasgora.observetree.Observable;

@Singleton
public class DocumentRemoveUpdateObservable extends Observable {

	public void notifyDocumentRemoveUpdate(){
		onValueChanged();
		notifyListeners();
	}
}
