package viz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import globals.Main;
import globals.PAppletSingleton;
import processing.core.PFont;
import processing.core.PVector;

@SuppressWarnings("static-access")
public class VizManager {

	Main p5;
	ArrayList<VizEvent> events;

	public static SimpleDateFormat dateFormatter;

	boolean dataLoaded;
	int differentEventCount;

	public static long startMillis;
	public static long stopMillis;

	public static PVector timelineStart;
	public static PVector timelineStop;
	public static float blockHeight;
	
	public PVector totalsGraphStart;
	public PVector totalsGraphStop;
	
	public String[] eventName;
	public int[] eventNameId;

	ColorPalette palette;
	static public int colorBack;
	static public int colorMiddle;
	static public int colorFore;

	PFont fontLight;
	PFont fontMedium;

	public VizManager() {

		p5 = getP5();

		events = new ArrayList<VizEvent>();
		dataLoaded = false;

		dateFormatter = new SimpleDateFormat("yyyy-M-dd,HH:mm:ss");

		createColorPalettes();
		buildData();
		buildViz();
	}

	public void update() {
		// timelineStart.set(p5.mouseX, 100);
		// timelineStop.set(p5.width - timelineStart.x, 100);
		/*
		 * for (int i = 0; i < events.size(); i++) {
		 * events.get(i).setVizLimits(timelineStart.x, timelineStop.x); }
		 */
	}

	public void render() {

		if (dataLoaded) {

			p5.stroke(200);
			p5.fill(250);

			for (int i = 0; i < events.size(); i++) {
				VizEvent event = events.get(i);
				event.render();
			}

			renderTotalsFinished();
			renderTotalEvents();
		}

		renderGui();

		// TEST COLOR PALETTE
		p5.rectMode(p5.CORNER);
		p5.noStroke();
		for (int i = 0; i < palette.getCount(); i++) {
			p5.fill(palette.getColor(i));
			p5.rect(i * 20, 0, 20, 20);
		}

	}

	private String[] loadFile() {
		return p5.loadStrings("data/log.txt");
		// p5.selectInput("Titulo de la Ventanita de Abrir", callback);
	}

	private void buildData() {
		p5.println(p5.sketchPath());
		String[] eventInfo = p5.loadStrings("data/info.txt");
		String[] data = p5.loadStrings("data/log.txt");

		if (data != null || eventInfo != null) {

			// LOAD EVENT INFO: EVENT NAME + DURATION OF EVENT
			ArrayList<String> eventsName = new ArrayList<String>();
			ArrayList<Integer> eventsDuration = new ArrayList<Integer>();

			differentEventCount = eventInfo.length - 1;
			for (int i = 1; i < eventInfo.length; i++) {
				String[] lineData = p5.split(eventInfo[i], ',');
				eventsName.add(lineData[0]);
				eventsDuration.add(Integer.parseInt(lineData[1]));
			}

			// STORE EVENT INFO IN GLOBAL ARRAY
			eventName = new String[eventsName.size()];
			eventNameId = new int[eventsName.size()];
			for (int i = 0; i < eventName.length; i++) {
				eventName[i] = eventsName.get(i);
				eventNameId[i] = i;
			}
			// p5.println(eventName);
			// p5.println(eventNameId);

			// LOAD DATE/MESSAGE DATA
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-M-dd,HH:mm:ss");

			for (int i = 1; i < data.length; i++) {

				String[] lineData = p5.split(data[i], ',');

				try {
					Date newDate = new Date();

					newDate = dateFormatter.parse(lineData[0] + "," + lineData[1]);
					VizEvent newEventData = new VizEvent(lineData[2], newDate);

					// SET EVENT DURATION (FROM eventInfo data)
					for (int j = 0; j < eventsName.size(); j++) {
						if (newEventData.getName().equals(eventsName.get(j))) {
							newEventData.setDuration(eventsDuration.get(j).intValue());
							newEventData.setId(j);
							newEventData.setColor(palette.getColor(newEventData.getId() % palette.getCount()));
							break;
						}
					}
					events.add(newEventData);
					dataLoaded = true;

				} catch (Exception e) {
					p5.println("Unable to parse Data, dude..!!");
					dataLoaded = false;
					e.printStackTrace();
				}
			}

		} else {
			p5.println("--|| DATA NOT LOADED, DUDE..!! (File not Found, maybe..) ");
			p5.println("--|| TRY LOADING IT MANUALLY");
		}

		// SET EVENT ACTIVE DURATION (BASED ON NEXT EVENT START TIME)
		for (int i = 0; i < events.size() - 1; i++) {
			VizEvent thisEvent = events.get(i);
			thisEvent.setActiveDurationInMillis((int) (events.get(i + 1).getStartTimeMillis() - thisEvent.getStartTimeMillis()));
		}
		events.get(events.size() - 1).setActiveDurationInMillis((int) events.get(events.size() - 1).getDurationMillis());

		// PRINT OUT EVENTS
		/*
		 * for (int i = 0; i < events.size(); i++) { VizEvent actualEvent =
		 * events.get(i); p5.println("--|| " + actualEvent.getId() + " :: " +
		 * actualEvent.getStartTimeAsString() + " - " + actualEvent.getName());
		 * }
		 */

	}

