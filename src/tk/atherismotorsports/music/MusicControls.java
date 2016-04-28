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

public class MusicControls extends JPanel{
	
	public JButton playPause = new JButton();
	public JButton skipForward = new JButton();
	public JButton skipBackward = new JButton();
	
	public BufferedImage playImage;
	public BufferedImage skipForwardImage;
	public BufferedImage skipBackwardImage;
	
	public Main main;
	public MusicPlayer musicPlayer;
	private Dimension panelSize = new Dimension(200, 40);
	
	public MusicControls(Main main){
		this.main = main;
		this.musicPlayer = main.musicPlayer;
		loadButtonImages();
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
		skipBackward.setIcon(new ImageIcon(skipBackwardImage));
		add(skipBackward, c);
		skipBackward.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
			}
		});
		
		c.gridx++;
		
		playPause.setBackground(MusicPlayer.grayBack);
		playPause.setForeground(Color.red);
		playPause.setIcon(new ImageIcon(playImage));
		add(playPause, c);
		playPause.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				musicPlayer.playToggleActions();
			}
		});
		
		c.gridx++;
		
		skipForward.setBackground(MusicPlayer.grayBack);
		skipForward.setForeground(Color.red);
		skipForward.setIcon(new ImageIcon(skipForwardImage));
		add(skipForward, c);
		skipForward.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				musicPlayer.skipForwardActions();
			}
		});
	}
	
	public void loadButtonImages(){
		try{
			playImage = ImageIO.read(MusicControls.class.getResource("/images/playPauseButton.png"));
			skipForwardImage = ImageIO.read(MusicControls.class.getResource("/images/skipForwardButton.png"));
			skipBackwardImage = ImageIO.read(MusicControls.class.getResource("/images/skipBackButton.png"));			
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
