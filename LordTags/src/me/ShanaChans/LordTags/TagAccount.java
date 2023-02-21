package me.ShanaChans.LordTags;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.bukkit.player.PlayerFields;
import me.neoblade298.neocore.shared.util.Gradient;
import me.neoblade298.neocore.shared.util.GradientManager;
import net.md_5.bungee.api.ChatColor;

public class TagAccount {
	private static PlayerFields pfields = TagManager.getPlayerFields();

	private Player p;
	private UUID uuid;
	private String display, nickDisplay, nickname;
	private ChatColor chatColor, nameColor;
	private Gradient nameGradient;
	private Tag tag;
	private ArrayList<Tag> tagCache;
	
	public TagAccount(Player p) {
		this.uuid = p.getUniqueId();
		if (pfields.exists("tag", uuid)) {
			Tag tag = TagManager.getTag((String) pfields.getValue(uuid, "tag"));
			if (tag != null) {
				this.tag = tag;
			}
			else {
				Bukkit.getLogger().warning("[LordTags] Failed to load tag " + pfields.getValue(uuid, "tag")
						+ " for player " + p.getName());
			}
		}

		if (pfields.exists("namegradient", uuid)) {
			Gradient gradient = GradientManager.get((String) (pfields.getValue(uuid, "namegradient")));
			if (gradient != null) {
				this.nameGradient = gradient;
			}
			else {
				Bukkit.getLogger().warning("[LordTags] Failed to load gradient "
						+ pfields.getValue(uuid, "gradient") + " for player " + p.getName());
			}
		}
		else if (pfields.exists("namecolor", uuid)) {
			nameColor = ChatColor.of((String) pfields.getValue(uuid, "namecolor"));
		}

		if (pfields.exists("nick", uuid)) {
			this.nickname = (String) pfields.getValue(uuid, "nick");
		}
		calculateDisplay();
	}

	public String getDisplay(boolean useNickname) {
		return useNickname ? nickDisplay : display;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
		if (this.nickname == null) {
			pfields.resetField("nickname", uuid);
		}
		else {
			pfields.changeField("nickname", nickname, uuid);
		}
		calculateDisplay();
	}

	public ChatColor getChatColor() {
		return chatColor;
	}

	public void setChatColor(ChatColor chatColor) {
		this.chatColor = chatColor;
		if (this.chatColor == null) {
			pfields.resetField("chatcolor", uuid);
		}
		else {
			pfields.changeField("chatcolor", chatColor.toString(), uuid);
		}
	}

	public ChatColor getNameColor() {
		return nameColor;
	}

	public void setNameColor(ChatColor nameColor) {
		this.nameColor = nameColor;
		this.nameGradient = null;
		if (this.nameColor == null) {
			pfields.resetField("namecolor", uuid);
		}
		else {
			pfields.changeField("namecolor", nameColor.toString(), uuid);
		}
		pfields.resetField("namegradient", uuid);
		calculateDisplay();
	}

	public Gradient getNameGradient() {
		return nameGradient;
	}

	public void setNameGradient(Gradient nameGradient) {
		this.nameGradient = nameGradient;
		this.nameColor = null;
		if (this.nameGradient == null) {
			pfields.resetField("namegradient", uuid);
		}
		else {
			pfields.changeField("namegradient", nameGradient.toString(), uuid);
		}
		pfields.resetField("namecolor", uuid);
		calculateDisplay();
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
		if (tag == null) {
			pfields.resetField("tag", uuid);
		}
		else {
			pfields.changeField("tag", tag.getId(), uuid);
		}
	}

	public ArrayList<Tag> getTagCache() {
		if (tagCache != null) return tagCache;

		tagCache = new ArrayList<Tag>();
		for (String tag : TagManager.getTagList()) {
			if (p.hasPermission("lordtags.tag." + tag)) {
				tagCache.add(TagManager.getTag(tag));
			}
		}
		return tagCache;
	}
	
	public void removeTagCache() {
		tagCache = null;
	}

	private void calculateDisplay() {
		if (nameColor != null) {
			display = nameColor + p.getName();
			nickDisplay = nameColor + (nickname != null ? nickname : p.getName());
		}
		else if (nameGradient != null) {
			display = nameGradient.apply(p.getName());
			nickDisplay = nameGradient.apply(nickname != null ? nickname : p.getName());
		}
		else {
			display = p.getName();
			nickDisplay = nickname != null ? nickname : p.getName();
		}
	}
}
