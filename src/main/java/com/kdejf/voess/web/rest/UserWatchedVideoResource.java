package com.kdejf.voess.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kdejf.voess.domain.UserWatchedVideo;

import com.kdejf.voess.repository.UserWatchedVideoRepository;
import com.kdejf.voess.web.rest.util.HeaderUtil;
import com.kdejf.voess.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserWatchedVideo.
 */
@RestController
@RequestMapping("/api")
public class UserWatchedVideoResource {

    private final Logger log = LoggerFactory.getLogger(UserWatchedVideoResource.class);
        
    @Inject
    private UserWatchedVideoRepository userWatchedVideoRepository;

    /**
     * POST  /user-watched-videos : Create a new userWatchedVideo.
     *
     * @param userWatchedVideo the userWatchedVideo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userWatchedVideo, or with status 400 (Bad Request) if the userWatchedVideo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-watched-videos")
    @Timed
    public ResponseEntity<UserWatchedVideo> createUserWatchedVideo(@RequestBody UserWatchedVideo userWatchedVideo) throws URISyntaxException {
        log.debug("REST request to save UserWatchedVideo : {}", userWatchedVideo);
        if (userWatchedVideo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userWatchedVideo", "idexists", "A new userWatchedVideo cannot already have an ID")).body(null);
        }
        UserWatchedVideo result = userWatchedVideoRepository.save(userWatchedVideo);
        return ResponseEntity.created(new URI("/api/user-watched-videos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userWatchedVideo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-watched-videos : Updates an existing userWatchedVideo.
     *
     * @param userWatchedVideo the userWatchedVideo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userWatchedVideo,
     * or with status 400 (Bad Request) if the userWatchedVideo is not valid,
     * or with status 500 (Internal Server Error) if the userWatchedVideo couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-watched-videos")
    @Timed
    public ResponseEntity<UserWatchedVideo> updateUserWatchedVideo(@RequestBody UserWatchedVideo userWatchedVideo) throws URISyntaxException {
        log.debug("REST request to update UserWatchedVideo : {}", userWatchedVideo);
        if (userWatchedVideo.getId() == null) {
            return createUserWatchedVideo(userWatchedVideo);
        }
        UserWatchedVideo result = userWatchedVideoRepository.save(userWatchedVideo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userWatchedVideo", userWatchedVideo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-watched-videos : get all the userWatchedVideos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userWatchedVideos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/user-watched-videos")
    @Timed
    public ResponseEntity<List<UserWatchedVideo>> getAllUserWatchedVideos(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of UserWatchedVideos");
        Page<UserWatchedVideo> page = userWatchedVideoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-watched-videos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-watched-videos/:id : get the "id" userWatchedVideo.
     *
     * @param id the id of the userWatchedVideo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userWatchedVideo, or with status 404 (Not Found)
     */
    @GetMapping("/user-watched-videos/{id}")
    @Timed
    public ResponseEntity<UserWatchedVideo> getUserWatchedVideo(@PathVariable Long id) {
        log.debug("REST request to get UserWatchedVideo : {}", id);
        UserWatchedVideo userWatchedVideo = userWatchedVideoRepository.findOne(id);
        return Optional.ofNullable(userWatchedVideo)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /user-watched-videos/:id : delete the "id" userWatchedVideo.
     *
     * @param id the id of the userWatchedVideo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-watched-videos/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserWatchedVideo(@PathVariable Long id) {
        log.debug("REST request to delete UserWatchedVideo : {}", id);
        userWatchedVideoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userWatchedVideo", id.toString())).build();
    }

}
