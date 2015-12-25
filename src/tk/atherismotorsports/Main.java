package tk.atherismotorsports;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main{

	private static final long serialVersionUID = 1L;

	private JFrame frame;
	
	private final int WIDTH = 800;
	private final int HEIGHT = 480;
	private final String title = "Atheris Motorsports";
	
	public BufferedImage backgroundImage;
	public BufferedImage musicImage;
	public BufferedImage weatherImage;
	public BufferedImage speedImage;
	public BufferedImage settingImage;
	public JLabel background;
	
	public Weather weather;
	public Main main;
	
	public Main(){
		main = this;
		loadImages();
		createFrame();
	}

	
	private void createFrame(){
		JButton music = new JButton();
		JButton weatherButton = new JButton();
		JButton speed = new JButton();
		JButton settings = new JButton();
		frame = new JFrame(title);
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(background);
		frame.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		
		music.setBackground(new Color(56, 56, 56));
		music.setIcon(new ImageIcon(musicImage));
		music.setBorderPainted(false);
		frame.add(music, c);
		
		c.gridy++;
		
		weatherButton.setBackground(new Color(56, 56, 56));
		weatherButton.setBorderPainted(false);
		weatherButton.setIcon(new ImageIcon((weatherImage)));
		frame.add(weatherButton, c);
		weatherButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				weather = new Weather(main);
			}
		});
		
		c.gridy = 0;
		c.gridx++;
		
		speed.setBackground(new Color(56, 56, 56));
		speed.setBorderPainted(false);
		speed.setIcon(new ImageIcon((speedImage)));
		frame.add(speed, c);
		
		c.gridy++;
		
		settings.setBackground(new Color(56, 56, 56));
		settings.setBorderPainted(false);
		settings.setIcon(new ImageIcon((settingImage)));
		frame.add(settings, c);
		
		
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
		}catch(IOException e){
			e.printStackTrace();
		}
		
		background = new JLabel(new ImageIcon(backgroundImage));
	}
	
}
