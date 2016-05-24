package tk.atherismotorsports.music.playlist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
	public JButton savePlaylist = new JButton("Save Playlist");
	
	public JScrollPane localSongScroll;
	public JScrollPane playlistScroll;
	public GridBagConstraints playlistC;
	public ArrayList<JButton> localSongButtons = new ArrayList<JButton>();
	public ArrayList<String> playlistSongs = new ArrayList<String>();
	public ArrayList<JLabel> playlistLabels = new ArrayList<JLabel>();
	public JTextField playlistNameField = new JTextField(30);
	
	public boolean edit = false;
	public String playlistName;
	
	public PlaylistManager(MusicPlayer musicPlayer){
		this.musicPlayer = musicPlayer;
		this.manager = this;
		backButton.setBackground(new Color(56, 56, 56));
		backButton.setBorderPainted(false);
		backButton.setIcon(new ImageIcon(Main.backImage));
		
		content();
		createFrame();
	}
	
	public PlaylistManager(MusicPlayer musicPlayer, String playlistName){
		edit = true;
		this.musicPlayer = musicPlayer;
		this.manager = this;
		this.playlistName = playlistName;
		backButton.setBackground(new Color(56, 56, 56));
		backButton.setBorderPainted(false);
		backButton.setIcon(new ImageIcon(Main.backImage));
		
		loadPlaylistSongs(playlistName);
		
		content(); //need to make changes here
		createFrame();
		
		//TODO change the createPlaylistButton to say save playlist and make sure it handles correct songs to add into file
		//TODO also make sure that removing songs from playlist works correctly
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
		panel.add(getTopBar(), BorderLayout.NORTH); //ok
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
				changeBackground(true);
				frame.setEnabled(false);
			}
		});
		
		return topPanel;
	}
	
	public void changeBackground(boolean prompt){
		Color lightColor = new Color(72, 72, 72);
		if(prompt){ //lightens all panels
			topPanel.setBackground(lightColor);
			centerPanel.setBackground(lightColor);
			songPanel.setBackground(lightColor);
			playlistPanel.setBackground(lightColor);
			buttonPanel.setBackground(lightColor);
			
			for(int i = 0; i < localSongButtons.size(); i++){
				localSongButtons.get(i).setBackground(lightColor);
				localSongButtons.get(i).repaint();
			}
			
			backButton.setBackground(lightColor);
			createPlaylist.setBackground(lightColor);
			savePlaylist.setBackground(lightColor);
			playlistNameField.setBackground(lightColor);
			
		}else{ //darkens panels again
			topPanel.setBackground(MusicPlayer.grayBack);
			centerPanel.setBackground(MusicPlayer.grayBack);
			songPanel.setBackground(MusicPlayer.grayBack);
			playlistPanel.setBackground(MusicPlayer.grayBack);
			buttonPanel.setBackground(MusicPlayer.grayBack);
			
			for(int i = 0; i < localSongButtons.size(); i++){
				localSongButtons.get(i).setBackground(MusicPlayer.grayBack);
				localSongButtons.get(i).repaint();
			}
			
			backButton.setBackground(MusicPlayer.grayBack);
			createPlaylist.setBackground(MusicPlayer.grayBack);
			savePlaylist.setBackground(MusicPlayer.grayBack);
			playlistNameField.setBackground(MusicPlayer.grayBack);
		}
	}
	
	public JComponent getCenterPanel(){
		centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setBackground(MusicPlayer.grayBack);
		GridBagConstraints c  = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
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
		playlistNameField.setFont(new Font("Verdana", Font.PLAIN, 18));
		playlistNameField.setFocusable(true);
		if(edit){
			playlistNameField.setText(playlistName);
			playlistNameField.setEditable(false);//temporary until I make a method to transfer files
		}
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
		createPlaylist.setFont(new Font("Arial", Font.PLAIN, 18));
		
		savePlaylist.setForeground(Color.red);
		savePlaylist.setBackground(MusicPlayer.grayBack);
		savePlaylist.setFont(new Font("Arial", Font.PLAIN, 18));
		if(!edit){
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
		}else{
			buttonPanel.add(savePlaylist, c);
			savePlaylist.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					//TODO need to add the capability to edit playlist name; currently just adding this for the future.
					if(playlistNameField.getText().trim() != "" && !musicPlayer.playlistFolderNames.contains(playlistNameField.getText().trim())){
						playlistName = playlistNameField.getText().trim();
						createPlaylistFolder();
						updateInternalPlaylist();
						frame.dispose();
					}else if(playlistNameField.getText().trim() != "" && musicPlayer.playlistFolderNames.contains(playlistNameField.getText().trim())){
						playlistName = playlistNameField.getText().trim();
						writePlaylistSongs(new File(musicPlayer.playlistDirectory + "/" + playlistNameField.getText().trim() + "/"));
						updateInternalPlaylist();
						frame.dispose();
					}
				}
			});
		}
		
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
		if(!edit){
			for(int i = 0; i < musicPlayer.songButtons.size(); i++){
				localSongButtons.add(new CreatePlaylistButton(musicPlayer, this, musicPlayer.songButtons.get(i).getText(), true));
				System.out.println(i);
				songPanel.add(localSongButtons.get(i), c);
				c.gridy++;
			}
		}else{
			for(int i = 0; i < musicPlayer.songButtons.size(); i++){
				if(!playlistSongs.contains(musicPlayer.songButtons.get(i).getText())){
					localSongButtons.add(new CreatePlaylistButton(musicPlayer, this, musicPlayer.songButtons.get(i).getText(), true));
				}else{
					localSongButtons.add(new CreatePlaylistButton(musicPlayer, this, musicPlayer.songButtons.get(i).getText(), false));
				}
				System.out.println(i);
				songPanel.add(localSongButtons.get(i), c);
				c.gridy++;
			}
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
			JLabel tempLabel = new JLabel(playlistSongs.get(i));
			tempLabel.setForeground(Color.red);
			tempLabel.setFont(new Font("Arial", Font.PLAIN, 16));
			Dimension labelSize = new Dimension(250, 35);
			tempLabel.setPreferredSize(labelSize);
			tempLabel.setMaximumSize(labelSize);
			tempLabel.setMinimumSize(labelSize);
			playlistLabels.add(tempLabel);
			playlistPanel.add(playlistLabels.get(i), playlistC);
			
			playlistC.gridx++;
			
			playlistPanel.add(new RemoveSongButton(this, playlistSongs.get(i)), playlistC);
			
			playlistC.gridy++;
			playlistC.gridx =0;
		}
		
		return playlistScroll;
	}
	
	public void updatePlaylistScroll(){
		playlistPanel.removeAll();
		playlistLabels.clear();
		playlistC.gridy = 0;
		for(int i = 0; i < playlistSongs.size(); i++){
			playlistC.gridx = 0;
			JLabel tempLabel = new JLabel(playlistSongs.get(i));
			tempLabel.setForeground(Color.red);
			tempLabel.setFont(new Font("Arial", Font.PLAIN, 16));
			Dimension labelSize = new Dimension(250, 35);
			tempLabel.setPreferredSize(labelSize);
			tempLabel.setMaximumSize(labelSize);
			tempLabel.setMinimumSize(labelSize);
			playlistLabels.add(tempLabel);
			playlistPanel.add(playlistLabels.get(i), playlistC);
			
			playlistC.gridx++;
			
			playlistPanel.add(new RemoveSongButton(this, tempLabel.getText()), playlistC);
			
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
	
	public void loadPlaylistSongs(String playlist){
		File playlistContent = new File(musicPlayer.playlistDirectory + "/" + playlist + "/playlistContent.txt");
		playlistSongs.clear();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(playlistContent));
			String line;
			
			while((line = reader.readLine()) != null){
				playlistSongs.add(line.trim());
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
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
	
	public void updateInternalPlaylist(){
		musicPlayer.loadFiles();
		musicPlayer.getPlaylistButtons();
		musicPlayer.leftPanel.remove(musicPlayer.playlistSongPanel);
		musicPlayer.getPlaylistSongPanel(playlistName);
		musicPlayer.leftPanel.add(musicPlayer.playlistSongPanel, BorderLayout.CENTER);
		musicPlayer.playlistSongPanel.repaint();
		musicPlayer.playlistSongPanel.revalidate();
		musicPlayer.leftPanel.repaint();
	}

}
