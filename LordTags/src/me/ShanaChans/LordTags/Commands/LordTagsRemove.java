package me.ShanaChans.LordTags.Commands;

import org.bukkit.command.CommandSender;

import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;

public class LordTagsRemove implements Subcommand
{

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Remove tag";
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return "remove";
	}

	@Override
	public String getPermission() {
		// TODO Auto-generated method stub
		return "lordtags.admin";
	}

	@Override
	public SubcommandRunner getRunner() {
		// TODO Auto-generated method stub
		return SubcommandRunner.BOTH;
	}

	@Override
	public void run(CommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		
	}
	

}
