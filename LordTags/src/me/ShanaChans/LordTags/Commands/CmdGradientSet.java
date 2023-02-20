package me.ShanaChans.LordTags.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.shared.commands.Arg;

import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neocore.shared.util.Gradient;
import me.neoblade298.neocore.shared.util.GradientManager;
import me.neoblade298.neocore.bukkit.util.Util;

public class CmdGradientSet extends Subcommand
{
	public CmdGradientSet(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("player", false), new Arg("gradient id"));
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
		
		Gradient g = GradientManager.get(args[offset]);
		if (g == null) {
			Util.msg(sender, "&cThat gradient doesn't exist!");
			return;
		}
		
		if (p == null) {
			Util.msg(sender, "&cThat player isn't online right now!");
			return;
		}
		TagManager.setNameGradient(p, g);
		Util.msg(sender, "&7Successfully set player's gradient");
	}
}
