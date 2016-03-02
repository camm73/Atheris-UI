package tk.atherismotorsports.music.playlist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import tk.atherismotorsports.Main;
import tk.atherismotorsports.music.NewMusicPlayer;

public class PlaylistManager {
	
	public final int WIDTH = Main.WIDTH;
	public final int HEIGHT = Main.HEIGHT;
	
	public NewMusicPlayer musicPlayer;
	public JFrame frame;
	public JPanel panel;
	public JPanel centerPanel;
	public JPanel topPanel;
	public JPanel buttonPanel;
	public JPanel songPanel;
	public JPanel playlistPanel;
	public JButton backButton = new JButton();
	
	public JScrollPane localSongScroll;
	public JScrollPane playlistScroll;
	public GridBagConstraints playlistC;
	public ArrayList<JButton> localSongButtons = new ArrayList<JButton>();
	public ArrayList<String> playlistSongs = new ArrayList<String>();
	public JTextField playlistNameField = new JTextField(30);
	
	public PlaylistManager(NewMusicPlayer musicPlayer){
		this.musicPlayer = musicPlayer;
		backButton.setBackground(new Color(56, 56, 56));
		backButton.setBorderPainted(false);
		backButton.setIcon(new ImageIcon(Main.backImage));
		
		content();
		createFrame();
	}
	
	public void createFrame(){
		frame = new JFrame();
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		frame.add(panel);
		frame.setVisible(true);
	}
	
	public void content(){
		panel = new JPanel(new BorderLayout());
		panel.setBackground(musicPlayer.grayBack);
		panel.add(getTopBar(), BorderLayout.NORTH);
		panel.add(getCenterPanel(), BorderLayout.CENTER);
		panel.add(getButtonBar(), BorderLayout.SOUTH);
	}
	
	public JComponent getTopBar(){
		topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(musicPlayer.grayBack);
		
		topPanel.add(backButton, BorderLayout.WEST);
		backButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				musicPlayer.frame.setAlwaysOnTop(true);
				frame.dispose();
				//TODO add confirmation that you want to exit without saving playlist
			}
		});
		
		return topPanel;
	}
	
	public JComponent getCenterPanel(){
		centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setBackground(musicPlayer.grayBack);
		GridBagConstraints c  = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		
		/*Dimension nameSize = new Dimension(175, 25);
		playlistNameField.setPreferredSize(nameSize);
		playlistNameField.setMaximumSize(nameSize);
		playlistNameField.setMinimumSize(nameSize);*/
		playlistNameField.setBackground(musicPlayer.grayBack);
		playlistNameField.setForeground(Color.red);
		centerPanel.add(playlistNameField, c);
		
		c.gridx--;
		c.gridy++;
		
		centerPanel.add(getSongScroll(), c);
		
		c.gridx = 2; 
		
		centerPanel.add(getPlaylistScroll(), c);
		

		
		//on top will be a jtextfield where you can name the playlist
		//then to the left there will be a jscrollpane with all the songs
		//then to the right you will see what is in the new playlist
		
		
		return centerPanel;
	}
	
	public JComponent getButtonBar(){
		buttonPanel = new JPanel(new GridBagLayout());
		buttonPanel.setBackground(musicPlayer.grayBack);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		
		
		return buttonPanel;
	}
	
	public JComponent getSongScroll(){
		//TODO just return the song scroll from the musicplayer class except this time
		//I need to clone it to another variable and change what check boxes do.
		songPanel = new JPanel(new GridBagLayout());
		Dimension songSize = new Dimension(400, 700);
		songPanel.setPreferredSize(songSize);
		songPanel.setMaximumSize(songSize);
		songPanel.setMinimumSize(songSize);
		songPanel.setBackground(musicPlayer.grayBack);
		localSongScroll = new JScrollPane(songPanel);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
		for(int i = 0; i < musicPlayer.songButtons.size(); i++){
			localSongButtons.add(new PlaylistButton(musicPlayer, this, musicPlayer.songButtons.get(i).getText()));
			System.out.println(i);
			songPanel.add(localSongButtons.get(i), c);
			c.gridy++;
		}
		
		return localSongScroll;
	}
	
	public JComponent getPlaylistScroll(){
		playlistPanel = new JPanel(new GridBagLayout());
		playlistPanel.setBackground(musicPlayer.grayBack);
		Dimension playlistSize = new Dimension(400, 700);
		playlistPanel.setMaximumSize(playlistSize);
		playlistPanel.setPreferredSize(playlistSize);
		playlistPanel.setMinimumSize(playlistSize);
		playlistScroll = new JScrollPane(playlistPanel);
		playlistScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		playlistC = new GridBagConstraints();
		playlistC.gridx = 0;
		playlistC.gridy = 0;
		
		for(int i = 0; i < playlistSongs.size(); i++){
			playlistPanel.add(new JLabel(playlistSongs.get(i)), playlistC);
			playlistC.gridy++;
		}
		
		return playlistScroll;
	}
	
	public void updatePlaylistScroll(){
		playlistPanel.removeAll();
		playlistC.gridy = 0;
		for(int i = 0; i < playlistSongs.size(); i++){
			JLabel tempLabel = new JLabel(playlistSongs.get(i));
			tempLabel.setForeground(Color.red);
			tempLabel.setFont(new Font("Arial", Font.PLAIN, 18));
			Dimension labelSize = new Dimension(250, 35);
			tempLabel.setPreferredSize(labelSize);
			tempLabel.setMaximumSize(labelSize);
			tempLabel.setMinimumSize(labelSize);
			playlistPanel.add(tempLabel, playlistC);
			playlistC.gridy++;
		}
		playlistPanel.repaint();
		playlistPanel.revalidate();
		playlistScroll.repaint();
	}

}
