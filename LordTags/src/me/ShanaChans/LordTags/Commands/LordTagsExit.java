package me.ShanaChans.LordTags.Commands;

import org.bukkit.command.CommandSender;

import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.bukkit.commands.CommandArguments;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.bukkit.commands.SubcommandRunner;
import net.md_5.bungee.api.ChatColor;

public class LordTagsExit implements Subcommand
{
	private static final CommandArguments args = new CommandArguments();
	@Override
	public String getDescription() 
	{
		return "Exit out of tag creation";
	}

	@Override
	public String getKey() 
	{
		return "exit";
	}

	@Override
	public String getPermission() 
	{
		return "lordtags.admin";
	}

	@Override
	public SubcommandRunner getRunner() 
	{
		return SubcommandRunner.BOTH;
	}
	
	@Override
	public ChatColor getColor() {
		return ChatColor.DARK_RED;
	}

	@Override
	public void run(CommandSender sender, String[] args) 
	{
		String author = sender.getName();
		if(!TagManager.getTagCreation().containsKey(author))
		{
			sender.sendMessage("§7You are not creating a Tag!");
			return;
		}
		sender.sendMessage("§7You have canceled creating a Tag!");
		TagManager.getTagCreation().remove(author);
	}
	
	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
