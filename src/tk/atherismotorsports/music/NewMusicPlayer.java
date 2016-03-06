package tk.atherismotorsports.music;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
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
import tk.atherismotorsports.json.JSONException;
import tk.atherismotorsports.json.JSONObject;
import tk.atherismotorsports.music.playlist.PlaylistManager;

public class NewMusicPlayer {

	public final int HEIGHT;
	public final int WIDTH;

	public JFrame frame;
	public JPanel panel;
	public JPanel leftPanel;
	public JPanel musicPanel;
	public JPanel playlistPanel;
	public JPanel internalPanel;
	public JPanel infoPanel;
	public JPanel playlistSongPanel;
	public JPanel playlistTopPanel;
	public JScrollPane songScroll;
	public JScrollPane playlistScroll;
	public JLabel timeLabel;
	public JLabel albumLabel;
	public JLabel titleLabel = new JLabel();
	public JButton backButton = new JButton();
	public JButton skipButton = new JButton();
	public JButton playToggle = new JButton();
	public JButton stopButton = new JButton();
	public JButton createPlaylistButton = new JButton("Create Playist");
	public JButton playlistViewButton = new JButton("Playlists");
	public JButton songListViewButton = new JButton("Songs");
	public JProgressBar seekBar;

	public File musicDirectory;
	public File playlistDirectory;
	public File artDirectory;

	public BufferedImage albumCover;

	public static Color grayBack = new Color(56, 56, 56);

	public ArrayList<File> songList;
	public ArrayList<JButton> songButtons = new ArrayList<JButton>();
	public ArrayList<File> playlistFolders;
	public ArrayList<JButton> playlistButtons = new ArrayList<JButton>();
	public ArrayList<String> playlistFolderNames = new ArrayList<String>();
	public ArrayList<File> albumArtFiles;
	public ArrayList<String> albumArtNames = new ArrayList<String>();

	public Thread songThread;
	public int songNum = 0;
	public static String songTitle = "";

	public double songFrames = 0.0;
	public double runtime = 0.0;
	public double songFPS = 0.0;
	public String albumName = "";
	public String artistName = "";
	public int songTime = 0;
	public int resumeFrame;
	public boolean countTime = false;

	public boolean pause = false;
	public boolean initial = true;

	public Main main;
	public AdvancedPlayer player;
	public File songFile;
	public SongPlayer sp;
	public NewMusicPlayer musicPlayer;
	public PlaylistManager manager;

	public NewMusicPlayer(Main main) {
		this.main = main;
		WIDTH = main.WIDTH;
		HEIGHT = main.HEIGHT;
		this.musicPlayer = this;

		timeLabel = new JLabel(Time.timeString);
		timeLabel.setForeground(Color.white);
		timeLabel.setFont(new Font("Stencil", Font.PLAIN, 28));
		backButton.setBackground(new Color(56, 56, 56));
		backButton.setBorderPainted(false);
		backButton.setIcon(new ImageIcon(main.backImage));
		seekBar = new JProgressBar(0, 400);
		songListViewButton.setEnabled(false);

		makeDirectories();
		loadFiles();
		loadArtwork();
		loadSongButtons();
		getPlaylistButtons();
		getPlaylistButtons();
		setSongTitle();
		setSongScroll();
		getPlaylistPanel();
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
		leftPanel.add(getSwitchPanel(), BorderLayout.NORTH);
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
		playlistDirectory = new File(directory + "/Atheris Playlists/");
		artDirectory = new File(directory + "/Atheris Artwork/");

		if (!musicDirectory.mkdirs()) {
			System.out.println("Error creating Atheris Music directory or it already exists");
		} else {
			System.out.println("Successfully created Atheris Music directory!");
		}

		if (playlistDirectory.mkdirs()) {
			System.out.println("Successfully create Atheris Playlist directory!");
		} else {
			System.out.println("Error creating Atheris Playlist directory or it already exists");
		}

		if (artDirectory.mkdirs()) {
			System.out.println("Successfully created Atheris Artwork directory");
		} else {
			System.out.println("Error creating Atheris Artwork directory or it already exists");
		}
	}

