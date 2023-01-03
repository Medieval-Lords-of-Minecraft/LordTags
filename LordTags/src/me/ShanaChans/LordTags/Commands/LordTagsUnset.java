package me.ShanaChans.LordTags.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.util.Util;
import net.md_5.bungee.api.ChatColor;

public class LordTagsUnset implements Subcommand
{
	private static final CommandArguments args = new CommandArguments(new CommandArgument("player", false));
	@Override
	public String getDescription() {
		return "Unsets a tag for a player";
	}

	@Override
	public String getKey() {
		return "unset";
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
		
		Player p = args.length > 0 ? Bukkit.getPlayer(args[0]) : (Player) sender;
		if (p == null) {
			Util.msg(sender, "&cThat player isn't online right now!");
		}
		TagManager.removePlayerTag(p);
	}
	
	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
