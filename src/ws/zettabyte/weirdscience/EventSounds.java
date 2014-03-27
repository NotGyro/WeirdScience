package ws.zettabyte.weirdscience;

import java.util.ArrayList;

import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class EventSounds {
	public ArrayList<String> sounds;
	public EventSounds(ArrayList<String> sounds) {
		this.sounds = sounds;
	}
	
	@ForgeSubscribe
	public void onSound(SoundLoadEvent event) {
		try {
			for(String sound : sounds) {
				event.manager.addSound(sound);
			}
		} catch (Exception e) {
			System.err.println("Failed to register one or more sounds.");
			System.err.println(e);
		}
	}
}
