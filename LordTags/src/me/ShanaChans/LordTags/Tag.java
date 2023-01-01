package me.ShanaChans.LordTags;

import org.bukkit.command.CommandSender;

import me.neoblade298.neocore.util.Util;

public class Tag 
{
	private String tagId;
	private String tagDisplay;
	private String tagDesc;
	
	public Tag(String tagId)
	{
		this.tagId = tagId;
	}
	
	public Tag(String tagId, String tagDisplay, String tagDesc)
	{
		this.tagId = tagId;
		this.tagDisplay = tagDisplay;
		this.tagDesc = tagDesc;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getTagDisplay() {
		return tagDisplay;
	}

	public void setTagDisplay(String tagDisplay) {
		this.tagDisplay = Util.translateColors(tagDisplay);
	}

	public String getTagDesc() {
		return tagDesc;
	}

	public void setTagDesc(String tagDesc) {
		this.tagDesc = tagDesc;
	}
	
	public boolean isFilled()
	{
		return tagId != null && tagDisplay != null && tagDesc != null;
	}
	
	public void preview(CommandSender s) {
		s.sendMessage("§7-- §cTag Creation §7--");
		if (tagId == null) {
			s.sendMessage("§cID§7: Not set");
		}
		else {
			s.sendMessage("§cID§7: " + getTagId());
		}
		if (tagDisplay == null) {
			s.sendMessage("§cDisplay§7: Not set");
		}
		else {
			s.sendMessage("§cDisplay§7: " + getTagDisplay());
		}
		if (tagDesc == null) {
			s.sendMessage("§cDescription§7: Not set");
		}
		else {
			s.sendMessage("§cDescription§7: " + getTagDesc());
		}
	}
	
	public void post(CommandSender s, Tag tag)
	{
		TagManager.createTag(tag);
		s.sendMessage("§7Successfully created Tag!");
	}

}
