package tk.atherismotorsports;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tk.atherismotorsports.music.MusicPlayer;
import tk.atherismotorsports.weather.Weather;

public class Settings {

	private final int WIDTH = Main.WIDTH;
	private final int HEIGHT = Main.HEIGHT;

	private Main main;
	private Time time;
	private Weather weather;
	
	private JLabel timeLabel;

	private JFrame settingsFrame;
	private JPanel settingsPanel;
	public JPanel topPanel;
	public JPanel centerPanel;
	
	public String[] colorChoices = {"Red", "Black", "White", "Green", "Orange"};
	
	public JButton backButton = new JButton();
	public JLabel weatherLabel = new JLabel("Weather App");
	public JLabel weatherColorLabel = new JLabel("Weather Text Color: ");
	public JComboBox<String> weatherColorComboBox;
	public JButton changeWeatherColor = new JButton("Save");

	public Settings(Main main) {
		this.main = main;
		weather = main.weather;
		time = main.time;
		timeLabel = new JLabel();
		timeLabel.setForeground(Color.white);
		timeLabel.setFont(new Font("Stencil", Font.PLAIN, 32));
		weatherColorComboBox = new JComboBox<String>(colorChoices);
		setLabelDetails();
		setComboBoxDetails();
		setButtonDetails();
		update();
		content();
		createFrame();
	}
	
	public void setLabelDetails(){
		weatherLabel.setFont(new Font("Arial", Font.PLAIN, 28));
		weatherLabel.setForeground(Color.red);
		
		weatherColorLabel.setFont(new Font("Arial", Font.PLAIN, 24));
		weatherColorLabel.setForeground(Color.red);
	}
	
	public void setComboBoxDetails(){
		weatherColorComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
		weatherColorComboBox.setForeground(Color.red);
	}
	
	public void setButtonDetails(){
		changeWeatherColor.setBackground(MusicPlayer.grayBack);
		changeWeatherColor.setForeground(Color.red);
	}

	public void createFrame() {
		settingsFrame = new JFrame();
		settingsFrame.setSize(WIDTH, HEIGHT);
		settingsFrame.setResizable(false);
		settingsFrame.setLocationRelativeTo(null);
		settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		settingsFrame.setUndecorated(true);
		settingsFrame.add(settingsPanel);
		settingsFrame.setAlwaysOnTop(true);
		settingsFrame.setVisible(true);
	}
	
	public void content(){
		settingsPanel = new JPanel(new BorderLayout());
		settingsPanel.setBackground(MusicPlayer.grayBack);
		settingsPanel.add(getTopPanel(), BorderLayout.NORTH);
		settingsPanel.add(getCenterPanel(), BorderLayout.CENTER);
	}

	public JComponent getTopPanel(){
		topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(MusicPlayer.grayBack);
		
		backButton.setForeground(Color.red);
		backButton.setIcon(new ImageIcon(main.backImage));
		backButton.setBackground(MusicPlayer.grayBack);
		topPanel.add(backButton, BorderLayout.WEST);
		backButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				main.settingsOpen = false;
				main.frame.setAlwaysOnTop(true);
				settingsFrame.dispose();
			}
		});
		
		timeLabel.setHorizontalAlignment(JLabel.CENTER);
		topPanel.add(timeLabel, BorderLayout.CENTER);
		
		return topPanel;
	}
	
	public JComponent getCenterPanel(){
		centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setBackground(MusicPlayer.grayBack);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.weighty = 0;
		
		//ADD color change for text in weather
		
		centerPanel.add(weatherLabel, c);
		c.gridy++;
		c.gridx = 0;
		
		centerPanel.add(weatherColorLabel, c);
		
		c.gridx++;
		
		centerPanel.add(weatherColorComboBox, c);
		
		c.gridx++;
		
		centerPanel.add(changeWeatherColor, c);
		changeWeatherColor.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				changeWeatherColor(weatherColorComboBox.getSelectedItem().toString());
				System.out.println("Weather color changed to: " + weather.weatherColor);
			}
		});
		
		return centerPanel;
	}
	
	public void changeWeatherColor(String color){
		Color localColor;
		
		if(color.equals("Red")){
			localColor =  Color.red;
		}else if(color.equals("Black")){
			localColor = Color.black;
		}else if(color.equals("White")){
			localColor = Color.white;
		}else if(color.equals("Green")){
			localColor = Color.green;
		}else if(color.equals("Orange")){
			localColor = Color.orange;
		}else{
			localColor = Color.red;
		}
		
		weather.weatherColor = localColor;
		weather.panel.removeAll();
		weather.weatherPanel.content();
		weather.panel.repaint();
		weather.panel.revalidate();
		
		//TODO write these settings to a file based on user profile
	}
	
	public void update(){
		timeLabel.setText(Time.timeString);
	}
}
