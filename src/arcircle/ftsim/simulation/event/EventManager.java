package arcircle.ftsim.simulation.event;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import arcircle.ftsim.simulation.chara.Chara;

public class EventManager {
	ArrayList<ArrayList<Event>> eventArray;

	public EventManager() {
		eventArray = new ArrayList<ArrayList<Event>>();
		for (int i = 0; i < Event.TYPE_NUM; i++) {
			eventArray.add(new ArrayList<Event>());
		}
	}

	public void checkStandEvent(Chara chara) {

	}

	public void checkEvent(Event event) {

	}

	public void loadEventTxt(String eventTxtPath) {
		try {
			File file = new File(eventTxtPath);
			BufferedReader br = new BufferedReader(new FileReader(file));

			String eventStr;

			while ((eventStr = br.readLine()) != null) {
				if (eventStr.length() == 0) {
					continue;
				}

				Event event = loadEvent(eventStr);
				if (event != null) {
					eventArray.get(event.eventType).add(event);
				}
			}

			br.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private Event loadEvent(String eventStr) {
		String[] eventStrs = eventStr.split(",");

//		ARRIVAL,no2,0,0,1,1,battleTalk02.txt
		if (eventStrs[0].equals(Event.ARRIVAL)) {
			EventArrival event = new EventArrival(eventStrs[6]);
			event.charaID = eventStrs[1];
			Point startP = new Point(Integer.valueOf(eventStrs[2]),
					Integer.valueOf(eventStrs[3]));
			Point endP = new Point(Integer.valueOf(eventStrs[4]),
					Integer.valueOf(eventStrs[5]));
			event.upperLeft = startP;
			event.LowerRight = endP;
			return event;
		}
//		CHARA_DIE,boss,battleTalk04.txt
		if (eventStrs[0].equals(Event.CHARA_DIE)) {
			EventCharaDie event = new EventCharaDie(eventStrs[2]);
			event.charaID = eventStrs[1];
			return event;
		}
//		ENEMY_BELOW,3,battleTalk05.txt
		if (eventStrs[0].equals(Event.ENEMY_BELOW)) {
			EventEnemyBelow event = new EventEnemyBelow(eventStrs[2]);
			event.enemyThreshold = Integer.valueOf(eventStrs[1]);
			return event;
		}
//		KILL_ENEMY,4,battleTalk06.txt
		if (eventStrs[0].equals(Event.KILL_ENEMY)) {
			EventKillEnemy event = new EventKillEnemy(eventStrs[2]);
			event.killEnemyNum = Integer.valueOf(eventStrs[1]);
			return event;
		}
//		TOUCH_CHARA,no1,boss,battleTalk01.txt
		if (eventStrs[0].equals(Event.TOUCH_CHARA)) {
			EventTouchChara event = new EventTouchChara(eventStrs[3]);
			event.chara1ID = eventStrs[1];
			event.chara2ID = eventStrs[2];
			return event;
		}
//		TURN_PROGRESS,3,battleTalk03.txt
		if (eventStrs[0].equals(Event.TURN_PROGRESS)) {
			EventTurnProgress event = new EventTurnProgress(eventStrs[2]);
			event.progressTurn = Integer.valueOf(eventStrs[1]);
			return event;
		}

		return null;
	}
}
