package arcircle.ftsim.simulation.model.task.confirmtarget;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.event.EventCharaDie;
import arcircle.ftsim.simulation.field.LoadField;
import arcircle.ftsim.simulation.model.Cursor;
import arcircle.ftsim.simulation.model.task.ConfirmTargetTask;

public class ConfirmCharaDie extends ConfirmTargetTaskLogic {
	Cursor cursor;
	ArrayList<Chara> charaList;
	EventCharaDie eventCharaDie;

	Chara targetChara;

	public static final int MAX_COUNT = 100;
	int count;

	@Override
	public void init(ConfirmTargetTask confirmTargetTask) {
		super.init(confirmTargetTask);

		if (confirmTargetTask.getEvent() instanceof EventCharaDie) {
			eventCharaDie = (EventCharaDie)confirmTargetTask.getEvent();
		}

		this.cursor = getConfirmTargetTask().getEventManager().getField().getCursor();
		this.charaList = getConfirmTargetTask().getEventManager().getField().getCharaListByCharaID(eventCharaDie.charaID);

		//TODO: とりあえず最初の一体をターゲットとする
		targetChara = charaList.get(0);
	}

	@Override
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {
		g.setColor(Color.red);
		if (count % 30 < 15) {
			g.drawRect(targetChara.pX + offsetX, targetChara.pY + offsetY, 32,
					LoadField.MAP_CHIP_SIZE);
			g.drawRect(targetChara.pX + offsetX - 1, targetChara.pY + offsetY
					- 1, LoadField.MAP_CHIP_SIZE + 2, LoadField.MAP_CHIP_SIZE + 2);
		} else {
			g.drawRect(targetChara.pX + offsetX - 2, targetChara.pY + offsetY
					- 2, LoadField.MAP_CHIP_SIZE + 4, LoadField.MAP_CHIP_SIZE + 4);
			g.drawRect(targetChara.pX + offsetX - 3, targetChara.pY + offsetY
					- 3, LoadField.MAP_CHIP_SIZE + 6, LoadField.MAP_CHIP_SIZE + 6);
		}
		g.setColor(Color.white);
	}

	@Override
	public void update(int delta) {
		this.cursor.pX = targetChara.pX;
		this.cursor.pY = targetChara.pY;
		this.cursor.x = targetChara.x;
		this.cursor.y = targetChara.y;

		count++;
		if (MAX_COUNT < count) {
			getConfirmTargetTask().finishTask();
		}
	}
}
