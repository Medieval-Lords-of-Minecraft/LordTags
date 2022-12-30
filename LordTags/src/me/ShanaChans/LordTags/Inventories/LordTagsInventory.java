package me.ShanaChans.LordTags.Inventories;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ShanaChans.LordTags.Tag;
import me.ShanaChans.LordTags.TagManager;

public class LordTagsInventory extends CustomInventory
{
	private final int MENU_MODEL = 5000;
	private final int TAG = 5001;
	private Inventory inv;
	
	public LordTagsInventory(Player p, int tagAmount, ArrayList<String> ids) 
	{
		inv = Bukkit.createInventory(p, 54, "§4Total Tags: " + tagAmount);
		ItemStack[] contents = inv.getContents();
		//ItemStack border = createGuiItem(Material.BLACK_STAINED_GLASS_PANE, " ", " ");

		int i = 0;
		for(String id : ids)
		{
			if(TagManager.getTags().containsKey(id))
			{
				Tag tag = TagManager.getTags().get(id);
				contents[i] = createTagGuiItem(Material.NAME_TAG, "§6Display§7: §f" + tag.getTagDisplay(), "§7" + tag.getTagDesc());
				i++;
			}
		}
		inv.setContents(contents);

		setupInventory(p, inv, this);
	}

	protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
		final ItemStack item = new ItemStack(material, 1);
		final ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList(lore));
		meta.setDisplayName(name);
		meta.setCustomModelData(MENU_MODEL);
		item.setItemMeta(meta);
		return item;
	}
	
	protected ItemStack createTagGuiItem(final Material material, final String name, final String... lore) {
		final ItemStack item = new ItemStack(material, 1);
		final ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList(lore));
		meta.setDisplayName(name);
		meta.setCustomModelData(TAG);
		item.setItemMeta(meta);
		return item;
	}
	
	public void handleInventoryClick(final InventoryClickEvent e) {
		
	}
	
	public void handleInventoryDrag(final InventoryDragEvent e) {
		
	}
	
	public void handleInventoryClose(final InventoryCloseEvent e) {
		
	}
	
	public boolean isUntouchable(ItemStack item) {
		return item != null && item.hasItemMeta() && item.getItemMeta().hasCustomModelData() &&
				(item.getItemMeta().getCustomModelData() == MENU_MODEL || item.getItemMeta().getCustomModelData() == TAG);
	}

}
