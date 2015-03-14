package arcircle.ftsim.simulation.command;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.item.Item;
import arcircle.ftsim.simulation.model.Field;
import arcircle.ftsim.state.simgame.SimGameModel;

public class CharaCommandWindow extends CommandWindow {

	public CharaCommandWindow(SimGameModel sgModel, Field field, Chara chara) {
		super(sgModel, field, chara);
	}

	/**
	 * TODO:攻撃範囲によるコマンドの限定，とくしゅコマンドの判定などが未実装
	 * @return
	 *
	 */
	protected void calcCommandList() {
		for (Item item : chara.getItemList()) {
			if (item.type == Item.TYPE_WEAPON) {
				commandFlagArray[1] = true;
			}
			if (item.type == Item.TYPE_SUPPORT) {
				commandFlagArray[2] = true;
			}
			if (item.type == Item.TYPE_USE) {
				commandFlagArray[4] = true;
			}
		}

		for (int i = 0; i < commandFlagArray.length; i++) {
			if (!commandFlagArray[i]) {
				continue;
			}

			if (Command.commandType[i].equals("いどう")) {
				Command command =
						new MoveCommand(Command.commandType[i], sgModel, this);
				commandList.add(command);
			}
			if (Command.commandType[i].equals("こうげき")) {
				Command command =
						new AttackCommand(Command.commandType[i], sgModel, this);
				commandList.add(command);
			}
			if (Command.commandType[i].equals("ほじょ")) {
				Command command =
						new SupportCommand(Command.commandType[i], sgModel, this);
				commandList.add(command);
			}
			if (Command.commandType[i].equals("たいき")) {
				Command command =
						new StandCommand(Command.commandType[i], sgModel, this);
				commandList.add(command);
			}
		}
	}
}
