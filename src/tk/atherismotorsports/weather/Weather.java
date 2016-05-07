package tk.atherismotorsports.weather;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import tk.atherismotorsports.Main;
import tk.atherismotorsports.Time;
import tk.atherismotorsports.json.JSONObject;
import tk.atherismotorsports.music.MusicPlayer;

public class Weather {

	private final int WIDTH = Main.WIDTH;
	private final int HEIGHT = Main.HEIGHT;

	public JFrame weatherFrame;
	public JPanel panel;
	public JPanel topPanel;
	public JPanel centerPanel;
	public JLayeredPane layeredPane;

	public JLabel timeLabel;
	public JButton backButton = new JButton();

	private Main main;
	private BackgroundPanel backgroundPanel;

	public final String weatherKey = "487f4731f8e2110fb34192e05beaa1ce";
	public String city;
	public String countryCode;
	public String zipCode;
	public String temperature;
	public String conditions;
	public String lowTemp;
	public String highTemp;
	public String humidity;
	public String windSpeed;
	public String windDirDegrees;
	public String sunriseTime;
	public String sunsetTime;
	
	public BufferedImage clearImage;
	public BufferedImage cloudImage;
	public BufferedImage rainImage;
	public BufferedImage thunderImage;
	public BufferedImage background;

	public ArrayList<String> tempList = new ArrayList<String>();
	public ArrayList<String> maxList = new ArrayList<String>();
	public ArrayList<String> minList = new ArrayList<String>();
	public ArrayList<String> conditionList = new ArrayList<String>();

	public final int days = 38;
	public boolean frameDone = false;

	public Weather(Main main) {
		this.main = main;
		this.city = main.city;
		this.zipCode = main.zipCode;
		this.countryCode = main.countryCode;
		timeLabel = new JLabel(Time.timeString);
		timeLabel.setForeground(Color.white);
		timeLabel.setFont(new Font("Stencil", Font.PLAIN, 28));
		timeLabel.setHorizontalAlignment(JLabel.CENTER);
		getWeather();
		getForecast();
		loadImages();
		content();
		createFrame();
		frameDone = true;
	}

	public void createFrame() {
		weatherFrame = new JFrame();
		weatherFrame.setSize(WIDTH, HEIGHT);
		weatherFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		weatherFrame.setLocationRelativeTo(null);
		weatherFrame.setResizable(false);
		weatherFrame.setUndecorated(true);
		weatherFrame.add(panel);
		weatherFrame.setVisible(false);
		frameDone = true;
	}

	public void content() {
		panel = new JPanel(new BorderLayout());
		panel.add(getTopPanel(), BorderLayout.NORTH);
		panel.add(getCenterPanel(), BorderLayout.CENTER);
	}

