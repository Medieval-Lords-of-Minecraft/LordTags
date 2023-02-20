package me.ShanaChans.LordTags.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import net.md_5.bungee.api.ChatColor;

public class CmdTags extends Subcommand
{
	public CmdTags(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		this.color = ChatColor.RED;
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		Player p = (Player) sender;
		TagManager.openTags(p);
	}
}
