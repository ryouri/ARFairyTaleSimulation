package arcircle.ftsim.simulation.event;

public class EventEnemyBelow extends Event {
	public int enemyThreshold;

	public EventEnemyBelow(String eventFileName) {
		super(eventFileName);
		this.eventType = Event.TYPE_ENEMY_BELOW;
	}
}
