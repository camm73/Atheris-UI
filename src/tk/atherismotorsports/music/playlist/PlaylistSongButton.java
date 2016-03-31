package tk.atherismotorsports.music.playlist;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import tk.atherismotorsports.music.MusicPlayer;

public class PlaylistSongButton extends JButton {
	private static final long serialVersionUID = 1L;

	private MusicPlayer musicPlayer;
	protected Dimension buttonSize = new Dimension(250, 40);
	protected int id;
	
	public PlaylistSongButton(MusicPlayer musicPlayer, String text, int id){
		this.musicPlayer = musicPlayer;
		this.id = id;
		musicPlayer.inPlaylist = true;
		setText(text);
		setForeground(Color.red);
		setFont(new Font("Arial", Font.BOLD, 14));
		setMaximumSize(buttonSize);
		setPreferredSize(buttonSize);
		setBackground(MusicPlayer.grayBack);
		
		setActionListener();
	}

	private void setActionListener() {
		addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				musicPlayer.playSong(getText(), 0, true);
				musicPlayer.songNum = id;
				musicPlayer.seekBar.setValue(0);
				musicPlayer.songTime = 0;
			}
		});
	}
}
