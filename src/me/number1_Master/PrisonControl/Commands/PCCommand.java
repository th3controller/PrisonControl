package me.number1_Master.PrisonControl.Commands;

import java.util.ArrayList;

import me.number1_Master.PrisonControl.Config.*;
import me.number1_Master.PrisonControl.Utils.*;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class PCCommand implements CommandExecutor
{
	private String prefix = Utils.getPrefix(true);
	
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args)
	{
		// Determine information for arguments.
		if(args.length == 0)
		{
			sender.sendMessage(prefix + "Not enough arguments!");
			return true;
		}
		String arg1 = args[0].toLowerCase();
		String arg2 = (args.length > 1) ? args[1] : "";
		String arg3 = (args.length > 2) ? args[2] : "";
		String arg4 = (args.length > 3) ? args[3] : "";
		
		// Check if the sender is a Player.
		Player player = (sender instanceof Player) ? (Player) sender : null;
		PrisonSpawn loc = (player != null) ? new PrisonSpawn(player.getLocation()) : null;
		
		if(arg1.equals("reload"))
		{
			if(!(Utils.hasPermission(sender, "prisoncontrol.reload"))) return true;
			
			// /pc reload
			if(arg2.equals(""))
			{
				Config.reload();
				Players.reload();
				Regions.reload();
				sender.sendMessage(prefix + "Configurations reloaded!");
				return true;
			}
			else
			{
				player.sendMessage(prefix + "Too many arguments!");
				return true;
			}
		}
		
		/*								*
		 * 	Handles /pc setspawn [name]	*
		 *								*/
		else if(player != null && arg1.equals("setspawn"))
		{
			if(!(Utils.hasPermission(player, "prisoncontrol.command.setspawn"))) return true;
			
			if(arg2.equals(""))
			{
				sender.sendMessage(prefix + "Not enough arguments!");
				return true;
			}
			else if(arg3.equals(""))
			{
				// /pc setspawn overflow
				if(arg2.equalsIgnoreCase("overflow"))
				{
					loc.saveTo("Prison.Spawn.Overflow");
					player.sendMessage(prefix + "The Overflow spawn has been set!");
					return true;
				}
				
				// /pc setspawn <spawn>
				if(!(Config.isSet("Prison.Spawns." + arg2))) loc.createAt("Prison.Spawns." + arg2);
				else loc.saveTo("Prison.Spawns." + arg2 + ".Spawn Location");
				player.sendMessage(prefix + "Cell Spawn " + arg2 + " has been set!");
				return true;
			}
			else
			{
				player.sendMessage(prefix + "Too many arguments!");
				return true;
			}
		}
		
		/*								*
		 * 	Handles /pc delspawn [name]	*
		 *								*/
		else if(arg1.equals("delspawn"))
		{
			if(!(Utils.hasPermission(sender, "prisoncontrol.command.delspawn"))) return true;
			
			if(arg2.equals(""))
			{
				sender.sendMessage(prefix + "Not enough arguments!");
				return true;
			}
			else if(arg3.equals(""))
			{
				// /pc delspawn overflow
				if(arg2.equalsIgnoreCase("overflow"))
				{
					PrisonSpawn mainSpawn = new PrisonSpawn(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
					mainSpawn.saveTo("Prison.Spawn.Overflow");
					sender.sendMessage(prefix + "Overflow spawn reset to default spawn point!");
					return true;
				}
				
				// /pc delspawn <spawn>
				if(Config.isSet("Prison.Spawns." + arg2))
				{
					Config.set("Prison.Spawns." + arg2, null);
					sender.sendMessage(prefix + "Spawn " + arg2 + " has been deleted!");
				}
				else sender.sendMessage(prefix + "Spawn " + arg2 + " does not exist!");
				return true;
			}
			else
			{
				sender.sendMessage(prefix + "Too many arguments!");
				return true;
			}
		}
		
		/*								*
		 * 	Handles /pc spawn [name]		*
		 *								*/
		else if(player != null && arg1.equals("spawn"))
		{
			if(!(Utils.hasPermission(player, "prisoncontrol.command.spawn"))) return true;
			
			if(arg2.equals(""))
			{
				sender.sendMessage(prefix + "Not enough arguments!");
				return true;
			}
			else if(arg3.equals(""))
			{
				// /pc spawn overflow
				if(arg2.equalsIgnoreCase("overflow"))
				{
					player.teleport(PrisonSpawn.toLocation(Config.getString("Prison.Spawn.Overflow")));
					sender.sendMessage(prefix + "Teleported to Overflow spawn!");
					return true;
				}
				
				// /pc spawn <spawn>
				if(Config.isSet("Prison.Spawns." + arg2))
				{
					player.teleport(PrisonSpawn.toLocation(Config.getString("Prison.Spawns." + arg2 + ".Spawn Location")));
					player.sendMessage(prefix + "Teleported to Spawn " + arg2 + "!");
				}
				else sender.sendMessage(prefix + "Spawn " + arg2 + " does not exist!");
				return true;
			}
			else
			{
				sender.sendMessage(prefix + "Too many arguments!");
				return true;
			}
		}
		
		else if(arg1.equals("addowner"))
		{
			if(!(Utils.hasPermission(sender, "prisoncontrol.command.addowner"))) return true;
			
			if(arg2.equals("") || arg3.equals(""))
			{
				sender.sendMessage(prefix + "Not enough arguments!");
				return true;
			}
			else if(arg4.equals(""))
			{
				if(Players.isSet(arg2) || Bukkit.getServer().getPlayer(arg2) != null)
				{
					// /pc addowner <player> overflow
					if(arg3.equalsIgnoreCase("overflow"))
					{
						Utils.addOwner(arg2, "Overflow");
						sender.sendMessage(prefix + "Successfully set " + arg2 + "'s spawn to the Overflow spawn!");
						return true;
					}
					
					// /pc addowner <player> <spawn>
					if(Config.isSet("Prison.Spawns." + arg3))
					{
						Utils.addOwner(arg2, arg3);
						sender.sendMessage(prefix + "Successfully set " + arg2 + "'s spawn to the Spawn " + arg3 + "!");
					}
					else sender.sendMessage(prefix + "Spawn " + arg3 + " does not exist!");
					return true;	
				}
				sender.sendMessage(prefix + "That player does not exist in the players.yml!");
				return true;
			}
			else
			{
				sender.sendMessage(prefix + "Too many arguments!");
				return true;
			}
		}
		
		else if(player != null && arg1.equals("region"))
		{
			if(arg2.equals("") || arg3.equals(""))
			{
				player.sendMessage(prefix + "Not enough arguments!");
				return true;
			}
			
			// /pc region <regionname> <p1|p2>
			else if(arg4.equals(""))
			{
				if(!(Regions.isSet("Regions." + arg2)))
				{
					Regions.createSection("Regions." + arg2 + ".P1", "");
					Regions.createSection("Regions." + arg2 + ".P2", "");
					Regions.createSection("Regions." + arg2 + ".Place.Allowed", new ArrayList<String>());
					Regions.createSection("Regions." + arg2 + ".Place.Blocked", new ArrayList<String>());
					Regions.createSection("Regions." + arg2 + ".Break.Allowed", new ArrayList<String>());
					Regions.createSection("Regions." + arg2 + ".Break.Blocked", new ArrayList<String>());
				}
				
				if(arg3.equals("p1")) Regions.set("Regions." + arg2 + ".P1", loc.toString());
				else if(arg3.equals("p2")) Regions.set("Regions." + arg2 + ".P2", loc.toString());
				else
				{
					player.sendMessage(prefix + "Unknown corner name! The corner must be labelled P1 or P2!");
					return true;
				}
				player.sendMessage(prefix + "Corner for region " + arg2 + " is set!");
				return true;
			}
			else
			{
				player.sendMessage(prefix + "Too many arguments!");
				return true;
			}
		}
		
		else if(player == null) sender.sendMessage(prefix + "You must be a player to execute that command!");
		else player.sendMessage(prefix + "Unknown PrisonControl Command!");
		return true;
	}
}
