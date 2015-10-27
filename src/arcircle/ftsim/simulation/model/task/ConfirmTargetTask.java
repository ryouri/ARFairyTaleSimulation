package arcircle.ftsim.simulation.model.task;

import org.newdawn.slick.Graphics;

import arcircle.ftsim.simulation.event.Event;
import arcircle.ftsim.simulation.event.EventManager;
import arcircle.ftsim.simulation.model.task.confirmtarget.ConfirmTargetTaskLogic;

public class ConfirmTargetTask extends Task {
	private EventManager eventManager;
	private ConfirmTargetTaskLogic confirmTargetTaskLogic;
	private Event event;

	public EventManager getEventManager() {
		return eventManager;
	}

	public ConfirmTargetTask(TaskManager taskManager, EventManager eventManager) {
		super(taskManager);
		this.eventManager = eventManager;

		this.event = eventManager.getWinConditionEachPhaseArray().get(0).get(0);


		String className = ConfirmTargetTaskLogic.packageName
				+ ConfirmTargetTaskLogic.classList[event.eventType];

		Class<?> recommBuilderClass = null;
		try {
			recommBuilderClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		if (recommBuilderClass == null) {
			System.err.println("Generate ConfirmTargetTask Error");
			System.exit(1);
		}

		try {
			this.confirmTargetTaskLogic = (ConfirmTargetTaskLogic) recommBuilderClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}


		confirmTargetTaskLogic.init(this);
	}

	@Override
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {
		confirmTargetTaskLogic.render(g, offsetX, offsetY, firstTileX, lastTileX, firstTileY, lastTileY);
	}

	@Override
	public void update(int delta) {
		confirmTargetTaskLogic.update(delta);

	}

	public Event getEvent() {
		return event;
	}

	public void finishTask() {
		taskManager.taskEnd();
	}
}
