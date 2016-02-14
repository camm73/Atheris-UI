package tk.atherismotorsports;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

import tk.atherismotorsports.music.MusicPlayer;
import tk.atherismotorsports.music.NewMusicPlayer;

public class Splash extends Canvas {

	private static final long serialVersionUID = -1471791406895679571L;
	protected BufferedImage splash;
	private JFrame splashWindow;
	public boolean splashEnabled = true;
	public double splashDuration = 3500;
	public boolean initial = true;
	public boolean threadInitial = true;
	public Thread initThread;

	public Main main = new Main();
	
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
		splashWindow.setAlwaysOnTop(false);
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
		
		g.fillRect(0, 20, counter/7, 20);
		
		
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
				main.frame.setVisible(false);
				main.musicPlayer = new NewMusicPlayer(main);
				main.musicPlayer.frame.setVisible(false);
				System.out.println("Done loading all preliminary things");
			}
			threadInitial = false;
		}
		
	}

}
