package me.ShanaChans.LordTags.Inventories;

import java.util.ArrayList;
import java.util.Arrays;

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
import org.bukkit.inventory.meta.SkullMeta;

import me.ShanaChans.LordTags.Tag;
import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.inventories.CoreInventory;

public class LordTagsInventory extends CoreInventory
{
	private final int MENU_MODEL = 5000;
	private final int TAG = 5001;
	private final int NEXT = 5002;
	private final int PREV = 5003;
	private final int HEAD = 5004;
	private int page;
	private ArrayList<String> ids;
	private int tagAmount;
	
	public LordTagsInventory(Player p, int tagAmount, ArrayList<String> ids) 
	{
		super(p, Bukkit.createInventory(p, 54, "§4Total Tags: " + tagAmount));
		this.ids = ids;
		this.tagAmount = tagAmount;
		page = 1;
		
		
		invSetup();
	}
	
	public void invSetup()
	{
		inv.clear();
		Tag curr = TagManager.getPlayerTag(p);
		String currDisplay = curr == null ? "§7None" : curr.getDisplay();
		
		ItemStack[] contents = inv.getContents();
		ItemStack border = createGuiItem(Material.BLACK_STAINED_GLASS_PANE, MENU_MODEL, " ", " ");
		ItemStack head = createGuiItem(Material.PLAYER_HEAD, HEAD, "§6Current Tag§7: " + currDisplay, "§7Click to remove current tag!");
		ItemStack next = createGuiItem(Material.GREEN_STAINED_GLASS_PANE, NEXT, "§6Next", "§7Page " + (page + 1));
		ItemStack prev = createGuiItem(Material.RED_STAINED_GLASS_PANE, PREV, "§6Previous", "§7Page " + (page - 1));
		SkullMeta playerHead = (SkullMeta) head.getItemMeta();
		playerHead.setOwningPlayer(p);
		head.setItemMeta(playerHead);
		//TagManager.getPlayers().get(p.getUniqueId()).getCurrentTag();
		
		contents[0] = border;
		contents[1] = border;
		contents[2] = border;
		contents[3] = border;
		contents[4] = border;
		contents[5] = border;
		contents[6] = border;
		contents[7] = border;
		contents[8] = border;
		
		contents[9] = border;
		contents[17] = border;
		contents[18] = border;
		contents[26] = border;
		contents[27] = border;
		contents[35] = border;
		contents[36] = border;
		contents[44] = border;
		
		
		if(page > 1)
		{
			contents[45] = prev;
		}
		else
		{
			contents[45] = border;
		}
		
		contents[46] = border;
		contents[47] = border;
		contents[48] = border;
		contents[49] = head;
		contents[50] = border;
		contents[51] = border;
		contents[52] = border;
		
		
		if(tagAmount > 28 * page)
		{
			contents[53] = next;
		}
		else
		{
			contents[53] = border;
		}
		
		if(!ids.isEmpty())
		{
			int i = 0;
			int j = 0;
			for(int k = 0 + 28 * (page - 1); k < ids.size(); k++)
			{
				
				if(i >= 4)
				{
					continue;
				}
				
				Tag tag = TagManager.getTag(ids.get(k));
				ItemStack item = new ItemStack(Material.NAME_TAG, 1);
				ItemMeta meta = item.getItemMeta();
				ArrayList<String> lore = new ArrayList<String>();
				lore.add("§6Display§7: §f" + tag.getDisplay());
				lore.add("§7" + tag.getDesc());
				//System.out.println(TagManager.getPlayers().get(p.getUniqueId()).getCurrentTag());
				//.out.println(tag.getTagId());
				boolean selected = curr != null && curr.getId().equals(tag.getId());
				if(selected)
				{
					meta.addEnchant(Enchantment.LUCK, 1, true);
					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				}
				meta.setLore(lore);
				meta.setDisplayName("§cID: " + tag.getId() + (selected ? "§9 (Selected)" : ""));
				meta.setCustomModelData(TAG);
				item.setItemMeta(meta);
				
				contents[(10 + 9*i) + j] = item;
				
				
				if(j >= 6)
				{
					i++;
					j = 0;
				}
				else
				{
					j++;
				}
				
			}
		}
		inv.setContents(contents);
	}
	

	protected ItemStack createGuiItem(final Material material, final int modelID, final String name, final String... lore) {
		final ItemStack item = new ItemStack(material, 1);
		final ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList(lore));
		meta.setDisplayName(name);
		meta.setCustomModelData(modelID);
		item.setItemMeta(meta);
		return item;
	}
	
	public void handleInventoryClick(final InventoryClickEvent e) {
		e.setCancelled(true);
		if(isNext(e.getCurrentItem()))
		{
			page++;
			invSetup();
		}
		if(isPrev(e.getCurrentItem()))
		{
			page--;
			invSetup();
		}
		if(isTag(e.getCurrentItem()))
		{
			String id = e.getCurrentItem().getItemMeta().getDisplayName().substring(6);
			TagManager.setPlayerTag((Player) e.getWhoClicked(), id);
			invSetup();
		}
		if(isHead(e.getCurrentItem()))
		{
			TagManager.removePlayerTag(p);
			invSetup();
		}
	}
	
	public void handleInventoryDrag(final InventoryDragEvent e) {
		e.setCancelled(true);
	}
	
	public void handleInventoryClose(final InventoryCloseEvent e) {
		
	}
	
	public boolean isUntouchable(ItemStack item) {
		return item != null && item.hasItemMeta() && item.getItemMeta().hasCustomModelData() &&
				(item.getItemMeta().getCustomModelData() == MENU_MODEL || item.getItemMeta().getCustomModelData() == TAG);
	}
	
	public boolean isNext(ItemStack item) {
		return item != null && item.hasItemMeta() && item.getItemMeta().hasCustomModelData() &&
				item.getItemMeta().getCustomModelData() == NEXT;
	}
	
	public boolean isPrev(ItemStack item) {
		return item != null && item.hasItemMeta() && item.getItemMeta().hasCustomModelData() &&
				item.getItemMeta().getCustomModelData() == PREV;
	}
	
	public boolean isTag(ItemStack item) {
		return item != null && item.hasItemMeta() && item.getItemMeta().hasCustomModelData() &&
				item.getItemMeta().getCustomModelData() == TAG;
	}
	
	public boolean isHead(ItemStack item) {
		return item != null && item.hasItemMeta() && item.getItemMeta().hasCustomModelData() &&
				item.getItemMeta().getCustomModelData() == HEAD;
	}

}
