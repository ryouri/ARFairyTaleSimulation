package arcircle.ftsim.simulation.field;

import java.util.HashMap;

public class Terrain {
	public String terrainName;
	public int mapChipStartX;
	public int mapChipStartY;
	public int avoidPoint;
	public int defencePoint;
	public HashMap<String, Integer> classNameCostMap;
	public Terrain() {
		classNameCostMap = new HashMap<String, Integer>();
	}
}
