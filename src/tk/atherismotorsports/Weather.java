package tk.atherismotorsports;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import tk.atherismotorsports.music.MusicPlayer;

public class Weather {
	
	private final int WIDTH = Main.WIDTH;
	private final int HEIGHT = Main.HEIGHT;
	
	public JFrame weatherFrame;
	public JPanel panel;
	public JPanel topPanel;
	public JPanel centerPanel;
	
	public JButton backButton = new JButton();
	
	private Main main;
	
	public boolean frameDone = false;

	public Weather(Main main){
		this.main = main;
		content();
		createFrame();
		frameDone = true;
	}
	
	public void createFrame(){
		weatherFrame = new JFrame();
		weatherFrame.setSize(WIDTH, HEIGHT);
		weatherFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		weatherFrame.setLocationRelativeTo(null);
		weatherFrame.setResizable(false);
		weatherFrame.setUndecorated(true);
		weatherFrame.add(panel);
		weatherFrame.setVisible(true);
		frameDone = true;
	}
	
	public void content(){
		panel = new JPanel(new BorderLayout());
		panel.add(getTopPanel(), BorderLayout.NORTH);
		panel.add(getCenterPanel(), BorderLayout.CENTER);
	}
	
	public JComponent getTopPanel(){
		topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(MusicPlayer.grayBack);
		
		backButton.setIcon(new ImageIcon(main.backImage));
		backButton.setBackground(MusicPlayer.grayBack);
		topPanel.add(backButton, BorderLayout.WEST);
		backButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				main.frame.setAlwaysOnTop(true);
				weatherFrame.dispose();
			}
		});
		
		return topPanel;
	}
	
	public JComponent getCenterPanel(){
		centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setBackground(MusicPlayer.grayBack);
		
		return centerPanel;
	}
	
	
	
}
