package editor.observable;

import com.google.inject.Singleton;
import io.github.stasgora.observetree.Observable;

@Singleton
public class DocumentInsertUpdateObservable extends Observable {

	public void notifyDocumentInsertUpdate(){
		onValueChanged();
		notifyListeners();
	}
}
