package tk.atherismotorsports.music.playlist;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

import tk.atherismotorsports.music.MusicPlayer;

public class CreatePlaylistButton extends JButton{

	private static final long serialVersionUID = 1L;
	public String buttonText = "";
	public MusicPlayer musicPlayer;
	public PlaylistManager manager;
	protected Dimension buttonSize = new Dimension(375, 30);
	
	
	public CreatePlaylistButton(MusicPlayer musicPlayer, PlaylistManager pm, String text){
		this.buttonText = text;
		this.musicPlayer = musicPlayer;
		this.manager = pm;
		setText(buttonText);
		setMaximumSize(buttonSize);
		setPreferredSize(buttonSize);
		setMinimumSize(buttonSize);
		this.setBackground(musicPlayer.grayBack);
		this.setForeground(Color.red);
		this.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				manager.playlistSongs.add(buttonText);
				manager.playlistLabels.add(new JLabel(buttonText));
				System.out.println("Added " + buttonText + " to playlist");
				setEnabled(false);
				manager.updatePlaylistScroll();
			}
		});
	}

}
