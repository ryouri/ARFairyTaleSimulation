package arcircle.ftsim.simulation.model.task;

import org.newdawn.slick.Graphics;

import arcircle.ftsim.simulation.event.Event;
import arcircle.ftsim.simulation.model.Field;
import arcircle.ftsim.simulation.talk.BattleTalkModel;
import arcircle.ftsim.simulation.talk.BattleTalkView;
import arcircle.ftsim.state.simgame.SimGameModel;

public class TalkTask extends Task {
	TaskManager taskManager;
	BattleTalkModel btModel;
	BattleTalkView btView;
	Event processEvent;
	Field field;

	public TalkTask(TaskManager taskManager,
			Event processEvent,
			Field field) {
		super(taskManager);

		this.taskManager = taskManager;
		this.processEvent = processEvent;
		this.field = field;

		SimGameModel sgModel = field.getSgModel();

		this.btModel = new BattleTalkModel(sgModel, processEvent.eventFileName);
		this.btView = new BattleTalkView(btModel, sgModel);

		sgModel.pushKeyInputStack(btModel);
		sgModel.addRendererArray(btView);
	}

	@Override
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {

	}

	@Override
	public void update(int delta) {
		if (btModel.isEnd()) {
			taskManager.taskEnd();
		}
	}
}
