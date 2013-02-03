package me.number1_Master.PrisonControl.Listeners;

import java.util.Set;

import me.number1_Master.PrisonControl.Config.*;
import me.number1_Master.PrisonControl.Utils.*;

import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener
{
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		if(Config.getBoolean("Prison.Spawn.Use"))
		{
			Player player = e.getPlayer();
			String playerName = e.getPlayer().getName();
			if(player.hasPlayedBefore() || Players.isSet(player.getName())) return;
			else
			{
				if(!(Players.isSet(playerName + ".Cell"))) Players.createSection(playerName + ".Cell", "");
				System.out.println("Teleporting ...");
				Set<String> spawns = Config.getConfigSection("Prison.Spawns.").getKeys(false);
				for(String spawn : spawns)
				{	
					if(Config.getBoolean("Prison.Spawns." + spawn + ".Used")) continue;
					else
					{
						player.teleport(PrisonSpawn.toLocation(Config.getString("Prison.Spawns." + spawn + ".Spawn Location")));
						Utils.addOwner(playerName, spawn);
						if(!(Config.getBoolean("Prison.Spawn.Loop"))) Config.set("Prison.Spawns." + spawn + ".Used", true);
						return;
					}
				}
				player.teleport(PrisonSpawn.toLocation(Config.getString("Prison.Spawn.Overflow")));
				Utils.addOwner(playerName, "Overflow");
				return;
			}
		}
	}
	
	@EventHandler
	public void onPlayerSpawn(PlayerRespawnEvent e)
	{ 
		if(!(Config.getBoolean("Prison.Spawn.Disable Bed Spawn")) && e.isBedSpawn()) return;
		if(Config.getBoolean("Prison.Spawn.Use"))
		{
			String spawn = Players.getString(e.getPlayer().getName() + ".Cell");
			spawn = (spawn.equalsIgnoreCase("Overflow") || spawn.equals("")) ? 
					"Prison.Spawn.Overflow" : "Prison.Spawns." + spawn + ".Spawn Location";
			e.setRespawnLocation(PrisonSpawn.toLocation(Config.getString(spawn)));
		}
	}
}
