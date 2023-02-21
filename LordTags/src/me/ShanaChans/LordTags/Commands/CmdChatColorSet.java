package me.ShanaChans.LordTags.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.shared.commands.Arg;

import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import net.md_5.bungee.api.ChatColor;
import me.neoblade298.neocore.bukkit.util.Util;

public class CmdChatColorSet extends Subcommand
{
	public CmdChatColorSet(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		color = ChatColor.DARK_RED;
		args.add(new Arg("player", false), new Arg("color"));
	}

	@Override
	public void run(CommandSender sender, String[] args) 
	{
		int offset = 0;
		Player p = Bukkit.getPlayer(args[0]);
		if (args.length > 1) {
			offset = 1;
			p = Bukkit.getPlayer(args[0]);
		}
		else {
			p = (Player) sender;
		}
		
		ChatColor c = TagManager.getChatColor(args[offset]);
		if (c == null) {
			Util.msg(sender, "&cThat color doesn't exist!");
			return;
		}
		
		if (p == null) {
			Util.msg(sender, "&cThat player isn't online right now!");
			return;
		}
		TagManager.getAccount(p.getUniqueId()).setChatColor(args[offset], c);
		Util.msg(sender, "&7Successfully set player's chat color");
	}
}
