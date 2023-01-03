package me.ShanaChans.LordTags.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import me.ShanaChans.LordTags.Tag;
import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import net.md_5.bungee.api.ChatColor;

public class LordTagsAutopost implements Subcommand
{
	private static final CommandArguments args = new CommandArguments(new CommandArgument("player"));
	@Override
	public String getDescription() 
	{
		return "Automatically finds a usable id and gives the player the tag";
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
		
		// Enter loop to find an unused number
		int iter = 1;
		String autoId = tag.getId();
		while (true) {
			// Auto id tag does not exist
			if (!TagManager.tagExists(autoId)) {
				tag.setId(autoId);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + args[0] + " permission set lordtags.tag." + tag.getId());
				Bukkit.getLogger().info("[LordTags] Auto-creator found tag id " + autoId + ", gave player " + args[0] + " tag.");
				TagManager.createTag(sender, tag);
				return;
			}
			
			// Auto id tag does exist and is the same as an existing one
			else {
				Tag comp = TagManager.getTag(autoId);
				if (tag.equals(comp)) {
					// Tag exists and is the exact same, give player permission and end
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + args[0] + " permission set lordtags.tag." + tag.getId());
					Bukkit.getLogger().info("[LordTags] Auto-creator found existing copy tag " + autoId + ", gave player " + args[0] + " tag.");
					return;
				}
			}
			
			autoId = tag.getId() + (iter++);
		}
	
		tag.post(sender, tag);
		TagManager.getTagCreation().remove(author);
	}
	
	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
