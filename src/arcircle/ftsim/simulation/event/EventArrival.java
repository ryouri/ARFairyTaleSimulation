package arcircle.ftsim.simulation.event;

import java.awt.Point;

public class EventArrival extends Event {
	public String charaID;
	public Point upperLeft;
	public Point LowerRight;

	public EventArrival(String eventFileName) {
		super(eventFileName);
		this.eventType = Event.TYPE_ARRIVAL;
	}
}
