import glengine.Window;

/**
 * The Window class is a singleton that creates a new window and runs it
 */
public class Main {
	/**
	 * It creates a window and runs it.
	 * @param args main args
	 */
	public static void main(String[] args) {
		Window window = Window.get();
		window.run();
	}
}
