package arcircle.ftsim.simulation.model.task;

import org.newdawn.slick.Graphics;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.chara.battle.CalcurateExp;

public class AddExpTask extends Task {
	private Chara chara;
	private int addExp;
	private CalcurateExp calcurateExp;

	public AddExpTask(TaskManager taskManager, Chara chara, int addExp) {
		super(taskManager);
		this.addExp = addExp;
		this.chara = chara;
		this.calcurateExp = new CalcurateExp(chara, addExp);
	}

	@Override
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {
	}

	@Override
	public void update(int delta) {
		//経験値のみ書き換え，レベル上がってたらレベルアップの処理へ
		this.chara.status.exp = calcurateExp.getAfterUpExp();

		if (calcurateExp.getUpLevel() > 0) {
			taskManager.addLevelUpTask(chara, calcurateExp);
		}

		taskManager.taskEnd();
	}
}
