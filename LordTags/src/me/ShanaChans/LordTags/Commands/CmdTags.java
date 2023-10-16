package me.ShanaChans.LordTags.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ShanaChans.LordTags.Inventories.LordTagsInventory;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import net.kyori.adventure.text.format.NamedTextColor;

public class CmdTags extends Subcommand
{
	public CmdTags(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		this.color = NamedTextColor.RED;
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		Player p = (Player) sender;
		new LordTagsInventory(p);
	}
}
