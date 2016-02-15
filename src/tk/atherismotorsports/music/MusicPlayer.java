package tk.atherismotorsports.music;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import tk.atherismotorsports.Main;

public class MusicPlayer {
	
	public final static int WIDTH = Main.WIDTH;
	public final static int HEIGHT = Main.HEIGHT;
	
	public Main main;
	public JFrame frame;
	public JPanel panel;
	
	public JPanel leftPanel;
	public ArrayList<File> songList = new ArrayList<File>();
	public ArrayList<JButton> songButtons = new ArrayList<JButton>();
	public JScrollPane songScroll;
	public File musicDirectory;
	public Thread songThread;
	public int songNum = 0;
	public static String songTitle = "";
	public File songFile;
	public JLabel titleLabel = new JLabel();
	final JFXPanel fxPanel;
	
	public static Color grayBack = new Color(56, 56, 56);
	
	public MusicPlayer(Main main){
		this.main = main;
		frame = new JFrame();
		panel = new JPanel(new BorderLayout());
		fxPanel = new JFXPanel();
		handleJavaFX();
		makeDirectories();
		loadFiles();
		loadSongButtons();
		setSongTitle();
		setSongScroll();
		content();
		createFrame();
		playSong("test");
	}
	
	public void createFrame(){
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.add(panel);
		frame.add(fxPanel);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
	}
	
	public void content(){
		panel.setBackground(grayBack);
		panel.add(getLeftPanel(), BorderLayout.WEST);
	}
	
	public void handleJavaFX(){
		Platform.runLater(new Runnable(){
			public void run(){
				initFx(fxPanel);
			}
		});
	}
	
	public void initFx(JFXPanel fxPanel){
		Scene scene = new SceneGenerator().createScene();
		fxPanel.setScene(scene);
	}
	
	public JComponent getLeftPanel() {
		leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(songScroll, BorderLayout.CENTER);
		// TODO possibly a button in the west to shrink down this list
		return leftPanel;
	}
	
	public void makeDirectories() {
		JFileChooser jfc = new JFileChooser();
		String directory;
		directory = jfc.getCurrentDirectory().toString();

		musicDirectory = new File(directory + "/Atheris Music/");

		if (!musicDirectory.mkdirs()) {
			System.out.println("Error creating Atheris Music directory or it already exists");
		} else {
			System.out.println("Successfully created Atheris Music directory!");
		}
	}
	
	public void loadFiles() {
		songList = new ArrayList<File>(Arrays.asList(musicDirectory.listFiles()));
	}

	public void loadSongButtons() {
		for (int i = 0; i < songList.size(); i++) {
			//songButtons.add(new SongButton(this, songList.get(i).getName(), i));
		}
	}

	public void setSongTitle() {
		if (songFile != null) {
			String temp = songFile.getName();
			songTitle = temp.replace(".mp3", "");
			titleLabel.setText(songTitle);
		} else {
			titleLabel.setText("");
		}
	}

	public void setSongScroll() {
		JPanel songPanel = new JPanel(new GridBagLayout());
		songPanel.setBackground(grayBack);
		songScroll = new JScrollPane(songPanel);
		songScroll.setViewportView(songPanel);
		songScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		songScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTH;
		for (int i = 0; i < songButtons.size(); i++) {
			songPanel.add(songButtons.get(i), c);
			c.gridy++;
		}
	}
	
	public void playSong(String path){
		Media media = new Media(new File(musicDirectory+"/Whatsername.mp3").toURI().toString());
		MediaPlayer player = new MediaPlayer(media);
		player.play();
	}
	
	class SceneGenerator{
		
		
		public Scene createScene(){
			
			return null;
		}
	}
	
	
}
