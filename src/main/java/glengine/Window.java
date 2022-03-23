package glengine;

import org.lwjgl.Version;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import java.nio.IntBuffer;
import org.lwjgl.system.MemoryStack;

import static java.util.Objects.requireNonNull;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

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

	public static Window get() {
		if (Window.window == null) {
			Window.window = new Window();
		}

		return Window.window;
	}

	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
		loop();
	}

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