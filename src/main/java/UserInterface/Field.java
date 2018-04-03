package UserInterface;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import Shaders.Shader;

public class Field {

	private static int vao, positionVbo, colorVbo;
	
	private static Shader shader;
	
	private static Matrix4f orto;
	
	
	
	protected int xPosition, yPosition;
	protected int width, height;
	protected boolean previousMouseOver;
	protected MouseListener mouseListener;
	
	private Matrix4f model;
	
	
	private Vector4f color;
	
	private static void setUpShader() {
		shader = new Shader("res/shaders/fieldVertex", "res/shaders/fieldFragment");
	}
	
	private static void setUpVertices() {
		// Erstellen der VBO
				positionVbo = GL15.glGenBuffers();
				colorVbo = GL15.glGenBuffers();
				
				
				// Einfügen der Positionsdaten in einen Buffer
				/*
				float[] positionData = {
						-0.5f,  0.5f,
						-0.5f, -0.5f,
						 0.5f, -0.5f,
						-0.5f,  0.5f,
						 0.5f, -0.5f,
						 0.5f,  0.5f
				};
				*/
				
				float[] positionData = {
						 0, 1,
						 1, 0,
						 0, 0,
						 
						 0, 1,
						 1, 1,
						 1, 0
						
				};
				
				FloatBuffer positionBuffer = BufferUtils.createFloatBuffer(positionData.length);
				positionBuffer.put(positionData);
				positionBuffer.flip();
				
				// Einfügen der Farben in einen Buffer
				float[] colorData = {
					1, 0, 0,
					0, 1, 0,
					0, 0, 1,
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
	
	public static void createOrtoMatrix(int width, int height) {
		orto = new Matrix4f().ortho2D(0, width, height, 0);
	}
	
	public static void cleanUp() {
		shader.delete();
		GL30.glDeleteVertexArrays(vao);
		GL15.glDeleteBuffers(colorVbo);
		GL15.glDeleteBuffers(positionVbo);
	}
	
	public static void init(int windowWidth, int windowHeight) {
		setUpShader();
		setUpVertices();
		createOrtoMatrix(windowWidth, windowHeight);
	}
	
	public Field(int width, int height, int xPosition, int yPosition, Vector4f color) {
		this.width = width;
		this.height = height;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		createModelMatrix();
		this.color = color;
		this.mouseListener = new MouseListener();
		this.previousMouseOver = false;
	}
	
	public Field(int width, int height, int xPosition, int yPosition, Vector3f color) {
		this.width = width;
		this.height = height;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		createModelMatrix();
		this.color = new Vector4f(color, 1);
		this.mouseListener = new MouseListener();
		this.previousMouseOver = false;
	}
	
	public Field() {
		this.width = 0;
		this.height = 0;
		this.xPosition = 0;
		this.yPosition = 0;
		createModelMatrix();
		this.color = new Vector4f(1,1,1,1);
		this.mouseListener = new MouseListener();
		this.previousMouseOver = false;
	}
	
	public void setPosition(int xPosition,  int yPosition) {
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}
	
	public void setXPosition(int xPosition) {
		this.xPosition = xPosition;
	}
	
	public void setYPosition(int yPosition) {
		this.yPosition = yPosition;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setMouseListener(MouseListener mouseListener) {
		this.mouseListener = mouseListener;
	}
	
	public void createModelMatrix() {
		model = new Matrix4f();
		model.translate(xPosition, yPosition, 0);
		model.scale(width, height, 1);
	}
	
	public void addPosition(int xAdd, int yAdd) {
		this.xPosition += xAdd;
		this.yPosition += yAdd;
		createModelMatrix();
	}
	
	public void draw() {
		shader.use();
		shader.setMat4("orto", orto);
		shader.setMat4("model", model);
		shader.setVector4("fieldColor", color);
		GL30.glBindVertexArray(vao);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
	}
	
	public void setColor(Vector4f color) {
		this.color = color;
	}
	
	public void setColor(Vector3f color) {
		this.color = new Vector4f(color, 1);
	}
	
	public void increaseSizeScaled(int xSize, int ySize) {
		this.xPosition -= xSize / 2;
		this.width += xSize;
		this.yPosition -= ySize / 2;
		this.height += ySize;
		createModelMatrix();
	}
	
	public void processInput(int xMousePos, int yMousePos, boolean mouseClicked) {
		boolean mouseOver = xMousePos > this.xPosition && xMousePos < this.xPosition + this.width && yMousePos > this.yPosition && yMousePos < this.yPosition + this.height; 
		// Mouse Click
		if(mouseOver && mouseClicked) {
			System.out.println("Clicked Element");
			this.mouseListener.mouseClick();
		}
		// Mouse Enter
		else if(mouseOver && !previousMouseOver) {
			System.out.println("Enter Element");
			this.mouseListener.mouseEnter();
		}
		// Mouse Leave
		else if(!mouseOver && previousMouseOver) {
			System.out.println("Leave Element");
			this.mouseListener.mouseLeave();
		}
		previousMouseOver = mouseOver;
		
	}
	
}
