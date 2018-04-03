package UserInterface;

import java.util.LinkedList;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class FieldBox extends Field {

	
	private LinkedList<Field> fields;
	
	public FieldBox(int width, int height, int xPosition, int yPosition, Vector3f color) {
		super(width, height, xPosition, yPosition, color);
		fields = new LinkedList<>();
	}
	
	public FieldBox(int width, int height, int xPosition, int yPosition, Vector4f color) {
		super(width, height, xPosition, yPosition, color);
		fields = new LinkedList<>();
	}
	
	public void addField(Field field) {
		field.addPosition(this.xPosition, this.yPosition);
		fields.add(field);
	}
	
	@Override
	public void addPosition(int xAdd, int yAdd) {
		super.addPosition(xAdd, yAdd);
		for(Field f : fields) {
			f.addPosition(xAdd, yAdd);
		}
	}
	
	@Override
	public void draw() {
		super.draw();
		for(Field f : fields) {
			f.draw();
		}
	}
	
	@Override
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
		for(Field f : fields) {
			f.processInput(xMousePos, yMousePos, mouseClicked);
		}
	}

}
