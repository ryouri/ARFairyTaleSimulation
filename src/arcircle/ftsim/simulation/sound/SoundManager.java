package arcircle.ftsim.simulation.sound;

import java.io.File;
import java.util.HashMap;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class SoundManager {
	HashMap<String, Sound> soundMap;

	public static final String battleSoundFolderPath = "sound/se/simulation/";

	public static final String SOUND_CURSOR = "cursor";

	public SoundManager(String folderPath) {
		loadSounds(folderPath);
	}

	private void loadSounds(String folderPath) {
		soundMap = new HashMap<String, Sound>();
	    File dir = new File(folderPath);
	    File[] files = dir.listFiles();
	    for (int i = 0; i < files.length; i++) {
	        File file = files[i];
	        String fileName = file.getName();
	        String soundName = fileName.split("\\.")[0];
	        try {
				Sound sound = new Sound(folderPath + "/" + fileName);
				soundMap.put(soundName, sound);
			} catch (SlickException e) {
				e.printStackTrace();
			}
	    }
	}

	public void playSound(String soundName) {
		if (!soundMap.containsKey(soundName)) {
			System.out.println("存在しないサウンド:" + soundName);
			return;
		}
		Sound sound = soundMap.get(soundName);
		sound.play();
	}
}
