package me.number1_Master.PrisonControl.Config;

import java.io.File;
import java.util.*;

import me.number1_Master.PrisonControl.PrisonControl;
import me.number1_Master.PrisonControl.Utils.*;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.*;

public class Regions 
{
	private static FileConfiguration regions;
	private static File regionsFile;
	
	public static void reload()
	{	
		if(regionsFile == null) regionsFile = new File(PrisonControl.dir, "regions.yml");
		
		regions = YamlConfiguration.loadConfiguration(regionsFile);

		regions.options().header("Need help? Check out PrisonControl's Bukkit Dev!");
		
		regions.options().copyHeader(true);
		save();
	}
	private static void save()
	{
		if(regions == null || regionsFile == null) return;
		
		try
		{ regions.save(regionsFile); }
		catch(Exception err)
		{ Log.s("Could not save regions.yml!"); }
	}
	private static void check()
	{ if(regions == null || regionsFile == null) reload(); }
	
	public static void createSection(String path, Object value)
	{
		check();
		regions.createSection(path);
		save();
		regions.set(path, value);
	}
	public static ConfigurationSection getConfigSection(String path)
	{
		check();
		return regions.getConfigurationSection(path);
	}
	public static String getString(String path)
	{
		check();
		return regions.getString(path);
	}
	public static List<String> getStringList(String path)
	{
		check();
		return regions.getStringList(path);
	}
	public static boolean isSet(String path)
	{
		check();
		return regions.isSet(path);
	}
	public static void set(String path, Object value)
	{
		check();
		regions.set(path, value);
		save();
	}
}