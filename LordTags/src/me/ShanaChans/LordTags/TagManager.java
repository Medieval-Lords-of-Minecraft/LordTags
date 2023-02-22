package me.ShanaChans.LordTags;

import java.awt.Color;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.ShanaChans.LordTags.Commands.*;
import me.ShanaChans.LordTags.Inventories.ChatColorInventory;
import me.ShanaChans.LordTags.Inventories.NameColorInventory;
import me.ShanaChans.LordTags.Listeners.LuckPermsListener;
import me.neoblade298.neocore.bukkit.NeoCore;
import me.neoblade298.neocore.bukkit.bungee.BungeeAPI;
import me.neoblade298.neocore.bukkit.bungee.PluginMessageEvent;
import me.neoblade298.neocore.bukkit.commands.SubcommandManager;
import me.neoblade298.neocore.bukkit.io.IOType;
import me.neoblade298.neocore.bukkit.player.PlayerFields;
import me.neoblade298.neocore.bukkit.util.Util;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neocore.shared.exceptions.NeoIOException;
import me.neoblade298.neocore.shared.util.Gradient;
import me.neoblade298.neocore.shared.util.GradientManager;
import net.md_5.bungee.api.ChatColor;

public class TagManager extends JavaPlugin implements Listener {
	private static TagManager inst;
	private static HashMap<String, Tag> tags = new HashMap<String, Tag>();
	private static HashMap<UUID, TagAccount> accounts = new HashMap<UUID, TagAccount>();
	private static HashMap<String, ChatColor> nameColors = new HashMap<String, ChatColor>();
	private static HashMap<String, ChatColor> chatColors = new HashMap<String, ChatColor>();

	private static TreeSet<String> tagList = new TreeSet<String>();
	private static ArrayList<String> gradientList = new ArrayList<String>();
	
	
	private static HashMap<String, Tag> tagCreation = new HashMap<String, Tag>();

	private static PlayerFields pfields;

