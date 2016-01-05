package org.linphone.newphone.photo;

public class FolderBean{
	/***图片地址**/
	private String topImagePath;
	/**图片名*/
	private String folderName;
	/**图片数*/
	private int imageCounts;
	
	public String getTopImagePath()	{
		return topImagePath;
	}
	
	public void setTopImagePath(String topImagePath)	{
		this.topImagePath = topImagePath;
		
	}
	
	public String getFolderName()	{
		return folderName;
	}
	
	public void setFolderName(String folderName)	{
		this.folderName = folderName;
	}

	public int getImageCounts()	{
		return imageCounts;
	}
	
	
	public void setImageCounts(int imageCounts)	{
		this.imageCounts = imageCounts;		
	}

}
