package tk.atherismotorsports.music;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class OpenPlaylistButton extends JButton {

	protected Dimension buttonSize = new Dimension(250, 30);
	
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
				/*System.out.println("Opening Playlist: " + getText());
				musicPlayer.playlistPanel.remove(musicPlayer.playlistButtonPanel);
				musicPlayer.internalPanel.removeAll();
				*/
				
			}
		});
	}

}
