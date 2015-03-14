package arcircle.ftsim.simulation.command;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.model.CharaCommandWindow;
import arcircle.ftsim.simulation.model.Field;
import arcircle.ftsim.state.simgame.SimGameModel;

public abstract class Command {
	public static final String[] commandType = {
		"いどう",   "こうげき", "ほじょ",
		"とくしゅ", "どうぐ",   "たいき"
	};

	public String name;

	SimGameModel sgModel;
	int windowX;
	int windowY;

	protected CharaCommandWindow charaCommandWindow;

	public void setWindowXY(int windowX, int windowY) {
		this.windowX = windowX;
		this.windowY = windowY;
	}
	public Command(String name, SimGameModel sgModel,
			CharaCommandWindow charaCommandWindow) {
		this.name = name;
		this.sgModel = sgModel;
		this.charaCommandWindow = charaCommandWindow;
	}


	/**
	 * 何も処理しない，そこで終了する
	 */
	public static final int PUSHED_NONE = 0;
	/**
	 * 非表示にする
	 */
	public static final int PUSHED_NOT_VISIBLE = 1;
	/**
	 * @param field
	 * @param chara
	 * @return 定数を返す
	 */
	abstract public int pushed(Field field, Chara chara);
}
