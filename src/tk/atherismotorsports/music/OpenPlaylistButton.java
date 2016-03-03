package tk.atherismotorsports.music;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

public class OpenPlaylistButton extends JButton {

	protected Dimension buttonSize = new Dimension(250, 30);

	public OpenPlaylistButton(NewMusicPlayer musicPlayer, String text) {
		//TODO finish this method
		setText(text);
		setFont(new Font("Arial", Font.BOLD, 14));
		setForeground(Color.red);
		setBackground(NewMusicPlayer.grayBack);
		setMaximumSize(buttonSize);
		setMinimumSize(buttonSize);
		setPreferredSize(buttonSize);
	}

}
