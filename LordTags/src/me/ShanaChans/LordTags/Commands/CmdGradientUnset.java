package me.ShanaChans.LordTags.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.shared.commands.Arg;

import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import net.kyori.adventure.text.format.NamedTextColor;
import me.neoblade298.neocore.bukkit.util.Util;

public class CmdGradientUnset extends Subcommand
{
	public CmdGradientUnset(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		color = NamedTextColor.DARK_RED;
		args.add(new Arg("player", false));
	}

	@Override
	public void run(CommandSender sender, String[] args) 
	{
		
		Player p = args.length > 0 ? Bukkit.getPlayer(args[0]) : (Player) sender;
		if (p == null) {
			Util.msg(sender, "&cThat player isn't online right now!");
			return;
		}
		TagManager.getAccount(p.getUniqueId()).setNameGradient(null);
		Util.msg(sender, "&7Successfully unset player's gradient");
	}
}
