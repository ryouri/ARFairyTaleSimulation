package arcircle.ftsim.simulation.command;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.model.Field;
import arcircle.ftsim.state.simgame.SimGameModel;

public class OptionCommandWindow extends CharaCommandWindow {

	public OptionCommandWindow(SimGameModel sgModel, Field field, Chara chara) {
		super(sgModel, field, chara);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public static final String TURN_END_COMMAND_NAME = "全たいき";
	public static final String TARGET_CONFIRMATION_COMMAND_NAME = "もくてき";

	/**
	 * TODO:攻撃範囲によるコマンドの限定，とくしゅコマンドの判定などが未実装
	 * @return
	 */
	protected void calcCommandList() {
		Command command =
				new EndCommand(TURN_END_COMMAND_NAME, sgModel, this);
		commandList.add(command);

		Command confirmTargetCommand =
				new ConfirmTargetCommand(TARGET_CONFIRMATION_COMMAND_NAME, sgModel, this);
		commandList.add(confirmTargetCommand);
	}
}
