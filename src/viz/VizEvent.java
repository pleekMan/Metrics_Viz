package viz;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VizEvent {

	String name;
	int id;
	
	Date startTime;
	long startTimeMillis;
	
	public VizEvent(String _name, Date _date, int _id){
		
		name = _name;
		startTime = _date;
		id = _id;
		
		startTimeMillis = convertToMillis(startTime);
		
		
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

	private long convertToMillis(Date _time) {
		return _time.getTime();
	}
}
