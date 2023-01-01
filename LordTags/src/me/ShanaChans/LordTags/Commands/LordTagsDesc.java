package me.ShanaChans.LordTags.Commands;

import org.bukkit.command.CommandSender;

import me.ShanaChans.LordTags.Tag;
import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;

public class LordTagsDesc implements Subcommand
{

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return "desc";
	}

	@Override
	public String getPermission() {
		// TODO Auto-generated method stub
		return "lordtags.admin";
	}

	@Override
	public SubcommandRunner getRunner() {
		// TODO Auto-generated method stub
		return SubcommandRunner.BOTH;
	}

	@Override
	public void run(CommandSender sender, String[] args) 
	{
		if(args.length > 0)
		{
			String author = sender.getName();
			if(!TagManager.getTagCreation().containsKey(author))
			{
				sender.sendMessage("§7You are not creating a Tag!");
				return;
			}
			Tag tag = TagManager.getTagCreation().get(author);
			
			String desc = args[0];
			for(int i = 1; i < args.length; i++)
			{
				desc += " " + args[i];
			}
			
			tag.setDesc(desc);
			tag.preview(sender);
		}
		
	}
	

}
