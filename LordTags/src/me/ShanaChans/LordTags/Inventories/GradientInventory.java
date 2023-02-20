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
import de.tr7zw.nbtapi.NBTItem;
import me.ShanaChans.LordTags.TagManager;
import me.neoblade298.neocore.bukkit.inventories.CoreInventory;
import me.neoblade298.neocore.shared.util.Gradient;
import me.neoblade298.neocore.shared.util.GradientManager;

public class GradientInventory extends CoreInventory
{
	private final int MENU_MODEL = 5000;
	private final static int GRADIENT = 5001;
	private final int NEXT = 5002;
	private final int PREV = 5003;
	private final int CURRENT_ICON = 5004;
	private int page;
	private static ItemStack[] icons;
	
	static {
		icons = new ItemStack[GradientManager.getGradients().size()];
		int i = 0;
		for (Gradient gradient : GradientManager.getGradients()) {
			icons[i++] = setupIcon(gradient);
		}
	}
	
	public GradientInventory(Player p) 
	{
		super(p, Bukkit.createInventory(p, 54, "§4Gradient Selection"));
		page = 1;
		
		invSetup();
	}
	
	private static ItemStack setupIcon(Gradient g) {
		ItemStack item = new ItemStack(Material.NAME_TAG);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(g.apply(g.getId()));
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(g.apply("Click to apply this gradient to your name!"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		meta.setCustomModelData(GRADIENT);
		NBTItem nbti = new NBTItem(item);
		nbti.setString("gradient", g.getId());
		return nbti.getItem();
	}
	
	private static ItemStack setupCurrentIcon(Gradient g) {
		ItemStack item = new ItemStack(Material.NAME_TAG);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(g.apply(g.getId() + " (Selected)"));
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(g.apply("This gradient is currently selected!"));
		meta.setLore(lore);
		meta.addEnchant(Enchantment.LUCK, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setCustomModelData(GRADIENT);
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setString("gradient", g.getId());
		return nbti.getItem();
	}
	
	public void invSetup()
	{
		inv.clear();
		Gradient curr = TagManager.getNameGradient(p);
		String currDisplay = curr != null ? curr.apply(curr.getId()) : "§7None";
		
		ItemStack[] contents = inv.getContents();
		ItemStack border = createGuiItem(Material.BLACK_STAINED_GLASS_PANE, MENU_MODEL, " ", " ");
		ItemStack currIcon = createGuiItem(Material.NAME_TAG, CURRENT_ICON, "§7Current Gradient: " + currDisplay, "§7Click to remove current gradient!");
		ItemStack next = createGuiItem(Material.GREEN_STAINED_GLASS_PANE, NEXT, "§6Next", "§7Page " + (page + 1));
		ItemStack prev = createGuiItem(Material.RED_STAINED_GLASS_PANE, PREV, "§6Previous", "§7Page " + (page - 1));
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
		contents[49] = currIcon;
		contents[50] = border;
		contents[51] = border;
		contents[52] = border;
		
		
		if(icons.length > 28 * page)
		{
			contents[53] = next;
		}
		else
		{
			contents[53] = border;
		}
		
		if(icons.length != 0)
		{
			int i = 0;
			int j = 0;
			for(int k = 0 + 28 * (page - 1); k < icons.length; k++)
			{
				
				if(i >= 4)
				{
					continue;
				}
				
				ItemStack item = icons[k];
				NBTItem nbti = new NBTItem(item);
				if (curr != null && nbti.getString("gradient").equals(curr.getId())) {
					contents[(10 + 9*i) + j] = setupCurrentIcon(curr);
				}
				else {
					contents[(10 + 9*i) + j] = icons[k];
				}
				
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
		else if(isPrev(e.getCurrentItem()))
		{
			page--;
			invSetup();
		}
		else if(isGradient(e.getCurrentItem()))
		{
			NBTItem nbti = new NBTItem(e.getCurrentItem());
			String id = nbti.getString("gradient");
			if (e.getCurrentItem().containsEnchantment(Enchantment.LUCK)) {
				TagManager.removeNameGradient((Player) e.getWhoClicked());
			}
			else if (TagManager.tagExists(id)) {
				TagManager.setNameGradient((Player) e.getWhoClicked(), GradientManager.get(id));
			}
			invSetup();
		}
		else if(isCurrentIcon(e.getCurrentItem()))
		{
			TagManager.removeNameGradient((Player) e.getWhoClicked());
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
				(item.getItemMeta().getCustomModelData() == MENU_MODEL || item.getItemMeta().getCustomModelData() == GRADIENT);
	}
	
	public boolean isNext(ItemStack item) {
		return item != null && item.hasItemMeta() && item.getItemMeta().hasCustomModelData() &&
				item.getItemMeta().getCustomModelData() == NEXT;
	}
	
	public boolean isPrev(ItemStack item) {
		return item != null && item.hasItemMeta() && item.getItemMeta().hasCustomModelData() &&
				item.getItemMeta().getCustomModelData() == PREV;
	}
	
	public boolean isGradient(ItemStack item) {
		return item != null && item.hasItemMeta() && item.getItemMeta().hasCustomModelData() &&
				item.getItemMeta().getCustomModelData() == GRADIENT;
	}
	
	public boolean isCurrentIcon(ItemStack item) {
		return item != null && item.hasItemMeta() && item.getItemMeta().hasCustomModelData() &&
				item.getItemMeta().getCustomModelData() == CURRENT_ICON;
	}

}
