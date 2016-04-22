package tk.atherismotorsports.music;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MusicControls extends JPanel{
	
	public JButton playPause = new JButton();
	public JButton stop = new JButton();
	public JButton skipForward = new JButton();
	public JButton skipBackward = new JButton();
	
	public MusicControls(){
		setBackground(MusicPlayer.grayBack);
		setLayout(new GridBagLayout());
		GridBagConstraints c =  new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
		skipBackward.setBackground(MusicPlayer.grayBack);
		skipBackward.setForeground(Color.red);
		add(skipBackward, c);
		skipBackward.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//TODO create skip back method; stop, then change current and play again
			}
		});
		
		c.gridx++;
		
		playPause.setBackground(MusicPlayer.grayBack);
		playPause.setForeground(Color.red);
		add(playPause, c);
	}

}
