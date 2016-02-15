package tk.atherismotorsports.music;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import tk.atherismotorsports.Main;
import tk.atherismotorsports.Time;

public class NewMusicPlayer {

	public final int HEIGHT;
	public final int WIDTH;

	public JFrame frame;
	public JPanel panel;
	public JPanel leftPanel;
	public JPanel musicPanel;
	public JScrollPane songScroll;
	public JLabel timeLabel;
	public JLabel titleLabel = new JLabel();
	public JButton backButton = new JButton();
	public JButton skipButton = new JButton();
	public JButton playToggle = new JButton();
	public JButton stopButton = new JButton();
	public JProgressBar seekBar;

	public File musicDirectory;

	public static Color grayBack = new Color(56, 56, 56);

	public ArrayList<File> songList = new ArrayList<File>();
	public ArrayList<JButton> songButtons = new ArrayList<JButton>();

	public Thread songThread;
	public int songNum = 0;
	public static String songTitle = "";

	public double songFrames = 0.0;
	public double runtime = 0.0;
	public double songFPS = 0.0;
	public String albumName = "";
	public String artistName = "";
	public BufferedImage albumCover;
	public int songTime = 0;
	public int resumeFrame;
	public boolean countTime = false;
	

	public boolean pause = false;

	public Main main;
	public AdvancedPlayer player;
	public File songFile;
	public SongPlayer sp;

	public NewMusicPlayer(Main main) {
		this.main = main;
		WIDTH = main.WIDTH;
		HEIGHT = main.HEIGHT;

		timeLabel = new JLabel(Time.timeString);
		timeLabel.setForeground(Color.white);
		timeLabel.setFont(new Font("Stencil", Font.PLAIN, 28));
		backButton.setBackground(new Color(56, 56, 56));
		backButton.setBorderPainted(false);
		backButton.setIcon(new ImageIcon(main.backImage));
		seekBar = new JProgressBar(0, 400);

		makeDirectories();
		loadFiles();
		loadSongButtons();
		setSongTitle();
		setSongScroll();
		content();
		createFrame();
		panel.repaint();
	}

	public void content() {
		panel = new JPanel(new BorderLayout());
		panel.setBackground(new Color(56, 56, 56));
		panel.add(getTopBar(), BorderLayout.NORTH);
		panel.add(getLeftPanel(), BorderLayout.WEST);
		panel.add(getInfoPanel(), BorderLayout.CENTER); // may rename this to
														// control panel
	}

	public void createFrame() {
		frame = new JFrame();
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(panel);
		frame.setVisible(true);
	}

	public JComponent getLeftPanel() {
		leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(songScroll, BorderLayout.CENTER);
		// TODO possibly a button in the west to shrink down this list
		return leftPanel;
	}

