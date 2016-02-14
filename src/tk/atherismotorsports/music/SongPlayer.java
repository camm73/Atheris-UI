package tk.atherismotorsports.music;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class SongPlayer implements Runnable{
	
	protected AdvancedPlayer player;
	public NewMusicPlayer musicPlayer;
	public int startTime;
	
	public Thread pauseThread;
	
	public SongPlayer(){
		
	}
	
	public SongPlayer(AdvancedPlayer player, NewMusicPlayer mp, int startTime){
		this.player = player;
		this.musicPlayer = mp;
		this.startTime = startTime;
		setPlaybackListener();
	}
	
	public void setPlaybackListener(){
		player.setPlayBackListener(new PlaybackListener(){
			public void playbackFinished(PlaybackEvent e){
				if(!musicPlayer.pause){
					if(musicPlayer.songNum < (musicPlayer.songList.size() - 1)){
						System.out.println("Song isn't the last one in the list");
						musicPlayer.songNum++;
						musicPlayer.songTime = 0;
						musicPlayer.playSong(musicPlayer.songList.get(musicPlayer.songNum).getName(), 0);
					}else if(musicPlayer.songNum == (musicPlayer.songList.size() - 1)){
						System.out.println("Was last song; skipping to the beginning");
						musicPlayer.songNum = 0;
						musicPlayer.songTime = 0;
						musicPlayer.playSong(musicPlayer.songList.get(musicPlayer.songNum).getName(), 0);
					}
				
				}else if(musicPlayer.pause){
					musicPlayer.countTime = false;
				}
			}
		});
	}

	@Override
	public void run() {
		try {
			player.play(startTime, Integer.MAX_VALUE);

		} catch (JavaLayerException e) {
			e.printStackTrace();
			player.stop();
		}
		//player.close();
	}

}
