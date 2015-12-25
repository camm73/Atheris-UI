package tk.atherismotorsports;

import java.awt.Graphics;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Weather {
	
	private final int WIDTH = 800;
	private final int HEIGHT = 480;
	
	private JFrame weatherFrame;
	private Main main;

	public Weather(Main main){
		this.main = main;
		createFrame();
	}
	
	public void createFrame(){
		weatherFrame = new JFrame();
		weatherFrame.setSize(WIDTH, HEIGHT);
		weatherFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		weatherFrame.setLocationRelativeTo(null);
		weatherFrame.setResizable(false);
		weatherFrame.setUndecorated(true);
		weatherFrame.add(new WeatherPanel());
		//weatherFrame.setAlwaysOnTop(true);
		weatherFrame.setVisible(true);
	}
	
	
	class WeatherPanel extends JPanel{
		
		private static final long serialVersionUID = -4504295841389630389L;


		public WeatherPanel(){
			setLayout(new GridBagLayout());
		}
		
		
		public void paintComponent(Graphics g){
			g.drawImage(main.backgroundImage, 0, 0, null);
		}
		
	}
}
