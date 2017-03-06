package com.kdejf.voess.repository;

import com.kdejf.voess.domain.UserFavVideo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserFavVideo entity.
 */
@SuppressWarnings("unused")
public interface UserFavVideoRepository extends JpaRepository<UserFavVideo,Long> {

    @Query("select userFavVideo from UserFavVideo userFavVideo where userFavVideo.user.login = ?#{principal.username}")
    List<UserFavVideo> findByUserIsCurrentUser();

}
