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
	
	public static int blockHeight;
	
	public VizEvent(String _name, Date _date){
		p5 = getP5();
		
		name = _name;
		startTime = _date;
		id = -1;
		
		startTimeMillis = convertToMillis(startTime);
		duration = -1;
		
		blockHeight = 50;
		
		p5.println("--|| " + name + " : " + getStartTimeAsString());
		
		
	}
	
	public void update(){
		
	}
	
	public void render(){
		float vizStart = VizManager.timelineStart.x;
		float vizStop = VizManager.timelineStop.x;
		
		float blockStart = VizManager.mapLongs(getStartTimeMillis(), VizManager.startMillis, (VizManager.startMillis + VizManager.totalMillis), vizStart, vizStop);
		float blockStop = VizManager.mapLongs(getStartTimeMillis() + getDurationMillis(), VizManager.startMillis, (VizManager.startMillis + VizManager.totalMillis),vizStart, vizStop);
		
		p5.rectMode(p5.CORNERS);
		p5.stroke(0,255,127 - (20 * id));
		p5.fill(0,255,127 - (20 * id), 127);

		float yPos = 100 + (blockHeight * id);
		p5.rect(blockStart, yPos - (blockHeight * 0.5f), blockStop, yPos + blockHeight, 5);
		p5.text(name, blockStart + 5,  yPos + 20);
		
		//p5.line(eventStart, yPos, eventStop, yPos);
		//
		
		
	}
	
	
	public void setVizLimits(float minLimitX, float maxLimitX){
		// METHOD TO COMMUNICATE WHERE THE VIZ START + STOP ARE (ZOOMING)
		// MMM LET´S JUST MAKE THE VARIABLES STATIC..   ;)
		
	}
	
	public void setId(int _id){
		id = _id;
		//p5.println(id);
	}
	
	public String getName(){
		return name;
	}
	
	public Date getStartTime(){
		return startTime;
	}
	public long getStartTimeMillis(){
		return startTimeMillis;
	}
	
	public String getStartTimeAsString(){
		return new SimpleDateFormat("yyyy-M-dd,HH:mm:ss").format(startTime);
	}
	
	public void setDuration(int eventDuration) {
		duration = eventDuration * 1000;
		
	}
	
	public long getDurationMillis(){
		return (long)duration;
	}
	
	public int getId() {
		return id;
	}

	private long convertToMillis(Date _time) {
		return _time.getTime();
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}


}
