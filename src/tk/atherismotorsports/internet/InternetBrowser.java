package tk.atherismotorsports.internet;

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

public class InternetBrowser {
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
	
	public InternetBrowser(Main main){
		this.main = main;
		initComponents();
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
                webEngine.load("http://www.iheart.com/");
                
                jfxPanel.setScene(scene);
            }  
        });  
    }
	
	public JFXPanel getRadioPanel(){
		return jfxPanel;
	}


}
