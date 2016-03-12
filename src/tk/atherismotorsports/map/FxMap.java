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

import com.sun.javafx.application.PlatformImpl;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import tk.atherismotorsports.Main;
import tk.atherismotorsports.Time;
import tk.atherismotorsports.music.MusicPlayer;

public class FxMap {
	
	public final int WIDTH = Main.WIDTH;
	public final int HEIGHT = Main.HEIGHT;
	
	protected JFXPanel jfxPanel;
    private Stage stage;  
    private WebView browser;
    private WebEngine webEngine;
    
    public JPanel panel;
    public JFrame frame;
    public JButton backButton = new JButton();
	public JLabel timeLabel;
	public Main main;
	
	public FxMap(Main main){
		this.main = main;
		timeLabel = new JLabel(Time.timeString);
		timeLabel.setForeground(Color.white);
		timeLabel.setFont(new Font("Stencil", Font.PLAIN, 28));
		backButton.setBackground(new Color(56, 56, 56));
		backButton.setBorderPainted(false);
		backButton.setIcon(new ImageIcon(main.backImage));
		initComponents();
		content();
		createFrame();
	}
	
	public void content(){
		panel = new JPanel(new BorderLayout());
		panel.add(getTopBar(), BorderLayout.NORTH);
		panel.add(jfxPanel, BorderLayout.CENTER);
	}
	
	public JComponent getTopBar(){
		JPanel top = new JPanel(new BorderLayout());
		//TODO finish this with time and backButton
		top.setBackground(MusicPlayer.grayBack);
		
		top.add(backButton, BorderLayout.WEST);
		backButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				frame.setAlwaysOnTop(false);
				main.frame.setAlwaysOnTop(true);
			}
		});
		
		timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		top.add(timeLabel, BorderLayout.CENTER);
		
		return top;
	}
	
	public void update(){
		timeLabel.setText(Time.timeString);
		timeLabel.repaint();
	}
	
	public void initComponents(){
		jfxPanel = new JFXPanel();
		createScene();
	}
	
	@SuppressWarnings("restriction")
	private void createScene() {  
        PlatformImpl.startup(new Runnable() {  
            @Override
            public void run() {
            	
                browser = new WebView();
                AnchorPane anchorPane = new AnchorPane();
                
                AnchorPane.setBottomAnchor(browser, 0.0);
                AnchorPane.setLeftAnchor(browser, 0.0);
                AnchorPane.setRightAnchor(browser, 0.0);
                AnchorPane.setTopAnchor(browser, 0.0);
                
                anchorPane.getChildren().add(browser);
                
                final Scene scene = new Scene(anchorPane);
                 
                // Set up the embedded browser:
                webEngine = browser.getEngine();
                webEngine.load("http://maps.google.com");
                
                jfxPanel.setScene(scene);
            }  
        });  
    }
	
	public void createFrame(){
		frame = new JFrame();
		frame = new JFrame();
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);
		frame.add(panel);
		frame.setVisible(true);
	}

}
