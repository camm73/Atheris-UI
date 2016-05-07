package tk.atherismotorsports.weather;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

	public JLabel timeLabel;
	public JButton backButton = new JButton();

	private Main main;

	public final String weatherKey = "487f4731f8e2110fb34192e05beaa1ce";
	public String city;
	public String countryCode;
	public String zipCode;
	public String temperature;
	public String conditions;
	public String lowTemp;
	public String highTemp;

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
		weatherFrame.setVisible(true);
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
				main.weatherOpen = false;
				weatherFrame.dispose();
			}
		});

		topPanel.add(timeLabel, BorderLayout.CENTER);

		return topPanel;
	}

	public JComponent getCenterPanel() {
		centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setBackground(MusicPlayer.grayBack);

		return centerPanel;
	}

	public void update() {
		timeLabel.setText(Time.timeString);
	}

	public void getWeather() {
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
				//lowTemp = json.getString("main.temp_min");
				//highTemp = json.getString("main.temp_max");
			} else {
				System.out.println("Failed to retreive weather");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