	private void buildViz() {

		startMillis = events.get(0).getStartTimeMillis();
		stopMillis = events.get(events.size() - 1).getStartTimeMillis();
		p5.println("--|| Timeline starts at: " + startMillis + " to " + stopMillis + " and lasts for " + (events.get(events.size() - 1).getStartTimeMillis() - startMillis));

		timelineStart = new PVector(80, 160);
		timelineStop = new PVector(p5.width - timelineStart.x, 400);
		blockHeight = (timelineStop.y - timelineStart.y) / eventName.length;
		
		totalsGraphStart = new PVector(timelineStart.x + 200,timelineStop.y + 80);
		totalsGraphStop = new PVector(totalsGraphStart.x + 200,totalsGraphStart.y + 100);

		
		fontLight = p5.loadFont("DINPro-Light-50.vlw");
		fontMedium = p5.loadFont("DINPro-Medium-50.vlw");

	}

	private void createColorPalettes() {

		colorBack = p5.color(0xFF2A363B);
		colorMiddle = p5.color(0xFF4f595E);
		colorFore = p5.color(0xFF737B7f);

		int[] newColors = new int[7];

		newColors[0] = p5.color(0xFF3EACA8);
		newColors[1] = p5.color(0xFFA2D4AB);
		newColors[2] = p5.color(0xFF99F898);
		newColors[3] = p5.color(0xFFFECEA8);
		newColors[4] = p5.color(0xFFFF847C);
		newColors[5] = p5.color(0xFFE84A5F);
		newColors[6] = p5.color(0xFF547a82);

		palette = new ColorPalette(newColors);
	}

	private void renderGui() {

		p5.rectMode(p5.CORNER);
		p5.textFont(fontMedium, 15);

		p5.stroke(colorMiddle);
		p5.line(timelineStart.x, timelineStart.y - 20, timelineStart.x, timelineStop.y + 20);
		p5.line(timelineStop.x, timelineStart.y - 20, timelineStop.x, timelineStop.y + 20);

		// HORIZONTAL LINES NAD EVENT NAME
		for (int i = 0; i < eventName.length; i++) {
			p5.stroke(colorMiddle);

			float lineY = timelineStart.y + (blockHeight * i);
			p5.line(timelineStart.x, lineY, timelineStop.x, lineY);

			p5.fill(colorMiddle, 10);
			p5.rect(timelineStart.x, lineY, 100, 20);
			p5.fill(230);
			p5.text(eventName[i], timelineStart.x + 5, lineY + 15);
		}
		p5.line(timelineStart.x, timelineStop.y, timelineStart.x, timelineStart.y);

	}

