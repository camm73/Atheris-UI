package tk.atherismotorsports.music;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import tk.atherismotorsports.Main;

public class NewMusicPlayer {

	public final int HEIGHT;
	public final int WIDTH;

	public JFrame frame;
	public JPanel panel;
	public JPanel leftPanel;
	public JPanel musicPanel;
	public JScrollPane songScroll;

	public File musicDirectory;

	public ArrayList<File> songList = new ArrayList<File>();
	public ArrayList<JButton> songButtons = new ArrayList<JButton>();

	public Main main;

	public NewMusicPlayer(Main main) {
		this.main = main;
		WIDTH = main.WIDTH;
		HEIGHT = main.HEIGHT;
		makeDirectories();
		loadFiles();
		loadSongButtons();
		content();
		createFrame();
	}

	public void content() {
		panel = new JPanel(new BorderLayout());
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
		setSongScroll();
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
			tmp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO play song with the given name of the button
				}
			});
			songButtons.add(tmp);
		}
	}

	public void setSongScroll() {
		songScroll = new JScrollPane();
		songScroll.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTH;
		for (int i = 0; i < songButtons.size(); i++) {
			songScroll.add(songButtons.get(i), c);
			c.gridy++;
		}
	}
}
