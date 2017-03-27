package com.kdejf.voess.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Streams.
 */
@Entity
@Table(name = "streams")
public class Streams implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "streamer_name")
    private String streamerName;

    @Column(name = "streamer_url")
    private String streamerUrl;

    @Lob
    @Column(name = "streamer_photo")
    private byte[] streamerPhoto;

    @Column(name = "streamer_photo_content_type")
    private String streamerPhotoContentType;

    @Column(name = "streamer_state")
    private Boolean streamerState;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreamerName() {
        return streamerName;
    }

    public Streams streamerName(String streamerName) {
        this.streamerName = streamerName;
        return this;
    }

    public void setStreamerName(String streamerName) {
        this.streamerName = streamerName;
    }

    public String getStreamerUrl() {
        return streamerUrl;
    }

    public Streams streamerUrl(String streamerUrl) {
        this.streamerUrl = streamerUrl;
        return this;
    }

    public void setStreamerUrl(String streamerUrl) {
        this.streamerUrl = streamerUrl;
    }

    public byte[] getStreamerPhoto() {
        return streamerPhoto;
    }

    public Streams streamerPhoto(byte[] streamerPhoto) {
        this.streamerPhoto = streamerPhoto;
        return this;
    }

    public void setStreamerPhoto(byte[] streamerPhoto) {
        this.streamerPhoto = streamerPhoto;
    }

    public String getStreamerPhotoContentType() {
        return streamerPhotoContentType;
    }

    public Streams streamerPhotoContentType(String streamerPhotoContentType) {
        this.streamerPhotoContentType = streamerPhotoContentType;
        return this;
    }

    public void setStreamerPhotoContentType(String streamerPhotoContentType) {
        this.streamerPhotoContentType = streamerPhotoContentType;
    }

    public Boolean isStreamerState() {
        return streamerState;
    }

    public Streams streamerState(Boolean streamerState) {
        this.streamerState = streamerState;
        return this;
    }

    public void setStreamerState(Boolean streamerState) {
        this.streamerState = streamerState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Streams streams = (Streams) o;
        if (streams.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, streams.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Streams{" +
            "id=" + id +
            ", streamerName='" + streamerName + "'" +
            ", streamerUrl='" + streamerUrl + "'" +
            ", streamerPhoto='" + streamerPhoto + "'" +
            ", streamerPhotoContentType='" + streamerPhotoContentType + "'" +
            ", streamerState='" + streamerState + "'" +
            '}';
    }
}
