package arcircle.ftsim.simulation.field;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import arcircle.ftsim.state.simgame.SimGameModel;

public class LoadTerrainInfo {

	public static void loadTerrainInfo(TerrainInfoSupplier terrainManager) {
		// マップチップ読み込み TODO;マジックナンバー！！！
		String itemListPath = SimGameModel.storiesFolder + "/TerrainInfo.txt";
		try {
			File file = new File(itemListPath);
			BufferedReader br = new BufferedReader(new FileReader(file));

			String terrainStr;

			terrainStr = br.readLine();
			if (terrainStr != null) {
				String[] terrainStrs = terrainStr.split(",");
				terrainManager.terrainChipWidth = Integer.valueOf(terrainStrs[0]);
				terrainManager.terrainChipHeight = Integer.valueOf(terrainStrs[1]);
			}

			while ((terrainStr = br.readLine()) != null) {
				if (terrainStr.length() == 0) {
					continue;
				}

				Terrain terrain = loadTerrain(terrainStr);
				if (terrain != null) {
					terrainManager.terrainMap.put(terrain.terrainName, terrain);
					terrainManager.terrainArray.add(terrain);
				}
			}

			br.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

		loadAutoTile(SimGameModel.storiesFolder + "/TerrainInfoAutoTile.txt");
	}


	private static void loadAutoTile(String AutoTileTxtPath) {
	}


	//地形名,マップチップの開始座標x,同左y,回避,防御
	//平地,0,0,0,0
	public static Terrain loadTerrain(String terrainStr) {
		String[] terrainStrs = terrainStr.split(",");

		if (terrainStrs.length <= 4) {
			return null;
		}

		Terrain terrain = new Terrain();
		terrain.terrainName = terrainStrs[0];
		System.out.println(terrain.terrainName);
		terrain.mapChipStartX = Integer.valueOf(terrainStrs[1]);
		terrain.mapChipStartY = Integer.valueOf(terrainStrs[2]);
		terrain.avoidPoint = Integer.valueOf(terrainStrs[3]);
		terrain.defencePoint = Integer.valueOf(terrainStrs[4]);

		return terrain;
	}
}