	public void onEnable() {
		inst = this;
		Bukkit.getServer().getLogger().info("LordTags Enabled");
		Bukkit.getPluginManager().registerEvents(this, this);
		initCommands();
		pfields = NeoCore.createPlayerFields("lordtags", this, true);
		pfields.initializeField("tag", "");
		pfields.initializeField("nickname", "");
		pfields.initializeField("namegradient", "");
		pfields.initializeField("namecolor", "");
		pfields.initializeField("chatcolor", "");

		Bukkit.getPluginManager().registerEvents(new LuckPermsListener(), this);
		try (Connection con = NeoCore.getConnection("TagManager");
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM lordtags_tags;");) {
			while (rs.next()) {
				Tag tag = new Tag(rs.getString("id"), rs.getString("display"), rs.getString("desc"), rs.getString("gradient"));
				tags.put(tag.getId(), tag);
				tagList.add(tag.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for (Gradient g : GradientManager.getGradients()) {
			gradientList.add(g.getId());
		}
		Collections.sort(gradientList);
		
		load();
		
		// Make this plugin safe to reload (must reload neoplaceholders too)
		for (Player p : Bukkit.getOnlinePlayers()) {
			accounts.put(p.getUniqueId(), new TagAccount(p));
		}
	}
	
	public static void load() {
		try {
			NeoCore.loadFiles(new File("/home/MLMC/Resources/shared/LordTags/config.yml"), (yml, file) -> {
				ConfigurationSection namecolors = yml.getConfigurationSection("namecolors");
				for (String key : namecolors.getKeys(false)) {
					nameColors.put(key, ChatColor.of(Color.decode(namecolors.getString(key))));
				}
				NameColorInventory.initialize(nameColors.keySet());

				ConfigurationSection chatcolors = yml.getConfigurationSection("chatcolors");
				for (String key : chatcolors.getKeys(false)) {
					chatColors.put(key, ChatColor.of(Color.decode(chatcolors.getString(key))));
				}
				ChatColorInventory.initialize(chatColors.keySet());
				
				TagAccount.load(yml.getStringList("namecolordefaults"), yml.getStringList("chatcolordefaults"));
			});
		} catch (NeoIOException e) {
			e.printStackTrace();
		}
	}
	
	public static ChatColor getChatColor(String key) {
		return chatColors.get(key);
	}
	
	public static ChatColor getNameColor(String key) {
		return nameColors.get(key);
	}

	public void onDisable() {
		org.bukkit.Bukkit.getServer().getLogger().info("LordTags Disabled");
		super.onDisable();
	}

	private void initCommands() {
		SubcommandManager tags = new SubcommandManager("tags", "mycommand.staff", ChatColor.DARK_RED, this);
		tags.register(new CmdTagsAutopost("autopost", "Automatically finds a usable id and gives the player the tag",
				null, SubcommandRunner.BOTH));
		tags.register(new CmdTagsCreate("create", "Start tag creation", null, SubcommandRunner.BOTH));
		tags.register(new CmdTagsDesc("desc", "Set tag description", null, SubcommandRunner.BOTH));
		tags.register(new CmdTagsDisplay("display", "Set tag display", null, SubcommandRunner.BOTH));
		tags.register(new CmdTagsDisplay("gradient", "Set tag gradient", null, SubcommandRunner.BOTH));
		tags.register(new CmdTagsId("id", "Set tag id", null, SubcommandRunner.BOTH));
		tags.register(new CmdTagsPost("post", "Complete tag creation", null, SubcommandRunner.BOTH));
		tags.register(new CmdTagsExit("exit", "Exit tag creation", null, SubcommandRunner.BOTH));
		tags.register(new CmdTagsView("view", "View existing tag creation", null, SubcommandRunner.BOTH));
		tags.register(new CmdTags("", "Show all available tags", "lordtags.open", SubcommandRunner.BOTH));
		tags.register(new CmdTagsSet("set", "Start", null, SubcommandRunner.BOTH));
		tags.register(new CmdTagsUnset("unset", "Start", null, SubcommandRunner.BOTH));
		tags.registerCommandList("help");

		SubcommandManager nick = new SubcommandManager("nick", "lordtags.nick", null, this);
		nick.register(new CmdNick("", "Sets your nickname", null, SubcommandRunner.BOTH));

		SubcommandManager gradient = new SubcommandManager("gradients", "lordtags.gradient", null, this);
		gradient.register(new CmdGradient("", "Opens the gradient picker", null, SubcommandRunner.PLAYER_ONLY));
		gradient.register(
				new CmdGradientSet("set", "Sets a player's gradient", "lordtags.admin", SubcommandRunner.BOTH));
		gradient.register(
				new CmdGradientUnset("unset", "Unsets a player's gradient", "lordtags.admin", SubcommandRunner.BOTH));
		gradient.registerCommandList("help");

		SubcommandManager namecolor = new SubcommandManager("namecolor", "lordtags.namecolor", null, this);
		namecolor.register(new CmdNameColor("", "Opens the color picker", null, SubcommandRunner.PLAYER_ONLY));
		namecolor
				.register(new CmdNameColorSet("set", "Sets a player's name color", "lordtags.admin", SubcommandRunner.BOTH));
		namecolor.register(
				new CmdNameColorUnset("unset", "Unsets a player's name color", "lordtags.admin", SubcommandRunner.BOTH));
		
		SubcommandManager chatcolor = new SubcommandManager("chatcolor", "lordtags.chatcolor", null, this);
		chatcolor.register(new CmdChatColor("", "Opens the color picker", null, SubcommandRunner.PLAYER_ONLY));
		chatcolor
				.register(new CmdChatColorSet("set", "Sets a player's chat color", "lordtags.admin", SubcommandRunner.BOTH));
		chatcolor.register(
				new CmdChatColorUnset("unset", "Unsets a player's chat color", "lordtags.admin", SubcommandRunner.BOTH));
		
		SubcommandManager lordtags = new SubcommandManager("lordtags", "lordtags.admin", null, this);
		lordtags.register(new CmdLordTagsReload("reload", "Reloads chat and name colors only", null, SubcommandRunner.BOTH));
	}
	
	public static TagAccount getAccount(UUID uuid) {
		return accounts.get(uuid);
	}

	public static TreeSet<String> getTagList() {
		return tagList;
	}
	
	public static ArrayList<String> getGradientList() {
		return gradientList;
	}

	public static void clearCaches(UUID uuid) {
		accounts.get(uuid).clearCaches();
	}
	
	public static void recalculateDefaults(UUID uuid) {
		accounts.get(uuid).calculateDefaults(false);
	}

	public static TagManager inst() {
		return inst;
	}

	public static Tag getTag(String id) {
		return tags.get(id);
	}

	public static boolean tagExists(String id) {
		return tags.containsKey(id);
	}

	// Only called by command to avoid endless pluginmsg loop
	public static void createTag(CommandSender s, Tag tag) {
		tags.put(tag.getId(), tag);
		tagList.add(tag.getId());
		String gradientSql = tag.getGradient() != null ? "'" + tag.getGradient() + "'" : "null";
		BungeeAPI.sendPluginMessage("lordtags_newtag", new String[] { tag.getId(), tag.getDisplay(), tag.getDesc(), tag.getGradient() != null ? tag.getGradient() : "" });
		try (Connection con = NeoCore.getConnection("TagManager"); Statement stmt = con.createStatement();) {
			stmt.executeUpdate("INSERT INTO lordtags_tags Values('" + tag.getSqlId() + "','" + tag.getSqlDisplay()
					+ "','" + tag.getSqlDesc() + "'," + gradientSql + ");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Util.msg(s, "&7Successfully created tag " + tag.getId());
	}

	// Only called by command to avoid endless pluginmsg loop
	public static void removeTag(CommandSender s, String id) {
		tags.remove(id);
		tagList.remove(id);

		BungeeAPI.sendPluginMessage("lordtags_removetag", new String[] { id });
		Util.msg(s, "&7Successfully removed tag " + id);
		try (Connection con = NeoCore.getConnection("TagManager"); Statement stmt = con.createStatement();) {
			stmt.executeUpdate("DELETE FROM lordtags_tags WHERE id = '" + id + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static HashMap<String, Tag> getTagCreation() {
		return tagCreation;
	}

	private static void removePlayersWithTag(String id) {
		for (TagAccount acct : accounts.values()) {
			if (acct.getTag().getId().equals(id)) {
				acct.setTag(null);
			}
		}
	}

	@EventHandler
	public void onPluginMessage(PluginMessageEvent e) {
		if (e.getChannel().equals("lordtags_newtag")) {
			ArrayList<String> msgs = e.getMessages();
			Tag tag = new Tag(msgs.get(0), msgs.get(1), msgs.get(2), msgs.get(3));
			tags.put(tag.getId(), tag);
			tagList.add(tag.getId());
			Bukkit.getLogger().info("[LordTags] Received plugin message to add tag " + tag.getId());
		}
		else if (e.getChannel().equals("lordtags_removetag")) {
			tags.remove(e.getMessages().get(0));
			tagList.remove(e.getMessages().get(0));
			removePlayersWithTag(e.getMessages().get(0));
			Bukkit.getLogger().info("[LordTags] Received plugin message to remove tag " + e.getMessages().get(0));
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		BukkitRunnable runnable = new BukkitRunnable() {
			public void run() {
				accounts.put(p.getUniqueId(), new TagAccount(p));
			}
		};
		NeoCore.addPostIORunnable(runnable, IOType.LOAD, p.getUniqueId(), false);
	}

	public static PlayerFields getPlayerFields() {
		return pfields;
	}
}
