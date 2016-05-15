package tk.atherismotorsports.user;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import tk.atherismotorsports.Main;
import tk.atherismotorsports.Splash;
import tk.atherismotorsports.music.MusicPlayer;

public class UserButton extends JButton {

	public UserProfiles userProfiles;
	
	public UserButton(String text, UserProfiles userProfiles){
		this.userProfiles = userProfiles;
		setBackground(MusicPlayer.grayBack);
		setText(text);
		setForeground(Color.red);
		setFont(new Font("Arial", Font.PLAIN, 24));
		setVerticalTextPosition(SwingConstants.BOTTOM);
		setHorizontalTextPosition(SwingConstants.CENTER);
		setIcon(new ImageIcon(userProfiles.headImage));
		
		getActionListener();
	}
	
	public void getActionListener(){
		addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Main.user = getText();
				new Splash();
				userProfiles.frame.dispose();
				
			}
		});
	}
	
}
