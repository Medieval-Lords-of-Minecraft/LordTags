package me.ShanaChans.LordTags.Commands;

import org.bukkit.command.CommandSender;

import me.ShanaChans.LordTags.Tag;
import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;

public class LordTagsCreate implements Subcommand
{

	@Override
	public String getDescription() 
	{
		return "Create a tag";
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
	public void run(CommandSender sender, String[] args) 
	{
		if(args.length > 0)
		{
			if(TagManager.tagExists(args[0]))
			{
				sender.sendMessage("§7Tag ID already exists!");
				return;
			}
			
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
	}
	

}
