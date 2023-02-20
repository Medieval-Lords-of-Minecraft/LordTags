package me.ShanaChans.LordTags.Inventories;

import java.util.ArrayList;
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
import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.bukkit.inventories.CoreInventory;
import net.md_5.bungee.api.ChatColor;

public class ColorInventory extends CoreInventory
{
	
	public ColorInventory(Player p) 
	{
		super(p, Bukkit.createInventory(p, 9, "§4Color Selection"));
		invSetup();
	}
	
	private ItemStack setupIcon(ItemStack item, ChatColor c) {
		ChatColor curr = TagManager.getNameColor(p);
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
		contents[0] = setupIcon(CoreInventory.createButton(Material.CYAN_WOOL, "&bCyan"), ChatColor.AQUA);
		contents[2] = setupIcon(CoreInventory.createButton(Material.LIME_WOOL, "&aLime"), ChatColor.GREEN);
		contents[4] = setupIcon(CoreInventory.createButton(Material.BLUE_WOOL, "&9Blue"), ChatColor.BLUE);
		contents[6] = setupIcon(CoreInventory.createButton(Material.RED_WOOL, "&cRed"), ChatColor.RED);
		contents[8] = setupIcon(CoreInventory.createButton(Material.PURPLE_WOOL, ChatColor.LIGHT_PURPLE + "Purple"), ChatColor.LIGHT_PURPLE);
		inv.setContents(contents);
	}

	@Override
	public void handleInventoryClick(final InventoryClickEvent e) {
		e.setCancelled(true);
		switch (e.getRawSlot()) {
		case 0: TagManager.setNameColor(p, ChatColor.AQUA);
		break;
		case 2: TagManager.setNameColor(p, ChatColor.GREEN);
		break;
		case 4: TagManager.setNameColor(p, ChatColor.BLUE);
		break;
		case 6: TagManager.setNameColor(p, ChatColor.RED);
		break;
		case 8: TagManager.setNameColor(p, ChatColor.LIGHT_PURPLE);
		break;
		}
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
