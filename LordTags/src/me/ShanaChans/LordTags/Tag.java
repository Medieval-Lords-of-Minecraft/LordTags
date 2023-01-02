package me.ShanaChans.LordTags;

import org.bukkit.command.CommandSender;

import me.neoblade298.neocore.util.Util;

public class Tag 
{
	private String id;
	private String display;
	private String desc;
	
	public Tag(String id)
	{
		this.id = id;
	}
	
	public Tag(String id, String display, String desc)
	{
		this.id = id;
		this.display = display;
		this.desc = desc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = Util.translateColors(display);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public boolean isFilled()
	{
		return id != null && display != null && desc != null;
	}
	
	public void preview(CommandSender s) {
		s.sendMessage("§7-- §cTag Creation §7--");
		if (id == null) {
			s.sendMessage("§cID§7: Not set");
		}
		else {
			s.sendMessage("§cID§7: " + getId());
		}
		if (display == null) {
			s.sendMessage("§cDisplay§7: Not set");
		}
		else {
			s.sendMessage("§cDisplay§7: " + getDisplay());
		}
		if (desc == null) {
			s.sendMessage("§cDescription§7: Not set");
		}
		else {
			s.sendMessage("§cDescription§7: " + getDesc());
		}
	}
	
	public void post(CommandSender s, Tag tag)
	{
		TagManager.createTag(s, tag);
		s.sendMessage("§7Successfully created Tag!");
	}

}
