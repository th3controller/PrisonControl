package me.number1_Master.PrisonControl;

import java.io.File;

import me.number1_Master.PrisonControl.Commands.PCCommand;
import me.number1_Master.PrisonControl.Config.*;
import me.number1_Master.PrisonControl.Listeners.*;

import org.bukkit.plugin.java.JavaPlugin;

public class PrisonControl extends JavaPlugin
{
	public static File dir;
	
	public void onEnable()
	{
		dir = this.getDataFolder();
		Config.reload();
		Players.reload();
		Regions.reload();
		
		this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
		this.getCommand("prisoncontrol").setExecutor(new PCCommand());
	}
	public void onDisable() {}
}
