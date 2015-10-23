package arcircle.ftsim.simulation.model.task;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;

import arcircle.ftsim.simulation.event.Event;

/**
 * 勝利条件の変更をするタスク
 */
public class ChangeWinConditionTask extends Task {

	ArrayList<ArrayList<Event>> winConditionEachPhaseArray;
	ArrayList<ArrayList<Event>> loseConditionEachPhaseArray;
	int currentPhase;

	public ChangeWinConditionTask(TaskManager taskManager,
			ArrayList<ArrayList<Event>> winConditionEachPhaseArray,
			ArrayList<ArrayList<Event>> loseConditionEachPhaseArray,
			int currentPhase) {
		super(taskManager);
		this.winConditionEachPhaseArray = winConditionEachPhaseArray;
		this.loseConditionEachPhaseArray = loseConditionEachPhaseArray;
		this.currentPhase = currentPhase;
	}

	@Override
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {
	}

	@Override
	public void update(int delta) {
	}

}
