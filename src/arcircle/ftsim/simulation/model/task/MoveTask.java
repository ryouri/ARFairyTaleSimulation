package arcircle.ftsim.simulation.model.task;

import org.newdawn.slick.Graphics;

import arcircle.ftsim.simulation.algorithm.range.Node;
import arcircle.ftsim.simulation.chara.Chara;

public class MoveTask extends Task {
	Node node;
	Chara chara;
	int moveIndex;
	int moveIndexMax;

	public MoveTask(TaskManager taskManager, Chara chara, Node node) {
		super(taskManager);
		this.node = node;
		this.chara = chara;
		this.moveIndex = -1;
		this.moveIndexMax = this.node.directionArray.size();
	}

	@Override
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {
	}

	@Override
	public void update(int delta) {
		if (moveIndex < 0) {
			// 移動開始！
			chara.setMoving(true);
			moveIndex = 0;
			chara.direction = node.directionArray.get(moveIndex);
		} else if (moveIndex >= moveIndexMax) {
			// 移動終了
			chara.setMoving(false);
			taskManager.taskEnd();
		} else {
			// 移動中
			chara.move();
			// 1マス移動完了していたら
			if (chara.x == node.pointXArray.get(moveIndex)
					&& chara.y == node.pointYArray.get(moveIndex)) {
				moveIndex++;
				if (moveIndex >= moveIndexMax) {
					// 移動終了
					chara.setMoving(false);
					taskManager.taskEnd();
					return;
				}
				chara.direction = node.directionArray.get(moveIndex);
				chara.setMoving(true);
			}
		}
	}
}
