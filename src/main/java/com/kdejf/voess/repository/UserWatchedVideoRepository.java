package com.kdejf.voess.repository;

import com.kdejf.voess.domain.UserWatchedVideo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserWatchedVideo entity.
 */
@SuppressWarnings("unused")
public interface UserWatchedVideoRepository extends JpaRepository<UserWatchedVideo,Long> {

    @Query("select userWatchedVideo from UserWatchedVideo userWatchedVideo where userWatchedVideo.user.login = ?#{principal.username}")
    List<UserWatchedVideo> findByUserIsCurrentUser();

}
