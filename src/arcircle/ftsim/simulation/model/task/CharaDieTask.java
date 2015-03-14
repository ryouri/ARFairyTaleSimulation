package arcircle.ftsim.simulation.model.task;

import org.newdawn.slick.Graphics;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.sound.SoundManager;

public class CharaDieTask extends Task {
	Chara dieChara;

	public static final int CHARA_DIE_TIME = 50;

	private int timer;

	public CharaDieTask(TaskManager taskManager, Chara chara) {
		super(taskManager);
		this.dieChara = chara;
		this.timer = 0;
	}

	@Override
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {

	}

	@Override
	public void update(int delta) {
		if (timer == 0) {
			taskManager.field.getSoundManager().playSound(SoundManager.SOUND_CHARA_DIE);
		}

		timer++;

		dieChara.setAlpha((100 - timer * 2) / 100);
		dieChara.setColor((100 - timer * 2) / 100);

		if (timer > CHARA_DIE_TIME) {
			taskManager.characters.removeChara(dieChara);
			taskManager.checkCharaDieEvent(dieChara.id);
			taskManager.taskEnd();
		}
	}
}
