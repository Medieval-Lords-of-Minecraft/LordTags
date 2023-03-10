package me.ShanaChans.LordTags.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import me.ShanaChans.LordTags.Tag;
import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.Arg;

import me.neoblade298.neocore.shared.commands.SubcommandRunner;

public class CmdTagsAutopost extends Subcommand
{
	public CmdTagsAutopost(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("player"));
	}

	@Override
	public String getDescription() 
	{
		return "Automatically finds a usable id and gives the player the tag";
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
		
		// Enter loop to find an unused number, limit to 10
		String autoId = tag.getId();
		for (int iter = 1; iter < 10; iter++) {
			// Auto id tag does not exist
			if (!TagManager.tagExists(autoId)) {
				tag.setId(autoId);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + args[0] + " permission set lordtags.tag." + tag.getId());
				Bukkit.getLogger().info("[LordTags] Auto-creator found tag id " + autoId + ", gave player " + args[0] + " tag.");
				tag.post(sender, tag);
				TagManager.getTagCreation().remove(author);
				return;
			}
			
			// Auto id tag does exist and is the same as an existing one
			else {
				Tag comp = TagManager.getTag(autoId);
				if (tag.equals(comp)) {
					// Tag exists and is the exact same, give player permission and end
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + args[0] + " permission set lordtags.tag." + tag.getId());
					Bukkit.getLogger().info("[LordTags] Auto-creator found existing copy tag " + autoId + ", gave player " + args[0] + " tag.");
					TagManager.getTagCreation().remove(author);
					return;
				}
			}
			
			autoId = tag.getId() + (iter);
		}
		Bukkit.getLogger().warning("[LordTags] Auto-creator failed to create id with tag " + tag.getId());
	}
}
