package tk.atherismotorsports.music.playlist;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tk.atherismotorsports.music.MusicPlayer;

public class PlaylistPrompt {
	
	public final int WIDTH = 720;
	public final int HEIGHT = 300;
	
	public MusicPlayer musicPlayer;
	public PlaylistManager manager;
	public JFrame frame;
	public JPanel panel;
	public JLabel promptLabel = new JLabel("Are you sure you want to exit without saving?");
	public JButton yesButton = new JButton("Yes, Exit");
	public JButton noButton = new JButton("No, Stay");
	
	
	public PlaylistPrompt(PlaylistManager manager){
		this.musicPlayer = manager.musicPlayer;
		this.manager = manager;
		content();
		createFrame();
	}
	
	public void createFrame(){
		frame = new JFrame();
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setAlwaysOnTop(true);
		frame.setUndecorated(true);
		frame.add(panel);
		frame.setVisible(true);
	}
	
	public void content(){
		panel = new JPanel(new GridBagLayout());
		panel.setBackground(MusicPlayer.grayBack);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		
		promptLabel.setFont(new Font("Stencil", Font.PLAIN, 20));
		promptLabel.setForeground(Color.red);
		panel.add(promptLabel, c);
		
		c.gridy++;
		c.gridx = 2;
		
		yesButton.setForeground(Color.red);
		yesButton.setBackground(MusicPlayer.grayBack);
		panel.add(yesButton, c);
		yesButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				frame.dispose();
				manager.frame.dispose();
				musicPlayer.frame.setAlwaysOnTop(true);
			}
		});
		
		c.gridx = 0;
		
		noButton.setForeground(Color.red);
		noButton.setBackground(MusicPlayer.grayBack);
		panel.add(noButton, c);
		noButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				frame.dispose();
				manager.frame.setEnabled(true);
				manager.changeBackground(false);
			}
		});
		
		
	}

}
