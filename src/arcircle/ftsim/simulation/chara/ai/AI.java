package arcircle.ftsim.simulation.chara.ai;

import arcircle.ftsim.simulation.model.Characters;
import arcircle.ftsim.simulation.model.Field;

public abstract class AI {

	abstract public void thinkAndDo(Field field, Characters characters);
}
