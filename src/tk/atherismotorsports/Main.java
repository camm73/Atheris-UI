package tk.atherismotorsports;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import javafx.application.Application;
import tk.atherismotorsports.music.NewMusicPlayer;

public class Main implements Runnable{

	public JFrame frame;
	public JPanel panel;
	
	public static final int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	public static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
	private final String title = "Atheris Motorsports";
	
	public BufferedImage backgroundImage;
	public BufferedImage musicImage;
	public BufferedImage weatherImage;
	public BufferedImage speedImage;
	public BufferedImage settingImage;
	public BufferedImage backImage;
	public BufferedImage mapImage;
	public JLabel background;
	public static JLabel timeLabel;
	public JLabel songLabel = new JLabel();
	public JButton exitButton = new JButton("Exit");
	
	public boolean running = false;
	public boolean settingsOpen = false;
	public boolean musicOpen = false;
	public Thread thread;
	
	public int alpha = 25;
	public boolean initial = true;
	
	public Weather weather;
	public NewMusicPlayer musicPlayer;
	public Settings settings;
	public Main main;
	public Time time;
	public MainPanel mp;
	public Map map;
	
	public Main(){
		main = this;
		time = new Time();
		timeLabel = new JLabel("");
		frame = new JFrame(title);
		start();
		loadImages();
		mp = new MainPanel();
		loadPanel();
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
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.setUndecorated(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void loadPanel(){
		panel = new JPanel(new BorderLayout());
		panel.add(mp, BorderLayout.CENTER);
		panel.add(getBottomPanel(), BorderLayout.SOUTH);
	}
	
	public JComponent getBottomPanel(){
		JPanel bottomPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		bottomPanel.setBackground(NewMusicPlayer.grayBack);
		
		Dimension songLabelSize = new Dimension(700,30);
		songLabel.setForeground(Color.red);
		songLabel.setPreferredSize(songLabelSize);
		songLabel.setMinimumSize(songLabelSize);
		songLabel.setMaximumSize(songLabelSize);
		songLabel.setHorizontalAlignment(SwingConstants.CENTER);
		songLabel.setFont(new Font("STENCIL", Font.PLAIN, 24));
		bottomPanel.add(songLabel, c);
		
		return bottomPanel;
	}
	
	
	private void loadImages(){
		try{
		backgroundImage = ImageIO.read(Main.class.getResource("/images/Atheris background.png"));
		musicImage = ImageIO.read(Main.class.getResource("/images/music image.png"));
		weatherImage = ImageIO.read(Main.class.getResource("/images/weather image.png"));
		speedImage = ImageIO.read(Main.class.getResource("/images/speed image.png"));
		settingImage = ImageIO.read(Main.class.getResource("/images/setting image.png"));
		backImage = ImageIO.read(Main.class.getResource("/images/back arrow.png"));
		mapImage = ImageIO.read(Main.class.getResource("/images/map image.png"));
		}catch(IOException e){
			e.printStackTrace();
		}
		
		background = new JLabel(new ImageIcon(backgroundImage));
	}
	
	public void update(){
		time.update();
		
		if(settingsOpen){
			settings.update();
		}
		if(musicOpen){
			musicPlayer.update();
		}
		if(musicOpen){
			songLabel.setText("Now Playing: " + NewMusicPlayer.songTitle + " -- " + musicPlayer.artistName);
		}
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
				timeLabel.setText(Time.timeString);
				
				frame.repaint();
				frame.revalidate();
			}
		}
		stop();
	}
	
	class MainPanel extends JPanel{
		
		public MainPanel(){
			setLayout(new GridBagLayout());
			content();
		}
		
		public void content(){
			JButton musicButton = new JButton();
			JButton weatherButton = new JButton();
			JButton speed = new JButton();
			JButton settingsButton = new JButton();
			JButton mapButton = new JButton();
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1.0;
			c.weighty = 1.0;
			
			musicButton.setBackground(new Color(56, 56, 56, alpha));
			musicButton.setIcon(new ImageIcon(musicImage));
			musicButton.setBorderPainted(false);
			musicButton.setOpaque(true);
			add(musicButton, c);
			musicButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(initial){
						frame.setAlwaysOnTop(false);
						musicPlayer.frame.setVisible(true);
						musicPlayer.frame.setAlwaysOnTop(true);
						musicOpen = true;
						initial = false;
					}else{
						frame.setAlwaysOnTop(false);
						musicPlayer.frame.setVisible(true);
						musicPlayer.frame.setAlwaysOnTop(true);
					}
				}
			});
			
			c.gridy++;
			
			weatherButton.setBackground(new Color(56, 56, 56, alpha));
			weatherButton.setBorderPainted(false);
			weatherButton.setIcon(new ImageIcon((weatherImage)));
			weatherButton.setOpaque(true);
			add(weatherButton, c);
			weatherButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.setAlwaysOnTop(true);
					weather = new Weather(main);
					while(!weather.frameDone){
						System.out.println("Weather frame loading");
					}
					frame.setAlwaysOnTop(false);
					weather.weatherFrame.setAlwaysOnTop(true);
				}
			});
			
			c.gridy = 1;
			c.gridx = 1;
			
			mapButton.setBackground(new Color(56, 56, 56, alpha));
			mapButton.setOpaque(true);
			mapButton.setBorderPainted(false);
			mapButton.setIcon(new ImageIcon(mapImage));
			add(mapButton, c);
			mapButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					map.mapFrame.setAlwaysOnTop(true);
					frame.setAlwaysOnTop(false);
				}
			});
			
			c.gridy = 1;
			c.gridx = 2;
			
			speed.setBackground(new Color(56, 56, 56, alpha));
			speed.setOpaque(true);
			speed.setBorderPainted(false);
			speed.setIcon(new ImageIcon((speedImage)));
			add(speed, c);
			
			c.gridy++;
			
			settingsButton.setBackground(new Color(56, 56, 56, alpha));
			settingsButton.setBorderPainted(false);
			settingsButton.setOpaque(true);
			settingsButton.setIcon(new ImageIcon((settingImage)));
			add(settingsButton, c);
			settingsButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					settings = new Settings(main);
					frame.setAlwaysOnTop(false);
					settingsOpen = true;
				}
			});
			
			c.gridx = 1;
			c.gridy = 0;
			c.weighty = 0.0;
			
			timeLabel.setForeground(Color.white);
			timeLabel.setFont(new Font("Stencil", Font.PLAIN, 32));
			add(timeLabel, c);
			
			c.gridx = 3;
			c.weightx = 0.0;
			
			exitButton.setBackground(musicPlayer.grayBack);
			exitButton.setForeground(Color.red);
			add(exitButton, c);
			exitButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					System.exit(0);
				}
			});
			
			c.gridy = 4;
			c.gridx = 1;
			
			
		}
		
		public void paintComponent(Graphics g){
			g.drawImage(backgroundImage, 0, 0, null);
		}
	}
}
