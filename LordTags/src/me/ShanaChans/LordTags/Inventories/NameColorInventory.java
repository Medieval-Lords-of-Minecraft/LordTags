package me.ShanaChans.LordTags.Inventories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ShanaChans.LordTags.TagAccount;
import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.bukkit.inventories.CoreInventory;
import me.neoblade298.neocore.bukkit.util.Util;
import net.md_5.bungee.api.ChatColor;

public class NameColorInventory extends CoreInventory
{
	private static ArrayList<String> colors;
	private TagAccount acct;
	public NameColorInventory(Player p) 
	{
		super(p, Bukkit.createInventory(p, 9, "§4Name Color Selection"));
		acct = TagManager.getAccount(p.getUniqueId());
		invSetup();
	}
	
	public static void initialize(Collection<String> keys) {
		colors = new ArrayList<String>();
		for (String key : keys) {
			colors.add(key);
		}
		Collections.sort(colors);
	}
	
	private ItemStack setupIcon(ChatColor c, String id) {
		ChatColor curr = acct.getNameColor();
		if (!p.hasPermission("lordtags.namecolor." + id)) return CoreInventory.createButton(Material.BARRIER, c + id + "&c(No Permission)");

		ItemStack item = CoreInventory.createButton(Material.NAME_TAG, c + id);
		if (curr != null && curr.equals(c)) {
			ItemMeta meta = item.getItemMeta();
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("§7This color is currently selected!");
			meta.setLore(lore);
			meta.addEnchant(Enchantment.LUCK, 1, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			item.setItemMeta(meta);
		}
		return item;
	}
	
	public void invSetup()
	{
		ItemStack[] contents = inv.getContents();
		int i = 0;
		for (String id : colors) {
			ChatColor c = TagManager.getNameColor(id);
			contents[i++] = setupIcon(c, id);
		}
		inv.setContents(contents);
	}

	@Override
	public void handleInventoryClick(final InventoryClickEvent e) {
		e.setCancelled(true);
		ItemStack item = e.getCurrentItem();
		if (item == null || item.getType() == Material.BARRIER) return;
		String id = colors.get(e.getRawSlot());
		ChatColor c = TagManager.getNameColor(id);
		acct.setNameColor(id, c);
		Util.msg(p, "&7Successfully set your name color to " + c + id);
		invSetup();
	}

	@Override
	public void handleInventoryDrag(final InventoryDragEvent e) {
		e.setCancelled(true);
	}
	
	@Override
	public void handleInventoryClose(final InventoryCloseEvent e) {
		
	}
}
