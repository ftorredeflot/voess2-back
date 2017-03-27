package com.kdejf.voess.repository;

import com.kdejf.voess.domain.Streams;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Streams entity.
 */
@SuppressWarnings("unused")
public interface StreamsRepository extends JpaRepository<Streams,Long> {

}
