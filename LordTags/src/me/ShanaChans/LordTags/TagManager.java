package me.ShanaChans.LordTags;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.ShanaChans.LordTags.Commands.*;
import me.ShanaChans.LordTags.Inventories.LordTagsInventory;
import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.bungee.BungeeAPI;
import me.neoblade298.neocore.bungee.PluginMessageEvent;
import me.neoblade298.neocore.commands.CommandManager;
import me.neoblade298.neocore.io.IOComponent;
import me.neoblade298.neocore.util.Util;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;

public class TagManager extends JavaPlugin implements Listener, IOComponent {
	private static TagManager inst;
	private static HashMap<String, Tag> tags = new HashMap<String, Tag>();
	private static HashMap<UUID, Tag> playerTags = new HashMap<UUID, Tag>();
	private static ArrayList<String> tagList = new ArrayList<String>();
	private static HashMap<String, Tag> tagCreation = new HashMap<String, Tag>();
	private static LuckPerms api;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("LordTags Enabled");
		Bukkit.getPluginManager().registerEvents(this, this);
		initCommands();
		NeoCore.registerIOComponent(this, this, "TagManager");
		inst = this;
		
		try {
			ResultSet rs = NeoCore.getStatement("TagManager").executeQuery("SELECT * FROM lordtags_tags;");
			while (rs.next()) {
				Tag tag = new Tag(rs.getString("id"), rs.getString("display"), rs.getString("desc"));
				tags.put(tag.getId(), tag);
				tagList.add(tag.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        api = null;
        if (provider != null) {
            api = provider.getProvider();
        }
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("LordTags Disabled");
	    super.onDisable();
	}
	
	private void initCommands() {
		CommandManager tags = new CommandManager("tags", this);
		tags.register(new LordTagsAutopost());
		tags.register(new LordTagsCreate());
		tags.register(new LordTagsDesc());
		tags.register(new LordTagsDisplay());
		tags.register(new LordTagsId());
		tags.register(new LordTagsPost());
		tags.register(new LordTagsExit());
		tags.register(new LordTagsView());
		tags.register(new LordTagsCommand());
		tags.register(new LordTagsSet());
		tags.register(new LordTagsUnset());
		tags.registerCommandList("help");
		this.getCommand("tags").setExecutor(tags);
	}
	
	public static void openTags(Player p)
	{
        ArrayList<String> ids = new ArrayList<String>();
        int tagAmount = 0;
		if (p.hasPermission("lordtags.tag.*")) {
			ids = tagList;
			tagAmount = tagList.size();
		}
		else {
			UserManager mngr = api.getUserManager();
	        User user = mngr.getUser(p.getName());
	        ArrayList<Node> tagPerms = (ArrayList<Node>) user.getNodes().stream()
	                .filter(node -> node.getKey().startsWith("lordtags.tag."))
	                .filter(node -> TagManager.tagExists(node.getKey().substring(13).toLowerCase()))
	                .collect(Collectors.toList());
	        
	        for(Node node : tagPerms)
	        {
        		ids.add(node.getKey().substring(13).toLowerCase());
            	tagAmount++;
	        }
		}
        new LordTagsInventory(p, tagAmount, ids);
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
		try {
			NeoCore.getStatement("TagManager").executeUpdate("INSERT INTO lordtags_tags Values('" + tag.getSqlId() + "','"
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
		try {
			NeoCore.getStatement("TagManager").executeUpdate("DELETE FROM lordtags_tags WHERE id = '" + id + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Tag getPlayerTag(Player p)
	{
		return playerTags.get(p.getUniqueId());
	}
	
	public static void setPlayerTag(Player p, Tag tag) {
		playerTags.put(p.getUniqueId(), tag);
	}
	
	public static void setPlayerTag(Player p, String id) {
		setPlayerTag(p, tags.get(id));
	}
	
	public static void removePlayerTag(Player p) {
		playerTags.remove(p.getUniqueId());
	}

	public static HashMap<String, Tag> getTagCreation() {
		return tagCreation;
	}
	
	private static void removePlayersWithTag(String id) {
		for (Entry<UUID, Tag> e : playerTags.entrySet()) {
			if (e.getValue().getId().equalsIgnoreCase(id)) {
				playerTags.remove(e.getKey());
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
		new BukkitRunnable() {
			public void run() {
				try {
					UUID uuid = p.getUniqueId();
					ResultSet rs = stmt.executeQuery("SELECT * FROM lordtags_players WHERE uuid = '" + uuid + "';");
					if (rs.next()) {
						Tag tag = tags.get(rs.getString("tag"));
						if (tag != null) {
							playerTags.put(uuid, tag);
						}
						else {
							Bukkit.getLogger().warning("[LordTags] Failed to load tag " + rs.getString("tag") + " for player " + p.getName());
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.runTaskLaterAsynchronously(this, 20L);
	}

	@Override
	public void preloadPlayer(OfflinePlayer p, Statement stmt) {}

	@Override
	public void savePlayer(Player p, Statement insert, Statement delete) {
		try {
			UUID uuid = p.getUniqueId();
			if (playerTags.containsKey(uuid)) {
				Tag tag = playerTags.get(uuid);
				insert.executeUpdate("REPLACE INTO lordtags_players VALUES ('" + 
						uuid + "','" + tag.getSqlId() + "');");
			}
			else {
				delete.executeUpdate("DELETE FROM lordtags_players WHERE uuid = '" + uuid + "';");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
