package editor;

/**
 * A proxy class to avoid error when launching a build. See https://github.com/javafxports/openjdk-jfx/issues/236 for details.
 */
public class Main {

	public static void main(String[] args) {
		TextEditor.main(args);
	}

}
