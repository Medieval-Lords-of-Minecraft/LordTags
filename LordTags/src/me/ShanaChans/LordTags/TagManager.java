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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.ShanaChans.LordTags.Commands.LordTagsCreate;
import me.ShanaChans.LordTags.Commands.LordTagsDesc;
import me.ShanaChans.LordTags.Commands.LordTagsDisplay;
import me.ShanaChans.LordTags.Commands.LordTagsPost;
import me.ShanaChans.LordTags.Commands.LordTagsCancel;
import me.ShanaChans.LordTags.Commands.LordTagsCommand;
import me.ShanaChans.LordTags.Commands.LordTagsRemove;
import me.ShanaChans.LordTags.Inventories.LordTagsInventory;
import me.neoblade298.neocore.commands.CommandManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;

public class TagManager extends JavaPlugin implements Listener{
	private static TagManager inst;
	private static HashMap<String, Tag> tags = new HashMap<String, Tag>();
	private static HashMap<UUID, Tag> playerTags = new HashMap<UUID, Tag>();
	private static HashMap<String, Tag> tagCreation = new HashMap<String, Tag>();
	private static LuckPerms api;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("LordTags Enabled");
		Bukkit.getPluginManager().registerEvents(this, this);
		initCommands();
		tags.put("Stitch", new Tag("Stitch", "§9Stitch" , "Donated for this tag!"));
		tags.put("Test1", new Tag("Test1", "Test1" , "Test1"));
		tags.put("Test2", new Tag("Test2", "Test1" , "Test1"));
		tags.put("Test3", new Tag("Test3", "Test1" , "Test1"));
		tags.put("Test4", new Tag("Test4", "Test1" , "Test1"));
		tags.put("Test5", new Tag("Test5", "Test1" , "Test1"));
		tags.put("Test6", new Tag("Test6", "Test1" , "Test1"));
		tags.put("Test7", new Tag("Test7", "Test1" , "Test1"));
		tags.put("Test8", new Tag("Test8", "Test1" , "Test1"));
		tags.put("Test9", new Tag("Test9", "Test1" , "Test1"));
		tags.put("Test10", new Tag("Test10", "Test1" , "Test1"));
		tags.put("Test11", new Tag("Test11", "Test1" , "Test1"));
		tags.put("Test12", new Tag("Test12", "Test1" , "Test1"));
		tags.put("Test13", new Tag("Test13", "Test1" , "Test1"));
		tags.put("Test14", new Tag("Test14", "Test1" , "Test1"));
		tags.put("Test15", new Tag("Test15", "Test1" , "Test1"));
		tags.put("Test16", new Tag("Test16", "Test1" , "Test1"));
		tags.put("Test17", new Tag("Test17", "Test1" , "Test1"));
		tags.put("Test18", new Tag("Test18", "Test1" , "Test1"));
		tags.put("Test19", new Tag("Test19", "Test1" , "Test1"));
		tags.put("Test20", new Tag("Test20", "Test1" , "Test1"));
		tags.put("Test21", new Tag("Test21", "Test1" , "Test1"));
		tags.put("Test22", new Tag("Test22", "Test1" , "Test1"));
		tags.put("Test23", new Tag("Test23", "Test1" , "Test1"));
		tags.put("Test24", new Tag("Test24", "Test1" , "Test1"));
		tags.put("Test25", new Tag("Test25", "Test1" , "Test1"));
		tags.put("Test26", new Tag("Test26", "Test1" , "Test1"));
		tags.put("Test27", new Tag("Test27", "Test1" , "Test1"));
		tags.put("Test28", new Tag("Test28", "Test1" , "Test1"));
		tags.put("Test29", new Tag("Test29", "Test1" , "Test1"));
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
        	if(tags.containsKey(node.getKey().substring(13)))
        	{
        		ids.add(node.getKey().substring(13));
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
	
	public static void createTag(Tag tag) {
		tags.put(tag.getTagId(), tag);
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
	
	@EventHandler
    public void join(PlayerJoinEvent e) 
    {
		// Load player's tag
    }
}
