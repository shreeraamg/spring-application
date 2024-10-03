package io.digitallly2024.webservice.dto;

public class CommentDto {

    private Long id;
    private String name;
    private Long userId;
    private String userName;
    private Long resourceId;

    public CommentDto() {
    }

    public CommentDto(Long id, String name, Long userId, String userName, Long resourceId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.userName = userName;
        this.resourceId = resourceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

}
