package tk.atherismotorsports.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import tk.atherismotorsports.Main;
import tk.atherismotorsports.Time;
import tk.atherismotorsports.music.MusicPlayer;

public class Map {
	
	public final int WIDTH = Main.WIDTH;
	public final int HEIGHT = Main.HEIGHT;
	
	public Main main;
	public JFrame mapFrame;
	public JPanel panel;
	
	public JButton backButton = new JButton();
	public JLabel timeLabel;
	
	public Thread browserThread;
	public Browser browser;
	public BrowserView view;
	public JPanel browserPanel;
	
	public Map(Main main){
		this.main = main;
		timeLabel = new JLabel(Time.timeString);
		timeLabel.setForeground(Color.white);
		timeLabel.setFont(new Font("Stencil", Font.PLAIN, 28));
		backButton.setBackground(new Color(56, 56, 56));
		backButton.setBorderPainted(false);
		backButton.setIcon(new ImageIcon(main.backImage));
		
		content();
		createFrame();
		
	}
	
	public void update(){
		timeLabel.setText(Time.timeString);
		timeLabel.repaint();
	}
	
	public void createFrame(){
		mapFrame = new JFrame();
		mapFrame.setSize(WIDTH, HEIGHT);
		mapFrame.setResizable(false);
		mapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mapFrame.setLocationRelativeTo(null);
		mapFrame.setUndecorated(true);
		mapFrame.add(panel);
		mapFrame.setVisible(true);
	}
	
	public void content(){
		panel = new JPanel(new BorderLayout());
		panel.add(getTopBar(), BorderLayout.NORTH);
		panel.add(getBrowserPanel(), BorderLayout.CENTER);
	}
	
	public JComponent getTopBar(){
		JPanel top = new JPanel(new BorderLayout());
		//TODO finish this with time and backButton
		top.setBackground(MusicPlayer.grayBack);
		
		top.add(backButton, BorderLayout.WEST);
		backButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mapFrame.setAlwaysOnTop(false);
				main.frame.setAlwaysOnTop(true);
			}
		});
		
		timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		top.add(timeLabel, BorderLayout.CENTER);
		
		return top;
	}
	
	public JComponent getBrowserPanel(){
		browserPanel = new JPanel(new BorderLayout());
		 
		browserThread = new Thread(new Runnable(){
			 public void run(){
				 browser = new Browser();
				 view = new BrowserView(browser);
				 browser.loadURL("maps.google.com");
				 browserPanel.add(view, BorderLayout.CENTER);
			 }
		 });
		 browserThread.start();
		 
		 
		return browserPanel;
	}
}
