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
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import tk.atherismotorsports.Main;
import tk.atherismotorsports.Splash;
import tk.atherismotorsports.music.MusicPlayer;

public class UserProfiles {

	public final int WIDTH = Main.WIDTH;
	public final int HEIGHT = Main.HEIGHT;

	public JFrame frame;
	public JPanel panel;
	public JPanel centerPanel;
	public JPanel titlePanel;

	public JScrollPane scrollPane;
	public JButton addUserButton;
	public JButton noUser;

	public ArrayList<File> userArray;
	public ArrayList<JButton> userButtons;

	public File userFolder;

	public JLabel titleLabel = new JLabel("Atheris");
	public JLabel instructLabel = new JLabel("Please choose your user profile");

	public BufferedImage plusImage;
	public BufferedImage headImage;
	public UserProfiles userProfile = this;
	
	public boolean userDone = false;

	public UserProfiles() {
		loadImages();
		addUserButton = new JButton("Add User");
		addUserButton.setBackground(MusicPlayer.grayBack);
		addUserButton.setForeground(Color.red);
		addUserButton.setFont(new Font("Arial", Font.PLAIN, 28));
		addUserButton.setHorizontalTextPosition(SwingConstants.CENTER);
		addUserButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		addUserButton.setIcon(new ImageIcon(plusImage));

		noUser = new JButton("Continue Without Signing In");
		noUser.setBackground(MusicPlayer.grayBack);
		noUser.setForeground(Color.red);
		noUser.setFont(new Font("Arial", Font.PLAIN, 28));
		noUser.setHorizontalAlignment(SwingConstants.CENTER);
		noUser.setHorizontalTextPosition(SwingConstants.CENTER);

		titleLabel.setForeground(Color.red);
		titleLabel.setFont(new Font("Alien Resurrection", Font.PLAIN, 42));

		instructLabel.setForeground(Color.red);
		instructLabel.setFont(new Font("Arial", Font.PLAIN, 32));

		loadFiles();
		loadUsers();
		loadUserButtons();
		content();
		createFrame();
	}

	public void loadFiles() {
		JFileChooser jfc = new JFileChooser();
		String directory = jfc.getCurrentDirectory().toString();

		userFolder = new File(directory + "/Atheris Users/");
		
		if(!userFolder.exists()){
			if(userFolder.mkdirs()){
				System.out.println("Created master user file");
			}
		}
		
		Main.userFolder = userFolder;
	}

	public void loadImages() {
		try {
			plusImage = ImageIO.read(UserProfiles.class.getResource("/images/plusImage.png"));
			headImage = ImageIO.read(UserProfiles.class.getResource("/images/personHead.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createFrame() {
		frame = new JFrame();
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setUndecorated(true);
		frame.add(panel);
		frame.setVisible(true);
	}

	public void content() {
		panel = new JPanel(new BorderLayout());
		panel.setBackground(MusicPlayer.grayBack);
		panel.add(getTitlePanel(), BorderLayout.NORTH);
		panel.add(getScrollPane(), BorderLayout.CENTER);
		panel.add(noUser, BorderLayout.SOUTH);
		noUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.user = null;
				new Splash();
			}
		});
	}

	public JComponent getTitlePanel() {
		titlePanel = new JPanel(new GridBagLayout());
		titlePanel.setBackground(MusicPlayer.grayBack);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;

		titlePanel.add(titleLabel, c);

		c.gridy++;

		titlePanel.add(instructLabel, c);

		return titlePanel;
	}

	public JComponent getScrollPane() {
		centerPanel = new JPanel(new GridBagLayout());
		scrollPane = new JScrollPane(centerPanel);

		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		centerPanel.setBackground(MusicPlayer.grayBack);

		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;

		for (int i = 0; i < userButtons.size(); i++) {
			centerPanel.add(userButtons.get(i), c);
			c.gridx++;
		}

		centerPanel.add(addUserButton, c);
		addUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CreateUser(userProfile);
				frame.dispose();
			}
		});

		return scrollPane;
	}

	public void loadUsers() {
		userArray = new ArrayList<File>(Arrays.asList(userFolder.listFiles()));
	}

	public void loadUserButtons() {
		userButtons = new ArrayList<JButton>();
		for(int i = 0; i < userArray.size(); i++){
			userButtons.add(new UserButton(userArray.get(i).getName(), this));
		}
		userDone = true;
	}
}
