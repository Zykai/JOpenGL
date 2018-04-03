import org.joml.Vector4f;

import UserInterface.Field;
import UserInterface.FieldBox;
import UserInterface.MouseListener;

public class Main {
	
	
	public static void main(String[] args) {
		
		Window w = new Window(800, 600, false);
		w.showWindow();
		w.enableBlend();
		
		
		
		
		
		// Elemente mit Event-Listener
		Field.init(800, 600);
		final Field f = new Field(300, 100, 50, 50, new Vector4f(1, 0, 0, 0.5f));
		f.setMouseListener(new MouseListener() {
			@Override
			public void mouseClick() {
				
			}
			
			@Override 
			public void mouseEnter() {
				f.setColor(new Vector4f(1, 0, 0, 0.8f));
				f.increaseSizeScaled(10, 10);
			}
			
			@Override 
			public void mouseLeave() {
				f.setColor(new Vector4f(1, 0, 0, 0.5f));
				f.increaseSizeScaled(-10, -10);
			}
		});
		Field f2 = new Field(300, 100, 50, 200, new Vector4f(0,0,1, 0.5f));
		f2.setMouseListener(new MouseListener() {
			@Override
			public void mouseClick() {
				
			}
			
			@Override 
			public void mouseEnter() {
				f2.setColor(new Vector4f(0,0,1, 0.8f));
				f2.increaseSizeScaled(10, 10);
			}
			
			@Override 
			public void mouseLeave() {
				f2.setColor(new Vector4f(0,0,1, 0.5f));
				f2.increaseSizeScaled(-10, -10);
			}
		});
		
		FieldBox box = new FieldBox(400, 350, 200, 150, new Vector4f(0.0f, 0.0f, 0.0f, 0.2f));
		box.addField(f);
		box.addField(f2);
		// Main-Loop
		while(!w.shouldClose()) {
			w.clear();
			
			box.draw();
			box.processInput(Globals.xMousePosition, Globals.yMousePosition, Globals.newClicked);
			w.updateWindow();
		}
		Field.cleanUp();
		
		w.closeWindow();		

	}

}