	private void renderTotalEvents() {

		int[] eventCounts = new int[differentEventCount];

		// SUM THE EVENTS COUNT
		for (int i = 0; i < events.size(); i++) {
			// p5.println(i + ": " + events.get(i).getId());
			eventCounts[events.get(i).getId()] += 1;
		}

		// DRAW AS PIE CHART (LABELS MISSING)
		/*
		 * p5.pushMatrix(); p5.translate(p5.width * 0.5f, p5.height * 0.5f); for
		 * (int i = 0; i < eventCounts.length; i++) { float pieSize =
		 * (eventCounts[i] / (float) events.size()) * p5.TWO_PI;
		 * 
		 * p5.noStroke(); p5.fill(50 + (50 * i), 0, 0);
		 * 
		 * p5.arc(0, 0, 200, 200, 0, pieSize);
		 * 
		 * p5.rotate(pieSize); } p5.popMatrix();
		 */

		// DRAW AS BARS
		p5.rectMode(p5.CORNER);
		p5.textAlign(p5.LEFT);
		//int barsStartY = 100;
		//int barsStopY = 300;
		float barX = timelineStart.x;
		float barY = timelineStart.y - 60;
		float barHeight = 20;

		for (int i = 0; i < eventCounts.length; i++) {
			float ratio = eventCounts[i] / (float) events.size();
			float barSize = ratio * (timelineStop.x - timelineStart.x);

			p5.noStroke();
			p5.fill(events.get(i).color);
			p5.rect(barX,barY, barSize, barHeight);

			p5.fill(200);
			p5.text(eventName[i] + " - " + p5.nf(ratio * 100, 0, 2) + "%", barX + 5, barY - 10);
			//p5.text(eventName[i] + " - " + p5.nf(ratio * 100, 0, 2) + "%", 200, 20 + 20 * i);

			p5.stroke(255);
			p5.line(barX, barY + barHeight, barX, barY - 20);

			barX += barSize;
		}

	}

	private void renderTotals() {

		// 2D ARRAY TO HOLD [ EVENT ][ NOT/FINISHED WATCHING]
		int[][] eventCounts = new int[differentEventCount][2];

		// SUM THE FINISHED / NOT FINISHED EVENTS COUNT
		for (int i = 0; i < events.size(); i++) {
			VizEvent thisEvent = events.get(i);
			if (thisEvent.wasFinished()) {
				eventCounts[thisEvent.getId()][0] += 1;
			} else {
				eventCounts[thisEvent.getId()][1] += 1;
			}
		}
		
		// DRAW AS BARS
		p5.rectMode(p5.CORNER);
		float barHeight = totalsGraphStop.y - totalsGraphStart.y;
		float barX = totalsGraphStart.x;

		for (int i = 0; i < eventCounts.length; i++) {
			float ratio = (eventCounts[i][0] + eventCounts[i][1]) / (float) events.size();
			float barSize = ratio * (totalsGraphStop.x - totalsGraphStart.x);
			float activeRatio = eventCounts[i][0] / (float) events.size();
			float activeBarSize = activeRatio * (totalsGraphStop.x - totalsGraphStart.x);
			
			// BAR BACKG = (CAN ONLY SEE) INACTIVE PORTION 
			p5.noStroke();
			p5.fill(events.get(i).color,127);
			p5.rect(barX, totalsGraphStart.y, barSize, barHeight);
			
			// VAR ACTIVE PORTION
			p5.fill(events.get(i).color);
			p5.rect(barX, totalsGraphStart.y, activeBarSize, barHeight);
			
			p5.fill(250);
			p5.text(eventName[i] + " - " + p5.nf(ratio * 100, 0, 2) + "%", barX, totalsGraphStart.y - 15);
			
			p5.text(p5.nf(   (((float)eventCounts[i][0]) / (eventCounts[i][0] + eventCounts[i][1])) * 100,   0, 2) + "%", barX, totalsGraphStop.y + 20);
			p5.text(p5.nf(   (((float)eventCounts[i][1]) / (eventCounts[i][0] + eventCounts[i][1])) * 100,   0, 2) + "%", barX + 100, totalsGraphStop.y + 20);

			
			barX += barSize;
		}

	}
	
