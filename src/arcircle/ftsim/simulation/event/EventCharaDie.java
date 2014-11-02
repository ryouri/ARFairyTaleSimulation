package arcircle.ftsim.simulation.event;

public class EventCharaDie extends Event {
	public String charaID;

	public EventCharaDie(String eventFileName) {
		super(eventFileName);
		this.eventType = Event.TYPE_CHARA_DIE;
	}
}
