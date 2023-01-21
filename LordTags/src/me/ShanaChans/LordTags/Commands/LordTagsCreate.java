package me.ShanaChans.LordTags.Commands;

import org.bukkit.command.CommandSender;

import me.ShanaChans.LordTags.Tag;
import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.bukkit.commands.CommandArgument;
import me.neoblade298.neocore.bukkit.commands.CommandArguments;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.bukkit.commands.SubcommandRunner;
import net.md_5.bungee.api.ChatColor;

public class LordTagsCreate implements Subcommand
{
	private static final CommandArguments args = new CommandArguments(new CommandArgument("id"));
	@Override
	public String getDescription() 
	{
		return "Start tag creation";
	}

	@Override
	public String getKey() 
	{
		return "create";
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
		if(TagManager.getTagCreation().containsKey(author))
		{
			sender.sendMessage("§7You are already creating a Tag!");
			return;
		}
		Tag tag = new Tag(args[0]);
		TagManager.getTagCreation().put(author, tag);
		tag.preview(sender);
	}
	
	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
