package me.ShanaChans.LordTags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.ShanaChans.LordTags.Commands.LordTagsCreate;
import me.ShanaChans.LordTags.Commands.LordTagsDesc;
import me.ShanaChans.LordTags.Commands.LordTagsDisplay;
import me.ShanaChans.LordTags.Commands.LordTagsPost;
import me.ShanaChans.LordTags.Commands.LordTagsCancel;
import me.ShanaChans.LordTags.Commands.LordTagsCommand;
import me.ShanaChans.LordTags.Commands.LordTagsRemove;
import me.ShanaChans.LordTags.Inventories.CustomInventory;
import me.ShanaChans.LordTags.Inventories.LordTagsInventory;
import me.neoblade298.neocore.commands.CommandManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;

public class TagManager extends JavaPlugin implements Listener{
	private static TagManager inst;
	private static HashMap<String, Tag> tags = new HashMap<String, Tag>();
	private static HashMap<UUID, TagPlayer> players = new HashMap<UUID, TagPlayer>();
	private static HashMap<String, Tag> tagCreation = new HashMap<String, Tag>();
	public static HashMap<Player, CustomInventory> viewingInventory = new HashMap<Player, CustomInventory>();
	private static LuckPerms api;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("LordTags Enabled");
		Bukkit.getPluginManager().registerEvents(this, this);
		initCommands();
		
		inst = this;
		
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
		tags.register(new LordTagsCreate());
		tags.register(new LordTagsDesc());
		tags.register(new LordTagsDisplay());
		tags.register(new LordTagsPost());
		tags.register(new LordTagsCancel());
		tags.register(new LordTagsRemove());
		tags.register(new LordTagsCommand());
		this.getCommand("tags").setExecutor(tags);
	}
	
	public static void openTags(Player p)
	{
		UserManager mngr = api.getUserManager();
        User user = mngr.getUser(p.getName());
        ArrayList<Node> tagPerms = (ArrayList<Node>) user.getNodes().stream()
                .filter(node -> node.getKey().startsWith("lordtags.tag."))
                .collect(Collectors.toList());
        
        int tagAmount = 0;
        ArrayList<String> ids = new ArrayList<String>();
        
        for(Node node : tagPerms)
        {
        	ids.add(node.getKey().substring(13));
        }
        
        new LordTagsInventory(p, tagAmount, ids);
	}
	
	public static TagManager inst() {
		return inst;
	}

	public static HashMap<UUID, TagPlayer> getPlayers() {
		return players;
	}

	public static HashMap<String, Tag> getTags() {
		return tags;
	}
	
	public static String getPlayerTag(Player p)
	{
		return players.get(p.getUniqueId()).getCurrentTag();
	}

	public static HashMap<String, Tag> getTagCreation() {
		return tagCreation;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (viewingInventory.containsKey(p)) {
			viewingInventory.get(p).handleInventoryClick(e);
		}
	}

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (viewingInventory.containsKey(p)) {
			viewingInventory.get(p).handleInventoryDrag(e);
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if (viewingInventory.containsKey(p) && e.getInventory() != null && e.getInventory() == viewingInventory.get(p).getInventory()) {
			viewingInventory.get(p).handleInventoryClose(e);
			viewingInventory.remove(p);
		}
	}
}
