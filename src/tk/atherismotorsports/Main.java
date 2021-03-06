package tk.atherismotorsports;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.github.sarxos.webcam.Webcam;

import tk.atherismotorsports.camera.BackupCamera;
import tk.atherismotorsports.internet.InternetBrowser;
import tk.atherismotorsports.json.JSONObject;
import tk.atherismotorsports.map.FxMap;
import tk.atherismotorsports.map.Map;
import tk.atherismotorsports.music.MusicControlPanel;
import tk.atherismotorsports.music.MusicPlayer;
import tk.atherismotorsports.music.VolumeControlPanel;
import tk.atherismotorsports.weather.Weather;

public class Main implements Runnable {

	public JFrame frame;
	public JPanel panel;

	public static final int WIDTH = 1366;
	public static final int HEIGHT = 768;
	private final String title = "Atheris Motorsports";

	public BufferedImage backgroundImage;
	public BufferedImage musicImage;
	public BufferedImage weatherImage;
	public BufferedImage speedImage;
	public BufferedImage settingImage;
	public BufferedImage cameraImage;
	public BufferedImage leftImage;
	public BufferedImage rightImage;
	public static BufferedImage backImage;
	public BufferedImage mapImage;
	public BufferedImage internetImage;
	public JLabel background;
	public static JLabel timeLabel;
	public JLabel songLabel = new JLabel();
	public JLabel tempLabel = new JLabel();
	public JButton exitButton = new JButton("Exit");
	
	public JButton musicButton = new JButton();
	public JButton weatherButton = new JButton();
	public JButton speed = new JButton();
	public JButton settingsButton = new JButton();
	public JButton mapButton = new JButton();
	public JButton cameraButton = new JButton();
	public JButton rightButton = new JButton();

	public boolean running = false;
	public boolean settingsOpen = false;
	public boolean musicOpen = false;
	public boolean mapOpen = false;
	public boolean cameraOpen = false;
	public boolean browserOpen = false;
	public boolean weatherOpen = false;
	public Thread thread;
	
	public static String user;
	public static File userFolder;
	
	public String city;
	public String zipCode;
	public String countryCode;

	public int alpha = 25;
	public boolean initial = true;

	public Weather weather;
	public MusicPlayer musicPlayer;
	public Settings settings;
	public BackupCamera camera;
	public Main main;
	public Time time;
	public AppPanel1 app1;
	public AppPanel2 app2;
	public Map map;
	public FxMap fxmap;
	public InternetBrowser inetBrowser;
	public MusicControlPanel musicControlPanel;
	public VolumeControlPanel volumeControlPanel;

	public Main() {
		main = this;
		time = new Time();
		timeLabel = new JLabel("");
		tempLabel.setFont(new Font("Stencil", Font.PLAIN, 24));
		tempLabel.setForeground(Color.white);
		frame = new JFrame(title);
		start();
		getLocation();
		loadImages();
		app1 = new AppPanel1();
		app2 = new AppPanel2();
		loadPanel();
		createFrame();
	}

	public void getLocation() {
		//A thread must be used here to avoid wait times at startup due to slow connection
		Thread locationThread = new Thread(new Runnable(){
			public void run(){
				try{
					URL url = new URL("http://ip-api.com/json");
					URLConnection connection = url.openConnection();
				
					String line;
					StringBuilder builder = new StringBuilder();
					BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
				
					JSONObject json = new JSONObject(builder.toString());
					String status = json.getString("status");
					System.out.println("Location retrieval was: " + status);
					if(status.equals("success")){
						city = json.getString("city");
						zipCode = json.getString("zip");
						countryCode = json.getString("countryCode");
					}else{
						System.out.println("Failed to retreive location");
					}
				}catch(IOException  e){
					e.printStackTrace();
				}
			}
		});
		
		if(!locationThread.isAlive()){
			locationThread.start();
		}else{
			try {
				locationThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			locationThread.start();
		}
	}

	public synchronized void start() {
		thread = new Thread(this, "Main");
		thread.start();
		running = true;
	}

	public synchronized void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		running = false;
	}

