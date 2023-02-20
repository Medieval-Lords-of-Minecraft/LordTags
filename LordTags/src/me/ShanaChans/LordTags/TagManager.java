package me.ShanaChans.LordTags;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.ShanaChans.LordTags.Commands.*;
import me.ShanaChans.LordTags.Inventories.LordTagsInventory;
import me.ShanaChans.LordTags.Listeners.LuckPermsListener;
import me.neoblade298.neocore.bukkit.NeoCore;
import me.neoblade298.neocore.bukkit.bungee.BungeeAPI;
import me.neoblade298.neocore.bukkit.bungee.PluginMessageEvent;
import me.neoblade298.neocore.bukkit.commands.SubcommandManager;
import me.neoblade298.neocore.bukkit.io.IOComponent;
import me.neoblade298.neocore.bukkit.io.IOType;
import me.neoblade298.neocore.bukkit.player.PlayerFields;
import me.neoblade298.neocore.bukkit.util.Util;
import me.neoblade298.neocore.shared.commands.SubcommandRunner;
import me.neoblade298.neocore.shared.util.Gradient;
import me.neoblade298.neocore.shared.util.GradientManager;
import net.md_5.bungee.api.ChatColor;

public class TagManager extends JavaPlugin implements Listener, IOComponent {
	private static TagManager inst;
	private static HashMap<String, Tag> tags = new HashMap<String, Tag>();
	private static HashMap<UUID, Tag> playerTags = new HashMap<UUID, Tag>();
	private static HashMap<UUID, String> playerDisplays = new HashMap<UUID, String>();
	private static HashMap<UUID, String> playerNicks = new HashMap<UUID, String>();
	private static HashMap<UUID, Gradient> nameGradients = new HashMap<UUID, Gradient>();
	private static HashMap<UUID, ChatColor> nameColors = new HashMap<UUID, ChatColor>();
	private static ArrayList<String> tagList = new ArrayList<String>();
	private static HashMap<String, Tag> tagCreation = new HashMap<String, Tag>();
	private static HashMap<UUID, ArrayList<Tag>> playerTagCache = new HashMap<UUID, ArrayList<Tag>>();
	
	private static PlayerFields pfields;
	
