package me.ShanaChans.LordTags.Commands;

import org.bukkit.command.CommandSender;

import me.ShanaChans.LordTags.Tag;
import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;

public class LordTagsPost implements Subcommand
{

	@Override
	public String getDescription() 
	{
		// TODO Auto-generated method stub
		return "Create a tag";
	}

	@Override
	public String getKey() 
	{
		// TODO Auto-generated method stub
		return "post";
	}

	@Override
	public String getPermission() 
	{
		// TODO Auto-generated method stub
		return "lordtags.admin";
	}

	@Override
	public SubcommandRunner getRunner() 
	{
		// TODO Auto-generated method stub
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
		
		if(TagManager.getTags().containsKey(tag.getTagId()))
		{
			sender.sendMessage("§7Tag with this Id already exists!");
			return;
		}
		
		tag.post(sender,tag.getTagId(), tag);
		TagManager.getTagCreation().remove(author);
	}
}
