package arcircle.ftsim.simulation.event;

public class EventTouchChara extends Event {
	public String chara1ID;
	public String chara2ID;

	public EventTouchChara(String eventFileName) {
		super(eventFileName);
		this.eventType = Event.TYPE_TOUCH_CHARA;
	}
}
