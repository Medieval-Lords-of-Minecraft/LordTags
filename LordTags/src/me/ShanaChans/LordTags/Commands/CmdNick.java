package me.ShanaChans.LordTags.Commands;

import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.shared.commands.Arg;
import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.bukkit.commands.Subcommand;
import me.neoblade298.neocore.bukkit.util.Util;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;

public class CmdNick extends Subcommand
{
	private static final Pattern NICK_REGEX = Pattern.compile("[\\w]{3,16}");
	public CmdNick(String key, String desc, String perm, SubcommandRunner runner) {
		super(key, desc, perm, runner);
		args.add(new Arg("nickname/clear"), new Arg("player", false));
	}

	@Override
	public void run(CommandSender s, String[] args) 
	{
		Player p = args.length > 1 ? Bukkit.getPlayer(args[0]) : (Player) s;
		if (args.length > 1 && !s.hasPermission("mycommand.staff")) {
			Util.msg(s, "&cYou don't have the permission to change other users' nicknames!");
			return;
		}
		if (p == null) {
			Util.msg(s, "&cThat player isn't online!");
			return;
		}
		if (!NICK_REGEX.matcher(args[0]).matches()) {
			Util.msg(s, "&cYour nickname must only include characters allowed in minecraft usernames and be at most 15 characters!");
			return;
		}
		
		if (args[0].equalsIgnoreCase("clear")) {
			TagManager.getAccount(p.getUniqueId()).setNickname(null);
			Util.msg(s, "&7Successfully cleared nickname!");
		}
		else {
			TagManager.getAccount(p.getUniqueId()).setNickname(args[0]);
			Util.msg(s, "&7Successfully set nickname!");
		}
	}
}
