package viz;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.text.html.BlockView;

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

	}

	public void render() {
		float vizStart = VizManager.timelineStart.x;
		float vizStop = VizManager.timelineStop.x;

		float blockStart = VizManager.mapLongs(getStartTimeMillis(), VizManager.startMillis, VizManager.stopMillis, vizStart, vizStop);
		float blockStop = VizManager.mapLongs(getStartTimeMillis() + getDurationMillis(), VizManager.startMillis, VizManager.stopMillis, vizStart, vizStop);
		float blockActiveStop = VizManager.mapLongs(getStartTimeMillis() + activeDuration, VizManager.startMillis, VizManager.stopMillis,vizStart, vizStop);
		
		float yPos = VizManager.timelineStart.y + (VizManager.blockHeight * id);

		p5.rectMode(p5.CORNERS);
		p5.noStroke();
		
		// ACTIVE BLOCK
		p5.fill(color, 20);
		p5.rect(blockStart, yPos, blockStop, yPos + VizManager.blockHeight, 5);
		//p5.text(name, blockStart + 5, yPos + 20);
		
		// ACTIVE BLOCK
		p5.fill(color);
		//p5.stroke(0, 255, 127 - (20 * id));
		p5.rect(blockStart, yPos, blockActiveStop, yPos + VizManager.blockHeight, 5);

		// p5.line(eventStart, yPos, eventStop, yPos);
		//

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

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}



}
