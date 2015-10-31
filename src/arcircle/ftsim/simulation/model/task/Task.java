package arcircle.ftsim.simulation.model.task;

import org.newdawn.slick.Graphics;

abstract class Task {
	TaskManager taskManager;

	public Task(TaskManager taskManager) {
		super();
		this.taskManager = taskManager;
	}

	public abstract void render(Graphics g, int offsetX, int offsetY,
			int firstTileX, int lastTileX, int firstTileY, int lastTileY);

	public abstract void update(int delta);
}