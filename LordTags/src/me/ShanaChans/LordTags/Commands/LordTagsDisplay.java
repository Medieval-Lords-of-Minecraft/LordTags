package me.ShanaChans.LordTags.Commands;

import org.bukkit.command.CommandSender;

import me.ShanaChans.LordTags.Tag;
import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import net.md_5.bungee.api.ChatColor;

public class LordTagsDisplay implements Subcommand
{
	@Override
	public String getDescription() {
		return "Set tag display";
	}

	@Override
	public String getKey() {
		return "display";
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
	
	@Override
	public String getArgOverride() {
		return "[Display]";
	}
}
