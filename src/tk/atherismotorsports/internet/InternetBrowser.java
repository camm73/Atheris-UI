package tk.atherismotorsports.internet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sun.javafx.application.PlatformImpl;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import tk.atherismotorsports.Main;
import tk.atherismotorsports.Time;
import tk.atherismotorsports.music.MusicPlayer;

public class InternetBrowser {
	public final int WIDTH = Main.WIDTH;
	public final int HEIGHT = Main.HEIGHT;

	protected JFXPanel jfxPanel;
	private Stage stage;
	private WebView browser;
	private WebEngine webEngine;

	public Browser jxBrowser;
	public BrowserView jxview;
	public JPanel jxPanel;
	public JPanel controlPanel;

	public JPanel panel;
	public JFrame frame;
	public JButton backButton = new JButton();
	public JLabel timeLabel;
	public Main main;
	public MusicPlayer musicPlayer;

	private JButton goBack = new JButton("Back");
	private JButton goForward = new JButton("Forward");
	private JButton loadButton = new JButton("Load");
	public JButton homeButton = new JButton("Home");
	private JTextField urlField = new JTextField(70);

	private boolean initial = true;
	public boolean useFx = true;
	public Thread bthread;

	public InternetBrowser(Main main) {
		this.main = main;
		this.musicPlayer = main.musicPlayer;
		timeLabel = new JLabel(Time.timeString);
		timeLabel.setForeground(Color.white);
		timeLabel.setFont(new Font("Stencil", Font.PLAIN, 28));
		backButton.setBackground(new Color(56, 56, 56));
		backButton.setBorderPainted(false);
		backButton.setIcon(new ImageIcon(main.backImage));

		if (useFx) {
			initComponents();
		} else {
			loadBrowser();
		}
		content();
		createFrame();
		initial = false;
	}

	public void initComponents() {
		jfxPanel = new JFXPanel();
		createScene();
	}

	public void content() {
		panel = new JPanel(new BorderLayout());
		panel.add(getTopBar(), BorderLayout.NORTH);
		if (useFx) {
			panel.add(jfxPanel, BorderLayout.CENTER);
		} else {
			panel.add(jxPanel, BorderLayout.CENTER);
		}
	}

	public void update() {
		timeLabel.setText(Time.timeString);

		if(jxBrowser != null){
			if (!jxBrowser.canGoBack()) {
				goBack.setEnabled(false);
			} else {
				goBack.setEnabled(true);
			}
	
			if (!jxBrowser.canGoForward()) {
				goForward.setEnabled(false);
			} else {
				goForward.setEnabled(true);
			}
		}
	}

	public JComponent getTopBar() {
		JPanel topBar = new JPanel(new BorderLayout());
		topBar.setBackground(musicPlayer.grayBack);
		topBar.add(backButton, BorderLayout.WEST);
		if (initial) {
			backButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					main.browserOpen = false;
					frame.setVisible(false);
					main.frame.setAlwaysOnTop(true);
				}
			});
		}

		timeLabel.setHorizontalAlignment(JLabel.CENTER);
		topBar.add(timeLabel, BorderLayout.CENTER);

		return topBar;
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
				webEngine.load("https://www.google.com/");

				jfxPanel.setScene(scene);
			}
		});
	}

	public void loadBrowser() {
		jxPanel = new JPanel(new BorderLayout());
		bthread = new Thread(new Runnable() {
			public void run() {
				jxBrowser = new Browser();
				jxview = new BrowserView(jxBrowser);
				jxBrowser.loadURL("http://www.google.com");
				jxPanel.add(jxview, BorderLayout.CENTER);
			}
		});
		bthread.start();

		jxPanel.add(getControlPanel(), BorderLayout.NORTH);
	}

	public JFXPanel getRadioPanel() {
		return jfxPanel;
	}

	public JComponent getControlPanel() {
		controlPanel = new JPanel(new GridBagLayout());
		controlPanel.setBackground(MusicPlayer.grayBack);
		
		//goBack.setIcon(new ImageIcon(musicPlayer.resizeArtwork(main.leftImage, BufferedImage.TYPE_INT_RGB, 30)));
		goBack.setBackground(MusicPlayer.grayBack);
		goBack.setForeground(Color.RED);

		//goForward.setIcon(new ImageIcon(musicPlayer.resizeArtwork(main.rightImage, BufferedImage.TYPE_INT_RGB, 30)));
		goForward.setBackground(MusicPlayer.grayBack);
		goForward.setForeground(Color.red);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;

		controlPanel.add(goBack, c);
		goBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jxBrowser.goBack();
			}
		});

		c.gridx++;

		controlPanel.add(goForward, c);
		goForward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jxBrowser.goForward();
			}
		});

		c.gridx++;
		
		urlField.setBackground(Color.lightGray);
		urlField.setForeground(Color.red);
		urlField.setFont(new Font("Arial", Font.PLAIN, 18));
		controlPanel.add(urlField, c);
		
		c.gridx++;
		
		loadButton.setBackground(MusicPlayer.grayBack);
		loadButton.setForeground(Color.red);
		controlPanel.add(loadButton, c);
		loadButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jxBrowser.loadURL(urlField.getText());
			}
		});
		
		c.gridx++;
		
		homeButton.setBackground(MusicPlayer.grayBack);
		homeButton.setForeground(Color.red);
		controlPanel.add(homeButton, c);
		homeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jxBrowser.loadURL("http://www.google.com");//TODO need to add this as an option to change homepage
			}
		});

		return controlPanel;
	}

	public void createFrame() {
		frame = new JFrame();
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(panel);
		frame.setVisible(true);
		System.out.println("made frame");
	}

}
