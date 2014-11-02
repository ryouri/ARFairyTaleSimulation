package arcircle.ftsim.simulation.event;

public class EventKillEnemy extends Event {
	public int killEnemyNum;

	public EventKillEnemy(String eventFileName) {
		super(eventFileName);
		this.eventType = Event.TYPE_KILL_ENEMY;
	}
}
