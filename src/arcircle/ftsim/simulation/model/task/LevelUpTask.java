package arcircle.ftsim.simulation.model.task;

import java.awt.Point;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.chara.Status;
import arcircle.ftsim.simulation.chara.battle.CalcurateExp;
import arcircle.ftsim.simulation.sound.SoundManager;

public class LevelUpTask extends Task {
	private Chara chara;
	private int addExp;
	private CalcurateExp calcurateExp;
	private Status charaStatus;
	private int counter;
	private final int CHAR_SIZE = 24;
	private final int LINE_INTERVAL = 10;
	private final int CenterX = 400;
	private final int CenterY = 320;
	private final int TIME_INTERVAL = 15;
	private final int TIME_OVER = 250;
	private HashMap<String,Point> objectPos;
	private Image levelUpWindow;

	public LevelUpTask(TaskManager taskManager, Chara chara, CalcurateExp calcurateExp) {
		super(taskManager);
		this.chara = chara;
		this.calcurateExp = calcurateExp;
		this.charaStatus = chara.status;
		try {
			levelUpWindow = new Image("./image/levelUpWindow.png");
		} catch (SlickException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		calcPos();
		counter = 0;
		taskManager.field.getCursor().isVisible = false;
	}

	/**各オブジェクトの位置を指定*/
	private void calcPos(){
		objectPos = new HashMap<String, Point>();
		//levelUpWindowの位置
		int tempX = CenterX - (levelUpWindow.getWidth() / 2);
		int tempY = CenterY - (levelUpWindow.getHeight() / 2);
		objectPos.put("WINDOW", new Point(tempX, tempY));
		//キャラネームの位置
		tempX = objectPos.get("WINDOW").x + 40;
		tempY = objectPos.get("WINDOW").y + 40;
		objectPos.put("CHARA_NAME", new Point(tempX, tempY));
		//レベルbefore
		tempX = objectPos.get("CHARA_NAME").x + FTSimulationGame.font.getWidth(chara.status.name) + CHAR_SIZE;
		tempY = objectPos.get("CHARA_NAME").y;
		objectPos.put("LEVEL_BEFORE", new Point(tempX, tempY));
		//レベルafter
		tempX = objectPos.get("LEVEL_BEFORE").x + CHAR_SIZE * 3;
		tempY = objectPos.get("LEVEL_BEFORE").y;
		objectPos.put("LEVEL_AFTER", new Point(tempX, tempY));
		//HPbefore
		tempX = objectPos.get("CHARA_NAME").x + 20;
		tempY = objectPos.get("CHARA_NAME").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos.put("HP_BEFORE", new Point(tempX, tempY));
		//HPafter
		tempX = objectPos.get("HP_BEFORE").x + CHAR_SIZE * 6;
		objectPos.put("HP_AFTER", new Point(tempX, objectPos.get("HP_BEFORE").y));
		//ちからbefore
		tempY = objectPos.get("HP_BEFORE").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos.put("POWER_BEFORE", new Point(objectPos.get("HP_BEFORE").x, tempY));
		//ちからafter
		objectPos.put("POWER_AFTER", new Point(objectPos.get("HP_AFTER").x, objectPos.get("POWER_BEFORE").y));
		//ぼうぎょbefore
		tempY = objectPos.get("POWER_BEFORE").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos.put("DEFENSE_BEFORE", new Point(objectPos.get("HP_BEFORE").x, tempY));
		//ぼうぎょafter
		objectPos.put("DEFENSE_AFTER", new Point(objectPos.get("HP_AFTER").x, objectPos.get("DEFENSE_BEFORE").y));
		//まりょくbefore
		tempY = objectPos.get("DEFENSE_BEFORE").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos.put("MPOWER_BEFORE", new Point(objectPos.get("HP_BEFORE").x, tempY));
		//まりょくafter
		objectPos.put("MPOWER_AFTER", new Point(objectPos.get("HP_AFTER").x, objectPos.get("MPOWER_BEFORE").y));
		//まぼうbefore
		tempY = objectPos.get("MPOWER_BEFORE").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos.put("MDEFENSE_BEFORE", new Point(objectPos.get("HP_BEFORE").x, tempY));
		//まぼうafter
		objectPos.put("MDEFENSE_AFTER", new Point(objectPos.get("HP_AFTER").x, objectPos.get("MDEFENSE_BEFORE").y));
		//はやさbefore
		tempY = objectPos.get("MDEFENSE_BEFORE").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos.put("SPEED_BEFORE", new Point(objectPos.get("HP_BEFORE").x, tempY));
		//はやさafter
		objectPos.put("SPEED_AFTER", new Point(objectPos.get("HP_AFTER").x, objectPos.get("SPEED_BEFORE").y));
		//わざbefore
		tempY = objectPos.get("SPEED_BEFORE").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos.put("TECH_BEFORE", new Point(objectPos.get("HP_BEFORE").x, tempY));
		//わざafter
		objectPos.put("TECH_AFTER", new Point(objectPos.get("HP_AFTER").x, objectPos.get("TECH_BEFORE").y));
		//たいかくbefore
		tempY = objectPos.get("TECH_BEFORE").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos.put("PHYSIQUE_BEFORE", new Point(objectPos.get("HP_BEFORE").x, tempY));
		//たいかくafter
		objectPos.put("PHYSIQUE_AFTER", new Point(objectPos.get("HP_AFTER").x, objectPos.get("PHYSIQUE_BEFORE").y));
		//運before
		tempY = objectPos.get("PHYSIQUE_BEFORE").y + CHAR_SIZE + LINE_INTERVAL;
		objectPos.put("LUCK_BEFORE", new Point(objectPos.get("HP_BEFORE").x, tempY));
		//運after
		objectPos.put("LUCK_AFTER", new Point(objectPos.get("HP_AFTER").x, objectPos.get("LUCK_BEFORE").y));
	}

	@Override
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {
		g.setFont(FTSimulationGame.font);
		g.setColor(Color.white);
		//レベルアップウィンドウの描画
		g.drawImage(levelUpWindow, objectPos.get("WINDOW").x, objectPos.get("WINDOW").y);
		//キャラネームの描画
		g.drawString(chara.status.name, objectPos.get("CHARA_NAME").x, objectPos.get("CHARA_NAME").y);
		//レベルアップの描画
		g.drawString("Lv." + charaStatus.level + " -> Lv." + (charaStatus.level + calcurateExp.getUpLevel()),
				objectPos.get("LEVEL_BEFORE").x, objectPos.get("LEVEL_BEFORE").y);
		//HPbeforeの描画
		g.drawString("HP " + charaStatus.maxHp,	objectPos.get("HP_BEFORE").x, objectPos.get("HP_BEFORE").y);
		//HPafterの描画
		if(counter >=TIME_INTERVAL && calcurateExp.getLevelUpStatus().hp > 0){
			if(counter == TIME_INTERVAL){
				taskManager.field.getSoundManager().playSound(SoundManager.SOUND_STATUS_UP);
			}
			g.drawString("->  " + (charaStatus.maxHp + calcurateExp.getLevelUpStatus().hp),
					objectPos.get("HP_AFTER").x, objectPos.get("HP_AFTER").y);
		}
		//ちからbefore
		g.drawString("ちから " + charaStatus.power,	objectPos.get("POWER_BEFORE").x, objectPos.get("POWER_BEFORE").y);
		//ちからafter
		if(counter >= (TIME_INTERVAL * 2) && calcurateExp.getLevelUpStatus().power > 0){
			if(counter == (TIME_INTERVAL * 2)){
				taskManager.field.getSoundManager().playSound(SoundManager.SOUND_STATUS_UP);
			}
			g.drawString("->  " + (charaStatus.power + calcurateExp.getLevelUpStatus().power),
					objectPos.get("POWER_AFTER").x, objectPos.get("POWER_AFTER").y);
		}
		//ぼうぎょbefore
		g.drawString("ぼうぎょ " + charaStatus.defense,	objectPos.get("DEFENSE_BEFORE").x, objectPos.get("DEFENSE_BEFORE").y);
		//ぼうぎょafter
		if(counter >= (TIME_INTERVAL * 3) && calcurateExp.getLevelUpStatus().defense > 0){
			if(counter == (TIME_INTERVAL * 3)){
				taskManager.field.getSoundManager().playSound(SoundManager.SOUND_STATUS_UP);
			}
			g.drawString("->  " + (charaStatus.defense + calcurateExp.getLevelUpStatus().defense),
					objectPos.get("DEFENSE_AFTER").x, objectPos.get("DEFENSE_AFTER").y);
		}
		//まりょくbefore
		g.drawString("まりょく " + charaStatus.magicPower,	objectPos.get("MPOWER_BEFORE").x, objectPos.get("MPOWER_BEFORE").y);
		//まりょくafter
		if(counter >= (TIME_INTERVAL * 4) && calcurateExp.getLevelUpStatus().magicPower > 0){
			if(counter == (TIME_INTERVAL * 4)){
				taskManager.field.getSoundManager().playSound(SoundManager.SOUND_STATUS_UP);
			}
			g.drawString("->  " + (charaStatus.magicPower + calcurateExp.getLevelUpStatus().magicPower),
					objectPos.get("MPOWER_AFTER").x, objectPos.get("MPOWER_AFTER").y);
		}
		//まぼうbefore
		g.drawString("まぼう " + charaStatus.magicDefense,	objectPos.get("MDEFENSE_BEFORE").x, objectPos.get("MDEFENSE_BEFORE").y);
		//ぼうぎょafter
		if(counter >= (TIME_INTERVAL * 5) && calcurateExp.getLevelUpStatus().magicDefense > 0){
			if(counter == (TIME_INTERVAL * 5)){
				taskManager.field.getSoundManager().playSound(SoundManager.SOUND_STATUS_UP);
			}
			g.drawString("->  " + (charaStatus.magicDefense + calcurateExp.getLevelUpStatus().magicDefense),
					objectPos.get("MDEFENSE_AFTER").x, objectPos.get("MDEFENSE_AFTER").y);
		}
		//はやさbefore
		g.drawString("はやさ " + charaStatus.speed,	objectPos.get("SPEED_BEFORE").x, objectPos.get("SPEED_BEFORE").y);
		//はやさafter
		if(counter >= (TIME_INTERVAL * 6) && calcurateExp.getLevelUpStatus().speed > 0){
			if(counter == (TIME_INTERVAL * 6)){
				taskManager.field.getSoundManager().playSound(SoundManager.SOUND_STATUS_UP);
			}
			g.drawString("->  " + (charaStatus.speed + calcurateExp.getLevelUpStatus().speed),
					objectPos.get("SPEED_AFTER").x, objectPos.get("SPEED_AFTER").y);
		}
		//わざbefore
		g.drawString("わざ " + charaStatus.tech,	objectPos.get("TECH_BEFORE").x, objectPos.get("TECH_BEFORE").y);
		//わざafter
		if(counter >= (TIME_INTERVAL * 7) && calcurateExp.getLevelUpStatus().tech > 0){
			if(counter == (TIME_INTERVAL * 7)){
				taskManager.field.getSoundManager().playSound(SoundManager.SOUND_STATUS_UP);
			}
			g.drawString("->  " + (charaStatus.tech + calcurateExp.getLevelUpStatus().tech),
					objectPos.get("TECH_AFTER").x, objectPos.get("TECH_AFTER").y);
		}
		//たいかくbefore
		g.drawString("たいかく " + charaStatus.physique,	objectPos.get("PHYSIQUE_BEFORE").x, objectPos.get("PHYSIQUE_BEFORE").y);
		//たいかくafter
		if(counter >= (TIME_INTERVAL * 8) && calcurateExp.getLevelUpStatus().physique > 0){
			if(counter == (TIME_INTERVAL * 8)){
				taskManager.field.getSoundManager().playSound(SoundManager.SOUND_STATUS_UP);
			}
			g.drawString("->  " + (charaStatus.physique + calcurateExp.getLevelUpStatus().physique),
					objectPos.get("PHYSIQUE_AFTER").x, objectPos.get("PHYSIQUE_AFTER").y);
		}
		//運before
		g.drawString("運 " + charaStatus.luck,	objectPos.get("LUCK_BEFORE").x, objectPos.get("LUCK_BEFORE").y);
		//運after
		if(counter >= (TIME_INTERVAL * 9) && calcurateExp.getLevelUpStatus().luck > 0){
			if(counter == (TIME_INTERVAL * 9)){
				taskManager.field.getSoundManager().playSound(SoundManager.SOUND_STATUS_UP);
			}
			g.drawString("->  " + (charaStatus.luck + calcurateExp.getLevelUpStatus().luck),
					objectPos.get("LUCK_AFTER").x, objectPos.get("LUCK_AFTER").y);
		}
	}

	@Override
	public void update(int delta) {
		counter++;
		if(counter > TIME_OVER){
			charaStatus.level += calcurateExp.getUpLevel();
			charaStatus.maxHp += calcurateExp.getLevelUpStatus().hp;
			charaStatus.setHp(charaStatus.getHp() + calcurateExp.getLevelUpStatus().hp);
			charaStatus.power        += calcurateExp.getLevelUpStatus().power;
			charaStatus.magicPower   += calcurateExp.getLevelUpStatus().magicPower;
			charaStatus.speed        += calcurateExp.getLevelUpStatus().speed;
			charaStatus.tech         += calcurateExp.getLevelUpStatus().tech;
			charaStatus.luck         += calcurateExp.getLevelUpStatus().luck;
			charaStatus.defense      += calcurateExp.getLevelUpStatus().defense;
			charaStatus.magicDefense += calcurateExp.getLevelUpStatus().magicDefense;
			charaStatus.move         += calcurateExp.getLevelUpStatus().move;
			charaStatus.physique     += calcurateExp.getLevelUpStatus().physique;
			taskManager.taskEnd();
			taskManager.field.getCursor().isVisible = true;
		}
	}
}
