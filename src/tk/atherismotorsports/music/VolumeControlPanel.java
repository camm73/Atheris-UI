package tk.atherismotorsports.music;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import tk.atherismotorsports.Main;

public class VolumeControlPanel extends JPanel{

	public JButton volumeUpButton = new JButton();
	public JButton volumeDownButton = new JButton();
	public JButton muteButton = new JButton();
	
	private Dimension panelSize = new Dimension(150, 40);
	public Main main;
	public MusicPlayer musicPlayer;
	
	public VolumeControlPanel(Main main){
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
		
		volumeDownButton.setIcon(new ImageIcon(musicPlayer.volumeDownImage));
		volumeDownButton.setBackground(musicPlayer.grayBack);
		add(volumeDownButton, c);
		volumeDownButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				MusicVolume.lowerVolume();
			}
		});
		
		c.gridx++;
		
		muteButton.setIcon(new ImageIcon(musicPlayer.muteImage));
		muteButton.setBackground(musicPlayer.grayBack);
		add(muteButton, c);
		muteButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				MusicVolume.toggleMute();
			}
		});
		
		c.gridx++;
		
		volumeUpButton.setIcon(new ImageIcon(musicPlayer.volumeUpImage));
		volumeUpButton.setBackground(musicPlayer.grayBack);
		add(volumeUpButton, c);
		volumeUpButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				MusicVolume.raiseVolume();
			}
		});
	}
}
