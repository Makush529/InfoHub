public interface SQLCommands {
    String GET_ALL_USERS="SELECT * FROM users";
    String GET_USER_BY_ID="SELECT * FROM users WHERE id=?";
    String CREATE_USER = "INSERT INTO users (username, password) VALUES (?, ?)";
}
