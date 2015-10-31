package arcircle.ftsim.simulation.event;

public class EventTurnProgress extends Event {
	public int startTurn;
	public int progressTurn;

	public EventTurnProgress(String eventFileName) {
		super(eventFileName);
		this.eventType = Event.TYPE_TURN_PROGRESS;
	}
}
