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

	public SongPlayer() {

	}

	public SongPlayer(AdvancedPlayer player, MusicPlayer mp, int startTime, boolean inPlaylist) {
		this.player = player;
		this.musicPlayer = mp;
		this.startTime = startTime;
		System.out.println("Creating Playback Listeners");
		if (!inPlaylist) {
			setRegPlaybackListener();
		} else {
			setPlaylistPlaybackListener();
		}
	}

	//This is for when playing from the master song list
	public void setRegPlaybackListener() {
		player.setPlayBackListener(new PlaybackListener() {
			public void playbackFinished(PlaybackEvent e) {
				if (!musicPlayer.pause) {
					if (musicPlayer.songNum < (musicPlayer.songList.size() - 1) || (musicPlayer.songNum == (musicPlayer.playlistSongs.size() - 1) && musicPlayer.skipBack)) {
						System.out.println("Song isn't the last one in the list");
						if(!musicPlayer.skipBack){
							musicPlayer.songNum++;
						}else{
							musicPlayer.songNum--;
						}
						musicPlayer.songTime = 0;
						musicPlayer.playSong(musicPlayer.songList.get(musicPlayer.songNum).getName(), 0, false);
					} else if (musicPlayer.songNum == (musicPlayer.songList.size() - 1) && !musicPlayer.skipBack) {
						System.out.println("Was last song; skipping to the beginning");
						musicPlayer.songNum = 0;
						musicPlayer.songTime = 0;
						musicPlayer.playSong(musicPlayer.songList.get(musicPlayer.songNum).getName(), 0, false);
					}else if(musicPlayer.songNum == 0 && musicPlayer.skipBack){
						musicPlayer.songNum = musicPlayer.playlistSongs.size() - 1;
						musicPlayer.songTime = 0;
						musicPlayer.playSong(musicPlayer.playlistSongs.get(musicPlayer.songNum), 0, false);
					}

				} else if (musicPlayer.pause) {
					musicPlayer.countTime = false;
				}
				musicPlayer.skipBack = false;
			}
		});
	}

	//This tells how to skip continue after songs when playing through a playlist
	public void setPlaylistPlaybackListener() {
		player.setPlayBackListener(new PlaybackListener() {
			public void playbackFinished(PlaybackEvent e) {
				if (!musicPlayer.pause) {
					if (musicPlayer.songNum < (musicPlayer.playlistSongs.size() - 1) || (musicPlayer.songNum == (musicPlayer.playlistSongs.size() - 1) && musicPlayer.skipBack)) {
						System.out.println("Song isn't the last one in the list");
						if(!musicPlayer.skipBack){
							musicPlayer.songNum++;
						}else{
							musicPlayer.songNum--;
						}
						musicPlayer.songTime = 0;
						musicPlayer.playSong(musicPlayer.playlistSongs.get(musicPlayer.songNum), 0, true);
					} else if (musicPlayer.songNum == (musicPlayer.playlistSongs.size() - 1) && ! musicPlayer.skipBack) {
						System.out.println("Was last song; skipping to the beginning");
						musicPlayer.songNum = 0;
						musicPlayer.songTime = 0;
						musicPlayer.playSong(musicPlayer.playlistSongs.get(musicPlayer.songNum), 0, true);
					}else if(musicPlayer.songNum == 0 && musicPlayer.skipBack){
						musicPlayer.songNum = musicPlayer.playlistSongs.size() - 1;
						musicPlayer.songTime = 0;
						musicPlayer.playSong(musicPlayer.playlistSongs.get(musicPlayer.songNum), 0, true);
					}
				} else {
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
		//player.close();
	}

}
