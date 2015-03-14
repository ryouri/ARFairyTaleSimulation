package arcircle.ftsim.simulation.model.task;

import org.newdawn.slick.Graphics;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.chara.Status;
import arcircle.ftsim.simulation.chara.battle.CalcurateExp;

public class LevelUpTask extends Task {
	private Chara chara;
	private int addExp;
	private CalcurateExp calcurateExp;
	private Status charaStatus;

	public LevelUpTask(TaskManager taskManager, Chara chara, CalcurateExp calcurateExp) {
		super(taskManager);
		this.chara = chara;
		this.calcurateExp = calcurateExp;
		this.charaStatus = chara.status;
	}

	@Override
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {
	}

	@Override
	public void update(int delta) {
		charaStatus.level += calcurateExp.getUpLevel();
		charaStatus.setHp(charaStatus.getHp() + calcurateExp.getLevelUpStatus().hp);
		charaStatus.power        += calcurateExp.getLevelUpStatus().power;
		charaStatus.magicPower   += calcurateExp.getLevelUpStatus().magicPower;
		charaStatus.speed        += calcurateExp.getLevelUpStatus().speed;
		charaStatus.tech         += calcurateExp.getLevelUpStatus().tech;
		charaStatus.luck         += calcurateExp.getLevelUpStatus().luck;
		charaStatus.defence      += calcurateExp.getLevelUpStatus().defence;
		charaStatus.magicDefence += calcurateExp.getLevelUpStatus().magicDefence;
		charaStatus.move         += calcurateExp.getLevelUpStatus().move;
		charaStatus.physique     += calcurateExp.getLevelUpStatus().physique;

		taskManager.taskEnd();
	}
}
