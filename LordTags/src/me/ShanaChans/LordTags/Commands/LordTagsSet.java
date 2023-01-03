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
	private static final CommandArguments args = new CommandArguments(new CommandArgument("tag id"), new CommandArgument("player", false));
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
		Tag tag = TagManager.getTag(args[0].toLowerCase());
		if (tag == null) {
			Util.msg(sender, "&cThat tag doesn't exist!");
		}
		
		Player p = args.length > 1 ? Bukkit.getPlayer(args[1]) : (Player) sender;
		if (p == null) {
			Util.msg(sender, "&cThat player isn't online right now!");
		}
		
		if (!p.hasPermission("lordtags.tag." + tag.getId())) {
			Util.msg(sender, "&cThat player doesn't have the permission to use that tag!");
		}
		TagManager.setPlayerTag(p, tag);
	}
	
	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
