package arcircle.ftsim.simulation.model.task.confirmtarget;

import org.newdawn.slick.Graphics;

public class ConfirmEnemyBelow extends ConfirmTargetTaskLogic {
	@Override
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {
	}

	@Override
	public void update(int delta) {
		getConfirmTargetTask().finishTask();
	}
}
