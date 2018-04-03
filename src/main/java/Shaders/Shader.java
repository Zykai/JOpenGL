package Shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import org.joml.*;

public class Shader {

	
	private int programID;
	
	public Shader(String vertexSource, String fragmentSource) {
		// Erstellen der einzelnen Shader
		int vertexShader = loadShader(vertexSource, GL20.GL_VERTEX_SHADER);
		int fragmentShader = loadShader(fragmentSource, GL20.GL_FRAGMENT_SHADER);
		
		// Zusammenfügen der Shader
		int _programID = linkProgram(vertexShader, fragmentShader);
		
		// Alte Programm können gelöscht werden
		GL20.glDeleteShader(vertexShader);
		GL20.glDeleteShader(fragmentShader);
		
		this.programID = _programID;
	}
	
	// aktiviert Shader
	public void use() {
		GL20.glUseProgram(this.programID);
	}
	
	// löscht Shader
	public void delete() {
		GL20.glDeleteProgram(this.programID);
	}
	
	// Uniform-Variabeln
	public void setFloat(String name, float value) {
		GL20.glUniform1f(GL20.glGetUniformLocation(this.programID, name), value);
	}
	public void setVector2(String name, Vector2f value) {
		GL20.glUniform2f(GL20.glGetUniformLocation(this.programID, name), value.x, value.y);
	}	
	public void setVector3(String name, Vector3f value) {
		GL20.glUniform3f(GL20.glGetUniformLocation(this.programID, name), value.x, value.y, value.z);
	}
	public void setMat4(String name, Matrix4f value) {
		FloatBuffer fb = BufferUtils.createFloatBuffer(16);
		value.get(fb);
		GL20.glUniformMatrix4fv(GL20.glGetUniformLocation(this.programID, name), false, fb);
	}
	public void setVector4(String name, Vector4f value) {
		GL20.glUniform4f(GL20.glGetUniformLocation(this.programID, name), value.x, value.y, value.z, value.w);
		
	}
	
	// Liest Shader-Programm aus Datei und erstellt Shader-Objekt
	private int loadShader(String source, int type) {
		//Auslesen der Datei
		String shaderCode = "";
		String line;
		try {
			BufferedReader reader = new BufferedReader( new FileReader(source) );
			while((line = reader.readLine()) != null) {
				shaderCode += line + "\n";
				System.out.println(line);
			}
			reader.close();
		}
		catch(Exception e) {
			System.out.println("Failed to read Shader-File");
			System.exit(-1);
		}
		
		// Erstellen des Objekts
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderCode);
		GL20.glCompileShader(shaderID);
		
		// Überprüfung auf Fehler
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
			   System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			   System.err.println("Could not compile shader!");
			   System.exit(-1);
		}
		
		return shaderID;
	}
	
	// Erstellt Shader-Programm aus 2 Shader Objekten
	private int linkProgram(int vertexShader, int fragmentShader) {
		// Erstellen und Zusammenfügen des Shader-Programms
		int shaderProgram = GL20.glCreateProgram();
		GL20.glAttachShader(shaderProgram, vertexShader);
		GL20.glAttachShader(shaderProgram, fragmentShader);
		GL20.glLinkProgram(shaderProgram);
		
		// Überprüfung auf Fehler
		if(GL20.glGetProgrami(shaderProgram, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetProgramInfoLog(shaderProgram, 500));
			System.out.println("Could not link program");
			System.exit(-1);
		}
		
		return shaderProgram;
			
	}

}
