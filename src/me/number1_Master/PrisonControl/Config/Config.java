package me.number1_Master.PrisonControl.Config;

import java.io.File;
import java.util.*;

import me.number1_Master.PrisonControl.PrisonControl;
import me.number1_Master.PrisonControl.Utils.*;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.*;

public class Config 
{
	private static FileConfiguration config;
	private static File configFile;
	
	public static void reload()
	{	
		if(configFile == null) configFile = new File(PrisonControl.dir, "config.yml");
		
		config = YamlConfiguration.loadConfiguration(configFile);

		config.options().header("Need help? Check out PrisonControl's Bukkit Dev!");
		
		config.addDefault("Prison.Version", "1.0");
		config.addDefault("Prison.Spawn.Use", false);
		config.addDefault("Prison.Spawn.Loop", false);
		config.addDefault("Prison.Spawn.Disable Bed Spawn", true);
		config.addDefault("Prison.Spawn.Overflow",
				new PrisonSpawn(Bukkit.getServer().getWorlds().get(0).getSpawnLocation()).toString());
		
		config.options().copyHeader(true);
		config.options().copyDefaults(true);
		save();
	}
	private static void save()
	{
		if(config == null || configFile == null) return;
		
		try
		{ config.save(configFile); }
		catch(Exception err)
		{ Log.s("Could not save config.yml!"); }
	}
	private static void check()
	{ if(config == null || configFile == null) reload(); }
	
	public static void createSection(String path, Object value)
	{
		check();
		config.createSection(path);
		save();
		config.set(path, value);
	}
	public static boolean getBoolean(String path)
	{
		check();
		return config.getBoolean(path);
	}
	public static ConfigurationSection getConfigSection(String path)
	{
		check();
		return config.getConfigurationSection(path);
	}
	public static int getInt(String path)
	{
		check();
		return config.getInt(path);
	}
	public static String getString(String path)
	{
		check();
		return config.getString(path);
	}
	public static boolean isSet(String path)
	{
		check();
		return config.isSet(path);
	}
	public static List<String> getStringList(String path)
	{
		check();
		return config.getStringList(path);
	}
	public static void set(String path, Object value)
	{
		check();
		config.set(path, value);
		save();
	}
}