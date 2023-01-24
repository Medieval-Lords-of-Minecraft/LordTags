package me.ShanaChans.LordTags.Commands;

import org.bukkit.command.CommandSender;

import me.ShanaChans.LordTags.Tag;
import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.bukkit.commands.SubcommandRunner;
import me.neoblade298.neocore.shared.util.SharedUtil;
import net.md_5.bungee.api.ChatColor;

public class LordTagsDesc implements Subcommand
{
	@Override
	public String getDescription() {
		return "Set tag description";
	}

	@Override
	public String getKey() {
		return "desc";
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
		if(args.length > 0)
		{
			String author = sender.getName();
			if(!TagManager.getTagCreation().containsKey(author))
			{
				sender.sendMessage("§7You are not creating a Tag!");
				return;
			}
			Tag tag = TagManager.getTagCreation().get(author);
			
			String desc = SharedUtil.connectArgs(args, 0);
			
			tag.setDesc(desc);
			tag.preview(sender);
		}
		
	}
	
	@Override
	public String getArgOverride() {
		return "[Description]";
	}
}
