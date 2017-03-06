package com.kdejf.voess.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A UserFavVideo.
 */
@Entity
@Table(name = "user_fav_video")
public class UserFavVideo implements Serializable {

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

    public UserFavVideo startDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public void setStartDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public User getUser() {
        return user;
    }

    public UserFavVideo user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Video getVideo() {
        return video;
    }

    public UserFavVideo video(Video video) {
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
        UserFavVideo userFavVideo = (UserFavVideo) o;
        if (userFavVideo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, userFavVideo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserFavVideo{" +
            "id=" + id +
            ", startDateTime='" + startDateTime + "'" +
            '}';
    }
}
