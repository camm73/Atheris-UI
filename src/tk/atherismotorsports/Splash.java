package tk.atherismotorsports;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import tk.atherismotorsports.camera.BackupCamera;
import tk.atherismotorsports.map.FxMap;
import tk.atherismotorsports.map.Map;
import tk.atherismotorsports.music.MusicPlayer;
import tk.atherismotorsports.weather.Weather;

public class Splash extends Canvas {

	private static final long serialVersionUID = -1471791406895679571L;
	protected BufferedImage splash;
	private JFrame splashWindow;
	public boolean splashEnabled = true;
	public double splashDuration = 5000;
	public boolean initial = true;
	public boolean threadInitial = true;
	public Thread initThread;

	public Main main;
	
	int w, h;
	long old;
	int counter = 0;

	public Splash() {
		showSplashScreen();
	}

	public void showSplashScreen() {
		splashWindow = new JFrame();
		try {
			splash = ImageIO.read(Splash.class.getResource("/images/atheris splashscreen.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		w = splash.getWidth();
		h = splash.getHeight();

		splashWindow.setSize(w, h);
		splashWindow.setAlwaysOnTop(true);
		splashWindow.setUndecorated(true);
		splashWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		splashWindow.setLocationRelativeTo(null);
		splashWindow.add(this);
		splashWindow.setVisible(true);
		old = System.currentTimeMillis();
		
		initThread = new Thread(new InitRunnable());
		while (splashEnabled) {
			counter++;
			if(counter % 100 == 0){
				render();
			}
			
			if(initial){
				initThread.start();
			}
			initial = false;
		}
		
		while(threadInitial){
			//System.out.println("Waiting for thread to finish loading songs");
		}
		
		main.frame.setVisible(true);
		main.frame.setAlwaysOnTop(true);
		splashWindow.dispose();
	}

	public void render() {
		checkSplashEnabled();
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.red);
		g.drawImage(splash, 0, 0, w, h, null);
		
		g.fillRect(0, 20, counter/14, 20);
		
		
		g.dispose();
		bs.show();
	}

	public void checkSplashEnabled() {
		if ((System.currentTimeMillis() - old) > splashDuration) {
			splashEnabled = false;
		}
	}
	
	
	class InitRunnable implements Runnable{

		@Override
		public void run() {
			if(threadInitial){
				main = new Main();
				main.frame.setVisible(false);
				main.musicPlayer = new MusicPlayer(main);
				main.musicPlayer.frame.setVisible(false);
				main.panel.remove(main.getBottomPanel());
				main.panel.add(main.getBottomPanel(), BorderLayout.SOUTH);
				main.fxmap = new FxMap(main);
				main.weather = new Weather(main);
				//main.camera = new BackupCamera(main);
				//main.map = new Map(main);
				//main.camera = new BackupCamera(main);
				System.out.println("Done loading all preliminary things");
			}
			threadInitial = false;
		}
		
	}

}
