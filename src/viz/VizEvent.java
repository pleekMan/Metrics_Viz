package viz;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.colorchooser.ColorChooserComponentFactory;
import javax.swing.text.html.BlockView;

import com.jogamp.opengl.FBObject.Colorbuffer;

import globals.Main;
import globals.PAppletSingleton;

public class VizEvent {

	Main p5;

	String name;
	int id;

	Date startTime;
	long startTimeMillis;
	int duration;
	int activeDuration;
	
	int color;
	
	double blockStart;
	double blockStop;
	double blockActiveStop;
	double blockPosY;
	

	//public static int blockHeight;

	public VizEvent(String _name, Date _date) {
		p5 = getP5();

		name = _name;
		startTime = _date;
		id = -1;

		startTimeMillis = convertToMillis(startTime);
		duration = -1;
		activeDuration = -1;

		//blockHeight = 50;

		color = 0;
		// p5.println("--|| " + id + " :: " + name + " : " +
		// getStartTimeAsString());

	}

	public void update() {
		blockStart = VizManager.mapDoubles((double)getStartTimeMillis(), VizManager.startMillis, VizManager.stopMillis, VizManager.timelineStart.x, VizManager.timelineStop.x);
		blockStop = VizManager.mapDoubles(getStartTimeMillis() + getDurationMillis(), VizManager.startMillis, VizManager.stopMillis, VizManager.timelineStart.x, VizManager.timelineStop.x);
		blockActiveStop = VizManager.mapDoubles(getStartTimeMillis() + activeDuration, VizManager.startMillis, VizManager.stopMillis,VizManager.timelineStart.x, VizManager.timelineStop.x);
		blockPosY = VizManager.timelineStart.y + (VizManager.blockHeight * id);

	}

	public void render() {

		p5.pushStyle();


		p5.rectMode(p5.CORNERS);
		if (isOver(p5.mouseX, p5.mouseY)) {
			p5.strokeWeight(3);
			p5.stroke(0,120,220);
		} else {
			//p5.strokeWeight(1);
			p5.noStroke();
		}
		// ACTIVE BLOCK
		p5.fill(color, 20);
		p5.rect((float)blockStart, (float)blockPosY, (float)blockStop, (float)(blockPosY + VizManager.blockHeight), 5);
		//p5.text(name, blockStart + 5, yPos + 20);
		
		// ACTIVE BLOCK
		p5.fill(color);
		//p5.stroke(0, 255, 127 - (20 * id));
		p5.rect((float)blockStart, (float)blockPosY, (float)blockActiveStop, (float)(blockPosY + VizManager.blockHeight), 5);
		
		// HOVER LABEL
		if (isOver(p5.mouseX, p5.mouseY)) {
			
			p5.rectMode(p5.CORNER);
			p5.textFont(VizManager.fontMedium);
			p5.textSize(15);
			
			p5.stroke(250);
			p5.strokeWeight(1);
			p5.line((float)blockStart, (float)blockPosY, (float)blockStart, VizManager.timelineStop.y + 5);
			
			p5.noStroke();
			p5.fill(0,127);
			p5.rect(p5.mouseX, p5.mouseY, p5.textWidth(getStartTimeAsString()) + 5, -20, 5);
			
			p5.fill(200);
			p5.noStroke();
			p5.text(getStartTimeAsString(), p5.mouseX + 3, p5.mouseY - 4);
			
		}
		
		p5.popStyle();


	}

	public void setVizLimits(float minLimitX, float maxLimitX) {
		// METHOD TO COMMUNICATE WHERE THE VIZ START + STOP ARE (ZOOMING)
		// MMM LET´S JUST MAKE THE VARIABLES STATIC.. ;)

	}

	public void setId(int _id) {
		id = _id;
		// p5.println(id);
	}

	public String getName() {
		return name;
	}

	public Date getStartTime() {
		return startTime;
	}

	public long getStartTimeMillis() {
		return startTimeMillis;
	}

	public String getStartTimeAsString() {
		return new SimpleDateFormat("yyyy-M-dd,HH:mm:ss").format(startTime);
	}

	public void setDuration(int eventDuration) {
		duration = eventDuration * 1000;
	}
	
	public void setActiveDurationInMillis(int _active) {
		activeDuration = _active <= duration ? _active : duration;
	}

	public long getDurationMillis() {
		return (long) duration;
	}

	public int getId() {
		return id;
	}
	
	public boolean wasFinished(){
		return activeDuration >= duration;
	}
	
	public void setColor(int _c){
		color = _c;
	}

	private long convertToMillis(Date _time) {
		return _time.getTime();
	}
	
	public boolean isOver(float _x, float _y){
		return _x > blockStart && _x < blockStop && _y > blockPosY && _y < blockPosY + VizManager.blockHeight;
	}
	
	public boolean isInsideVizRange(double minRange, double maxRange){
		return blockActiveStop > minRange && blockStart < maxRange;
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}



}