	public void onEnable() {
		inst = this;
		Bukkit.getServer().getLogger().info("LordTags Enabled");
		Bukkit.getPluginManager().registerEvents(this, this);
		initCommands();
		NeoCore.registerIOComponent(this, this, "TagManager");
		pfields = NeoCore.createPlayerFields("lordtags", this, true);
		pfields.initializeField("tag", "");
		pfields.initializeField("nick", "");
		pfields.initializeField("namegradient", "");

		Bukkit.getPluginManager().registerEvents(new LuckPermsListener(), this);
		try (Connection con = NeoCore.getConnection("TagManager");
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM lordtags_tags;");){
			while (rs.next()) {
				Tag tag = new Tag(rs.getString("id"), rs.getString("display"), rs.getString("desc"));
				tags.put(tag.getId(), tag);
				tagList.add(tag.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("LordTags Disabled");
	    super.onDisable();
	}
	
	private void initCommands() {
		SubcommandManager tags = new SubcommandManager("tags", "mycommand.staff", ChatColor.DARK_RED, this);
		tags.register(new CmdTagsAutopost("autopost", "Automatically finds a usable id and gives the player the tag", null, SubcommandRunner.BOTH));
		tags.register(new CmdTagsCreate("create", "Start tag creation", null, SubcommandRunner.BOTH));
		tags.register(new CmdTagsDesc("desc", "Set tag description", null, SubcommandRunner.BOTH));
		tags.register(new CmdTagsDisplay("display", "Set tag display", null, SubcommandRunner.BOTH));
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
		gradient.register(new CmdGradientSet("set", "Sets a player's gradient", "lordtags.admin", SubcommandRunner.BOTH));
		gradient.register(new CmdGradientUnset("unset", "Unsets a player's gradient", "lordtags.admin", SubcommandRunner.BOTH));
		gradient.registerCommandList("help");
		
		SubcommandManager namecolors = new SubcommandManager("namecolors", "lordtags.namecolors", null, this);
		namecolors.register(new CmdNamecolor("", "Opens the color picker", null, SubcommandRunner.PLAYER_ONLY));
		namecolors.register(new CmdNamecolorSet("set", "Sets a player's color", "lordtags.admin", SubcommandRunner.BOTH));
		namecolors.register(new CmdNamecolorUnset("unset", "Unsets a player's color", "lordtags.admin", SubcommandRunner.BOTH));
	}
	
	public static void openTags(Player p)
	{
        new LordTagsInventory(p, getCachedTags(p));
	}
	
	public static ArrayList<Tag> getCachedTags(Player p) {
		UUID uuid = p.getUniqueId();
		if (playerTagCache.containsKey(p.getUniqueId())) return playerTagCache.get(uuid);
		
		ArrayList<Tag> cache = new ArrayList<Tag>();
		for (String tag : tagList) {
			if (p.hasPermission("lordtags.tag." + tag)) {
				cache.add(tags.get(tag));
			}
		}
		playerTagCache.put(p.getUniqueId(), cache);
		return cache;
	}
	
	public static void removeCache(UUID uuid) {
		playerTagCache.remove(uuid);
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
		BungeeAPI.sendPluginMessage("lordtags_newtag", new String[] {tag.getId(), tag.getDisplay(), tag.getDesc()});
		Util.msg(s, "&7Successfully created tag " + tag.getId());
		try (Connection con = NeoCore.getConnection("TagManager");
				Statement stmt = con.createStatement();){
			stmt.executeUpdate("INSERT INTO lordtags_tags Values('" + tag.getSqlId() + "','"
					+ tag.getSqlDisplay() + "','" + tag.getSqlDesc() + "');");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Only called by command to avoid endless pluginmsg loop
	public static void removeTag(CommandSender s, String id) {
		tags.remove(id);
		tagList.remove(id);
		
		BungeeAPI.sendPluginMessage("lordtags_removetag", new String[] {id});
		Util.msg(s, "&7Successfully removed tag " + id);
		try (Connection con = NeoCore.getConnection("TagManager");
				Statement stmt = con.createStatement();){
			stmt.executeUpdate("DELETE FROM lordtags_tags WHERE id = '" + id + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Tag getPlayerTag(Player p)
	{
		return playerTags.get(p.getUniqueId());
	}
	
	public static String getPlayerNick(Player p) {
		return playerNicks.get(p.getUniqueId());
	}
	
	public static void setPlayerTag(Player p, Tag tag) {
		playerTags.put(p.getUniqueId(), tag);
		pfields.changeField("tag", tag.getId(), p.getUniqueId());
	}
	
	public static void setPlayerTag(Player p, String id) {
		setPlayerTag(p, tags.get(id));
	}
	
	public static void removePlayerTag(Player p) {
		playerTags.remove(p.getUniqueId());
		pfields.resetField("tag", p.getUniqueId());
	}

	public static HashMap<String, Tag> getTagCreation() {
		return tagCreation;
	}
	
	private static void removePlayersWithTag(String id) {
		for (Entry<UUID, Tag> e : playerTags.entrySet()) {
			if (e.getValue().getId().equalsIgnoreCase(id)) {
				playerTags.remove(e.getKey());
				pfields.resetField("tag", e.getKey());
			}
		}
	}
	
	@EventHandler
	public void onPluginMessage(PluginMessageEvent e) {
		if (e.getChannel().equals("lordtags_newtag")) {
			ArrayList<String> msgs = e.getMessages();
			Tag tag = new Tag(msgs.get(0), msgs.get(1), msgs.get(2));
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

	@Override
	public void cleanup(Statement insert, Statement delete) {}

	@Override
	public void loadPlayer(Player p, Statement stmt) {
		playerDisplays.put(p.getUniqueId(), p.getName());
		BukkitRunnable runnable = new BukkitRunnable() {
			public void run() {
				UUID uuid = p.getUniqueId();
				if (pfields.exists("tag", uuid)) {
					Tag tag = tags.get(pfields.getValue(uuid, "tag"));
					if (tag != null) {
						playerTags.put(uuid, tag);
					}
					else {
						Bukkit.getLogger().warning("[LordTags] Failed to load tag " + pfields.getValue(uuid, "tag") + " for player " + p.getName());
					}
				}
				
				if (pfields.exists("namegradient", uuid)) {
					Gradient gradient = GradientManager.get((String) (pfields.getValue(uuid, "namegradient")));
					if (gradient != null) {
						nameGradients.put(uuid, gradient);
					}
					else {
						Bukkit.getLogger().warning("[LordTags] Failed to load gradient " + pfields.getValue(uuid, "gradient") + " for player " + p.getName());
					}
				}
				else if (pfields.exists("namecolor", uuid)) {
					nameColors.put(uuid, ChatColor.of((String) pfields.getValue(uuid, "namecolor")));
				}
				
				if (pfields.exists("nick", uuid)) {
					setPlayerNick(p, (String) pfields.getValue(uuid, "nick"));
				}
				calculatePlayerDisplay(p);
			}
		};
		NeoCore.addPostIORunnable(runnable, IOType.LOAD, p.getUniqueId(), false);
	}

	@Override
	public void preloadPlayer(OfflinePlayer p, Statement stmt) {}

	@Override
	public void savePlayer(Player p, Statement insert, Statement delete) {} // All saving handled by NeoCore
	
	public static void setPlayerNick(Player p, String nick) {
		UUID uuid = p.getUniqueId();
		playerNicks.put(uuid, nick);
		calculatePlayerDisplay(p);
	}
	
	public static void removePlayerNick(Player p) {
		playerNicks.remove(p.getUniqueId());
		calculatePlayerDisplay(p);
	}
	
	public static void setNameGradient(Player p, Gradient gradient) {
		nameGradients.put(p.getUniqueId(), gradient);
		calculatePlayerDisplay(p);
	}
	
	public static void calculatePlayerDisplay(Player p) {
		UUID uuid = p.getUniqueId();
		String name = playerNicks.getOrDefault(uuid, p.getName());
		if (nameGradients.containsKey(uuid)) {
			playerDisplays.put(uuid, nameGradients.get(uuid).apply(name));
		}
		else if (nameColors.containsKey(uuid)) {
			name = nameColors.get(uuid) + name;
		}
		playerDisplays.put(uuid, name);
	}
	
	public static void removeNameGradient(Player p) {
		nameGradients.remove(p.getUniqueId());
		calculatePlayerDisplay(p);
	}
	
	public static Gradient getNameGradient(Player p) {
		return nameGradients.get(p.getUniqueId());
	}
	
	public static void setNameColor(Player p, ChatColor color) {
		nameColors.put(p.getUniqueId(), color);
		calculatePlayerDisplay(p);
	}
	
	public static ChatColor getNameColor(Player p) {
		return nameColors.get(p.getUniqueId());
	}
	
	public static void removeNameColor(Player p) {
		nameColors.remove(p.getUniqueId());
		calculatePlayerDisplay(p);
	}
	
	public static String getPlayerDisplay(Player p) {
		return playerDisplays.getOrDefault(p.getUniqueId(), p.getName());
	}
}
