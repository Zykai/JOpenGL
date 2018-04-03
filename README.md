# OpenGLUI

- Ein OpenGL Library für 2D-Benutzeroberflächen
- Enthält Event-Handler für Maus-Events
- Benutzt [LWJGL 3](https://github.com/LWJGL/lwjgl3) und [JOML](https://github.com/JOML-CI/JOML) über Gradle

![Bild des Programms](https://github.com/Zykai/JOpenGL/blob/master/openglui.jpg)
# Erstellen eines Elements
```java
int width = 300, height = 100, xPos = 50, yPos = 50;
Vector4f color = new Vector4f(0, 0, 1, 1);
Field.init(800, 600);
Field field = new Field(width, height, xPos, yPos, color);
Field.draw();
```
# Hinzufügen von MouseListener
```java
field.setMouseListener(new MouseListener() {
	@Override
	public void mouseClick() {
		
	}
			
	@Override 
	public void mouseEnter() {
		field.setColor(new Vector4f(1, 0, 0, 0.8f));
		field.increaseSizeScaled(10, 10);
	}
			
	@Override 
	public void mouseLeave() {
		field.setColor(new Vector4f(1, 0, 0, 0.5f));
		field.increaseSizeScaled(-10, -10);
	}
});
field.processInput(xMousePosition, yMousePosition, newClicked);
```
# Einfügen in FieldBox
```java
FieldBox box = new FieldBox(400, 350, 200, 150, new Vector4f(0.0f, 0.0f, 0.0f, 0.2f));
box.addField(field);
box.draw();
```
Die Positionen von Elementen in einer FieldBox sind relativ zu der Position der FieldBox.
Die "draw"-Methode sowie die "processInput"-Methode der Fieldbox rufen die jeweiligen Methoden alle Unter-Elemente auf.
