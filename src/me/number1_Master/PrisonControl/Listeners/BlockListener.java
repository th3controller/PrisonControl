package me.number1_Master.PrisonControl.Listeners;

import java.util.*;

import me.number1_Master.PrisonControl.Config.*;
import me.number1_Master.PrisonControl.Utils.*;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.*;

public class BlockListener implements Listener
{
	private String prefix = Utils.getPrefix(true);
	
	@EventHandler
	public void onSignChange(SignChangeEvent e)
	{
		Player player = e.getPlayer();
		String line1 = e.getLine(0);
		
		if(line1.startsWith("[Cell] "))
		{			
			if(!(Utils.hasPermission(player, "prisoncontrol.sign.create")))
			{
				e.setCancelled(true);
				return;
			}
			
			line1 = line1.replace("[Cell] ", "");
			
			if(Config.isSet("Prison.Spawns." + line1))
			{
				new PrisonSpawn(e.getBlock().getLocation()).saveTo("Prison.Spawns." + line1 + ".Sign Location");
				e.setLine(0, ChatColor.RED + "[Cell " + line1 + "]");
				player.sendMessage(prefix + "Cell successfully linked!");
				return;
			}
			e.setCancelled(true);
			player.sendMessage(prefix + "That cell does not exist!");
			return;
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e)
	{
		Player player = e.getPlayer();
		String blockId = e.getBlock().getTypeId() + "";
		Location loc = e.getBlock().getLocation();
		
		if(e.getBlock().getType() == Material.SIGN_POST || e.getBlock().getType() == Material.WALL_SIGN)
		{
			Sign sign = (Sign) e.getBlock().getState();
			String line1 = ChatColor.stripColor(sign.getLine(0));
			if(line1.startsWith("[Cell "))
			{
				if(!(Utils.hasPermission(player, "prisoncontrol.sign.break")))
				{
					e.setCancelled(true);
					return;
				}
				line1 = line1.replace("[Cell ", "");
				line1 = line1.replace("]", "");
				Config.set("Prison.Spawns." + line1 + ".Sign Location", "");
			}
		}
		
		if(!(Regions.isSet("Regions"))) return;
		Set<String> regions = Regions.getConfigSection("Regions").getKeys(false);
		for(String region : regions)
		{
			Location p1 = PrisonSpawn.toLocation(Regions.getString("Regions." + region + ".P1"));
			Location p2 = PrisonSpawn.toLocation(Regions.getString("Regions." + region + ".P2"));
			
			double maxX = (p1.getX() > p2.getX()) ? p1.getX() : p2.getX();
			double minX = (p1.getX() < p2.getX()) ? p1.getX() : p2.getX();;
			if(loc.getX() <= maxX && loc.getX() >= minX)
			{
				double maxY = (p1.getY() > p2.getY()) ? p1.getY() : p2.getY();
				double minY = (p1.getY() < p2.getY()) ? p1.getY() : p2.getY();;
				if(loc.getY() <= maxY && loc.getY() >= minY)
				{
					double maxZ = (p1.getZ() > p2.getZ()) ? p1.getZ() : p2.getZ();
					double minZ = (p1.getZ() < p2.getZ()) ? p1.getZ() : p2.getZ();;
					if(loc.getZ() <= maxZ && loc.getZ() >= minZ)
					{
						List<String> allowed = Regions.getStringList("Regions." + region + ".Break.Allowed");
						List<String> blocked = Regions.getStringList("Regions." + region + ".Break.Blocked");
						
						boolean cancel;
						if(allowed.isEmpty() && blocked.isEmpty()) cancel = true;
						else if(allowed.isEmpty() && !(blocked.isEmpty()))
						{
							if(blocked.contains(blockId)) cancel = true;
							else cancel = false;
						}
						else if(!(allowed.isEmpty()) && blocked.isEmpty())
						{
							if(allowed.contains(blockId)) cancel = false;
							else cancel = true;
						}
						else
						{
							if(allowed.contains(blockId)) cancel = false;
							else cancel = true;
						}
						if(cancel)
						{
							if(Utils.hasPermission(player, "prisoncontrol." + region + ".break") || player.isOp()) continue;
							e.setCancelled(true);
						}
						return;
					}
					continue;
				}
				continue;
			}
			continue;
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e)
	{
		Player player = e.getPlayer();
		String blockId = e.getBlock().getTypeId() + "";
		Location loc = e.getBlock().getLocation();
		
		if(!(Regions.isSet("Regions"))) return;
		Set<String> regions = Regions.getConfigSection("Regions").getKeys(false);
		for(String region : regions)
		{
			Location p1 = PrisonSpawn.toLocation(Regions.getString("Regions." + region + ".P1"));
			Location p2 = PrisonSpawn.toLocation(Regions.getString("Regions." + region + ".P2"));
			
			double maxX = (p1.getX() > p2.getX()) ? p1.getX() : p2.getX();
			double minX = (p1.getX() < p2.getX()) ? p1.getX() : p2.getX();;
			if(loc.getX() <= maxX && loc.getX() >= minX)
			{
				double maxY = (p1.getY() > p2.getY()) ? p1.getY() : p2.getY();
				double minY = (p1.getY() < p2.getY()) ? p1.getY() : p2.getY();;
				if(loc.getY() <= maxY && loc.getY() >= minY)
				{
					double maxZ = (p1.getZ() > p2.getZ()) ? p1.getZ() : p2.getZ();
					double minZ = (p1.getZ() < p2.getZ()) ? p1.getZ() : p2.getZ();;
					if(loc.getZ() <= maxZ && loc.getZ() >= minZ)
					{
						List<String> allowed = Regions.getStringList("Regions." + region + ".Place.Allowed");
						List<String> blocked = Regions.getStringList("Regions." + region + ".Place.Blocked");
						
						boolean cancel;
						if(allowed.isEmpty() && blocked.isEmpty())cancel = true;
						else if(allowed.isEmpty() && !(blocked.isEmpty()))
						{
							if(blocked.contains(blockId)) cancel = true;
							else cancel = false;
						}
						else if(!(allowed.isEmpty()) && blocked.isEmpty())
						{
							if(allowed.contains(blockId)) cancel = false;
							else cancel = true;
						}
						else
						{
							if(allowed.contains(blockId)) cancel = false;
							else cancel = true;
						}
						if(cancel)
						{
							if(Utils.hasPermission(player, "prisoncontrol." + region + ".place") || player.isOp()) continue;
							e.setCancelled(true);
						}
						return;
					}
					continue;
				}
				continue;
			}
			continue;
		}
	}
}
