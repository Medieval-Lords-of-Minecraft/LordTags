package me.ShanaChans.LordTags.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ShanaChans.LordTags.Tag;
import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.util.Util;
import net.md_5.bungee.api.ChatColor;

public class LordTagsSet implements Subcommand
{
	private static final CommandArguments args = new CommandArguments(new CommandArgument("player", false), new CommandArgument("tag id"));
	@Override
	public String getDescription() {
		return "Set the tag for a player";
	}

	@Override
	public String getKey() {
		return "set";
	}

	@Override
	public String getPermission() {
		return "lordtags.admin";
	}
	
	@Override
	public ChatColor getColor() {
		return ChatColor.DARK_RED;
	}

	@Override
	public SubcommandRunner getRunner() {
		return SubcommandRunner.BOTH;
	}

	@Override
	public void run(CommandSender sender, String[] args) 
	{
		int offset = 0;
		Player p;
		if (args.length > 1) {
			offset = 1;
			p = Bukkit.getPlayer(args[0]);
		}
		else {
			p = (Player) sender;
		}
		
		Tag tag = TagManager.getTag(args[offset].toLowerCase());
		if (tag == null) {
			Util.msg(sender, "&cThat tag doesn't exist!");
			return;
		}
		
		if (p == null) {
			Util.msg(sender, "&cThat player isn't online right now!");
			return;
		}
		
		if (!p.hasPermission("lordtags.tag." + tag.getId())) {
			Util.msg(sender, "&cThat player doesn't have the permission to use that tag!");
			return;
		}
		TagManager.setPlayerTag(p, tag);
		Util.msg(sender, "&7Successfully set player's tag");
	}
	
	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
