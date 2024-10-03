package io.digitallly2024.commonlib.domain.message;

public class ResetPasswordMessage {
    private String email;
    private String token;

    public ResetPasswordMessage() {
    }

    public ResetPasswordMessage(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "ResetPasswordMessage{" +
                "email='" + email + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
