package tk.atherismotorsports;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class MusicPlayer {
	
	private Main main;
	private JFrame musicFrame;
	
	private final int WIDTH, HEIGHT;
	
	public MusicPlayer(Main main){
		this.main = main;
		this.WIDTH = main.WIDTH;
		this.HEIGHT = main.HEIGHT;
		createFrame();
	}
	
	
	private void createFrame(){
		musicFrame = new JFrame();
		musicFrame.setSize(WIDTH, HEIGHT);
		musicFrame.setResizable(false);
		musicFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		musicFrame.setUndecorated(true);
		musicFrame.setLocationRelativeTo(null);
		//musicFrame.setAlwaysOnTop(true);
		musicFrame.add(new MusicPanel());
		musicFrame.setVisible(true);
	}
	
	
	class MusicPanel extends JPanel{
		
		private JButton backButton = new JButton();
		private JPanel songPanel = new JPanel();
		//protected List<>
		private JScrollPane songScroll = new JScrollPane(songPanel);
		private Insets insets = getInsets();
		
		public File musicDirectory;
		
		public MusicPanel(){
			backButton.setBackground(new Color(56, 56, 56));
			backButton.setIcon(new ImageIcon(main.backImage));
			createMusicFolder();
			loadMusicList();
			content();
		}
		
		public void createMusicFolder(){
			JFileChooser jfc = new JFileChooser();
			String directory;
			directory = jfc.getCurrentDirectory().toString();
			
			musicDirectory = new File(directory + "/Atheris Music/");
			
			if(!musicDirectory.mkdirs()){
				System.out.println("Error creating Atheris Music directory or it already exists");
			}else{
				System.out.println("Successfully created Atheris Music directory!");
			}
		}
		
		public void loadMusicList(){
			//TODO use code from test maker to load array of files
		}
		
		
		public void content(){
			setLayout(null);
			Dimension buttonSize = backButton.getPreferredSize();
			backButton.setBounds(insets.left + 40, insets.top, buttonSize.width, buttonSize.height);
			add(backButton);
			
			Dimension songSize = songScroll.getPreferredSize();
			songScroll.setBounds(insets.left, insets.top+40, songSize.width, songSize.height);
			add(songScroll);
			backButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					musicFrame.dispose();
				}
			});
			
		}
		
		
		public void paintComponent(Graphics g){
			g.drawImage(main.backgroundImage, 0, 0, null);
		}
		
	}
		

}
