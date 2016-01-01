package tk.atherismotorsports;

import java.util.Calendar;

import javax.swing.JPanel;

public class Time{
	
	public String hours;
	public String minutes;
	public String seconds;
	public static String timeString = "";
	
	public Time(){
		update();
	}
	
	public void update(){
		seconds = String.valueOf(Calendar.getInstance().get(Calendar.SECOND));
		if(String.valueOf(Calendar.getInstance().get(Calendar.HOUR)).equals("0")){
			hours = "12";
		}else{
			hours = String.valueOf(Calendar.getInstance().get(Calendar.HOUR));
		}
		minutes = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
		
		if(Integer.parseInt(minutes) < 10){
			minutes = "0" + String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
		}
		
		if(Integer.parseInt(seconds) < 10){
			seconds = "0" + String.valueOf(Calendar.getInstance().get(Calendar.SECOND));
		}
		
		timeString = hours + ":" + minutes + ":" + seconds;
	}
	
	

}