	public JComponent getMusicPanel() {
		musicPanel = new JPanel(new GridBagLayout());

		return musicPanel;
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
			songButtons.add(new SongButton(this, songList.get(i).getName(), i));
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

	public JComponent getTopBar() {
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(grayBack);
		topPanel.add(backButton, BorderLayout.WEST);
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				main.frame.setAlwaysOnTop(true);
			}
		});

		timeLabel.setHorizontalAlignment(JLabel.CENTER);
		topPanel.add(timeLabel, BorderLayout.CENTER);

		return topPanel;
	}

	public JComponent getInfoPanel() {
		JPanel infoPanel = new JPanel(new GridBagLayout());
		infoPanel.setBackground(grayBack);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTH;

		titleLabel.setForeground(Color.RED);
		titleLabel.setHorizontalAlignment(JLabel.CENTER);

		Dimension titleSize = new Dimension(500, 300);
		titleLabel.setPreferredSize(titleSize);
		titleLabel.setMaximumSize(titleSize);
		titleLabel.setFont(new Font("Stencil", Font.PLAIN, 34));
		infoPanel.add(titleLabel, c);
		
		c.gridy++;
		if(albumCover != null){
			JLabel temp = new JLabel(new ImageIcon(albumCover));
			infoPanel.add(temp, c);
			c.gridy++;
		}
		
		Dimension seekSize = new Dimension(400, 30);
		seekBar.setValue(0);
		seekBar.setStringPainted(true);
		seekBar.setBackground(Color.white);
		seekBar.setForeground(Color.red);
		seekBar.setMaximumSize(seekSize);
		seekBar.setMinimumSize(seekSize);
		seekBar.setPreferredSize(seekSize);
		infoPanel.add(seekBar, c);

		c.gridy++;
		c.gridx = 0;

		infoPanel.add(skipButton, c);
		skipButton.setText("Skip");
		skipButton.setBackground(grayBack);
		skipButton.setForeground(Color.red);
		skipButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!pause){
					player.stop();
					seekBar.setValue(0);
				}else{
					playToggle.setText("Pause");
					songTime = 0;
					seekBar.setValue(0);
					if(songNum < (songList.size() - 1)){
						songNum++;
						playSong(songList.get(songNum).getName(), 0);
					}else if(songNum == (songList.size() - 1)){
						songNum = 0;
						playSong(songList.get(songNum).getName(), 0);
					}
				}
			}
		});
		
		c.gridy++;
		
		stopButton.setText("Stop");
		stopButton.setBackground(grayBack);
		stopButton.setForeground(Color.red);
		infoPanel.add(stopButton, c);
		stopButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				player.stop();
				player.close();
				songTime = 0;
				seekBar.setValue(0);
				countTime = false;
				playToggle.setText("Play");
				titleLabel.setText("");
				titleLabel.repaint();
			}
		});

		c.gridy--;
		c.gridx = 2;

		infoPanel.add(playToggle, c);
		playToggle.setText("Pause");
		playToggle.setBackground(grayBack);
		playToggle.setForeground(Color.red);
		playToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (playToggle.getText().equals("Pause")) {
					playToggle.setText("Play");
					pause = true;
					player.stop();
				} else {
					playToggle.setText("Pause");
					pause = false;
					playSong(songFile.getName(), (int) (songTime*songFPS));
					System.out.println("First: " + songTime*songFPS + "    Rounded: "+ (int) (songTime*songFPS));
					countTime = true;
				}
			}
		});

		return infoPanel;
	}

	int time = 0;

	public void update() {
		if(countTime){
			getSongTime();
		}
		//System.out.println(songTime);
		
		seekBar.setString("Song Length");
		seekBar.setValue((int) ((songTime/runtime)*400));
		
		timeLabel.setText(Time.timeString);
		setSongTitle();
		panel.repaint();
		panel.revalidate();
		// System.out.println(songFrames);
		//System.out.println("songFrames: " + songFrames + "   songFPS: " + songFPS + "   runtime: " + runtime);
	}

	public void getSongData(File song) {
		try {
			Mp3File currentSong = new Mp3File(song);
			songFrames = (long) currentSong.getFrameCount();
			runtime = currentSong.getLengthInSeconds();
			songFPS = (songFrames / runtime);
			
			if(currentSong.hasId3v2Tag()){
				ID3v2 id3v2tag = currentSong.getId3v2Tag();
				artistName =  id3v2tag.getArtist();
				albumName = id3v2tag.getAlbum();
				byte[] imageData = id3v2tag.getAlbumImage();
				System.out.println(imageData);
				if(imageData != null){
					RandomAccessFile file = new RandomAccessFile("album-artwork", "rw");
				    file.write(imageData);
				    file.close();
				}
			}
			
			if(artistName == null){
				artistName = "Unknown Artist";
			}
			
			if(albumName == null){
				albumName = "Unknown Album";
			}
			
		} catch (UnsupportedTagException | InvalidDataException | IOException e) {
			e.printStackTrace();
		}
	}
	
	long prev = System.currentTimeMillis() / 1000;
	public void getSongTime(){
		long now = System.currentTimeMillis() / 1000;
		if(now-prev >= 1){
			prev = now;
			songTime++;
			
		}
	}

	public void playSong(String text, int startTime) {
		songFile = new File(musicDirectory + "/" + text);
		System.out.println("Song File: " + songFile);
		// getAlbumArt(songFile);

		if (player != null) {
			player.close();
		}

		try {
			FileInputStream fis = new FileInputStream(songFile);
			player = new AdvancedPlayer(fis);
			sp = new SongPlayer(player, this, startTime);
			getSongData(songFile);
			songThread = new Thread(sp);
			songThread.start();
			countTime = true;
		} catch (IOException | JavaLayerException e) {
			e.printStackTrace();
		}
	}

	/*
	 * public void getAlbumArt(File song){ // build the search request that
	 * looks for images of music. ItemSearchRequest request = new
	 * ItemSearchRequest(); request.setSearchIndex("Music");
	 * request.setResponseGroup(Arrays.asList("Images"));
	 * request.setArtist(artist); request.setTitle(album);
	 * 
	 * // create a new amazon client using the access key. sign up for an //
	 * amazon web services account here: //
	 * https://aws-portal.amazon.com/gp/aws/developer/registration/index.html
	 * AmazonA2SClient client = new AmazonA2SClient(accessKeyId, "");
	 * 
	 * // create a search response from the search request. ItemSearchResponse
	 * response = client.itemSearch(request);
	 * 
	 * // get the URL to the amazon image (if one was returned). String url =
	 * response.getItems().get(0).getItem().get(0).getLargeImage().getURL();
	 * 
	 * 
	 * ImageIcon icon = null;
	 * 
	 * try { icon = url == null ? null : new ImageIcon(new URL(url)); } catch
	 * (MalformedURLException e) { // do nothing - don't care. }
	 * 
	 * }
	 */

}
