package me.ShanaChans.LordTags;

public class TagPlayer 
{
	private String currentTag;
	
	public TagPlayer(String currentTag)
	{
		if(currentTag == null)
		{
			currentTag = "";
		}
		this.currentTag = currentTag;
	}

	public String getCurrentTag() 
	{
		return currentTag;
	}

	public void setCurrentTag(String currentTag) 
	{
		this.currentTag = currentTag;
	}
	
}
