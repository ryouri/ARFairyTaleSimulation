package arcircle.ftsim.simulation.model.task;

import org.newdawn.slick.Graphics;

import arcircle.ftsim.simulation.model.Field;

public class WinTask extends Task {
	Field field;

	public WinTask(TaskManager taskManager, Field field) {
		super(taskManager);
		this.field = field;
	}

	@Override
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {
	}

	@Override
	public void update(int delta) {
		//勝利！
		//talkStateへ
		field.getSgModel().nextState();
	}
}
