package globals;

import processing.core.*;
import viz.VizManager;

import java.util.Date;
import java.util.Calendar;

import controlP5.*;

public class Main extends PApplet {
	
	VizManager vizManager;
	
	public void settings() {
		size(800, 500);
		
	}

	public void setup() {
		//frameRate(5);
		setPAppletSingleton();

		vizManager = new VizManager();

	}

	public void draw() {
		background(25);
		//drawBackLines();
		//drawMouseCoordinates();
		
		vizManager.update();
		vizManager.render();

	}

	private void drawBackLines() {
		stroke(200);
		float offset = frameCount % 40;
		for (int i = 0; i < width; i += 40) {
			line(i + offset, 0, i + offset, height);
		}

	}

	private void drawMouseCoordinates() {
		// MOUSE POSITION
		fill(255, 0, 0);
		text("FR: " + frameRate, 20, 20);
		text("X: " + mouseX + " / Y: " + mouseY, mouseX, mouseY);
	}

	public void keyPressed() {
		//println("FC: " + frameCount);
		vizManager.keyPressed(key);
	}

	public void mousePressed() {

	}

	public void mouseReleased() {

	}

	public void mouseClicked() {
	}

	public void mouseDragged() {

	}

	public void mouseMoved() {

	}
	

	public static void main(String args[]) {
		/*
		 * if (args.length > 0) { String memorySize = args[0]; }
		 */

		PApplet.main(new String[] { Main.class.getName() });
		// PApplet.main(new String[] {
		// "--present","--hide-stop",Main.class.getName() }); //
		// PRESENT MODE
	}

	private void setPAppletSingleton() {
		PAppletSingleton.getInstance().setP5Applet(this);
	}

}