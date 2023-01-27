package me.ShanaChans.LordTags.Commands;

import org.bukkit.command.CommandSender;

import me.ShanaChans.LordTags.Tag;
import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;

public class LordTagsDisplay extends Subcommand
{
	public LordTagsDisplay(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.setOverride("[Display]");
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
		
		String display = args[0];
		for(int i = 1; i < args.length; i++)
		{
			display += " " + args[i];
		}
		
		tag.setDisplay(display);
		tag.preview(sender);
	}
}