	public void loadFiles() {
		songList = new ArrayList<File>(Arrays.asList(musicDirectory.listFiles()));
		playlistFolders = new ArrayList<File>(Arrays.asList(playlistDirectory.listFiles()));

		playlistFolderNames.clear();
		for (int i = 0; i < playlistFolders.size(); i++) {
			playlistFolderNames.add(playlistFolders.get(i).getName());
		}
	}

	public void loadArtwork() {
		albumArtFiles = new ArrayList<File>(Arrays.asList(artDirectory.listFiles()));

		for (int i = 0; i < albumArtFiles.size(); i++) {
			albumArtNames.add(albumArtFiles.get(i).getName());
		}
	}

	public void loadSongButtons() {
		for (int i = 0; i < songList.size(); i++) {
			songButtons.add(new SongButton(this, songList.get(i).getName(), i));
		}
	}

	public void getPlaylistButtons() {
		// TODO for loop getting all of the playlist folders
		playlistButtons.clear();
		for (int i = 0; i < playlistFolders.size(); i++) {
			playlistButtons.add(new OpenPlaylistButton(this, playlistFolders.get(i).getName()));
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

	public JComponent getSwitchPanel() {
		JPanel switchPanel = new JPanel(new GridBagLayout());
		switchPanel.setBackground(grayBack);
		songListViewButton.setBackground(grayBack);
		playlistViewButton.setBackground(grayBack);
		songListViewButton.setForeground(Color.red);
		playlistViewButton.setForeground(Color.red);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		switchPanel.add(songListViewButton, c);
		songListViewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				songListViewButton.setEnabled(false);
				playlistViewButton.setEnabled(true);
				switchToSongView();
			}
		});

		c.gridx++;

