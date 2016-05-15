package tk.atherismotorsports.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tk.atherismotorsports.Main;
import tk.atherismotorsports.Splash;
import tk.atherismotorsports.music.MusicPlayer;

public class CreateUser {
	
	public final int WIDTH = 1366;
	public final int HEIGHT = 768;
	
	public JFrame frame;
	public JPanel panel;
	public JPanel mainPanel;
	public JPanel topPanel;
	
	public JLabel nameLabel = new JLabel("Your Name: ");
	public JTextField nameField = new JTextField(40);
	public JButton nextButton = new JButton("Next");
	public JButton backButton = new JButton();
	
	public BufferedImage backImage;
	
	public UserProfiles userProfiles;
	
	public CreateUser(UserProfiles userProfiles){
		this.userProfiles = userProfiles;
		loadImage();
		content();
		createFrame();
	}
	
	public void loadImage(){
		try{
			backImage = ImageIO.read(CreateUser.class.getResource("/images/back arrow.png"));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void createFrame(){
		frame = new JFrame();
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setUndecorated(true);
		frame.add(panel);
		frame.setVisible(true);
	}
	
	public void content(){
		panel = new JPanel(new BorderLayout());
		panel.add(getTopBar(), BorderLayout.NORTH);
		panel.add(getMainPanel(), BorderLayout.CENTER);
	}
	
	public JComponent getTopBar(){
		topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(MusicPlayer.grayBack);
		
		backButton.setBackground(MusicPlayer.grayBack);
		backButton.setIcon(new ImageIcon(backImage));
		topPanel.add(backButton, BorderLayout.WEST);
		backButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new UserProfiles();
				frame.dispose();
			}
		});
		return topPanel;
	}

	public JComponent getMainPanel(){
		mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBackground(MusicPlayer.grayBack);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
		nameLabel.setFont(new Font("Arial", Font.PLAIN, 26));
		nameLabel.setForeground(Color.red);
		mainPanel.add(nameLabel, c);
		
		c.gridx++;
		
		nameField.setForeground(Color.red);
		nameField.setBackground(MusicPlayer.grayBack);
		nameField.requestFocus();
		nameField.setFont(new Font("Arial", Font.PLAIN, 26));
		mainPanel.add(nameField, c);
		
		c.gridy++;
		
		nextButton.setFont(new Font("Arial", Font.PLAIN, 28));
		nextButton.setBackground(MusicPlayer.grayBack);
		nextButton.setForeground(Color.red);
		mainPanel.add(nextButton, c);
		nextButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				nameField.setEditable(false);
				createUser(nameField.getText());
				Main.user = nameField.getText();
				new Splash();
				frame.dispose();
			}
		});
		
		return mainPanel;
	}
	
	public void createUser(String user){
		
		File userDir = new File(userProfiles.userFolder + "/" + user + "/");
		
		if(userDir.mkdirs()){
			System.out.println("Successfully created user directory");
		}else{
			System.out.println("Problem creating user dir");
		}
	}
	
}
