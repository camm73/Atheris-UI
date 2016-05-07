package tk.atherismotorsports.camera;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import tk.atherismotorsports.Main;
import tk.atherismotorsports.Time;
import tk.atherismotorsports.music.MusicPlayer;

public class BackupCamera {
	
	private final int WIDTH = Main.WIDTH;
	private final int HEIGHT = Main.HEIGHT;
	
	public Main main;
	public JPanel panel;
	public JFrame frame;
	public JLabel timeLabel;
	public JButton backButton = new JButton();
	public Webcam webcam;
	public WebcamPanel webcamPanel;
	public JPanel centerPanel;
	public Dimension[] nonStandardResolutions;
	public Thread webcamThread;
	
	public boolean initial = true;

	public BackupCamera(Main main) {
		this.main = main;
		timeLabel = new JLabel(Time.timeString);
		timeLabel.setForeground(Color.white);
		timeLabel.setFont(new Font("Stencil", Font.PLAIN, 28));
		timeLabel.setHorizontalAlignment(JLabel.CENTER);
		backButton.setBackground(new Color(56, 56, 56));
		backButton.setBorderPainted(false);
		backButton.setIcon(new ImageIcon(main.backImage));
		
		content();
		createFrame();
	}
	
	public void content(){
		panel = new JPanel(new BorderLayout());
		panel.setBackground(MusicPlayer.grayBack);
		panel.add(getTopBar(), BorderLayout.NORTH);
		panel.add(getCenterPanel(), BorderLayout.CENTER);
	}
	
	public JComponent getTopBar(){
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(MusicPlayer.grayBack);
		topPanel.add(timeLabel, BorderLayout.CENTER);
		topPanel.add(backButton, BorderLayout.WEST);
		backButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				main.frame.setAlwaysOnTop(true);
				frame.dispose();
				webcam.close();
				frame.setAlwaysOnTop(false);
			}
		});
		return topPanel;
	}
	
	public JComponent getCenterPanel(){
		centerPanel = new JPanel(new BorderLayout());
		
		nonStandardResolutions = new Dimension[] {
				WebcamResolution.PAL.getSize(),
				WebcamResolution.HD720.getSize()
			};
		
		webcamThread = new Thread(new Runnable(){
			public void run(){
				if(initial){
					webcam = Webcam.getDefault();
					webcam.setCustomViewSizes(nonStandardResolutions);
					webcam.setViewSize(WebcamResolution.HD720.getSize());
				}
				initial = false;
				webcamPanel = new WebcamPanel(webcam);
				//make written stuff only available if it is enabled in settings
				webcamPanel.setFPSDisplayed(false);
				webcamPanel.setDisplayDebugInfo(false);
				webcamPanel.setImageSizeDisplayed(false);
				webcamPanel.setMirrored(true);
				centerPanel.add(webcamPanel, BorderLayout.CENTER);
			}
		});
		webcamThread.start();
		
		return centerPanel;
	}
	
	public void createFrame(){
		frame = new JFrame();
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);
		frame.add(panel);
		frame.setVisible(true);
	}
	
	public void update(){
		timeLabel.setText(Time.timeString);
		panel.repaint();
		panel.revalidate();
	}

}
