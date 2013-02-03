package me.number1_Master.PrisonControl.Utils;

import me.number1_Master.PrisonControl.Config.Config;

import org.bukkit.*;

public class PrisonSpawn
{
	private String world;
	private long x;
	private long y;
	private long z;
	
	public PrisonSpawn(Location location)
	{ 
		world = location.getWorld().getName();
		x = Math.round(location.getX());
		y = Math.round(location.getY());
		z = Math.round(location.getZ());
	}
	
	public void createAt(String path)
	{
		if(path.toLowerCase().contains("overflow"))
			Log.s("Failed to create a location! Please report issue to number1_Master immediately!");;
		
		Config.createSection(path + ".Spawn Location", this.toString());
		Config.createSection(path + ".Sign Location", "");
		Config.createSection(path + ".Used", false);
	}
	
	public void saveTo(String path)
	{ Config.set(path, this.toString()); }
	
	public String toString()
	{ return world + " " + x + " " + y + " " + z; }
	
	public static Location toLocation(String location)
	{
		String[] loc = location.split(" ");
		try
		{	
			World world = Bukkit.getServer().getWorld(loc[0]);
			int x = Integer.parseInt(loc[1]);
			int y = Integer.parseInt(loc[2]);
			int z = Integer.parseInt(loc[3]);
			return new Location(world, x, y, z);
		}
		catch(NumberFormatException err)
		{
			Log.s(location + " is an invalid location in the config.yml!");
			return null;
		}
	}
}
