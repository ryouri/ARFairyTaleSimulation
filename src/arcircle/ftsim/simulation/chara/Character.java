package arcircle.ftsim.simulation.chara;

import java.util.ArrayList;

import org.newdawn.slick.command.Command;

import arcircle.ftsim.simulation.item.Item;

public class Character {
	public String name;
	public int gender;

	public int level;
	public int exp;

	public int hp;
	public int power;
	public int magicPower;
	public int spead;
	public int tech;
	public int luck;
	public int defence;
	public int magicDefence;

	public ArrayList<Item> itemList;
	public ArrayList<Command> commandList;
}