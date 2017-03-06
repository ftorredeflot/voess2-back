package com.kdejf.voess.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A UserWatchedVideo.
 */
@Entity
@Table(name = "user_watched_video")
public class UserWatchedVideo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "start_date_time")
    private ZonedDateTime startDateTime;

    @ManyToOne
    private User user;

    @ManyToOne
    private Video video;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartDateTime() {
        return startDateTime;
    }

    public UserWatchedVideo startDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public void setStartDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public User getUser() {
        return user;
    }

    public UserWatchedVideo user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Video getVideo() {
        return video;
    }

    public UserWatchedVideo video(Video video) {
        this.video = video;
        return this;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserWatchedVideo userWatchedVideo = (UserWatchedVideo) o;
        if (userWatchedVideo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, userWatchedVideo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserWatchedVideo{" +
            "id=" + id +
            ", startDateTime='" + startDateTime + "'" +
            '}';
    }
}
