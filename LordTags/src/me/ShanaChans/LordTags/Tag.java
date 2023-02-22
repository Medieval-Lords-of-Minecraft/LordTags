package me.ShanaChans.LordTags;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import me.neoblade298.neocore.shared.util.GradientManager;
import me.neoblade298.neocore.shared.util.SharedUtil;

public class Tag implements Comparable<Tag>
{
	private String id, display, desc, sqlId, sqlDisplay, sqlDesc, gradient;
	
	public Tag(String id)
	{
		this.id = id.toLowerCase();
		this.sqlId = this.id.replaceAll("'", "''");
	}
	
	// No need for sql-parsed stuff because this won't ever go to sql again
	public Tag(String id, String display, String desc, String gradient)
	{
		this.id = id.toLowerCase();
		this.display = gradient != null ? GradientManager.applyGradient(gradient, display) : display;
		this.desc = desc;
		this.gradient = gradient; // Sometimes will be null, sometimes "", doesn't matter since it's never saved after posting
	}

	public String getId() {
		return id;
	}
	
	public String getSqlId() {
		return sqlId;
	}

	public void setId(String id) {
		this.id = id.toLowerCase();
		this.sqlId = this.id.replaceAll("'", "\\\\'");
	}

	public String getDisplay() {
		return display;
	}
	
	public String getSqlDisplay() {
		return sqlDisplay;
	}

	public void setDisplay(String display) {
		this.display = SharedUtil.translateColors(display);
		this.sqlDisplay = this.display.replaceAll("'", "\\\\'");
	}

	public String getDesc() {
		return desc;
	}
	
	public String getSqlDesc() {
		return sqlDesc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
		this.sqlDesc = this.desc.replaceAll("'", "\\\\'");
	}
	
	public void setGradient(String gradient) {
		this.gradient = gradient;
	}
	
	public String getGradient() { 
		return this.gradient;
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
		Bukkit.getLogger().info("[LordTags] Created new tag with id " + tag.id);
	}
	
	public boolean equals(Tag tag) {
		return id.equals(tag.id) && display.equals(tag.display) && desc.equals(tag.desc);
	}

	@Override
	public int compareTo(Tag o) {
		return id.compareTo(o.id);
	}

}
