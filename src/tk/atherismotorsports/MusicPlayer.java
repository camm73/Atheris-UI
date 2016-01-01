package tk.atherismotorsports;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

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
	public MusicPanel musicPanel;
	public AdvancedPlayer currentPlayer;
	public Thread songThread;
	
	public int songNum;
	public ArrayList<JButton> songButtons;
	private JButton backButton = new JButton();
	private JPanel songListPanel = new JPanel();
	// protected List<>
	private JScrollPane songScroll;

	public ArrayList<File> songList;

	public File musicDirectory;
	public Time time;
	public JLabel timeLabel;

	private final int WIDTH, HEIGHT;

	public MusicPlayer(Main main) {
		this.main = main;
		this.WIDTH = main.WIDTH;
		this.HEIGHT = main.HEIGHT;
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
		musicPanel = new MusicPanel();
		musicFrame.add(musicPanel);
		musicFrame.setVisible(true);
	}
	public void playSong(String songName) {
		File songFile = new File(musicDirectory + "/" + songName);
		try {
			FileInputStream fis = new FileInputStream(songFile);
			if (songThread.isAlive()) {
				currentPlayer.close();
			}
			currentPlayer = new AdvancedPlayer(fis);
			songThread = new Thread(new SongRunnable());
			if(!songThread.isAlive()){
				songThread.start();
			}
		} catch (FileNotFoundException | JavaLayerException e) {
			e.printStackTrace();
		}
	}

	class MusicPanel extends JPanel {

		private Insets insets = getInsets();
		
		public MusicPanel() {
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

			songButtons = new ArrayList<JButton>();
			for (int i = 0; i < songList.size(); i++) {
				songButtons.add(new JButton(songList.get(i).getName()));
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
				JButton tmp = songButtons.get(i);
				tmp.setBackground(new Color(50, 50, 50, 255));
				tmp.setForeground(Color.red);
				songListPanel.add(tmp, c);
				songNum = i;
				tmp.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						playSong(tmp.getText());
					}
				});

				c.gridy++;
			}
		}


		public void content() {

			add(getTopBar(), BorderLayout.NORTH);

			songScroll = new JScrollPane(songListPanel);
			songScroll.setViewportView(songListPanel);
			songScroll.setBackground(new Color(56, 56, 56, 255));
			songScroll.setOpaque(true);
			songScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			add(songScroll, BorderLayout.WEST);
			backButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					musicFrame.setVisible(false);
					main.frame.setAlwaysOnTop(true);
				}
			});
			
			add(getSongInfo(), BorderLayout.EAST);

		}
		
		public JComponent getSongInfo(){
			JPanel songInfo = new JPanel(new GridBagLayout());
			
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
				currentPlayer.setPlayBackListener(new PlaybackListener(){
					public void playbackFinished(PlaybackEvent e){
						if(songNum == songButtons.size()){
							playSong(songButtons.get(songNum+1).getText());
						}else{
							playSong(songButtons.get(0).getText());
						}
					}
				});
				currentPlayer.play();
			} catch (JavaLayerException e) {
				e.printStackTrace();
			}
		}

	}

}
