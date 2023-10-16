package me.ShanaChans.LordTags.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ShanaChans.LordTags.Inventories.GradientInventory;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import net.kyori.adventure.text.format.NamedTextColor;

public class CmdGradient extends Subcommand
{
	public CmdGradient(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		this.color = NamedTextColor.RED;
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		new GradientInventory((Player) sender);
	}
}
