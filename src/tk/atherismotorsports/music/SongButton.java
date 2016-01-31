package tk.atherismotorsports.music;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class SongButton extends JButton{
	private static final long serialVersionUID = -3753774576995824513L;
	
	private NewMusicPlayer musicPlayer;
	private String buttonText;
	protected Dimension buttonSize = new Dimension(250, 30);
	public final int id;

	public SongButton(NewMusicPlayer mp, String text, final int id){
		this.musicPlayer = mp;
		this.id = id;
		buttonText = text;
		this.setText(buttonText);
		//System.out.println(text);
		setMaximumSize(buttonSize);
		setPreferredSize(buttonSize);
		this.setBackground(musicPlayer.grayBack);
		this.setForeground(Color.red);
		this.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println(buttonText);
				musicPlayer.playSong(buttonText);
				musicPlayer.songNum = id;
			}
		});
	}

}
