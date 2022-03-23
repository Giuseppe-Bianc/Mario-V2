package glengine;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static java.util.Objects.requireNonNull;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * The Window class is a singleton class that creates a GLFW window and handles all the events
 */
public class Window {
	private final int width, height;
	private final String title;
	private long glfwWindow;

	private static Window window = null;

	private Window() {
		this.width = 960;
		this.height = 540;
		this.title = "Mario";
	}

	/**
	 * The get() function returns the Window object if it exists, otherwise it creates a new Window object
	 * and returns it
	 *
	 * @return The Window object.
	 */
	public static Window get() {
		if (Window.window == null) {
			Window.window = new Window();
		}

		return Window.window;
	}

	/**
	 * "Create an OpenGL context and continuously render a rotating triangle."
	 *
	 * The rest of the code is just the usual boilerplate to setup a window, OpenGL context, and display
	 * loop
	 */
	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
		loop();
	}

	/**
	 * Initialize the GLFW library and create a window
	 */
	public void init() {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW.");
		}

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
		glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
		if (glfwWindow == NULL) {
			throw new IllegalStateException("Failed to create the GLFW window.");
		}

		try ( MemoryStack st = stackPush() ) {
			IntBuffer pW = st.mallocInt(1);
			IntBuffer pH = st.mallocInt(1);
			glfwGetWindowSize(glfwWindow , pW, pH);
			GLFWVidMode vm = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(glfwWindow,(requireNonNull(vm).width() - pW.get(0)) / 2,
					(requireNonNull(vm).height() - pH.get(0)) / 2
			);
		}

		glfwMakeContextCurrent(glfwWindow);
		glfwSwapInterval(1);
		glfwShowWindow(glfwWindow);
		GL.createCapabilities();
	}

	/**
	 * The main loop of the program.Responsible for clearing the screen,  swapping the buffers,
	 * and polling for events.The main loop also updates the dt, which is used to calculate the
	 * time elapsed since the last frame.The main loop also updates the time variable beginTime,
	 * which is used to calculate the time elapsed since the program started .The main loop also
	 * swaps the buffers, which is necessary for displaying the new frame.The main loop also
	 * checks if the window should be  closed, which is done by calling glfwWindowShouldClose.The
	 * main loop also clears the screen, which is done by calling glClearColor and glClear.The
	 * main loop also swaps the buffers, which is done by calling glfwSwapBuffers.
	 */
	public void loop() {
		float beginTime = (float) glfwGetTime(), endTime, dt = 0;

		while (!glfwWindowShouldClose(glfwWindow)) {
			glfwPollEvents();

			glClearColor(0, 0, 0, 1);
			glClear(GL_COLOR_BUFFER_BIT);

			glfwSwapBuffers(glfwWindow);

			endTime = (float) glfwGetTime();
			dt = endTime - beginTime;
			beginTime = endTime;
		}
	}
}