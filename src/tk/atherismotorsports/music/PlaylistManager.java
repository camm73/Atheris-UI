package tk.atherismotorsports.music;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tk.atherismotorsports.Main;

public class PlaylistManager {
	
	public final int WIDTH = Main.WIDTH;
	public final int HEIGHT = Main.HEIGHT;
	
	public NewMusicPlayer musicPlayer;
	public JFrame frame;
	public JPanel panel;
	public JPanel centerPanel;
	public JPanel topPanel;
	
	public JTextField playlistNameField = new JTextField(40);
	
	public PlaylistManager(NewMusicPlayer musicPlayer){
		this.musicPlayer = musicPlayer;
		content();
		createFrame();
	}
	
	public void createFrame(){
		frame = new JFrame();
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(panel);
		frame.setVisible(true);
	}
	
	public void content(){
		panel = new JPanel(new BorderLayout());
		panel.setBackground(musicPlayer.grayBack);
		panel.add(getTopBar(), BorderLayout.NORTH);
		panel.add(getCenterPanel(), BorderLayout.CENTER);
	}
	
	public JComponent getTopBar(){
		topPanel = new JPanel(new BorderLayout());
		
		return topPanel;
	}
	
	public JComponent getCenterPanel(){
		centerPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c  = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1.0;
		
		
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
	
	public JComponent getSongScroll(){
		//TODO just return the song scroll from the musicplayer class except this time
		//I need to clone it to another variable and change what check boxes do.
		
		return null;
	}
	
	public JComponent getPlaylistScroll(){
		//TODO add all of the selected songs to this list
		
		return null;
	}

}
