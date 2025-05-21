import Restaurant.*;
public class UserProfileProxy implements UserProfile {
    private String username;
    private UserProfileImpl userProfile;

    public UserProfileProxy(String username) {
        this.username = username;
    }

    @Override
    public String getFullName() {
        if (userProfile == null) {
            userProfile = new UserProfileImpl(username);
        }
        return userProfile.getFullName();
    }

    @Override
    public String getUsername() {
        if (userProfile == null) {
            userProfile = new UserProfileImpl(username);
        }
        return userProfile.getUsername();
    }

    @Override
    public String getPhoneNumber() {
        if (userProfile == null) {
            userProfile = new UserProfileImpl(username);
        }
        return userProfile.getPhoneNumber();
    }

    @Override
    public String getEmail() {
        if (userProfile == null) {
            userProfile = new UserProfileImpl(username);
        }
        return userProfile.getEmail();
    }
}
