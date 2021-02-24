package com.cloudshare.cloudshareapi.model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "file_share")
public class FileShare {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "file_id")
    private FileMeta fileId;

    @Column(name ="share_id")
    private String shareId;

    @Column(name = "size")
    private Long size;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FileMeta getFileId() {
        return fileId;
    }

    public void setFileId(FileMeta fileId) {
        this.fileId = fileId;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
}
