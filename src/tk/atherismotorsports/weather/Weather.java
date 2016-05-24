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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

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
	public JPanel currentPanel;
	public JPanel forecastPanel;
	public JLayeredPane layeredPane;

	public Color clear = new Color(0, 0, 0, 0);

	public JLabel timeLabel;
	public JButton backButton = new JButton();

	private Main main;
	public WeatherPanel weatherPanel;

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
	
	public final String units = "°F";
	
	public Color weatherColor = Color.red;

	public JLabel locationLabel = new JLabel();
	public JLabel currentConditionLabel = new JLabel();
	public JLabel currentTempLabel = new JLabel();
	public JLabel currentLowLabel = new JLabel();
	public JLabel currentHighLabel = new JLabel();
	public JLabel currentWindSpeedLabel = new JLabel();
	public JLabel currentWindDirLabel = new JLabel();
	public JLabel currentSunriseLabel = new JLabel();
	public JLabel currentSunsetLabel = new JLabel();
	public JLabel futureLabel = new JLabel("5 Day Forecast");

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
	
	public boolean weatherDone = false;
	public boolean failed = false;

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
		/*while(!weatherDone){
			System.out.println("Weather not finished!");
		}*/
		
		if(!failed){
			loadImages();
			createFrame();
			frameDone = true;
		}
		
		if(failed){
			main.weatherButton.setEnabled(false);
		}
	}

	public void createFrame() {
		weatherFrame = new JFrame();
		weatherFrame.setSize(WIDTH, HEIGHT);
		weatherFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		weatherFrame.setLocationRelativeTo(null);
		weatherFrame.setResizable(false);
		weatherFrame.setUndecorated(true);
		weatherFrame.add(new WeatherPanel());
		weatherFrame.setVisible(false);
		frameDone = true;
	}

	public void update() {
		timeLabel.setText(Time.timeString);
	}

	public void getWeather() {
		Thread weatherThread = new Thread(new Runnable() {
			public void run() {
				try {
					URL url = new URL("http://api.openweathermap.org/data/2.5/weather?zip=" + zipCode + "," + countryCode + "&units=imperial" + "&APPID=" + weatherKey);
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
					failed = true;
					System.out.println("Failed to retrieve primary weather information");
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
					URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?zip=" + zipCode + "," + countryCode + "&units=imperial" + "&APPID=" + weatherKey);
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
						for (int i = 0; i < days; i += 8) {
							tempList.add(json.getJSONArray("list").getJSONObject(i).getJSONObject("main").get("temp").toString());
							minList.add(json.getJSONArray("list").getJSONObject(i).getJSONObject("main").get("temp_min").toString());
							maxList.add(json.getJSONArray("list").getJSONObject(i).getJSONObject("main").get("temp_max").toString());
							conditionList.add(json.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).get("main").toString());
						}

						System.out.println("Successfully retrieved forecast");
					} else {
						System.out.println("Failed to retreive forecast");
					}
					weatherDone = true;
				} catch (IOException e) {
					failed = true;
					System.out.println("Failed to retrieve forecast information");
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

	public void loadImages() {
		try {
			clearImage = ImageIO.read(Weather.class.getResource("/images/clearBackground.png"));
			rainImage = ImageIO.read(Weather.class.getResource("/images/rainBackground.png"));
			cloudImage = ImageIO.read(Weather.class.getResource("/images/cloudBackground.png"));
			thunderImage = ImageIO.read(Weather.class.getResource("/images/thunderstormBackground.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class WeatherPanel extends JPanel {

		public WeatherPanel() {
			setBackgroundImage();
			setLayout(new BorderLayout());
			panel = this;
			weatherPanel = this;
			content();
		}

		public void content() {
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
			centerPanel = new JPanel(new BorderLayout());
			centerPanel.setBackground(clear);

			centerPanel.add(getCurrentPanel(), BorderLayout.CENTER);
			centerPanel.add(getForecastPanel(), BorderLayout.SOUTH);

			return centerPanel;
		}

		public JComponent getCurrentPanel() {
			currentPanel = new JPanel(new GridBagLayout());
			currentPanel.setBackground(clear);
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 1;
			c.gridy = 0;

			locationLabel.setFont(new Font("Arial", Font.PLAIN, 48));
			locationLabel.setForeground(weatherColor);
			locationLabel.setText(city + ", " + countryCode);
			currentPanel.add(locationLabel, c);
			
			c.gridy++;
			
			currentConditionLabel.setFont(new Font("Arial", Font.PLAIN, 42));
			currentConditionLabel.setForeground(weatherColor);
			currentConditionLabel.setText(conditions);
			
			c.gridy++;
			
			currentTempLabel.setFont(new Font("Arial", Font.PLAIN, 54));
			currentTempLabel.setForeground(weatherColor);
			currentTempLabel.setText(temperature + units);
			currentPanel.add(currentTempLabel, c);
			
			c.gridx = 0;
			c.gridy++;
			
			currentLowLabel.setFont(new Font("Arial", Font.PLAIN, 36));
			currentLowLabel.setForeground(weatherColor);
			currentLowLabel.setText("Low: " + lowTemp + units);
			currentPanel.add(currentLowLabel, c);
			
			c.gridx = 2;
			
			currentHighLabel.setFont(new Font("Arial", Font.PLAIN, 36));
			currentHighLabel.setForeground(weatherColor);
			currentHighLabel.setText("High: " + highTemp + units);
			currentPanel.add(currentHighLabel, c);
			
			c.gridx = 0;
			c.gridy++;
			
			currentWindSpeedLabel.setFont(new Font("Arial", Font.PLAIN, 28));
			currentWindSpeedLabel.setForeground(weatherColor);
			currentWindSpeedLabel.setText("Wind Speed: " + windSpeed + " mph");
			currentPanel.add(currentWindSpeedLabel, c);
			
			c.gridx = 2;
			
			
			currentWindDirLabel.setFont(new Font("Arial", Font.PLAIN, 28));
			if(windDirDegrees != null){ //for some reason this occasionally returns null
				currentWindDirLabel.setText("Wind Direction: " + getCardinal(Double.parseDouble(windDirDegrees)));
			}
			currentWindDirLabel.setForeground(weatherColor);
			currentPanel.add(currentWindDirLabel, c);
			
			c.gridx = 0;
			c.gridy++;
			
			currentSunriseLabel.setFont(new Font("Arial", Font.PLAIN, 28));
			currentSunriseLabel.setForeground(weatherColor);
			currentSunriseLabel.setText("Sunrise: " + convertUnix(sunriseTime) + " AM");
			currentPanel.add(currentSunriseLabel, c);
			
			c.gridx = 2;
			
			currentSunsetLabel.setFont(new Font("Arial", Font.PLAIN, 28));
			currentSunsetLabel.setForeground(weatherColor);
			currentSunsetLabel.setText("Sunset: " + convertUnix(sunsetTime) + " PM");
			currentPanel.add(currentSunsetLabel, c);
			
			return currentPanel;
		}

		public JComponent getForecastPanel() {
			forecastPanel = new JPanel(new GridBagLayout());
			forecastPanel.setBackground(clear);
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 2;
			c.gridy = 0;
			c.weightx = 1.0;
			
			futureLabel.setFont(new Font("Arial", Font.PLAIN, 36));
			futureLabel.setForeground(weatherColor);
			forecastPanel.add(futureLabel, c);
			
			c.gridx = 0;
			c.gridy++;
			
			for(int i = 0; i < tempList.size(); i++){
				JLabel dayTemp = new JLabel(tempList.get(i));
				dayTemp.setForeground(weatherColor);
				dayTemp.setFont(new Font("Arial", Font.PLAIN, 32));
				forecastPanel.add(dayTemp, c);
				c.gridx++;
			}
			
			c.gridy++;
			c.gridx = 0;
			
			for(int i = 0; i < conditionList.size(); i++){
				JLabel dayCondition = new JLabel(conditionList.get(i));
				dayCondition.setFont(new Font("Arial", Font.PLAIN, 28));
				dayCondition.setForeground(weatherColor);
				forecastPanel.add(dayCondition, c);
				c.gridx++;
			}
			return forecastPanel;
		}
		
		public String getCardinal(double deg){
			String cardinal = "";
			
			if(deg > 315.0 || deg <= 45.0){
				cardinal = "North";
			}else if(deg > 45.0 && deg <= 135.0){
				cardinal = "East";
			}else if(deg > 135.0 && deg <= 225.0){
				cardinal = "South";
			}else{
				cardinal = "West";
			}
			
			return cardinal;
		}
		
		public String convertUnix(String time){
			String newTime = "";
			
			long unixTime= Long.parseLong(time);
			Date date = new Date(unixTime*1000L);
			SimpleDateFormat sdf = new SimpleDateFormat("K:mm");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT-7"));
			newTime = sdf.format(date);
			return newTime;
		}

		protected void setBackgroundImage() {
			if (conditions != null) {
				if (conditions.equals("Clear")) {
					background = clearImage;
				} else if (conditions.equals("Clouds")) {
					background = cloudImage;
				} else if (conditions.equals("Rain")) {
					background = rainImage;
				} else if (conditions.equals("Extreme")) {
					background = thunderImage;
				} else if(conditions.equals("Mist")){ 
					background = rainImage;
				}else{
					System.out.println("None of the above conditions; Conditions: " + conditions);
				}
			}
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(background, 0, 0, null);
		}
	}

}
