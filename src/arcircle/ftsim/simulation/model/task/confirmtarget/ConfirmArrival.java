package arcircle.ftsim.simulation.model.task.confirmtarget;

import java.awt.Point;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import arcircle.ftsim.simulation.event.EventArrival;
import arcircle.ftsim.simulation.field.LoadField;
import arcircle.ftsim.simulation.model.Cursor;
import arcircle.ftsim.simulation.model.Field;
import arcircle.ftsim.simulation.model.task.ConfirmTargetTask;

public class ConfirmArrival extends ConfirmTargetTaskLogic {
	EventArrival eventArrival;
	Point upperLeft;
	Point lowerRight;
	Cursor cursor;

	public static final int MAX_COUNT = 120;
	int count;

	private Color arrivalColor;

	@Override
	public void init(ConfirmTargetTask confirmTargetTask) {
		super.init(confirmTargetTask);

		if (confirmTargetTask.getEvent() instanceof EventArrival) {
			eventArrival = (EventArrival)confirmTargetTask.getEvent();
		}

		this.cursor = getConfirmTargetTask().getEventManager().getField().getCursor();

		this.upperLeft = eventArrival.upperLeft;
		this.lowerRight = eventArrival.LowerRight;
		arrivalColor = new Color(1.0f, 0.5f, 0.5f, 0.35f);
	}

	@Override
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {
		for (int y = upperLeft.y; y <= lowerRight.y; y++) {
			for (int x = upperLeft.x; x <= lowerRight.x; x++) {
				if (!(firstTileY <= y && y < lastTileY)) {
					continue;
				}
				if (!(firstTileX <= x && x < lastTileX)) {
					continue;
				}

				Color nowColor = new Color(arrivalColor);
				nowColor.a += (count % 60 - 10) * 0.01f;

				g.setColor(nowColor);
				g.fillRect(Field.tilesToPixels(x) + offsetX,
						   Field.tilesToPixels(y) + offsetY,
						   LoadField.MAP_CHIP_SIZE, LoadField.MAP_CHIP_SIZE);
				g.setColor(Color.white);
			}
		}
	}

	@Override
	public void update(int delta) {
		this.cursor.pX = upperLeft.x * 32;
		this.cursor.pY = upperLeft.y * 32;
		this.cursor.x = upperLeft.x;
		this.cursor.y = upperLeft.y;

		count++;
		if (MAX_COUNT < count) {
			getConfirmTargetTask().finishTask();
		}
	}
}
