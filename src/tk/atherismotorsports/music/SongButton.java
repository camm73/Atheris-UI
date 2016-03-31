package tk.atherismotorsports.music;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class SongButton extends JButton{
	private static final long serialVersionUID = -3753774576995824513L;
	
	private MusicPlayer musicPlayer;
	private String buttonText;
	protected Dimension buttonSize = new Dimension(250, 40);
	public final int id;

	public SongButton(MusicPlayer mp, String text, final int id){
		this.musicPlayer = mp;
		this.id = id;
		buttonText = text;
		this.setText(buttonText);
		musicPlayer.inPlaylist = false;
		//System.out.println(text);
		setFont(new Font("Arial", Font.BOLD, 14));
		setMaximumSize(buttonSize);
		setPreferredSize(buttonSize);
		this.setBackground(musicPlayer.grayBack);
		this.setForeground(Color.red);
		this.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//System.out.println(buttonText);
				musicPlayer.playSong(buttonText, 0, false);
				musicPlayer.songNum = id;
				musicPlayer.seekBar.setValue(0);
				musicPlayer.songTime = 0;
			}
		});
	}
	
	/*public SongButton(MusicPlayer music, String text, final int id){
		this.fxmp = music;
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
				musicPlayer.seekBar.setValue(0);
				System.out.println("Value: " + musicPlayer.seekBar.getValue());
				musicPlayer.songTime = 0;
				musicPlayer.songNum = id;
				musicPlayer.playSong(buttonText, 0);
				
				//TODO Change this
			}
		});
	}*/

}
