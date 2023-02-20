package me.ShanaChans.LordTags.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ShanaChans.LordTags.Inventories.ColorInventory;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;

public class CmdNamecolor extends Subcommand
{
	public CmdNamecolor(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		new ColorInventory((Player) sender);
	}
}
