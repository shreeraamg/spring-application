package io.digitallly2024.webservice.entity;

import io.digitallly2024.webservice.enums.ResourceEnums;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String description;

    private ResourceEnums.Category resourceCategory;

    private Long votes;

    @ManyToOne(
            targetEntity = User.class,
            optional = false,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "user_id", nullable = false)
    private User createdBy;

    @OneToMany(targetEntity = Comment.class, mappedBy = "resource", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ResourceEnums.Category getResourceCategory() {
        return resourceCategory;
    }

    public void setResourceCategory(ResourceEnums.Category resourceCategory) {
        this.resourceCategory = resourceCategory;
    }

    public Long getVotes() {
        return votes;
    }

    public void setVotes(Long votes) {
        this.votes = votes;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

}