	private void renderTotalsFinished() {

		// 2D ARRAY TO HOLD [ EVENT ][ NOT/FINISHED WATCHING]
		int[][] eventCounts = new int[differentEventCount][2];

		// SUM THE FINISHED / NOT FINISHED EVENTS COUNT
		for (int i = 0; i < events.size(); i++) {
			VizEvent thisEvent = events.get(i);
			if (thisEvent.wasFinished()) {
				eventCounts[thisEvent.getId()][0] += 1;
			} else {
				eventCounts[thisEvent.getId()][1] += 1;
			}
		}
		
		// DRAW AS BARS
		p5.rectMode(p5.CORNER);
		p5.textSize(12);
		
		// GLOBALS
		float barHeight = (totalsGraphStop.y - totalsGraphStart.y)/ eventName.length;
		float barY = totalsGraphStart.y;
		
		// OVERALL ACTIVE/NON BAR 
		float overallFinishedRatio = 0;
		for (int i = 0; i < eventCounts.length; i++) {
			overallFinishedRatio += eventCounts[i][0];
		}
		overallFinishedRatio = overallFinishedRatio / events.size();
		
		p5.fill(200);
		p5.textAlign(p5.CENTER);
		p5.text("CONCLUIDOS", totalsGraphStart.x, totalsGraphStart.y - 40);
		p5.text("NO CONCLUIDOS", totalsGraphStop.x, totalsGraphStart.y - 40);

		
		p5.fill(colorMiddle);
		p5.rect(totalsGraphStart.x, totalsGraphStart.y - 30, totalsGraphStop.x - totalsGraphStart.x, barHeight);
		p5.fill(200);
		p5.rect(totalsGraphStart.x, totalsGraphStart.y - 30, overallFinishedRatio * (totalsGraphStop.x - totalsGraphStart.x), barHeight);
		
		p5.textAlign(p5.RIGHT);
		p5.text(p5.nf(overallFinishedRatio * 100, 0, 2) + " %", totalsGraphStart.x - 5, totalsGraphStart.y - 30 + (barHeight * 0.75f));
		p5.textAlign(p5.LEFT);
		p5.text(p5.nf( (1 - overallFinishedRatio) * 100,   0, 2) + " %", totalsGraphStop.x + 5, totalsGraphStart.y - 30 + (barHeight * 0.75f));
		
		
		
		// SINGLE EVENT OVERALL/NON BAR
		for (int i = 0; i < eventCounts.length; i++) {
			//float ratio = (eventCounts[i][0] + eventCounts[i][1]) / eventCounts[i].length;
			float barSize = totalsGraphStop.x - totalsGraphStart.x;
			float activeRatio = eventCounts[i][0] / (eventCounts[i][0] + (float)eventCounts[i][1]);
			float activeBarSize = activeRatio * barSize;
			
			// BAR BACKG = (CAN ONLY SEE) INACTIVE PORTION 
			p5.noStroke();
			p5.fill(events.get(i).color,50);
			p5.rect(totalsGraphStart.x, barY, barSize, barHeight);
			
			// VAR ACTIVE PORTION
			p5.fill(events.get(i).color);
			p5.rect(totalsGraphStart.x, barY, activeBarSize, barHeight);
			
			// EVENT NAME
			p5.textAlign(p5.LEFT);
			p5.fill(250);
			p5.text(eventName[i], timelineStart.x, barY + (barHeight * 0.75f));
			
			// %
			p5.textAlign(p5.RIGHT);
			p5.text(p5.nf(activeRatio * 100, 0, 2) + " %", totalsGraphStart.x - 5, barY + (barHeight * 0.75f));
			p5.textAlign(p5.LEFT);
			p5.text(p5.nf( (1 - activeRatio) * 100,   0, 2) + " %", totalsGraphStop.x + 5, barY + (barHeight * 0.75f));
			
			p5.stroke(colorFore);
			p5.line(timelineStart.x, barY, totalsGraphStop.x + 20, barY);
			
			barY += barHeight;
		}

	}

	public void keyPressed(char _key) {

		// PRINT OUT EVENTS
		/*
		for (int i = 0; i < events.size(); i++) {
			VizEvent actualEvent = events.get(i);
			p5.println("--|| " + actualEvent.getId() + " :: " + actualEvent.getStartTimeAsString() + " - " + actualEvent.getName());
		}
		*/
		
		if(_key == 'd'){
			
		}
	}

	static public float mapLongs(long value, long start1, long stop1, float start2, float stop2) {
		return start2 + (stop2 - start2) * ((value - start1) / (float) (stop1 - start1));
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
