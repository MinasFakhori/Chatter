package domains.brighton.mf600.chatter;

public class User {
    private String username;
    private String email;
    private String profileImage;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String username, String email, String profileImage) {
        this.username = username;
        this.email = email;
        this.profileImage = profileImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

}
