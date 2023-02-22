package me.ShanaChans.LordTags.Listeners;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.ShanaChans.LordTags.TagManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;

public class LuckPermsListener implements Listener {
	private static HashSet<UUID> recentlyJoined = new HashSet<UUID>();
	public LuckPermsListener() {
		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		if (provider == null) return;
		
		JavaPlugin pl = TagManager.inst();
	    LuckPerms api = provider.getProvider();
        EventBus eventBus = api.getEventBus();

        eventBus.subscribe(pl, NodeAddEvent.class, e -> {
        	System.out.println("Key: " + e.getNode().getKey())
        	System.out.println("Type: " + e.getNode().getType());
        	if (!e.getNode().getKey().startsWith("lordtags.")) return;
        	if (!e.isUser()) return;
        	
    		UUID uuid = UUID.fromString(e.getTarget().getIdentifier().getName());
    		if (recentlyJoined.contains(uuid)) return;
    		
    		TagManager.clearCaches(uuid);
        });
        eventBus.subscribe(pl, NodeRemoveEvent.class, e -> {
        	if (!e.getNode().getKey().startsWith("lordtags.")) return;
        	if (!e.isUser()) return;
        	
    		UUID uuid = UUID.fromString(e.getTarget().getIdentifier().getName());
    		if (recentlyJoined.contains(uuid)) return;
    		
    		TagManager.clearCaches(uuid);
        });
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		recentlyJoined.add(e.getPlayer().getUniqueId());
		new BukkitRunnable() {
			public void run() {
				recentlyJoined.remove(e.getPlayer().getUniqueId());
			}
		}.runTaskLater(TagManager.inst(), 100L);
	}
}
