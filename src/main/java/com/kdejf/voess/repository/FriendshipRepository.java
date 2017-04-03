package com.kdejf.voess.repository;

import com.kdejf.voess.domain.Friendship;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Friendship entity.
 */
@SuppressWarnings("unused")
public interface FriendshipRepository extends JpaRepository<Friendship,Long> {

    @Query("select friendship from Friendship friendship where friendship.frienshipFrom.login = ?#{principal.username}")
    List<Friendship> findByFrienshipFromIsCurrentUser();

    @Query("select friendship from Friendship friendship where friendship.frienshipTo.login = ?#{principal.username}")
    List<Friendship> findByFrienshipToIsCurrentUser();

    Friendship findByFrienshipFromIdAndFrienshipToId(Long from,Long to);


    @Query("select fr from Friendship fr where fr.frienshipFrom.id = :f and fr.frienshipTo.id = :t and  fr.finishDateTime is null" )
    Friendship findByFrienshipFromIdAndFrienshipToIdAndFinishDateTimeIsNotdefined(@Param("f") Long from,@Param("t") Long to);

}
