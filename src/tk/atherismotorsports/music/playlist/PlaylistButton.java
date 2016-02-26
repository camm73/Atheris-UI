package tk.atherismotorsports.music.playlist;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import tk.atherismotorsports.music.NewMusicPlayer;

public class PlaylistButton extends JButton{
	
	public String buttonText = "";
	public NewMusicPlayer musicPlayer;
	public PlaylistManager manager;
	protected Dimension buttonSize = new Dimension(250, 30);
	
	
	public PlaylistButton(NewMusicPlayer musicPlayer, PlaylistManager pm, String text){
		this.buttonText = text;
		this.musicPlayer = musicPlayer;
		this.manager = pm;
		setText(buttonText);
		setMaximumSize(buttonSize);
		setPreferredSize(buttonSize);
		this.setBackground(musicPlayer.grayBack);
		this.setForeground(Color.red);
		this.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				manager.playlistSongs.add(buttonText);
				System.out.println("Added " + buttonText + " to playlist");
				setEnabled(false);
				//TODO update playlistScrollPanel
			}
		});
	}

}
