package me.ShanaChans.LordTags.Commands;

import org.bukkit.command.CommandSender;
import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.bukkit.util.Util;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import net.md_5.bungee.api.ChatColor;

public class CmdLordTagsReload extends Subcommand
{
	public CmdLordTagsReload(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		this.color = ChatColor.RED;
	}

	@Override
	public void run(CommandSender s, String[] args) {
		TagManager.load();
		Util.msg(s, "Successfully reloaded!");
	}
}
