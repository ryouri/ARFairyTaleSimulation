package arcircle.ftsim.simulation.model.task;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;

import arcircle.ftsim.simulation.algorithm.range.Node;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.event.Event;
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

	public void addOneTaskAndGenerateArray (Task task) {
		ArrayList<Task> taskArray = new ArrayList<Task>();
		taskArray.add(task);
		taskArrayArray.add(taskArray);
	}

	public void addStandCharaTask(Chara chara) {
		StandCharaTask standCharaTask = new StandCharaTask(this, chara);
		addOneTaskAndGenerateArray(standCharaTask);
	}

	public void addMoveTask(Chara chara, Node moveNode) {
		MoveTask moveTask = new MoveTask(this, chara, moveNode);
		addOneTaskAndGenerateArray(moveTask);
	}

	public void addAttackTask(Chara attackChara, Chara damageChara) {
		AttackTask attackTask = new AttackTask(this, attackChara, damageChara);
		addOneTaskAndGenerateArray(attackTask);
	}

	public void addTalkTask(Event processEvent) {
		TalkTask talkTask = new TalkTask(this, processEvent, field);
		addOneTaskAndGenerateArray(talkTask);
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
}
