package tk.atherismotorsports;

import javax.swing.SwingUtilities;

public class Start {

	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new Splash();
				//Main main = new Main();
				//main.frame.setVisible(false);
				//MusicPlayer mp = new MusicPlayer(main);
			}
		});
	}
}
