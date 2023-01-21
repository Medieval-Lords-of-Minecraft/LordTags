package me.ShanaChans.LordTags.Commands;

import org.bukkit.command.CommandSender;

import me.ShanaChans.LordTags.Tag;
import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.bukkit.commands.CommandArguments;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.bukkit.commands.SubcommandRunner;
import net.md_5.bungee.api.ChatColor;

public class LordTagsPost implements Subcommand
{
	private static final CommandArguments args = new CommandArguments();
	@Override
	public String getDescription() 
	{
		return "Complete tag creation";
	}

	@Override
	public String getKey() 
	{
		return "post";
	}

	@Override
	public String getPermission() 
	{
		return "lordtags.admin";
	}
	
	@Override
	public ChatColor getColor() {
		return ChatColor.DARK_RED;
	}

	@Override
	public SubcommandRunner getRunner() 
	{
		return SubcommandRunner.BOTH;
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
		
		Tag tag = TagManager.getTagCreation().get(author);
		
		if(!tag.isFilled())
		{
			sender.sendMessage("§7You must fill all parts of a Tag!");
			return;
		}
		
		if(TagManager.tagExists(tag.getId()))
		{
			sender.sendMessage("§7Tag with this Id already exists!");
			return;
		}
		
		tag.post(sender, tag);
		TagManager.getTagCreation().remove(author);
	}
	
	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
