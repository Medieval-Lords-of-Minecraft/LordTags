package me.ShanaChans.LordTags;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.bukkit.player.PlayerFields;
import me.neoblade298.neocore.shared.util.Gradient;
import me.neoblade298.neocore.shared.util.GradientManager;
import net.md_5.bungee.api.ChatColor;

public class TagAccount {
	private static PlayerFields pfields = TagManager.getPlayerFields();

	private static ArrayList<StringPair> nameColorDefaults = new ArrayList<StringPair>();
	private static ArrayList<StringPair> chatColorDefaults = new ArrayList<StringPair>();

	private Player p;
	private UUID uuid;
	private String display, nickDisplay, nickname;
	private ChatColor chatColor, nameColor;
	private ChatColor defChatColor = ChatColor.WHITE, defNameColor = ChatColor.GRAY;
	private Gradient nameGradient;
	private Tag tag;
	private ArrayList<Tag> tagCache;
	private ArrayList<String> gradientCache;
	
	public static void load(List<String> ndefaults, List<String> cdefaults) {
		nameColorDefaults.clear();
		chatColorDefaults.clear();
		for (String line : ndefaults) {
			String[] split = line.split(":");
			nameColorDefaults.add(new StringPair(split[0], split[1]));
		}
		
		for (String line : cdefaults) {
			String[] split = line.split(":");
			chatColorDefaults.add(new StringPair(split[0], split[1]));
		}
	}
	
	public TagAccount(Player p) {
		this.p = p;
		this.uuid = p.getUniqueId();
		// Tag
		if (pfields.exists("tag", uuid)) {
			Tag tag = TagManager.getTag((String) pfields.getValue(uuid, "tag"));
			if (tag != null) {
				if (!p.hasPermission("lordtags.tag." + tag.getId())) {
					Bukkit.getLogger().info("[LordTags] Player " + p.getName() + " doesn't have permission for tag " + tag.getId());
					setTag(null);
					return;
				}
				this.tag = tag;
			}
			else {
				Bukkit.getLogger().warning("[LordTags] Failed to load tag " + pfields.getValue(uuid, "tag")
						+ " for player " + p.getName());
			}
		}

		// Gradient
		if (pfields.exists("namegradient", uuid)) {
			Gradient gradient = GradientManager.get((String) (pfields.getValue(uuid, "namegradient")));
			if (gradient != null) {
				if (!p.hasPermission("lordtags.namegradient." + gradient.getId())) {
					Bukkit.getLogger().info("[LordTags] Player " + p.getName() + " doesn't have permission for gradient " + gradient.getId());
					setNameGradient(null);
					return;
				}
				this.nameGradient = gradient;
			}
			else {
				Bukkit.getLogger().warning("[LordTags] Failed to load gradient "
						+ pfields.getValue(uuid, "gradient") + " for player " + p.getName());
			}
		}
		else if (pfields.exists("namecolor", uuid)) {
			String id = (String) pfields.getValue(uuid, "namecolor");
			if (!p.hasPermission("lordtags.namecolor." + id)) {
				Bukkit.getLogger().info("[LordTags] Player " + p.getName() + " doesn't have permission for name color " + id);
				setNameColor(null, null);
				return;
			}
			nameColor = TagManager.getNameColor(id);
		}
		
		if (pfields.exists("chatcolor", uuid)) {
			String id = (String) pfields.getValue(uuid, "chatcolor");
			if (!p.hasPermission("lordtags.chatcolor." + id)) {
				Bukkit.getLogger().info("[LordTags] Player " + p.getName() + " doesn't have permission for chat color " + id);
				setChatColor(null, null);
				return;
			}
			chatColor = TagManager.getChatColor((String) pfields.getValue(uuid, "chatcolor"));
		}

		if (pfields.exists("nick", uuid)) {
			if (!p.hasPermission("lordtags.nick")) {
				Bukkit.getLogger().info("[LordTags] Player " + p.getName() + " doesn't have permission to use a nickname!");
				setNickname(null);
				return;
			}
			this.nickname = (String) pfields.getValue(uuid, "nick");
		}
		calculateDefaults(true);
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
		return chatColor != null ? chatColor : defChatColor;
	}

	public void setChatColor(String id, ChatColor chatColor) {
		this.chatColor = chatColor;
		if (this.chatColor == null) {
			pfields.resetField("chatcolor", uuid);
		}
		else {
			pfields.changeField("chatcolor", id, uuid);
		}
	}

	public ChatColor getNameColor() {
		return nameColor != null ? nameColor : defNameColor;
	}

	public void setNameColor(String id, ChatColor nameColor) {
		this.nameColor = nameColor;
		this.nameGradient = null;
		if (this.nameColor == null) {
			pfields.resetField("namecolor", uuid);
		}
		else {
			pfields.changeField("namecolor", id, uuid);
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
			pfields.changeField("namegradient", nameGradient.getId(), uuid);
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
	
	public ArrayList<String> getGradientCache() {
		if (gradientCache != null) return gradientCache;
		
		gradientCache = new ArrayList<String>();
		for (String gradient : TagManager.getGradientList()) {
			if (p.hasPermission("lordtags.namegradients." + gradient)) {
				gradientCache.add(gradient);
			}
		}
		return gradientCache;
	}
	
	public void clearCaches() {
		tagCache = null;
		gradientCache = null;
	}
	
	public void calculateDefaults(boolean initial) {
		boolean changed = false;
		for (StringPair pair : nameColorDefaults) {
			if (p.hasPermission(pair.getKey())) {
				ChatColor newNameColor = TagManager.getNameColor(pair.getValue());
				if (defNameColor.equals(newNameColor)) {
					changed = true;
					defNameColor = newNameColor;
				}
			}
		}
		for (StringPair pair : chatColorDefaults) {
			if (p.hasPermission(pair.getKey())) {
				ChatColor newChatColor = TagManager.getNameColor(pair.getValue());
				if (defChatColor.equals(newChatColor)) {
					changed = true;
					defChatColor = newChatColor;
				}
			}
		}
		
		// Reset everything if a new default was made available
		if (changed && !initial) {
			setNameGradient(null);
			setNameColor(null, null);
			setChatColor(null, null);
		}
	}

	private void calculateDisplay() {
		if (nameGradient != null) {
			display = nameGradient.apply(p.getName());
			nickDisplay = nameGradient.apply(nickname != null ? "*" + nickname : p.getName());
		}
		else if (nameColor != null) {
			display = nameColor + p.getName();
			nickDisplay = nameColor + (nickname != null ? "*" + nickname : p.getName());
		}
		else {
			display = defNameColor + p.getName();
			nickDisplay = defNameColor + (nickname != null ? nickname : p.getName());
		}
	}
}
