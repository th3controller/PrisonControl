package me.number1_Master.PrisonControl.Utils;

import me.number1_Master.PrisonControl.Config.*;

import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.command.*;

public class Utils
{
	public static ChatColor b = ChatColor.BLUE;
	public static ChatColor r = ChatColor.RED;
	
	private static String prefix = "[PrisonControl] ";
	public static String getPrefix(boolean colorize)
	{
		if(colorize) return b + prefix + r;
		else return prefix;
	}
	
	public static boolean hasPermission(CommandSender player, String permissionNode)
	{
		if(player.hasPermission(permissionNode)) return true;
		player.sendMessage(getPrefix(true) + "You do not have permission to do that!");
		return false;
	}
	
	public static void addOwner(String playerName, String index)
	{
		Players.set(playerName + ".Cell", index);
		
		if(index.equalsIgnoreCase("Overflow")) return;
		else index = "Prison.Spawns." + index;
		
		Location loc = PrisonSpawn.toLocation(Config.getString(index + ".Sign Location"));
		if(loc.getWorld().getBlockAt(loc).getType() == Material.SIGN_POST || 
				loc.getWorld().getBlockAt(loc).getType() == Material.WALL_SIGN)
		{
			Sign sign = (Sign) loc.getBlock().getState();
			if(sign.getLine(1).equals(playerName) || sign.getLine(2).equals(playerName) || sign.getLine(3).equals(playerName))
				return;
			if(sign.getLine(1).equals("")) sign.setLine(1, playerName);
			else if(sign.getLine(2).equals("")) sign.setLine(2, playerName);
			else if(sign.getLine(3).equals(""))
			{
				sign.setLine(3,playerName);
				Config.set(index + ".Used", true);
			}
			sign.update();
			return;
		}
	}
}
