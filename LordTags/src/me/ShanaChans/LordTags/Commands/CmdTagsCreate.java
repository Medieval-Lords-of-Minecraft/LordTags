package me.ShanaChans.LordTags.Commands;

import org.bukkit.command.CommandSender;

import me.ShanaChans.LordTags.Tag;
import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.shared.commands.Arg;

import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neocore.shared.util.SharedUtil;

public class CmdTagsCreate extends Subcommand
{
	public CmdTagsCreate(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("id"));
		args.setMax(-1);
	}

	@Override
	public void run(CommandSender sender, String[] args) 
	{
		String author = sender.getName();
		if(TagManager.getTagCreation().containsKey(author))
		{
			sender.sendMessage("§7You are already creating a Tag!");
			return;
		}
		String id = SharedUtil.connectArgs(args).replaceAll(" ", "");
		Tag tag = new Tag(id);
		TagManager.getTagCreation().put(author, tag);
		tag.preview(sender);
	}
}
