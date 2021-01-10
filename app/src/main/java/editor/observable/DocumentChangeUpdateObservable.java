package editor.observable;

import com.google.inject.Singleton;
import io.github.stasgora.observetree.Observable;

@Singleton
public class DocumentChangeUpdateObservable extends Observable {

	public void notifyDocumentChangeUpdate(){
		onValueChanged();
		notifyListeners();
	}
}
