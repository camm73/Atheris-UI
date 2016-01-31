package tk.atherismotorsports.music;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class SongPlayer implements Runnable{
	
	protected AdvancedPlayer player;
	public NewMusicPlayer musicPlayer;
	public boolean playing = true;
	
	public Thread thread;
	
	public SongPlayer(){
		
	}
	
	public SongPlayer(AdvancedPlayer player, NewMusicPlayer mp){
		this.player = player;
		this.musicPlayer = mp;
		setPlaybackListener();
	}
	
	public void setPlaybackListener(){
		player.setPlayBackListener(new PlaybackListener(){
			public void playbackFinished(PlaybackEvent e){
				if(musicPlayer.songNum < (musicPlayer.songList.size() - 1)){
					System.out.println("Song isn't the last one in the list");
					musicPlayer.songNum++;
					musicPlayer.playSong(musicPlayer.songList.get(musicPlayer.songNum).getName());
				}else if(musicPlayer.songNum == (musicPlayer.songList.size() - 1)){
					System.out.println("Was last song; skipping to the beginning");
					musicPlayer.songNum = 0;
					musicPlayer.playSong(musicPlayer.songList.get(musicPlayer.songNum).getName());
				}
			}
		});
	}

	@Override
	public void run() {
		if(playing){
			try {
				player.play();
			} catch (JavaLayerException e) {
				e.printStackTrace();
			}
		}else{
			player.stop();
		}
		//player.close();
	}

}
