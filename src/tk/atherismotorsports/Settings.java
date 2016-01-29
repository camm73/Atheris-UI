package tk.atherismotorsports;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Settings {

	private final int WIDTH = 800;
	private final int HEIGHT = 480;

	private Main main;
	private Time time;
	
	private JLabel timeLabel;

	private JFrame settingsFrame;

	public Settings(Main main) {
		this.main = main;
		time = main.time;
		timeLabel = new JLabel();
		timeLabel.setForeground(Color.white);
		timeLabel.setFont(new Font("Stencil", Font.PLAIN, 24));
		update();
		createFrame();
	}

	public void createFrame() {
		settingsFrame = new JFrame();
		settingsFrame.setSize(WIDTH, HEIGHT);
		settingsFrame.setResizable(false);
		settingsFrame.setLocationRelativeTo(null);
		settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		settingsFrame.setUndecorated(true);
		settingsFrame.add(new SettingsPanel());
		settingsFrame.setAlwaysOnTop(true);
		settingsFrame.setVisible(true);
	}
	

	
	public void update(){
		timeLabel.setText(Time.timeString);
	}

	class SettingsPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		private JButton backButton = new JButton();

		public SettingsPanel() {
			setLayout(null);
			backButton.setBackground(new Color(56, 56, 56));
			backButton.setIcon(new ImageIcon(main.backImage));
			content();
		}

		protected void content() {
			Dimension backSize = backButton.getPreferredSize();
			Insets insets = getInsets();
			backButton.setBounds(insets.left + 40, insets.top, backSize.width, backSize.height);
			add(backButton);
			backButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					main.settingsOpen = false;
					main.frame.setAlwaysOnTop(true);
					settingsFrame.dispose();
				}
			});

			Dimension timeSize = timeLabel.getPreferredSize();
			timeLabel.setBounds(insets.left + 354, insets.top, timeSize.width, timeSize.height);
			add(timeLabel);
		}

		public void paintComponent(Graphics g) {
			g.drawImage(main.backgroundImage, 0, 0, null);
		}
	}

}
