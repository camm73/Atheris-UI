package tk.atherismotorsports.music.playlist;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import tk.atherismotorsports.music.MusicPlayer;

public class RemoveSongButton extends JButton {
	private static final long serialVersionUID = 1L;
	
	private BufferedImage iconImage;
	private ArrayList<JButton> songButtons = new ArrayList<JButton>();
	protected PlaylistManager manager;
	private String songName;
	
	public RemoveSongButton(PlaylistManager manager, String song){
		this.manager = manager;
		this.songName = song;
		loadIcon();
		loadSongButtons();
		setIcon(new ImageIcon(iconImage));
		setBackground(MusicPlayer.grayBack);
		addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				for(int i = 0; i < songButtons.size(); i++){
					if(songButtons.get(i).getText().equals(songName)){
						makeChanges(i);
						break;
					}
				}
			}
		});
	}
	
	public void loadIcon(){
		try{
			iconImage = ImageIO.read(RemoveSongButton.class.getResource("/images/xButton.png"));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void loadSongButtons(){
		songButtons = manager.localSongButtons;
	}
	
	public void makeChanges(int num){
			manager.localSongButtons.get(num).setEnabled(true);
			manager.playlistPanel.remove(new JLabel(songName));
			manager.playlistPanel.repaint();
		
	}
	

}
