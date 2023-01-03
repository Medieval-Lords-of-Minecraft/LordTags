package me.ShanaChans.LordTags.Commands;

import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import me.ShanaChans.LordTags.Tag;
import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import net.md_5.bungee.api.ChatColor;

public class LordTagsConvert implements Subcommand
{
	private static final CommandArguments args = new CommandArguments();
	@Override
	public String getDescription() 
	{
		return "Convert old deluxetags over to new system";
	}

	@Override
	public String getKey() 
	{
		return "convert";
	}

	@Override
	public String getPermission() 
	{
		return "lordtags.admin";
	}
	
	@Override
	public ChatColor getColor() {
		return ChatColor.DARK_RED;
	}

	@Override
	public SubcommandRunner getRunner() 
	{
		return SubcommandRunner.BOTH;
	}

	@Override
	public void run(CommandSender sender, String[] args) 
	{
		try {
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(new File("/home/MLMC/DevServers/ServerDev/plugins/DeluxeTags/config.yml"));
			ConfigurationSection sec = cfg.getConfigurationSection("deluxetags");
			HashSet<String> tags = new HashSet<String>();
			for (String tag : sec.getKeys(false)) {
				tags.add(tag);
			}
			Statement stmt = NeoCore.getStatement("TagManager");
			
			for (String tag : tags) {
				Tag tagObj;
				if (tag.endsWith("2") && !tags.contains(tag.substring(0, tag.length() - 1))) {
					System.out.println("Redundant tag converted: " + tag);
					tagObj = new Tag(tag.substring(0, tag.length() - 1), sec.getString(tag + ".tag"), sec.getString(tag + ".description")); // Remove redundant tag
				}
				else {
					tagObj = new Tag(tag, sec.getString(tag + ".tag"), sec.getString(tag + ".description"));
				}
				String display = tagObj.getDisplay();
				display = display.substring(0, display.length() - 1); // Remove space at end
				tagObj.setDisplay(display);
				tagObj.setDesc(tagObj.getDesc().substring(2)); // Remove first color code
			}
			stmt.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
