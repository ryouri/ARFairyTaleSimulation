package arcircle.ftsim.simulation.model.task;

import org.newdawn.slick.Graphics;

import arcircle.ftsim.simulation.model.Field;
import arcircle.ftsim.simulation.model.task.lose.LoseTaskModel;
import arcircle.ftsim.simulation.model.task.lose.LoseTaskView;

public class LoseTask extends Task{
	LoseTaskModel ltModel;
	LoseTaskView ltView;
	Field field;
	boolean isActivate;


	public LoseTask(TaskManager taskManager, Field field) {
		super(taskManager);
		this.field = field;
		isActivate = false;
	}

	public void returnTitle() {
		field.getSgModel().removeKeyInputStackByField();
		field.getSgModel().removeRendererArrayEnd();
		field.getSgModel().returnGameStartState();
	}

	@Override
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {
	}

	@Override
	public void update(int delta) {
		if (!isActivate) {
			isActivate = true;
			ltModel = new LoseTaskModel(this);
			ltView = new LoseTaskView(ltModel);
			field.getSgModel().pushKeyInputStack(ltModel);
			field.getSgModel().addRendererArray(ltView);
		}
	}
}
