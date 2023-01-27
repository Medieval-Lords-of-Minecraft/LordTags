package me.ShanaChans.LordTags.Commands;

import org.bukkit.command.CommandSender;

import me.ShanaChans.LordTags.Tag;
import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neocore.shared.util.SharedUtil;

public class LordTagsDesc extends Subcommand
{
	public LordTagsDesc(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.setOverride("[Description]");
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
}
