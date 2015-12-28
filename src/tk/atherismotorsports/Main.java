package tk.atherismotorsports;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main implements Runnable{

	private static final long serialVersionUID = 1L;

	private JFrame frame;
	
	public final int WIDTH = 800;
	public final int HEIGHT = 480;
	private final String title = "Atheris Motorsports";
	
	public BufferedImage backgroundImage;
	public BufferedImage musicImage;
	public BufferedImage weatherImage;
	public BufferedImage speedImage;
	public BufferedImage settingImage;
	public BufferedImage backImage;
	public JLabel background;
	public JLabel timeLabel;
	
	public boolean running = false;
	public Thread thread;
	
	public int alpha = 25;
	
	public Weather weather;
	public MusicPlayer musicPlayer;
	public Main main;
	public Time time;
	
	public Main(){
		main = this;
		time = new Time();
		timeLabel = new JLabel(time.hours + ":" + time.minutes);
		frame = new JFrame(title);
		start();
		loadImages();
		createFrame();
	}
	
	public synchronized void start(){
		thread = new Thread(this, "Main");
		thread.start();
		running = true;
	}

	public synchronized void stop(){
		try{
			thread.join();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		running = false;
	}
	
	private void createFrame(){
		JButton musicButton = new JButton();
		JButton weatherButton = new JButton();
		JButton speed = new JButton();
		JButton settings = new JButton();
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(background);
		frame.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1.0;
		c.weighty = 1.0;
		
		musicButton.setBackground(new Color(56, 56, 56, alpha));
		musicButton.setIcon(new ImageIcon(musicImage));
		musicButton.setBorderPainted(false);
		musicButton.setOpaque(true);
		frame.add(musicButton, c);
		musicButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//musicPlayer = new MusicPlayer(main);
			}
		});
		
		c.gridy++;
		
		weatherButton.setBackground(new Color(56, 56, 56, alpha));
		weatherButton.setBorderPainted(false);
		weatherButton.setIcon(new ImageIcon((weatherImage)));
		weatherButton.setOpaque(true);
		frame.add(weatherButton, c);
		weatherButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				weather = new Weather(main);
			}
		});
		
		c.gridy = 1;
		c.gridx = 2;
		
		speed.setBackground(new Color(56, 56, 56, alpha));
		speed.setOpaque(true);
		speed.setBorderPainted(false);
		speed.setIcon(new ImageIcon((speedImage)));
		frame.add(speed, c);
		
		c.gridy++;
		
		settings.setBackground(new Color(56, 56, 56, alpha));
		settings.setBorderPainted(false);
		settings.setOpaque(true);
		settings.setIcon(new ImageIcon((settingImage)));
		frame.add(settings, c);
		
		c.gridx = 1;
		c.gridy = 0;
		
		timeLabel.setForeground(Color.white);
		timeLabel.setFont(new Font("Stencil", Font.PLAIN, 18));
		frame.add(timeLabel, c);
		
		frame.setResizable(false);
		frame.setUndecorated(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void loadImages(){
		try{
		backgroundImage = ImageIO.read(Main.class.getResource("/images/Atheris background.png"));
		musicImage = ImageIO.read(Main.class.getResource("/images/music image.png"));
		weatherImage = ImageIO.read(Main.class.getResource("/images/weather image.png"));
		speedImage = ImageIO.read(Main.class.getResource("/images/speed image.png"));
		settingImage = ImageIO.read(Main.class.getResource("/images/setting image.png"));
		backImage = ImageIO.read(Main.class.getResource("/images/back arrow.png"));
		}catch(IOException e){
			e.printStackTrace();
		}
		
		background = new JLabel(new ImageIcon(backgroundImage));
	}
	
	public void update(){
		time.update();
		//System.out.println(time.hours + ":" + time.minutes + ":" + time.seconds);
	}
	
	public void run(){
		long prev = System.nanoTime();
		final double limit = 1000000000.0 / 60.0;
		double delta = 0;
		while(running){
			long now = System.nanoTime();
			delta += (now-prev) / limit;
			prev = now;
			while(delta >= 1){
				update();
				if(Integer.parseInt(time.seconds) < 10){
					timeLabel.setText(time.hours + ":" + time.minutes + ":0" + time.seconds);
				}else{
					timeLabel.setText(time.hours + ":" + time.minutes + ":" + time.seconds);
				}
				frame.repaint();
				frame.revalidate();
			}
		}
		stop();
	}
}
