package com.kdejf.voess.repository;

import com.kdejf.voess.domain.UserFavVideo;
import com.kdejf.voess.domain.User;
import com.kdejf.voess.domain.Video;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the UserFavVideo entity.
 */
@SuppressWarnings("unused")
public interface UserFavVideoRepository extends JpaRepository<UserFavVideo,Long> {

    @Query("select userFavVideo from UserFavVideo userFavVideo where userFavVideo.user.login = ?#{principal.username}")
    List<UserFavVideo> findByUserIsCurrentUser();


    @Query("select userFavVideo from UserFavVideo userFavVideo where userFavVideo.user= :u and userFavVideo.video= :v")
    UserFavVideo findByuserandvideo(@Param("u") User user,@Param("v") Video video);

}
