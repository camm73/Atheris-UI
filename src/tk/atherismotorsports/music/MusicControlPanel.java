package tk.atherismotorsports.music;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import tk.atherismotorsports.Main;

public class MusicControlPanel extends JPanel{
	
	public JButton playPause = new JButton();
	public JButton skipForward = new JButton();
	public JButton skipBackward = new JButton();
	
	public Main main;
	public MusicPlayer musicPlayer;
	private Dimension panelSize = new Dimension(150, 40);
	
	public MusicControlPanel(Main main){
		this.main = main;
		this.musicPlayer = main.musicPlayer;
		setBackground(MusicPlayer.grayBack);
		setLayout(new GridBagLayout());
		setMaximumSize(panelSize);
		loadPanel();
	}
	
	private void loadPanel(){
		GridBagConstraints c =  new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
		skipBackward.setBackground(MusicPlayer.grayBack);
		skipBackward.setForeground(Color.red);
		skipBackward.setIcon(new ImageIcon(musicPlayer.skipBackwardImage));
		add(skipBackward, c);
		skipBackward.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				musicPlayer.skipBackActions();
				main.musicOpen = true;
			}
		});
		
		c.gridx++;
		
		playPause.setBackground(MusicPlayer.grayBack);
		playPause.setForeground(Color.red);
		playPause.setIcon(new ImageIcon(musicPlayer.playImage));
		add(playPause, c);
		playPause.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				musicPlayer.playToggleActions();
				main.musicOpen = true;
			}
		});
		
		c.gridx++;
		
		skipForward.setBackground(MusicPlayer.grayBack);
		skipForward.setForeground(Color.red);
		skipForward.setIcon(new ImageIcon(musicPlayer.skipForwardImage));
		add(skipForward, c);
		skipForward.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				musicPlayer.skipForwardActions();
				main.musicOpen = true;
			}
		});
	}

}
