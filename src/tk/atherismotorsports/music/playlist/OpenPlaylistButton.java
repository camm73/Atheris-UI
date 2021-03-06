package tk.atherismotorsports.music.playlist;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import tk.atherismotorsports.music.MusicPlayer;

public class OpenPlaylistButton extends JButton {

	protected Dimension buttonSize = new Dimension(250, 40);
	
	private MusicPlayer musicPlayer;

	public OpenPlaylistButton(MusicPlayer musicPlayer, String text) {
		this.musicPlayer = musicPlayer;
		setText(text);
		setFont(new Font("Arial", Font.BOLD, 14));
		setForeground(Color.red);
		setBackground(MusicPlayer.grayBack);
		setMaximumSize(buttonSize);
		setMinimumSize(buttonSize);
		setPreferredSize(buttonSize);
		
		setActionListener();
	}
	
	private void setActionListener(){
		addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				musicPlayer.viewPlaylistContents(getText());
				musicPlayer.currentPlaylist = getText();
				musicPlayer.openPlaylist = true;
			}
		});
	}

}
