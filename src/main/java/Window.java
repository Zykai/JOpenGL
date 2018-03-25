import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

public class Window {

	private long window;
	
	public Window(int width, int height, boolean fullScreen) {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();
		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() ) 
			throw new IllegalStateException("Unable to initialize GLFW");

			// Window-Hints
			glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // Fenster ist anfangs versteckt
			glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // Fenstergröße ist variabel
			// OpenGL Version 3.3
			glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
			glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
			
			// Core Profile
			glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
				
			// Erstellt das Fenster
			window = glfwCreateWindow(width, height, "Hello World!", fullScreen ? glfwGetPrimaryMonitor() : NULL, NULL);
			if ( window == NULL )
				throw new RuntimeException("Failed to create the GLFW window");

			// Lambda-Funktion, die bei Tastatur-Events aufgerufen wird
			glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
				if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
					glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
			});
			
			// Ordnet Fenster in der Mitte an
			try ( MemoryStack stack = stackPush() ) {
				IntBuffer pWidth = stack.mallocInt(1); // int*
				IntBuffer pHeight = stack.mallocInt(1); // int*

				// Get the window size passed to glfwCreateWindow
				glfwGetWindowSize(window, pWidth, pHeight);

				// Get the resolution of the primary monitor
				GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

				// Center the window
				glfwSetWindowPos(
					window,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2
				);
			} // the stack frame is popped automatically
			
			// aktueller OpenGL-Kontext
			glfwMakeContextCurrent(window);
				
			// V-Sync, synchronisieren der Framerate mit Monitor
			glfwSwapInterval(1);
				
			// This line is critical for LWJGL's interoperation with GLFW's
			// OpenGL context, or any context that is managed externally.
			// LWJGL detects the context that is current in the current thread,
			// creates the GLCapabilities instance and makes the OpenGL
			// bindings available for use.
			GL.createCapabilities();
			
			// Setzt den Viewport, d.h. wie viele Pixel werden verwendet
			GL11.glViewport(0, 0, width, height);
			// ClearColor
			setClearColor(0.5f, 0.5f, 0.5f);
	}
	
	// Leert den Framebuffer, d.h. aktuelles Bild wird gelöscht (und Tiefenwerte)
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public void updateWindow() {
		// Tauschen der beiden Buffer
		glfwSwapBuffers(window); // swap the color buffers
		// Suche nach Events
		glfwPollEvents();
	}
	
	// Zeigt Fenster
	public void showWindow() {
		glfwShowWindow(window);
	}
	
	// Versteckt Fenster
	public void hideWindow() {
		GLFW.glfwHideWindow(window);
	}
	
	// Schließt das Fenster
	public void closeWindow() {
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	// Überprüft ob das Fenster geschlossen werden soll
	public boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}
	
	// Ändert die Farbe nach clear() Aufruf
	public void setClearColor(float r, float g, float b) {
		glClearColor(r, g, b, 0.0f);
	}
	
}
