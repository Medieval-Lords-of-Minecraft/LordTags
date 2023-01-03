package me.ShanaChans.LordTags.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;

public class LordTagsCommand implements Subcommand
{
	private static final CommandArguments args = new CommandArguments();
	@Override
	public String getDescription() {
		return "See your tags!";
	}

	@Override
	public String getKey() {
		return "";
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public SubcommandRunner getRunner() {
		return SubcommandRunner.PLAYER_ONLY;
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		Player p = (Player) sender;
		TagManager.openTags(p);
	}
	
	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
