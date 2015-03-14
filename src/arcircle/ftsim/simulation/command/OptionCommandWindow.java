package arcircle.ftsim.simulation.command;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.model.CharaCommandWindow;
import arcircle.ftsim.simulation.model.Field;
import arcircle.ftsim.state.simgame.SimGameModel;

public class OptionCommandWindow extends CharaCommandWindow {

	public OptionCommandWindow(SimGameModel sgModel, Field field, Chara chara) {
		super(sgModel, field, chara);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	/**
	 * TODO:攻撃範囲によるコマンドの限定，とくしゅコマンドの判定などが未実装
	 * @return
	 *
	 */
	protected void calcCommandList() {

	}

}
