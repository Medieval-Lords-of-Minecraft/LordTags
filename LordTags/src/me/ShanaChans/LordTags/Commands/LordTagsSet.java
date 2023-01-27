package me.ShanaChans.LordTags.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ShanaChans.LordTags.Tag;
import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.shared.commands.Arg;

import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neocore.bukkit.util.Util;

public class LordTagsSet extends Subcommand
{
	public LordTagsSet(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("player", false), new Arg("tag id"));
	}

	@Override
	public void run(CommandSender sender, String[] args) 
	{
		int offset = 0;
		Player p;
		if (args.length > 1) {
			offset = 1;
			p = Bukkit.getPlayer(args[0]);
		}
		else {
			p = (Player) sender;
		}
		
		Tag tag = TagManager.getTag(args[offset].toLowerCase());
		if (tag == null) {
			Util.msg(sender, "&cThat tag doesn't exist!");
			return;
		}
		
		if (p == null) {
			Util.msg(sender, "&cThat player isn't online right now!");
			return;
		}
		
		if (!p.hasPermission("lordtags.tag." + tag.getId())) {
			Util.msg(sender, "&cThat player doesn't have the permission to use that tag!");
			return;
		}
		TagManager.setPlayerTag(p, tag);
		Util.msg(sender, "&7Successfully set player's tag");
	}
}
