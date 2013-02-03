package me.number1_Master.PrisonControl.Config;

import java.io.File;

import me.number1_Master.PrisonControl.PrisonControl;
import me.number1_Master.PrisonControl.Utils.Log;

import org.bukkit.configuration.file.*;

public class Players 
{
	private static FileConfiguration players;
	private static File playersFile;
	
	public static void reload()
	{	
		if(playersFile == null) playersFile = new File(PrisonControl.dir, "players.yml");
		
		players = YamlConfiguration.loadConfiguration(playersFile);

		players.options().header("Need help? Check out PrisonControl's Bukkit Dev!");
		
		players.options().copyHeader(true);
		save();
	}
	private static void save()
	{
		if(players == null || playersFile == null) return;
		
		try
		{ players.save(playersFile); }
		catch(Exception err)
		{ Log.s("Could not save players.yml!"); }
	}
	private static void check()
	{ if(players == null || playersFile == null) reload(); }
	
	public static void createSection(String path, Object value)
	{
		check();
		players.createSection(path);
		save();
		players.set(path, value);
	}
	public static String getString(String path)
	{
		check();
		return players.getString(path);
	}
	public static boolean isSet(String path)
	{
		check();
		return players.isSet(path);
	}
	public static void set(String path, Object value)
	{
		check();
		players.set(path, value);
		save();
	}
}