	private void createFrame() {
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.setUndecorated(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void loadPanel() {
		panel = new JPanel(new BorderLayout());
		panel.add(getTopBar(), BorderLayout.NORTH);
		panel.add(app1, BorderLayout.CENTER);
		panel.add(getBottomPanel(), BorderLayout.SOUTH);
	}

	public JComponent getTopBar() {
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(MusicPlayer.grayBack);
		
		topPanel.add(tempLabel, BorderLayout.WEST);

		timeLabel.setForeground(Color.white);
		timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		timeLabel.setFont(new Font("Stencil", Font.PLAIN, 32));
		topPanel.add(timeLabel, BorderLayout.CENTER);

		exitButton.setBackground(musicPlayer.grayBack);
		exitButton.setForeground(Color.red);
		topPanel.add(exitButton, BorderLayout.EAST);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		return topPanel;
	}

	public JComponent getBottomPanel() {
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBackground(MusicPlayer.grayBack);

		if (musicPlayer != null) {
			bottomPanel.add(musicControlPanel = new MusicControlPanel(this), BorderLayout.WEST);
		}

		Dimension songLabelSize = new Dimension(700, 40);
		songLabel.setForeground(Color.red);
		songLabel.setPreferredSize(songLabelSize);
		songLabel.setMinimumSize(songLabelSize);
		songLabel.setMaximumSize(songLabelSize);
		songLabel.setHorizontalAlignment(SwingConstants.CENTER);
		songLabel.setFont(new Font("STENCIL", Font.PLAIN, 24));
		bottomPanel.add(songLabel, BorderLayout.CENTER);
		
		if(musicPlayer != null){
			bottomPanel.add(volumeControlPanel = new VolumeControlPanel(this), BorderLayout.EAST);
		}

		return bottomPanel;
	}

	private void loadImages() {
		try {
			backgroundImage = ImageIO.read(Main.class.getResource("/images/Atheris background.png"));
			musicImage = ImageIO.read(Main.class.getResource("/images/music image.png"));
			weatherImage = ImageIO.read(Main.class.getResource("/images/weather image.png"));
			speedImage = ImageIO.read(Main.class.getResource("/images/speed image.png"));
			settingImage = ImageIO.read(Main.class.getResource("/images/setting image.png"));
			backImage = ImageIO.read(Main.class.getResource("/images/back arrow.png"));
			mapImage = ImageIO.read(Main.class.getResource("/images/map image.png"));
			cameraImage = ImageIO.read(Main.class.getResource("/images/camera.png"));
			rightImage = ImageIO.read(Main.class.getResource("/images/right arrow.png"));
			leftImage = ImageIO.read(Main.class.getResource("/images/left arrow.png"));
			internetImage = ImageIO.read(Main.class.getResource("/images/internet image.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		background = new JLabel(new ImageIcon(backgroundImage));
	}

	int weatherCount = 0;
	public void update() {
		time.update();		
		
		weatherCount++;
		if(weather != null){
			tempLabel.setText(weather.temperature + " �F");
			if(weatherCount % 180000 == 0){//retreive weather ever 10 mins
				weather.getWeather();
				tempLabel.setText(weather.temperature + " �F");
				tempLabel.repaint();
				tempLabel.revalidate();
			}
		}
		
		if (settingsOpen) {
			settings.update();
		}
		
		if (musicOpen) {
			musicPlayer.update();
		}
		
		if (cameraOpen) {
			camera.update();
		}

		if (musicPlayer != null) {
			if (musicPlayer.iconCover != null) {
				songLabel.setIcon(new ImageIcon(musicPlayer.iconCover));
			}
			songLabel.setText("Now Playing: " + MusicPlayer.songTitle + " -- " + musicPlayer.artistName);
			songLabel.revalidate();
		}
		
		
		//add weatherOpen
		if(weatherOpen){
			weather.update();
		}

		if (mapOpen) {
			if (map != null) {
				map.update();
			}
			if (fxmap != null) {
				fxmap.update();
			}

		}

		if (browserOpen) {
			inetBrowser.update();
		}

		// System.out.println(time.hours + ":" + time.minutes + ":" +
		// time.seconds);
	}

	public void run() {
		long prev = System.nanoTime();
		final double limit = 1000000000.0 / 300.0;
		double delta = 0;

		while (running) {
			long now = System.nanoTime();
			delta += (now - prev) / limit;
			prev = now;
			while (delta >= 1) {
				frame.repaint();
				frame.revalidate();
				update();
				timeLabel.setText(Time.timeString);
				delta--;
			}
		}
		stop();
	}

	class AppPanel1 extends JPanel {
		private static final long serialVersionUID = 1L;

		public AppPanel1() {
			setLayout(new GridBagLayout());
			content();
		}

		public void content() {
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;
			c.weightx = 1.0;
			c.weighty = 1.0;

			musicButton.setBackground(new Color(56, 56, 56, alpha));
			musicButton.setIcon(new ImageIcon(musicImage));
			musicButton.setBorderPainted(false);
			musicButton.setOpaque(true);
			add(musicButton, c);
			musicButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (initial) {
						frame.setAlwaysOnTop(false);
						musicPlayer.frame.setVisible(true);
						musicPlayer.frame.setAlwaysOnTop(true);
						musicOpen = true;
						initial = false;
					} else {
						frame.setAlwaysOnTop(false);
						musicPlayer.frame.setVisible(true);
						musicPlayer.frame.setAlwaysOnTop(true);
					}
				}
			});

			c.gridy = 2;

			weatherButton.setBackground(new Color(56, 56, 56, alpha));
			weatherButton.setBorderPainted(false);
			weatherButton.setIcon(new ImageIcon((weatherImage)));
			weatherButton.setOpaque(true);
			add(weatherButton, c);
			weatherButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					weatherOpen = true;
					weather.weatherFrame.setVisible(true);
					weather.weatherFrame.setAlwaysOnTop(true);
				}
			});

			c.gridy = 0;
			c.gridx = 2;

			mapButton.setBackground(new Color(56, 56, 56, alpha));
			mapButton.setOpaque(true);
			mapButton.setBorderPainted(false);
			mapButton.setIcon(new ImageIcon(mapImage));
			add(mapButton, c);
			mapButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					mapOpen = true;
					if (map != null) {
						map.mapFrame.setAlwaysOnTop(true);
					} else {
						fxmap.frame.setAlwaysOnTop(true);
					}
					frame.setAlwaysOnTop(false);
				}
			});

			c.gridy = 2;

			cameraButton.setBackground(new Color(56, 56, 56, alpha));
			cameraButton.setBorderPainted(false);
			cameraButton.setOpaque(true);
			cameraButton.setIcon(new ImageIcon(cameraImage));
			add(cameraButton, c);
			cameraButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					camera = new BackupCamera(main);
					camera.frame.setAlwaysOnTop(true);
					cameraOpen = true;
					frame.setAlwaysOnTop(false);
				}
			});
			Webcam wc = Webcam.getDefault();
			if (wc == null) {
				cameraButton.setEnabled(false);
			}

			c.gridy = 0;
			c.gridx = 3;

			speed.setBackground(new Color(56, 56, 56, alpha));
			speed.setOpaque(true);
			speed.setBorderPainted(false);
			speed.setIcon(new ImageIcon((speedImage)));
			add(speed, c);

			c.gridy = 2;

			settingsButton.setBackground(new Color(56, 56, 56, alpha));
			settingsButton.setBorderPainted(false);
			settingsButton.setOpaque(true);
			settingsButton.setIcon(new ImageIcon((settingImage)));
			add(settingsButton, c);
			settingsButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					settings = new Settings(main);
					frame.setAlwaysOnTop(false);
					settingsOpen = true;
				}
			});

			c.gridx = 4;
			c.gridy = 1;
			c.weightx = 0.0;
			c.weighty = 0.0;

			rightButton.setBackground(musicPlayer.grayBack);
			rightButton.setIcon(new ImageIcon(rightImage));
			add(rightButton, c);
			rightButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					panel.remove(app1);
					panel.add(app2);
					panel.repaint();
				}
			});

		}

		public void paintComponent(Graphics g) {
			g.drawImage(backgroundImage, 0, 0, null);
		}
	}

	class AppPanel2 extends JPanel {

		public AppPanel2() {
			setLayout(new GridBagLayout());
			getApp2();
		}

		public void getApp2() {
			JButton leftButton = new JButton();
			JButton browserButton = new JButton();
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 0.0;
			c.weighty = 0.0;

			leftButton.setBackground(MusicPlayer.grayBack);
			leftButton.setIcon(new ImageIcon(leftImage));
			add(leftButton, c);
			leftButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					panel.remove(app2);
					panel.add(app1);
					panel.repaint();
				}
			});

			c.gridx = 1;
			c.gridy = 0;
			c.weightx = 1.0;
			c.weighty = 1.0;
			c.anchor = GridBagConstraints.WEST;

			browserButton.setBackground(new Color(56, 56, 56, alpha));
			browserButton.setIcon(new ImageIcon(internetImage));
			browserButton.setBorderPainted(false);
			browserButton.setOpaque(true);
			add(browserButton, c);
			browserButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frame.setAlwaysOnTop(false);
					if (inetBrowser == null) {
						inetBrowser = new InternetBrowser(main);
					} else {
						inetBrowser.frame.setVisible(true);
					}
					browserOpen = true;
				}
			});

		}

		public void paintComponent(Graphics g) {
			g.drawImage(backgroundImage, 0, 0, null);
		}
	}
}
