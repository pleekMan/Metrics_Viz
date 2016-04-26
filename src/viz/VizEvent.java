package viz;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VizEvent {

	String name;
	int id;
	
	Date startTime;
	long startTimeMillis;
	int duration;
	
	public VizEvent(String _name, Date _date, int _id){
		
		name = _name;
		startTime = _date;
		id = _id;
		
		startTimeMillis = convertToMillis(startTime);
		duration = -1;
		
		
	}
	
	public void update(){
		
	}
	
	public void render(){
		
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

	private long convertToMillis(Date _time) {
		return _time.getTime();
	}


}
