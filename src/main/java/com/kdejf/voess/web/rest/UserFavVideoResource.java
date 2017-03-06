package com.kdejf.voess.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kdejf.voess.domain.UserFavVideo;

import com.kdejf.voess.repository.UserFavVideoRepository;
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
 * REST controller for managing UserFavVideo.
 */
@RestController
@RequestMapping("/api")
public class UserFavVideoResource {

    private final Logger log = LoggerFactory.getLogger(UserFavVideoResource.class);
        
    @Inject
    private UserFavVideoRepository userFavVideoRepository;

    /**
     * POST  /user-fav-videos : Create a new userFavVideo.
     *
     * @param userFavVideo the userFavVideo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userFavVideo, or with status 400 (Bad Request) if the userFavVideo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-fav-videos")
    @Timed
    public ResponseEntity<UserFavVideo> createUserFavVideo(@RequestBody UserFavVideo userFavVideo) throws URISyntaxException {
        log.debug("REST request to save UserFavVideo : {}", userFavVideo);
        if (userFavVideo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userFavVideo", "idexists", "A new userFavVideo cannot already have an ID")).body(null);
        }
        UserFavVideo result = userFavVideoRepository.save(userFavVideo);
        return ResponseEntity.created(new URI("/api/user-fav-videos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userFavVideo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-fav-videos : Updates an existing userFavVideo.
     *
     * @param userFavVideo the userFavVideo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userFavVideo,
     * or with status 400 (Bad Request) if the userFavVideo is not valid,
     * or with status 500 (Internal Server Error) if the userFavVideo couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-fav-videos")
    @Timed
    public ResponseEntity<UserFavVideo> updateUserFavVideo(@RequestBody UserFavVideo userFavVideo) throws URISyntaxException {
        log.debug("REST request to update UserFavVideo : {}", userFavVideo);
        if (userFavVideo.getId() == null) {
            return createUserFavVideo(userFavVideo);
        }
        UserFavVideo result = userFavVideoRepository.save(userFavVideo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userFavVideo", userFavVideo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-fav-videos : get all the userFavVideos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userFavVideos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/user-fav-videos")
    @Timed
    public ResponseEntity<List<UserFavVideo>> getAllUserFavVideos(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of UserFavVideos");
        Page<UserFavVideo> page = userFavVideoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-fav-videos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-fav-videos/:id : get the "id" userFavVideo.
     *
     * @param id the id of the userFavVideo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userFavVideo, or with status 404 (Not Found)
     */
    @GetMapping("/user-fav-videos/{id}")
    @Timed
    public ResponseEntity<UserFavVideo> getUserFavVideo(@PathVariable Long id) {
        log.debug("REST request to get UserFavVideo : {}", id);
        UserFavVideo userFavVideo = userFavVideoRepository.findOne(id);
        return Optional.ofNullable(userFavVideo)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /user-fav-videos/:id : delete the "id" userFavVideo.
     *
     * @param id the id of the userFavVideo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-fav-videos/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserFavVideo(@PathVariable Long id) {
        log.debug("REST request to delete UserFavVideo : {}", id);
        userFavVideoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userFavVideo", id.toString())).build();
    }

}
