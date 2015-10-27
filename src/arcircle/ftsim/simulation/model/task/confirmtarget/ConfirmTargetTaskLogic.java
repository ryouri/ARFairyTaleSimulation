package arcircle.ftsim.simulation.model.task.confirmtarget;

import org.newdawn.slick.Graphics;

import arcircle.ftsim.simulation.model.task.ConfirmTargetTask;

public abstract class ConfirmTargetTaskLogic {
	public static final String packageName = "arcircle.ftsim.simulation.model.task.confirmtarget.";

	public static final String[] classList =
		{
			"ConfirmTouchChara",
			"ConfirmArrival",
			"ConfirmTurnProgress",
			"ConfirmCharaDie",
			"ConfirmEnemyBelow",
			"ConfirmKillEnemy",
		};

	private ConfirmTargetTask confirmTargetTask;

	public ConfirmTargetTask getConfirmTargetTask() {
		return confirmTargetTask;
	}
	public void init(ConfirmTargetTask confirmTargetTask) {
		this.confirmTargetTask = confirmTargetTask;
	}
	public abstract void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY);
	public abstract void update(int delta);
}
