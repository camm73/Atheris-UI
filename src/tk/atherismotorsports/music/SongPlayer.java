package tk.atherismotorsports.music;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class SongPlayer implements Runnable {

	protected AdvancedPlayer player;
	public MusicPlayer musicPlayer;
	public int startTime;

	public Thread pauseThread;
	public boolean inPlaylist;

	public SongPlayer() {

	}

	public SongPlayer(AdvancedPlayer player, MusicPlayer mp, int startTime, boolean inPlaylist) {
		this.player = player;
		this.musicPlayer = mp;
		this.startTime = startTime;
		this.inPlaylist = inPlaylist;
		System.out.println("Creating Playback Listener");
		if(!inPlaylist){
			setRegPlaybackListener();
		}else{
			setPlaylistPlaybackListener();
		}
	}

	//TODO for inPlaylist it needs to be from playlist list of songs not songList
	public void setRegPlaybackListener() {
		player.setPlayBackListener(new PlaybackListener() {
			public void playbackFinished(PlaybackEvent e) {
				if (!musicPlayer.pause) {
					if ((musicPlayer.songNum < (musicPlayer.songList.size() - 1) && !musicPlayer.skipBack)) {
						System.out.println("Song isn't the last one in the list; skipback: " + musicPlayer.skipBack);
						musicPlayer.songNum++;
						musicPlayer.songTime = 0;
						musicPlayer.playSong(musicPlayer.songList.get(musicPlayer.songNum).getName(), 0, false);
					} else if (musicPlayer.songNum <= (musicPlayer.songList.size() - 1) && musicPlayer.skipBack
							&& musicPlayer.songNum > 0) {
						System.out.println("Song isn't the last one in the list; skipBack: " + musicPlayer.skipBack);
						musicPlayer.songNum--;
						musicPlayer.songTime = 0;
						musicPlayer.playSong(musicPlayer.songList.get(musicPlayer.songNum).getName(), 0, false);
					} else if (musicPlayer.songNum == (musicPlayer.songList.size() - 1) && !musicPlayer.skipBack) {
						System.out.println("Was last song; skipping to the beginning");
						musicPlayer.songNum = 0;
						musicPlayer.songTime = 0;
						musicPlayer.playSong(musicPlayer.songList.get(musicPlayer.songNum).getName(), 0, false);
					} else if (musicPlayer.songNum == 0 && musicPlayer.skipBack) {
						System.out.println("Songnum: " + musicPlayer.songNum + " after the shift to end of songList");
						musicPlayer.songTime = 0;
						musicPlayer.songNum = (musicPlayer.songList.size() - 1);
						musicPlayer.playSong(musicPlayer.songList.get(musicPlayer.songNum).getName(), 0, false);
					} else {
						System.out.println("None of the above --- " + (musicPlayer.songList.size() - 1));
					}

				} else if (musicPlayer.pause) {
					musicPlayer.countTime = false;
				}
				musicPlayer.skipBack = false;
			}
		});
	}
	
	public void setPlaylistPlaybackListener(){
		player.setPlayBackListener(new PlaybackListener(){
			public void playbackFinished(PlaybackEvent e){
				if (!musicPlayer.pause) {
					if ((musicPlayer.songNum < (musicPlayer.playlistSongs.size() - 1) && !musicPlayer.skipBack)) {
						System.out.println("Song isn't the last one in the list");
						musicPlayer.songNum++;
						musicPlayer.songTime = 0;
						musicPlayer.playSong(musicPlayer.playlistSongs.get(musicPlayer.songNum), 0, true);
					} else if (musicPlayer.songNum <= (musicPlayer.playlistSongs.size() - 1) && musicPlayer.skipBack
							&& musicPlayer.songNum > 0) {
						System.out.println("Song isn't the last one in the list");
						musicPlayer.songNum--;
						musicPlayer.songTime = 0;
						musicPlayer.playSong(musicPlayer.playlistSongs.get(musicPlayer.songNum), 0, true);
					} else if (musicPlayer.songNum == (musicPlayer.playlistSongs.size() - 1) && !musicPlayer.skipBack) {
						System.out.println("Was last song; skipping to the beginning");
						musicPlayer.songNum = 0;
						musicPlayer.songTime = 0;
						musicPlayer.playSong(musicPlayer.playlistSongs.get(musicPlayer.songNum), 0, true);
					} else if (musicPlayer.songNum == 0 && musicPlayer.skipBack) {
						System.out.println("Songnum: " + musicPlayer.songNum + " after the shift to end of playlistSongs");
						musicPlayer.songNum = (musicPlayer.playlistSongs.size() - 1);
						musicPlayer.songTime = 0;
						musicPlayer.playSong(musicPlayer.playlistSongs.get(musicPlayer.songNum), 0, true);
					} else {
						System.out.println("SongNum: " + musicPlayer.songNum + "  None of the above --- " + (musicPlayer.playlistSongs.size() - 1));
					}

				} else if (musicPlayer.pause) {
					musicPlayer.countTime = false;
				}
				musicPlayer.skipBack = false;
			}
		});
	}

	@Override
	public void run() {
		try {
			player.play(startTime, Integer.MAX_VALUE);
		} catch (JavaLayerException e) {
			e.printStackTrace();
			player.close();
		}
		// player.close();
	}

}
