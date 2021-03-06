package arcircle.ftsim.simulation.event;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.model.Field;

public class EventManager {
	/**
	 * 計算量を減らすため，各イベントごとに配列に分けられている
	 */
	ArrayList<ArrayList<Event>> eventArray;


	/**
	 * フェイズごとの勝利条件が入っている。
	 * phaseNowでフェイズの進行状況を管理
	 */
	private ArrayList<ArrayList<Event>> winConditionEachPhaseArray;
	/**
	 * フェイズごとの敗北条件が入っている。
	 * phaseNowでフェイズの進行状況を管理
	 */
	private ArrayList<ArrayList<Event>> loseConditionEachPhaseArray;

	/**
	 * 今のフェイズの進行状況（intで表現）
	 */
	public int currentPhase;

	Field field;

	public ArrayList<String> getWinConditionString() {
		ArrayList<String> winStringArray = new ArrayList<String>();

		if (winConditionEachPhaseArray.isEmpty()) {
			return null;
		}

		for (Event event : winConditionEachPhaseArray.get(0)) {
			winStringArray.add(event.eventID);
		}
		return winStringArray;
	}

	public ArrayList<String> getLoseConditionString() {
		ArrayList<String> loseStringArray = new ArrayList<String>();

		if (loseConditionEachPhaseArray.isEmpty()) {
			return null;
		}

		for (Event event : loseConditionEachPhaseArray.get(0)) {
			loseStringArray.add(event.eventID);
		}
		return loseStringArray;
	}

	public ArrayList<ArrayList<Event>> getWinConditionEachPhaseArray() {
		return winConditionEachPhaseArray;
	}

	public ArrayList<ArrayList<Event>> getLoseConditionEachPhaseArray() {
		return loseConditionEachPhaseArray;
	}

	public EventManager(Field field) {
		eventArray = new ArrayList<ArrayList<Event>>();
		for (int i = 0; i < Event.TYPE_NUM; i++) {
			eventArray.add(new ArrayList<Event>());
		}

		winConditionEachPhaseArray = new ArrayList<ArrayList<Event>>();
		loseConditionEachPhaseArray = new ArrayList<ArrayList<Event>>();

		this.field = field;
		this.currentPhase = 0;
	}

	public void checkStandEvent(Chara chara) {
		//到達イベントの処理
		Point upperLeft = new Point(chara.x, chara.y);
		EventArrival eventArrival = new EventArrival("");
		eventArrival.charaID = chara.id;
		eventArrival.upperLeft = upperLeft;
		checkEvent(eventArrival);

		//キャラ同士隣接イベントの処理
		int[][] direction = {
				{-1,  0},
				{ 0, -1},
				{ 1,  0},
				{ 0,  1}};

		for(int[] xyArray : direction) {
			Chara neighborChara =
					field.getXYChara(chara.x - xyArray[0], chara.y - xyArray[1]);
			if (neighborChara == null) {
				continue;
			}

			EventTouchChara eventTouchChara = new EventTouchChara("");
			eventTouchChara.chara1ID = chara.id;
			eventTouchChara.chara2ID = neighborChara.id;
			checkEvent(eventTouchChara);
		}
	}


	public void checkCharaDieEvent(String id) {
		//キャラが死んだ時のイベントの処理
		EventCharaDie eventCharaDie = new EventCharaDie("");
		eventCharaDie.charaID = id;
		checkEvent(eventCharaDie);
	}

	public void checkEnemyBelowEvent(int enemyNum) {
		//キャラが死んだ時のイベントの処理
		EventEnemyBelow eventEnemyBelow = new EventEnemyBelow("");
		eventEnemyBelow.enemyThreshold = enemyNum;
		checkEvent(eventEnemyBelow);

	}

	public void checkTurnElapsed(int nowTurn) {
		EventTurnProgress eventTurnProgress = new EventTurnProgress("");
		eventTurnProgress.progressTurn = nowTurn;
		checkEvent(eventTurnProgress);
	}

	private void startEvent(Event processEvent
			, ArrayList<Event> removeEventArray) {
		startBattleTalk(processEvent);
		removeEventArray.add(processEvent);
	}

	/**
	 * TODO: ここでTaskを生成して，そこにTalkTaskを入れる
	 * @param processEvent
	 */
	private void startBattleTalk(Event processEvent) {
		field.getTaskManager().addTalkTask(processEvent);
	}

