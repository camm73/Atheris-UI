package tk.atherismotorsports;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class MusicPlayer {

	private Main main;
	public JFrame musicFrame;
	public AdvancedPlayer currentPlayer;
	public Thread songThread;

	public int songNum;
	public HashMap<String, JButton> songButtons;
	public HashMap<JButton, String> oppSongButtons;
	private JButton backButton = new JButton();
	private JPanel songListPanel = new JPanel();
	// protected List<>
	private JScrollPane songScroll;

	public ArrayList<File> songList;
	public BufferedImage albumCover;
	public JLabel albumCoverLabel;
	public JPanel songInfo;

	public File musicDirectory;
	public Time time;
	public JLabel timeLabel;
	public String songFullName;
	public static String songName;
	public String editedSongName;
	public JLabel songNameLabel;

	private final int WIDTH, HEIGHT;
	public MusicPanel musicPanel;


	public MusicPlayer(Main main) {
		this.main = main;
		this.WIDTH = main.WIDTH;
		this.HEIGHT = main.HEIGHT;
		musicPanel = new MusicPanel();
		createFrame();
	}

	private void createFrame() {
		musicFrame = new JFrame();
		musicFrame.setSize(WIDTH, HEIGHT);
		musicFrame.setResizable(false);
		musicFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		musicFrame.setUndecorated(true);
		musicFrame.setLocationRelativeTo(null);
		// musicFrame.setAlwaysOnTop(true);
		musicFrame.add(musicPanel);
		musicFrame.setVisible(true);
	}
	
	public void content() {
		while(musicPanel == null){
			System.out.println("Waiting for music Panel to load");
			musicPanel = new MusicPanel();
		}
		musicPanel.add(musicPanel.getTopBar(), BorderLayout.NORTH);

		songScroll = new JScrollPane(songListPanel);
		songScroll.setViewportView(songListPanel);
		songScroll.setBackground(new Color(56, 56, 56, 255));
		songScroll.setOpaque(true);
		songScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		musicPanel.add(songScroll, BorderLayout.WEST);
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				musicFrame.setVisible(false);
				main.frame.setAlwaysOnTop(true);
			}
		});

		musicPanel.add(musicPanel.getSongInfo(), BorderLayout.EAST);

	}

	public void playSong(String songName) {
		File songFile = new File(musicDirectory + "/" + songName);
		try {
			FileInputStream fis = new FileInputStream(songFile);
			if (songThread.isAlive()) {
				currentPlayer.close();
				try {
					songThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			currentPlayer = new AdvancedPlayer(fis);
			songThread = new Thread(new SongRunnable());
			if (!songThread.isAlive()) {
				songThread.start();
			}
			setAlbumCover();
		} catch (FileNotFoundException | JavaLayerException e) {
			e.printStackTrace();
		}
	}
	
	public void setSongName(){
		songName = songFullName.replace(".mp3", "");
		editedSongName = songName.replace(" ", "+");
		songNameLabel = new JLabel(songName);
	}
	
	public void setAlbumCover(){
		String imageUrl = "http://www.avajava.com/images/avajavalogo.jpg";
	    String destinationFile = musicDirectory + "/" + editedSongName + ".jpg";

	    try {
			saveImage(imageUrl, destinationFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//key: AKIAILLGZ5FRFEIOU2NQ
	public static void saveImage(String imageUrl, String destinationFile) throws IOException {
	    URL url = new URL(imageUrl);
	    InputStream is = url.openStream();
	    OutputStream os = new FileOutputStream(destinationFile);

	    byte[] b = new byte[2048];
	    int length;

	    while ((length = is.read(b)) != -1) {
	        os.write(b, 0, length);
	    }

	    is.close();
	    os.close();
	}

	class MusicPanel extends JPanel {

		private static final long serialVersionUID = 8198695019655562023L;
		private Insets insets = getInsets();

		public MusicPanel() {
			musicPanel = this;
			setLayout(new BorderLayout());
			backButton.setBackground(new Color(56, 56, 56));
			backButton.setIcon(new ImageIcon(main.backImage));
			time = main.time;
			timeLabel = new JLabel(Time.timeString);
			timeLabel.setForeground(Color.white);
			timeLabel.setFont(new Font("Stencil", Font.PLAIN, 24));
			update();
			createMusicFolder();
			loadMusicList();
			createListPanel();
			content();
		}

		public void createMusicFolder() {
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

		public void loadMusicList() {
			// TODO use code from test maker to load array of files
			songList = new ArrayList<File>(Arrays.asList(musicDirectory.listFiles()));

			songButtons = new HashMap<String, JButton>();
			for (int i = 0; i < songList.size(); i++) {
				songButtons.put(String.valueOf(i), new SongButton(songList.get(i).getName()));
			}
			oppSongButtons = new HashMap<JButton, String>();

			for (int i = 0; i < songList.size(); i++) {
				oppSongButtons.put(songButtons.get(String.valueOf(i)), String.valueOf(i));
			}
		}

		public void createListPanel() {
			songListPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();

			songListPanel.setOpaque(true);
			songListPanel.setBackground(new Color(56, 56, 56, 0));

			c.gridx = 0;
			c.gridy = 0;

			c.weighty = 1.0;

			songThread = new Thread();
			for (int i = 0; i < songButtons.size(); i++) {
				JButton tmp = songButtons.get(String.valueOf(i));
				tmp.setBackground(new Color(50, 50, 50, 255));
				tmp.setForeground(Color.red);
				songNum = Integer.parseInt(oppSongButtons.get(tmp));
				System.out.println("songNum: " + songNum);
				songListPanel.add(tmp, c);

				c.gridy++;
			}
		}
		


		public JComponent getSongInfo() {
			songInfo = new JPanel(new GridBagLayout());
			GridBagConstraints c= new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.weighty = 1.0;
			if(albumCover != null){
				albumCoverLabel = new JLabel(new ImageIcon(albumCover));
			}else{
				System.out.println("albumCoverLabel = null");
			}
			
			if(songNameLabel != null){
				songInfo.add(songNameLabel, c);
			}else{
				System.out.println("songNameLabel = null");
			}
			
			c.gridy++;
			if(albumCoverLabel != null){
				songInfo.add(albumCoverLabel, c);
			}else{
				System.out.println("albumCoverLabel = null");
			}
			
			return songInfo;
		}

		public JComponent getTopBar() {
			JPanel top = new JPanel(new BorderLayout());
			top.setBackground(new Color(56, 56, 56, 0));
			top.setOpaque(true);
			top.add(backButton, BorderLayout.WEST);
			backButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					main.musicOpen = false;
					musicFrame.dispose();
				}
			});

			timeLabel.setHorizontalAlignment(JLabel.CENTER);
			top.add(timeLabel, BorderLayout.CENTER);

			return top;
		}

		public void update() {
			timeLabel.setText(Time.timeString);
			repaint();
			revalidate();
		}

		public void paintComponent(Graphics g) {
			g.drawImage(main.backgroundImage, 0, 0, null);
		}

	}

	class SongRunnable implements Runnable {

		@Override
		public void run() {
			try {
				currentPlayer.setPlayBackListener(new PlaybackListener() {
					public void playbackFinished(PlaybackEvent e) {
						System.out.println(songNum + "   " + songButtons.size());
						if (songNum <= (songButtons.size() - 1)) {
							JButton next = songButtons.get(songNum + 1);
							playSong(next.getText());
							songFullName = next.getText();
							setSongName();
							musicPanel.removeAll();
							content();
							musicPanel.repaint();
							System.out.println("here");
						} else {
							playSong(songButtons.get(0).getText());
							songFullName = songButtons.get(0).getText();
							setSongName();
							musicPanel.removeAll();
							content();
							System.out.println("Here as well");
							musicPanel.repaint();
						}
					}
				});
				currentPlayer.play();
			} catch (JavaLayerException e) {
				e.printStackTrace();
			}
		}

	}

	class SongButton extends JButton {

		private static final long serialVersionUID = -4108225614529661693L;
		protected Dimension buttonSize = new Dimension(250, 30);
		
		public SongButton(String txt) {
			setText(txt);
			setMaximumSize(buttonSize);
			setPreferredSize(buttonSize);
			act();
		}

		public void act() {
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					songFullName = getText(); // TODO rename text
					setSongName();
					playSong(songFullName);
					setAlbumCover();
					musicPanel.removeAll();
					content();
					System.out.println("I'm here");
					musicPanel.repaint();
				}
			});
		}
	}
	
}
