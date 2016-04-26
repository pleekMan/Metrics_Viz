package viz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import globals.Main;
import globals.PAppletSingleton;

public class VizManager {

	Main p5;
	ArrayList<VizEvent> events;

	public static SimpleDateFormat dateFormatter;

	boolean dataLoaded;

	long startMillis;
	long totalMillis;

	public VizManager() {

		p5 = getP5();

		events = new ArrayList<VizEvent>();
		dataLoaded = false;

		dateFormatter = new SimpleDateFormat("yyyy-M-dd,HH:mm:ss");

		buildData();
		buildViz();
	}

	public void update() {

	}

	public void render() {

		p5.stroke(200);
		p5.fill(250);

		for (int i = 0; i < events.size(); i++) {
			VizEvent event = events.get(i);

			String name = event.getName();
			float eventStart = mapLongs(event.getStartTimeMillis(), startMillis, (startMillis + totalMillis), 0f, p5.width);
			// TODO PROCESSING´S map() THROWS NAN IF NUMBERS ARE TOO EXTREME
			// (CONVERTING LONG TO FLOAT)

			p5.line(eventStart, 0, eventStart, ( (float)p5.height / events.size()) * i);
			p5.text(name, eventStart,  ( (float)p5.height / events.size()) * i + 20);

		}
	}

	private String[] loadFile() {
		return p5.loadStrings("data/log.txt");
		// p5.selectInput("Titulo de la Ventanita de Abrir", callback);
	}

	private void buildData() {
		p5.println(p5.sketchPath());
		String[] data = loadFile();

		if (data != null) {

			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-M-dd,HH:mm:ss");

			for (int i = 1; i < data.length; i++) {

				String[] lineData = p5.split(data[i], ',');

				try {
					Date newDate = new Date();
					;
					newDate = dateFormatter.parse(lineData[0] + "," + lineData[1]);
					VizEvent newEventData = new VizEvent(lineData[2], newDate, i);

					events.add(newEventData);

					dataLoaded = true;

				} catch (Exception e) {
					p5.println("Unable to parse Date data, dude..!!");
					dataLoaded = false;
					e.printStackTrace();
				}
			}
		} else {
			p5.println("--|| DATA NOT LOADED, DUDE..!! (File not Found, maybe..) ");
		}
	}

	private void buildViz() {

		startMillis = events.get(0).getStartTimeMillis();
		totalMillis = events.get(events.size() - 1).getStartTimeMillis() - startMillis;
		p5.println("--|| Timeline starts at: " + startMillis + " and lasts for " + totalMillis);

	}

	public void keyPressed(char _key) {

		for (int i = 0; i < events.size(); i++) {
			VizEvent actualEvent = events.get(i);
			p5.println(actualEvent.getStartTimeAsString() + " - " + actualEvent.getName());
		}
	}

	public float mapLongs(long value, long start1, long stop1, float start2, float stop2) {
		return start2 + (stop2 - start2) * ((value - start1) / (float)(stop1 - start1));
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
