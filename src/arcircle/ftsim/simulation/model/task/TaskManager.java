package arcircle.ftsim.simulation.model.task;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;

import arcircle.ftsim.simulation.chara.Chara;
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

	public void addAttackTask(Chara attackChara, Chara damageChara) {
		AttackTask attackTask = new AttackTask(this, attackChara, damageChara);
		ArrayList<Task> taskArray = new ArrayList<Task>();
		taskArray.add(attackTask);
		taskArrayArray.add(taskArray);
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
