package tk.atherismotorsports.music.playlist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.Port;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import tk.atherismotorsports.Main;
import tk.atherismotorsports.music.MusicPlayer;

public class PlaylistManager {
	
	public final int WIDTH = Main.WIDTH;
	public final int HEIGHT = Main.HEIGHT;
	
	public MusicPlayer musicPlayer;
	public PlaylistManager manager;
	public JFrame frame;
	public JPanel panel;
	public JPanel centerPanel;
	public JPanel topPanel;
	public JPanel buttonPanel;
	public JPanel songPanel;
	public JPanel playlistPanel;
	public JLabel songLabel = new JLabel("Choose songs below");
	public JLabel playlistLabel = new JLabel("Songs in playlist");
	public JButton backButton = new JButton();
	public JButton createPlaylist = new JButton("Create Playlist");
	
	public JScrollPane localSongScroll;
	public JScrollPane playlistScroll;
	public GridBagConstraints playlistC;
	public ArrayList<JButton> localSongButtons = new ArrayList<JButton>();
	public ArrayList<String> playlistSongs = new ArrayList<String>();
	public JTextField playlistNameField = new JTextField(30);
	
	public PlaylistManager(MusicPlayer musicPlayer){
		this.musicPlayer = musicPlayer;
		this.manager = this;
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
				PlaylistPrompt prompt = new PlaylistPrompt(manager);
				frame.setEnabled(false);
				//TODO add confirmation that you want to exit without saving playlist
			}
		});
		
		return topPanel;
	}
	
	public JComponent getCenterPanel(){
		centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setBackground(musicPlayer.grayBack);
		GridBagConstraints c  = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
		//TODO add label that says songs to add
		songLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		songLabel.setForeground(Color.red);
		centerPanel.add(songLabel, c);
		
		c.gridx++;
		
		Dimension nameSize = new Dimension(175, 25);
		playlistNameField.setPreferredSize(nameSize);
		playlistNameField.setMaximumSize(nameSize);
		playlistNameField.setMinimumSize(nameSize);
		playlistNameField.setBackground(musicPlayer.grayBack);
		playlistNameField.setForeground(Color.red);
		centerPanel.add(playlistNameField, c);
		
		c.gridx++;
		
		playlistLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		playlistLabel.setForeground(Color.red);
		centerPanel.add(playlistLabel, c);
		
		c.gridx = 0;
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
		c.anchor = GridBagConstraints.CENTER;
		
		createPlaylist.setForeground(Color.red);
		createPlaylist.setBackground(MusicPlayer.grayBack);
		buttonPanel.add(createPlaylist, c);
		createPlaylist.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(playlistNameField.getText().trim() != "" && !musicPlayer.playlistFolderNames.contains(playlistNameField.getText().trim())){
					createPlaylistFolder();
					updateMusicPlaylist();
					frame.dispose();
				}else if(musicPlayer.playlistFolderNames.contains(playlistNameField.getText().trim())){
					//TODO warn the person that they can't create folder with same name.
				}else{
					//TODO give warning that you need to enter name
				}
			}
		});
		
		return buttonPanel;
	}
	
	public JComponent getSongScroll(){
		songPanel = new JPanel(new GridBagLayout());
		Dimension songSize = new Dimension(400, 600);
		songPanel.setBackground(musicPlayer.grayBack);
		localSongScroll = new JScrollPane(songPanel);
		localSongScroll.setViewportView(songPanel);
		localSongScroll.setPreferredSize(songSize);
		localSongScroll.setMaximumSize(songSize);
		localSongScroll.setMinimumSize(songSize);
		localSongScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 1.0;
		
		for(int i = 0; i < musicPlayer.songButtons.size(); i++){
			localSongButtons.add(new CreatePlaylistButton(musicPlayer, this, musicPlayer.songButtons.get(i).getText()));
			System.out.println(i);
			songPanel.add(localSongButtons.get(i), c);
			c.gridy++;
		}
		localSongScroll.repaint();
		
		return localSongScroll;
	}
	
	public JComponent getPlaylistScroll(){
		playlistPanel = new JPanel(new GridBagLayout());
		playlistPanel.setBackground(musicPlayer.grayBack);
		Dimension playlistSize = new Dimension(400, 600);
		playlistScroll = new JScrollPane(playlistPanel);
		playlistScroll.setMaximumSize(playlistSize);
		playlistScroll.setPreferredSize(playlistSize);
		playlistScroll.setMinimumSize(playlistSize);
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
	
	public void createPlaylistFolder(){
		File playlistFolder = new File(musicPlayer.playlistDirectory + "/" + playlistNameField.getText() + "/");
		if(playlistFolder.mkdirs()){
			System.out.println("Succesfful in creating playlist " + playlistNameField.getText());
			writePlaylistSongs(playlistFolder);
		}else{
			System.out.println("Error creating playist " + playlistNameField.getText());
		}
	}
	
	public void writePlaylistSongs(File folder){
		File playlistContent = new File(folder + "/playlistContent.txt");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(playlistContent));
			
			for(int i = 0; i < playlistSongs.size(); i++){
				writer.write(playlistSongs.get(i));
				writer.newLine();
			}
			System.out.println("Wrote playlists to file");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateMusicPlaylist(){
		musicPlayer.loadFiles();
		musicPlayer.getPlaylistButtons();
		musicPlayer.internalPanel.removeAll();
		musicPlayer.getPlaylistScroll();
		//musicPlayer.playlistScroll.repaint();
		musicPlayer.internalPanel.repaint();
		
	}

}
