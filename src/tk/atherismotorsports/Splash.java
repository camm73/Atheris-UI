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

public class Splash extends Canvas {

	protected BufferedImage splash;
	private JFrame splashWindow;
	public boolean splashEnabled = true;
	public double splashDuration = 3500;
	

	int w, h;
	long old;
	private JProgressBar pb = new JProgressBar();
	int counter = 0;

	public Splash() {
		showSplashScreen();
	}

	private void showSplashScreen() {
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
		
		while (splashEnabled) {
			counter++;
			if(counter % 100 == 0){
				render();
			}
			
		}
		new Main();
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
		
		g.fillRect(0, 20, counter/100, 20);
		
		
		g.dispose();
		bs.show();
	}

	public void checkSplashEnabled() {
		if ((System.currentTimeMillis() - old) > splashDuration) {
			splashEnabled = false;
		}
	}
	

}
