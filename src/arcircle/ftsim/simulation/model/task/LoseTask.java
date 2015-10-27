package arcircle.ftsim.simulation.model.task;

import org.newdawn.slick.Graphics;

import arcircle.ftsim.simulation.model.Field;

public class LoseTask extends Task{
	Field field;

	public LoseTask(TaskManager taskManager, Field field) {
		super(taskManager);
		this.field = field;
	}

	@Override
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {
	}

	@Override
	public void update(int delta) {
	}
}