		switchPanel.add(playlistViewButton, c);
		playlistViewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				songListViewButton.setEnabled(true);
				playlistViewButton.setEnabled(false);
				switchToPlaylistView();
			}
		});
		return switchPanel;
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
		infoPanel = new JPanel(new GridBagLayout());
		infoPanel.setBackground(grayBack);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTH;

		titleLabel.setForeground(Color.RED);
		titleLabel.setHorizontalAlignment(JLabel.CENTER);

		Dimension titleSize = new Dimension(500, 100);
		titleLabel.setPreferredSize(titleSize);
		titleLabel.setMaximumSize(titleSize);
		titleLabel.setFont(new Font("Stencil", Font.PLAIN, 34));
		infoPanel.add(titleLabel, c);

		c.gridy++;

		if (albumLabel != null) {
			infoPanel.add(albumLabel, c);
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
		if(initial){
			skipButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!pause) {
						player.stop();
						seekBar.setValue(0);
					} else {
						playToggle.setText("Pause");
						songTime = 0;
						seekBar.setValue(0);
						if (songNum < (songList.size() - 1)) {
							songNum++;
							playSong(songList.get(songNum).getName(), 0);
						} else if (songNum == (songList.size() - 1)) {
							songNum = 0;
							playSong(songList.get(songNum).getName(), 0);
						}
					}
				}
			});
		}

		c.gridy++;

		stopButton.setText("Stop");
		stopButton.setBackground(grayBack);
		stopButton.setForeground(Color.red);
		infoPanel.add(stopButton, c);
		if(initial){
			stopButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
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
		}

		c.gridy--;
		c.gridx = 2;

		infoPanel.add(playToggle, c);
		playToggle.setText("Pause");
		playToggle.setBackground(grayBack);
		playToggle.setForeground(Color.red);
		if(initial){
			playToggle.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (playToggle.getText().equals("Pause")) {
						playToggle.setText("Play");
						pause = true;
						player.stop();
					} else {
						playToggle.setText("Pause");
						pause = false;
						playSong(songFile.getName(), (int) (songTime * songFPS));
						System.out.println("First: " + songTime * songFPS + "    Rounded: " + (int) (songTime * songFPS));
						countTime = true;
					}
				}
			});
		}
		initial = false;

		return infoPanel;
	}

	int time = 0;

	public void update() {
		if (countTime) {
			getSongTime();
		}
		// System.out.println(songTime);

		seekBar.setString("Song Length");
		seekBar.setValue((int) ((songTime / runtime) * 400));

		timeLabel.setText(Time.timeString);
		setSongTitle();
		panel.repaint();
		panel.revalidate();
		// System.out.println(songFrames);
		// System.out.println("songFrames: " + songFrames + " songFPS: " +
		// songFPS + " runtime: " + runtime);
	}

	public void switchToSongView() {
		leftPanel.remove(playlistPanel);
		playlistPanel.repaint();
		leftPanel.add(songScroll);
		leftPanel.repaint();
		leftPanel.revalidate();
	}

	public void switchToPlaylistView() {
		leftPanel.remove(songScroll);
		leftPanel.add(playlistPanel, BorderLayout.CENTER);
		playlistPanel.repaint();
		leftPanel.repaint();
		leftPanel.revalidate();
	}
	
	public void viewPlaylistContents(String playlist){
		leftPanel.remove(playlistPanel);
		getPlaylistSongPanel(playlist);
		leftPanel.add(playlistSongPanel, BorderLayout.CENTER);
		playlistSongPanel.repaint();
		leftPanel.repaint();
		leftPanel.revalidate();
	}

	boolean playlistInitial = true;
	public void getPlaylistSongPanel(String playlist){
		playlistSongPanel = new JPanel(new BorderLayout());
		playlistSongPanel.add(playlistTopPanel, BorderLayout.NORTH);
		
		//top panel
		JButton songEditButton = new JButton("Edit Playlist");
		JButton returnButton = new JButton("Back");
		playlistTopPanel = new JPanel(new BorderLayout());
		
		songEditButton.setBackground(grayBack);
		songEditButton.setForeground(Color.red);
		songEditButton.setFont(new Font("Arial", Font.BOLD, 14));
		playlistTopPanel.add(songEditButton, BorderLayout.EAST);
		if(playlistInitial){
			songEditButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					//TODO add actions here >> delete songs from playlist
				}
			});
		}
		
		returnButton.setBackground(grayBack);
		returnButton.setForeground(Color.red);
		returnButton.setFont(new Font("Arial", Font.BOLD, 14));
		playlistTopPanel.add(returnButton, BorderLayout.WEST);
		if(playlistInitial){
			leftPanel.remove(playlistSongPanel);
			leftPanel.add(playlistPanel, BorderLayout.CENTER);
			playlistPanel.repaint();
			leftPanel.repaint();
			leftPanel.revalidate();
		}
		
		
		//center panel
		
		//TODO playlistSongScroll with all songs in playlist
		
		playlistInitial = false;
	}
	
	public void getPlaylistPanel() {
		playlistPanel = new JPanel(new BorderLayout());
		playlistPanel.setBackground(grayBack);
		internalPanel = new JPanel(new GridBagLayout());
		playlistScroll = new JScrollPane(internalPanel);
		playlistScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JPanel buttonPanel = new JPanel(new GridBagLayout());

		internalPanel.setBackground(NewMusicPlayer.grayBack);

		buttonPanel.setBackground(grayBack);
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridx = 0;
		c1.gridy = 0;
		c1.weightx = 1.0;

		createPlaylistButton.setForeground(Color.red);
		createPlaylistButton.setBackground(NewMusicPlayer.grayBack);
		buttonPanel.add(createPlaylistButton, c1);
		createPlaylistButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manager = new PlaylistManager(musicPlayer);
				frame.setAlwaysOnTop(false);
			}
		});

		// TODO add way to edit these by renaming and deleting

		playlistPanel.add(buttonPanel, BorderLayout.NORTH);

		getPlaylistScroll();
		playlistPanel.add(playlistScroll, BorderLayout.CENTER);
	}

	public void getPlaylistScroll() {
		// playlistScroll is scroll pane with is contained in playlistPanel
		// internal panel is the JPanel that is contained in playlistScroll

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 1.0;
		for (int i = 0; i < playlistButtons.size(); i++) {
			internalPanel.add(playlistButtons.get(i), c);
			c.gridy++;
		}
	}

	public void getArtwork(){
		try{
			String editedName = albumName.replace(" ", "+");
			URL url = new URL("https://itunes.apple.com/search?term="+ editedName + "&entity=album");
	        URLConnection connection = url.openConnection();
	
	        String line;
	        StringBuilder builder = new StringBuilder();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        while((line = reader.readLine()) != null) {
	            builder.append(line);
	        }
	
	        JSONObject json = new JSONObject(builder.toString());
	        if(json.getJSONArray("results").length() > 0){
		        String imageUrl = json.getJSONArray("results").getJSONObject(0).getString("artworkUrl100");
		        BufferedImage image = ImageIO.read(new URL(imageUrl));
		        
		        File albumArt = new File(artDirectory + "/" + albumName + ".jpg");
		        BufferedImage resizedArt = resizeArtwork(image, BufferedImage.TYPE_INT_RGB);
		        ImageIO.write(resizedArt, "jpg", albumArt);
		        
		        updateAlbumArtList();
		        albumCover = resizedArt;
		        albumLabel = new JLabel(new ImageIcon(albumCover));
		        albumLabel.repaint();
		        panel.remove(infoPanel);
		        panel.add(getInfoPanel());
		        panel.repaint();
		        panel.revalidate();
	        }
		}catch(JSONException | IOException e){
			e.printStackTrace();
		}
	}

	private BufferedImage resizeArtwork(BufferedImage originalImage, int type) {
		int imgSize = 150;
		BufferedImage resizedImage = new BufferedImage(imgSize, imgSize, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, imgSize, imgSize, null);
		g.dispose();

		return resizedImage;
	}

	public void setArtwork(String album) {
		File cover = new File(artDirectory + "/" + album + ".jpg");
		try {
			albumCover = ImageIO.read(cover);
			albumLabel = new JLabel(new ImageIcon(albumCover));
			albumLabel.repaint();
			panel.remove(infoPanel);
			panel.add(getInfoPanel());
			panel.repaint();
			panel.revalidate();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateAlbumArtList() {
		albumArtNames.clear();
		albumArtFiles.clear();
		albumArtFiles = new ArrayList<File>(Arrays.asList(artDirectory.listFiles()));

		for (int i = 0; i < albumArtFiles.size(); i++) {
			albumArtNames.add(albumArtFiles.get(i).getName());
		}
	}

	public void getSongData(File song) {
		try {
			Mp3File currentSong = new Mp3File(song);
			songFrames = (long) currentSong.getFrameCount();
			runtime = currentSong.getLengthInSeconds();
			songFPS = (songFrames / runtime);

			if (currentSong.hasId3v2Tag()) {
				ID3v2 id3v2tag = currentSong.getId3v2Tag();
				artistName = id3v2tag.getArtist();
				albumName = id3v2tag.getAlbum();
			}

			if (artistName == null || artistName == "") {
				artistName = "Unknown Artist";
			}

			if (albumName == null || albumName == "") {
				albumName = "Unknown Album";
			} else {
				if (!albumArtNames.contains(albumName)) {
					getArtwork();
				} else {
					setArtwork(albumName);
				}
			}

		} catch (UnsupportedTagException | InvalidDataException | IOException e) {
			e.printStackTrace();
		}
	}

	long prev = System.currentTimeMillis() / 1000;

	public void getSongTime() {
		long now = System.currentTimeMillis() / 1000;
		if (now - prev >= 1) {
			prev = now;
			songTime++;

		}
	}

	public void playSong(String text, int startTime) {
		songFile = new File(musicDirectory + "/" + text);
		System.out.println("Song File: " + songFile);

		if (player != null) {
			player.close();
		}

		try {
			FileInputStream fis = new FileInputStream(songFile);
			player = new AdvancedPlayer(fis);
			sp = new SongPlayer(player, this, startTime);
			songThread = new Thread(sp);
			songThread.start();
			Thread songDataThread = new Thread(new Runnable() {
				public void run() {
					getSongData(songFile);
				}
			});
			songDataThread.start();
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