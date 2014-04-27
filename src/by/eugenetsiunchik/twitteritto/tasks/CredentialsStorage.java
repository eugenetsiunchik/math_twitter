package by.eugenetsiunchik.twitteritto.tasks;

public class CredentialsStorage {

	private static CredentialsStorage instance;

	public static CredentialsStorage getInstance() {
		if (instance == null)
			instance = new CredentialsStorage();
		return instance;
	}

	private String savedImageUrl = null;
	private String savedName;
	private String profileName;
	private String friendsCount;
	private String followersCount;
	private String statusesCount;
	private String description;
	
	public String getFriendsCount() {
		return friendsCount;
	}

	public void setFriendsCount(String friendsCount) {
		this.friendsCount = friendsCount;
	}

	public String getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(String followersCount) {
		this.followersCount = followersCount;
	}

	public String getStatusesCount() {
		return statusesCount;
	}

	public void setStatusesCount(String statusesCount) {
		this.statusesCount = statusesCount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setImageUrl(String imageUrl) {

		savedImageUrl = imageUrl;

	}

	public String getImageUrl() {

		return savedImageUrl;
	}
	
	public void setName(String name) {

		savedName = name;

	}
	public String getName() {

		return savedName;
	}
	
	public void setProfile(String profileName){
		
		this.profileName = profileName;
		
	}

	public String getProfile() {
		
		return profileName;
	}
	

}