	public JComponent getTopPanel() {
		topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(MusicPlayer.grayBack);

		backButton.setIcon(new ImageIcon(main.backImage));
		backButton.setBackground(MusicPlayer.grayBack);
		topPanel.add(backButton, BorderLayout.WEST);
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.frame.setAlwaysOnTop(true);
				weatherFrame.setAlwaysOnTop(false);
				weatherFrame.setVisible(false);
				main.weatherOpen = false;
			}
		});

		topPanel.add(timeLabel, BorderLayout.CENTER);

		return topPanel;
	}

	public JComponent getCenterPanel() {
		layeredPane = new JLayeredPane();
		
		layeredPane.add(backgroundPanel = new BackgroundPanel(), JLayeredPane.DEFAULT_LAYER);
		backgroundPanel.setBounds(0, 0, 1366, 738);
		backgroundPanel.repaint();
		centerPanel = new JPanel(new GridBagLayout());
		
		layeredPane.add(centerPanel, JLayeredPane.PALETTE_LAYER);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 1.0;
		
		//TODO layout weather details
	

		return layeredPane;
	}

	public void update() {
		timeLabel.setText(Time.timeString);
	}

	public void getWeather() {
		Thread weatherThread = new Thread(new Runnable() {
			public void run() {
				try {
					URL url = new URL("http://api.openweathermap.org/data/2.5/weather?zip=" + zipCode + ","
							+ countryCode + "&units=imperial" + "&APPID=" + weatherKey);
					URLConnection connection = url.openConnection();

					String line;
					StringBuilder builder = new StringBuilder();
					BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}

					JSONObject json = new JSONObject(builder.toString());
					int status = json.getJSONObject("main").length();
					if (status > 0) {
						temperature = json.getJSONObject("main").get("temp").toString();
						conditions = json.getJSONArray("weather").getJSONObject(0).getString("main");
						lowTemp = json.getJSONObject("main").get("temp_min").toString();
						highTemp = json.getJSONObject("main").get("temp_max").toString();
						humidity = json.getJSONObject("main").get("humidity").toString();
						windSpeed = json.getJSONObject("wind").get("speed").toString();
						windDirDegrees = json.getJSONObject("wind").get("deg").toString();
						sunriseTime = json.getJSONObject("sys").get("sunrise").toString();
						sunsetTime = json.getJSONObject("sys").get("sunset").toString();

						System.out.println("Successfully retrieved data");
					} else {
						System.out.println("Failed to retreive weather");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		if (!weatherThread.isAlive()) {
			weatherThread.start();
		} else {
			try {
				weatherThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			weatherThread.start();
		}
	}

	public void getForecast() {
		Thread forecastThread = new Thread(new Runnable() {
			public void run() {
				try {
					URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?zip=" + zipCode + ","
							+ countryCode + "&units=imperial" + "&APPID=" + weatherKey);
					URLConnection connection = url.openConnection();

					String line;
					StringBuilder builder = new StringBuilder();
					BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}

					JSONObject json = new JSONObject(builder.toString());
					String status = json.get("cnt").toString();
					if (Integer.parseInt(status) > 0) {
						clearArrays();
						for (int i = 0; i < days; i += 9) {
							tempList.add(json.getJSONArray("list").getJSONObject(i).getJSONObject("main").get("temp")
									.toString());
							minList.add(json.getJSONArray("list").getJSONObject(i).getJSONObject("main").get("temp_min")
									.toString());
							maxList.add(json.getJSONArray("list").getJSONObject(i).getJSONObject("main").get("temp_max")
									.toString());
							conditionList.add(json.getJSONArray("list").getJSONObject(i).getJSONArray("weather")
									.getJSONObject(0).get("main").toString());
						}

						System.out.println("Successfully retrieved forecast");
					} else {
						System.out.println("Failed to retreive forecast");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		if (!forecastThread.isAlive()) {
			forecastThread.start();
		} else {
			try {
				forecastThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			forecastThread.start();
		}
	}

	public void clearArrays() {
		tempList.clear();
		minList.clear();
		maxList.clear();
	}
	
	public void loadImages(){
		try{
			clearImage = ImageIO.read(Weather.class.getResource("/images/clearBackground.png"));
			rainImage = ImageIO.read(Weather.class.getResource("/images/rainBackground.png"));
			cloudImage = ImageIO.read(Weather.class.getResource("/images/cloudBackground.png"));
			thunderImage = ImageIO.read(Weather.class.getResource("/images/thunderstormBackground.png"));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	class BackgroundPanel extends JPanel{
		
		public BackgroundPanel(){
			setBackgroundImage();
		}
		
		protected void setBackgroundImage(){
			if(conditions.equals("Clear")){
				background = clearImage;
			}else if(conditions.equals("Clouds")){
				background = cloudImage;
			}else if(conditions.equals("Rain")){
				background = rainImage;
			}else if(conditions.equals("Extreme")){
				background = thunderImage;
			}else{
				System.out.println("None of the above conditions");
			}
			
		}
		
		public void paintComponent(Graphics g){
			g.drawImage(background, 0, 0, null);
		}
	}

}