	public void checkEvent(Event checkEvent) {
		int eventType = checkEvent.eventType;
		ArrayList<Event> removeEventArray = new ArrayList<Event>();

		for (Event event : eventArray.get(eventType)) {
			if(eventEquals(event, checkEvent)) {
				startEvent(event, removeEventArray);
			}
		}

		for (Event event : removeEventArray) {
			eventArray.get(event.eventType).remove(event);
		}

		checkEndConditionEvent(checkEvent);
	}

	public void checkEndConditionEvent(Event checkEvent) {
		//Eventのチェックが2つ同時に発生し、勝利条件を満たすとcurrentPhaseが大きい状態でここに到達する可能性がある
		if (winConditionEachPhaseArray.size() <= currentPhase) {
			return;
		}

		for (Event event : winConditionEachPhaseArray.get(currentPhase)) {
			if(eventEquals(event, checkEvent)) {
				startBattleTalk(event);
				currentPhase++;
				//TODO:勝利の処理！ phaseNowが配列の閾値以上なら終了する
				if (currentPhase >= winConditionEachPhaseArray.size()) {
					field.getTaskManager().addWinTask();
					return;
				} else {
					//条件の変化タスクを発生
					field.getTaskManager().addNextWinConditionTask(
							winConditionEachPhaseArray, loseConditionEachPhaseArray,
							currentPhase);
				}

			}
		}
		for (Event event : loseConditionEachPhaseArray.get(currentPhase)) {
			if(eventEquals(event, checkEvent)) {
				startBattleTalk(event);
				currentPhase++;
				//TODO:敗北の処理！
				if (currentPhase >= loseConditionEachPhaseArray.size()) {
					field.getTaskManager().addLoseTask();
				}
			}
		}
	}

	public boolean eventEquals(Event event, Event checkEvent) {
		if (event instanceof EventTouchChara
				&& checkEvent instanceof EventTouchChara) {
			EventTouchChara searchEvent = (EventTouchChara)checkEvent;
			EventTouchChara processEvent = (EventTouchChara)event;
			if ((searchEvent.chara1ID.equals(processEvent.chara1ID)
					&& searchEvent.chara2ID.equals(processEvent.chara2ID))
					|| (searchEvent.chara1ID.equals(processEvent.chara2ID)
					&& searchEvent.chara2ID.equals(processEvent.chara1ID))) {
				return true;
			}
		} else if (event instanceof EventArrival
				&& checkEvent instanceof EventArrival ) {
			EventArrival searchEvent = (EventArrival)checkEvent;
			EventArrival processEvent = (EventArrival)event;
			if (searchEvent.charaID.equals(processEvent.charaID)
					&& searchEvent.upperLeft.x >= processEvent.upperLeft.x
					&& searchEvent.upperLeft.y >= processEvent.upperLeft.y
					&& searchEvent.upperLeft.x <= processEvent.LowerRight.x
					&& searchEvent.upperLeft.y <= processEvent.LowerRight.y) {
				return true;
			}
		} else if (event instanceof EventCharaDie
				&& checkEvent instanceof EventCharaDie ) {
			EventCharaDie searchEvent = (EventCharaDie)checkEvent;
			EventCharaDie processEvent = (EventCharaDie)event;
			if (searchEvent.charaID.equals(processEvent.charaID)) {
				return true;
			}
		} else if (event instanceof EventEnemyBelow
				&& checkEvent instanceof EventEnemyBelow ) {
			EventEnemyBelow searchEvent = (EventEnemyBelow)checkEvent;
			EventEnemyBelow processEvent = (EventEnemyBelow)event;
			if (searchEvent.enemyThreshold <= processEvent.enemyThreshold) {
				return true;
			}
		} else if (event instanceof EventKillEnemy
				&& checkEvent instanceof EventKillEnemy ) {
			EventKillEnemy searchEvent = (EventKillEnemy)checkEvent;
			EventKillEnemy processEvent = (EventKillEnemy)event;
			if (searchEvent.killEnemyNum >= processEvent.killEnemyNum) {
				return true;
			}
		} else if (event instanceof EventTurnProgress
				&& checkEvent instanceof EventTurnProgress ) {
			EventTurnProgress searchEvent = (EventTurnProgress)checkEvent;
			EventTurnProgress processEvent = (EventTurnProgress)event;
			if (searchEvent.progressTurn >= processEvent.startTurn + processEvent.progressTurn) {
				return true;
			}
		}
		return false;
	}


