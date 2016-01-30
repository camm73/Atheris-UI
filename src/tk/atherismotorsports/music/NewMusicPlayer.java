package tk.atherismotorsports.music;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
	public JButton backButton = new JButton();
	
	public File musicDirectory;
	
	public static Color grayBack = new Color(56, 56, 56);

	public ArrayList<File> songList = new ArrayList<File>();
	public ArrayList<JButton> songButtons = new ArrayList<JButton>();

	public Main main;

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
		
		makeDirectories();
		loadFiles();
		loadSongButtons();
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
		//TODO possibly a button in the east to shrink down this list
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
			JButton tmp = new JButton(songList.get(i).getName());
			tmp.setBackground(grayBack);
			tmp.setForeground(Color.red);
			tmp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO play song with the given name of the button
				}
			});
			songButtons.add(tmp);
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
	
	public JComponent getTopBar(){
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(grayBack);
		topPanel.add(backButton, BorderLayout.WEST);
		backButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				main.musicOpen = false;
				frame.setVisible(false);
				main.frame.setAlwaysOnTop(true);
			}
		});
		
		timeLabel.setHorizontalAlignment(JLabel.CENTER);
		topPanel.add(timeLabel, BorderLayout.CENTER);
		
		return topPanel;
	}
	
	public void update(){
		timeLabel.setText(Time.timeString);
		panel.repaint();
		panel.revalidate();
	}
}
