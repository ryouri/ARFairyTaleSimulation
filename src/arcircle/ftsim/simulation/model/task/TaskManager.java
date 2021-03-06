
package arcircle.ftsim.simulation.model.task;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;

import arcircle.ftsim.simulation.algorithm.range.Node;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.chara.battle.CalcurateExp;
import arcircle.ftsim.simulation.event.Event;
import arcircle.ftsim.simulation.event.EventManager;
import arcircle.ftsim.simulation.model.Characters;
import arcircle.ftsim.simulation.model.Field;

public class TaskManager {

	Characters characters;

	Field field;

	private ArrayList<ArrayList<Task>> taskArrayArray;

	public TaskManager(Field field, Characters characters) {
		this.field = field;
		this.characters = characters;
		this.taskArrayArray = new ArrayList<ArrayList<Task>>();
	}

	public void checkCharaDieEvent(String id) {
		field.eventManager.checkCharaDieEvent(id);
	}

	public void addWinTask () {
		WinTask winTask = new WinTask(this, field);
		addOneTaskAndGenerateArray(winTask);
	}
	public void addLoseTask() {
		LoseTask loseTask = new LoseTask(this, field);
		addOneTaskAndGenerateArray(loseTask);
	}
	public void addOneTaskAndGenerateArray (Task task) {
		ArrayList<Task> taskArray = new ArrayList<Task>();
		taskArray.add(task);
		taskArrayArray.add(taskArray);
	}

	public void addStandCharaTask(Chara chara, boolean stand) {
		StandCharaTask standCharaTask = new StandCharaTask(this, chara, stand);
		addOneTaskAndGenerateArray(standCharaTask);
	}

	public void addMoveTask(Chara chara, Node moveNode) {
		MoveTask moveTask = new MoveTask(this, chara, moveNode);
		addOneTaskAndGenerateArray(moveTask);
	}

	public void addAttackTask(Chara attackChara, Chara damageChara,Point attackPoint) {
		AttackTask attackTask = new AttackTask(this, attackChara, damageChara, attackPoint);
		addOneTaskAndGenerateArray(attackTask);
	}

	public void addTalkTask(Event processEvent) {
		TalkTask talkTask = new TalkTask(this, processEvent, field);
		addOneTaskAndGenerateArray(talkTask);
	}

	public void addTurnEndTask(Characters characters, int camp) {
		TurnEndTask turnEndTask = new TurnEndTask(this, characters, camp);
		addOneTaskAndGenerateArray(turnEndTask);
	}

	public void addCharaDieTask(Chara chara) {
		CharaDieTask charaDieTask = new CharaDieTask(this, chara);
		addOneTaskAndGenerateArray(charaDieTask);
	}

	public void addExpTask(Chara chara, int addExp) {
		AddExpTask addExpTask = new AddExpTask(this, chara, addExp);
		addOneTaskAndGenerateArray(addExpTask);
	}

	public void addLevelUpTask(Chara chara, CalcurateExp calcurateExp) {
		LevelUpTask levelUpTask = new LevelUpTask(this, chara, calcurateExp);
		addOneTaskAndGenerateArray(levelUpTask);
	}

	public void addHealTask(Chara healChara, Chara healedChara) {
		HealTask healTask = new HealTask(this, healChara, healedChara);
		addOneTaskAndGenerateArray(healTask);
	}
	public void addConfirmTargetTask(EventManager eventManager) {
		ConfirmTargetTask confirmTargetTask = new ConfirmTargetTask(this, eventManager);
		addOneTaskAndGenerateArray(confirmTargetTask);
	}

	public void addNextWinConditionTask(
			ArrayList<ArrayList<Event>> winConditionEachPhaseArray,
			ArrayList<ArrayList<Event>> loseConditionEachPhaseArray,
			int currentPhase) {
		ChangeWinConditionTask changeTask =
				new ChangeWinConditionTask(this, winConditionEachPhaseArray, loseConditionEachPhaseArray, currentPhase);

	}

	public boolean existTask() {
		return !taskArrayArray.isEmpty();
	}

	public void processRender(
			Graphics g, int offsetX, int offsetY,
			int firstTileX, int lastTileX,
			int firstTileY, int lastTileY) {
		if (taskArrayArray.size() == 0) {
			return;
		}

		ArrayList<Task> taskArray = taskArrayArray.get(0);
		for (Task task : taskArray) {
			task.render(g, offsetX, offsetY, firstTileX, lastTileX, firstTileY, lastTileY);
		}
	}

	public void processUpdate(int delta) {
		if (taskArrayArray.size() == 0) {
			return;
		}

		ArrayList<Task> taskArray = taskArrayArray.get(0);
		for (Task task : taskArray) {
			task.update(delta);
		}
	}

	public void taskEnd() {
		if (!taskArrayArray.isEmpty()) {
			taskArrayArray.remove(0);
		}
	}

	public void occurEffect(int px, int py, String effectName) {
		field.occurEffect(px, py, effectName);
	}

	public void checkEnemyBelowEvent() {
		field.checkEnemyBelowEvent();
	}
}
