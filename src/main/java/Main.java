import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.*;

import Shaders.Shader;

public class Main {
	
	private static Shader shader;
	private static int positionVbo, colorVbo, vao;

	public static void createObjects() {
		// Erstellen der VBO
		positionVbo = GL15.glGenBuffers();
		colorVbo = GL15.glGenBuffers();
		
		
		// Einfügen der Positionsdaten in einen Buffer
		float[] positionData = {-0.5f, -0.5f,
			     0.5f, -0.5f, 
			     0.0f,  0.50f};
		FloatBuffer positionBuffer = BufferUtils.createFloatBuffer(positionData.length);
		positionBuffer.put(positionData);
		positionBuffer.flip();
		
		// Einfügen der Farben in einen Buffer
		float[] colorData = {
			1, 0, 0,
			0, 1, 0,
			0, 0, 1
		};
		FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(colorData.length);
		colorBuffer.put(colorData);
		colorBuffer.flip();
		
		
		// Füllen der VBO mit Werten aus dem Buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, positionVbo	);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, positionBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorVbo	);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_STATIC_DRAW);
		
		// Erstellen des VAO
		vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		
		// Bindet VBO an VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, positionVbo);
		GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0);	
		GL20.glEnableVertexAttribArray(0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorVbo);
		GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);	
		GL20.glEnableVertexAttribArray(1);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}
	
	public static void loadShader() {
		shader = new Shader("src/main/java/Shaders/vertexShader", "src/main/java/Shaders/fragmentShader");
		shader.use();
	}
	
	// Räumt Ressourcen nach Programmende auf
	public static void cleanUp() {
		shader.delete();
		GL30.glDeleteVertexArrays(vao);
		GL15.glDeleteBuffers(positionVbo);
		GL15.glDeleteBuffers(colorVbo);
	}
	
	public static void main(String[] args) {
		
		Window w = new Window(800, 600, false);
		w.showWindow();
		
		
		loadShader();
		createObjects();
		
		GL30.glBindVertexArray(vao);
		
		float height = 0;
		
		// Main-Loop
		while(!w.shouldClose()) {
			w.clear();
			height = (float) Math.sin(GLFW.glfwGetTime());
			shader.setFloat("height", height);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
			w.updateWindow();
		}
		
		cleanUp();
		
		w.closeWindow();		

	}

}