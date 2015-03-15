package arcircle.ftsim.simulation.model.task;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.chara.battle.CalcurateExp;
import arcircle.ftsim.simulation.sound.SoundManager;

public class AddExpTask extends Task {
	private Chara chara;
	private int addExp;
	private CalcurateExp calcurateExp;
	private Image expUpWindow;
	private final int CenterX = 400;
	private final int CenterY = 320;
	private int counter;
	private final int BAR_WIDTH = 400;
	private final int BAR_HEIGHT = 10;
	private int maxCounter;
	private int expLength = 0;
	private int lvWinPosX;
	private int lvWinPosY;
	private int expUpPosX;
	private int expUpPosY;
	private int expBarPosX;
	private int expBarPosY;

	public AddExpTask(TaskManager taskManager, Chara chara, int addExp) {
		super(taskManager);
		this.addExp = addExp;
		this.chara = chara;
		this.calcurateExp = new CalcurateExp(chara, addExp);
		try {
			expUpWindow = new Image("./image/expUpWindow.png");
		} catch (SlickException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		this.chara.status.exp = calcurateExp.getAfterUpExp();
		//最大カウンター値
		maxCounter = calcurateExp.getAddExp() * 2;
		//描画する経験値量
		expLength = calcurateExp.getBeforeUpExp() * 4;
		counter = 0;
		calcPos();

		taskManager.field.getCursor().isVisible = false;
	}

	/** 描画位置の計算メソッド */
	private void calcPos(){
		//レベルアップ用Windowの表示位置
		lvWinPosX = CenterX - (expUpWindow.getWidth() / 2);
		lvWinPosY = CenterY - (expUpWindow.getHeight() / 2);
		//経験値上昇の記述位置
		expUpPosX = lvWinPosX + 20;
		expUpPosY = lvWinPosY + 20;
		//経験値バーの描画
		expBarPosX = CenterX - (BAR_WIDTH / 2);
		expBarPosY = expUpPosY + 40;
	}

	@Override
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {
		g.setFont(FTSimulationGame.font);
		//レベルアップ用Windowの表示
		g.drawImage(expUpWindow, lvWinPosX, lvWinPosY);
		//経験値上昇の記述描画
		g.setColor(Color.white);
		g.drawString(chara.status.name + " Exp. " + (expLength/4) + "/100", expUpPosX, expUpPosY);
		//経験値バー背景の描画
		g.setColor(Color.lightGray);
		g.fillRect(expBarPosX, expBarPosY, BAR_WIDTH, BAR_HEIGHT);
		//経験値バー内容の描画
		g.setColor(Color.green);
		g.fillRect(expBarPosX, expBarPosY, expLength, BAR_HEIGHT);
		taskManager.field.getSoundManager().playSound(SoundManager.SOUND_EXP_UP);
		g.setColor(Color.white);
	}

	@Override
	public void update(int delta) {
		counter++;
		expLength+=2;
		if(expLength > 400){
			taskManager.field.getSoundManager().playSound(SoundManager.SOUND_LEVEL_UP);
			expLength = 0;
		}
		if(counter == maxCounter){
			if (calcurateExp.getUpLevel() > 0) {
				taskManager.addLevelUpTask(chara, calcurateExp);
			}
			taskManager.taskEnd();
			taskManager.field.getCursor().isVisible = true;
		}else{}
	}
}