	public static final int TYPE_WIN = 0;
	public static final String WIN = "WIN";
	public static final int TYPE_LOSE = 1;
	public static final String LOSE = "LOSE";

	public static final String PHASE = "PHASE";
	public static final String PHASEEND = "PHASEEND";
	public void loadEndConditionTxt(String endConditionTxtPath) {
		try {
			File file = new File(endConditionTxtPath);
			BufferedReader br = new BufferedReader(new FileReader(file));

			String endConditionLine;

			while ((endConditionLine = br.readLine()) != null) {
				if (endConditionLine.length() == 0) {
					continue;
				}

				//PHASEが来たら読み出し開始
				if (endConditionLine.equals(PHASE)) {
					ArrayList<Event> winConditionArray = new ArrayList<Event>();
					ArrayList<Event> loseConditionArray = new ArrayList<Event>();

					endConditionLine = br.readLine();
					if (endConditionLine == null) {
						System.err.println("endConditoinFileError");
						System.exit(1);
					}
					//PHASEENDが来るまで読み出す
					while(!endConditionLine.equals(PHASEEND)) {
						String[] endConditionStrs = endConditionLine.split(",");
						String eventStr =
								endConditionLine.replace(
										endConditionStrs[0] + ",", "");
						Event event = loadEvent(eventStr);

						if (endConditionStrs[0].equals(WIN)) {
							winConditionArray.add(event);
						} else if (endConditionStrs[0].equals(LOSE)) {
							loseConditionArray.add(event);
						} else {
							System.err.println("endConditoinFileError");
							System.exit(1);
						}

						endConditionLine = br.readLine();
						if (endConditionLine == null) {
							System.err.println("endConditoinFileError");
							System.exit(1);
						}
					}

					winConditionEachPhaseArray.add(winConditionArray);
					loseConditionEachPhaseArray.add(loseConditionArray);
				}
			}

			br.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
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
		if (eventStrs[1].equals(Event.ARRIVAL)) {
			EventArrival event = new EventArrival(eventStrs[7]);
			event.eventID = eventStrs[0];
			event.charaID = eventStrs[2];
			Point startP = new Point(Integer.valueOf(eventStrs[3]),
					Integer.valueOf(eventStrs[4]));
			Point endP = new Point(Integer.valueOf(eventStrs[5]),
					Integer.valueOf(eventStrs[6]));
			event.upperLeft = startP;
			event.LowerRight = endP;
			return event;
		}
//		CHARA_DIE,boss,battleTalk04.txt
		if (eventStrs[1].equals(Event.CHARA_DIE)) {
			EventCharaDie event = new EventCharaDie(eventStrs[3]);
			event.eventID = eventStrs[0];
			event.charaID = eventStrs[2];
			return event;
		}
//		ENEMY_BELOW,3,battleTalk05.txt
		if (eventStrs[1].equals(Event.ENEMY_BELOW)) {
			EventEnemyBelow event = new EventEnemyBelow(eventStrs[3]);
			event.eventID = eventStrs[0];
			event.enemyThreshold = Integer.valueOf(eventStrs[2]);
			return event;
		}
//		KILL_ENEMY,4,battleTalk06.txt
		if (eventStrs[1].equals(Event.KILL_ENEMY)) {
			EventKillEnemy event = new EventKillEnemy(eventStrs[3]);
			event.eventID = eventStrs[0];
			event.killEnemyNum = Integer.valueOf(eventStrs[2]);
			return event;
		}
//		TOUCH_CHARA,no1,boss,battleTalk01.txt
		if (eventStrs[1].equals(Event.TOUCH_CHARA)) {
			EventTouchChara event = new EventTouchChara(eventStrs[4]);
			event.eventID = eventStrs[0];
			event.chara1ID = eventStrs[2];
			event.chara2ID = eventStrs[3];
			return event;
		}
//		TURN_PROGRESS,3,battleTalk03.txt
		if (eventStrs[1].equals(Event.TURN_PROGRESS)) {
			EventTurnProgress event = new EventTurnProgress(eventStrs[3]);
			event.eventID = eventStrs[0];
			event.progressTurn = Integer.valueOf(eventStrs[2]);
			return event;
		}

		return null;
	}

	public Field getField() {
		return field;
	}
}