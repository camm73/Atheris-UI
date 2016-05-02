package tk.atherismotorsports.music;

import java.io.IOException;

public abstract class MusicVolume {
	
	
	public static void lowerVolume(){
		try{
			String command = "nircmd.exe changesysvolume -3000";
			Process p = Runtime.getRuntime().exec(command);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void raiseVolume(){
		try{
			String command = "nircmd.exe changesysvolume 3000";
			Process p = Runtime.getRuntime().exec(command);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void toggleMute(){
		try{
			String command = "nircmd.exe setsysvolume 0";
			Process p = Runtime.getRuntime().exec(command);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
}
