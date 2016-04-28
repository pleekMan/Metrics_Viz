package viz;

import globals.Main;
import globals.PAppletSingleton;

public class ColorPalette {
	Main p5;
	
	int[] colors;
	
	ColorPalette(int[] _colors){
		p5 = getP5();
		colors = _colors;
	}
	
	public int getCount(){
		return colors.length;
	}
	
	public int getColor(int index){
		//p5.fill(p5.color(colors[index]));
		return index < colors.length ? colors[index] : 0;
		//return p5.color(255,p5.random(255),0);
	}
	
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
