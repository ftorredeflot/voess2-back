package com.kdejf.voess.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kdejf.voess.domain.UserWatchedVideo;
import com.kdejf.voess.domain.UserFavVideo;
import com.kdejf.voess.domain.User;
import com.kdejf.voess.domain.Video;
import com.kdejf.voess.domain.Game;

import com.kdejf.voess.security.SecurityUtils;

import com.kdejf.voess.repository.VideoRepository;
import com.kdejf.voess.repository.UserRepository;
import com.kdejf.voess.repository.UserWatchedVideoRepository;
import com.kdejf.voess.repository.UserFavVideoRepository;
import com.kdejf.voess.repository.GameRepository;

import com.kdejf.voess.web.rest.util.HeaderUtil;
import com.kdejf.voess.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.time.ZonedDateTime;

/**
 * REST controller for managing Video.
 */
@RestController
@RequestMapping("/api")
public class VideoResource {

    private final Logger log = LoggerFactory.getLogger(VideoResource.class);

    @Inject
    private VideoRepository videoRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private UserWatchedVideoRepository userwatchedRepository;
    @Inject
    private UserFavVideoRepository userfavRepository;
    @Inject
    private GameRepository grepo;


    /**
     * POST  /videos : Create a new video.
     *
     * @param video the video to create
     * @return the ResponseEntity with status 201 (Created) and with body the new video, or with status 400 (Bad Request) if the video has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/videos")
    @Timed
    public ResponseEntity<Video> createVideo(@RequestBody Video video) throws URISyntaxException {
        log.debug("REST request to save Video : {}", video);
        if (video.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("video", "idexists", "A new video cannot already have an ID")).body(null);
        }
        Video result = videoRepository.save(video);
        return ResponseEntity.created(new URI("/api/videos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("video", result.getId().toString()))
            .body(result);
    }


    @PostMapping("/video/{id}/userFav")
    @Timed
    public ResponseEntity<UserFavVideo> userFavset(@PathVariable Long id) throws URISyntaxException {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        //Video video= videoRepository.findOne(id);
        UserFavVideo exist = userfavRepository.findByUserAndVideoId(user, id);
        ZonedDateTime today = ZonedDateTime.now();

        if (exist != null) {
            log.debug("REST request to save UserFavVideo : {}", exist);
            //exist.setStartDateTime(today);
            //userfavRepository.save(exist);

        } else {
            exist = new UserFavVideo();
            exist.setVideo(videoRepository.findOne(id));
            exist.setUser(user);
            exist.setStartDateTime(today);
            userfavRepository.save(exist);
        }

        return Optional.ofNullable(exist)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /**
     * POST  /video/{id}/userPlayed : Create a new liked video.
     *
     * @param id of video
     * @return UserWatchedVideo
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/video/{id}/userPlayed")
    @Timed
    public ResponseEntity<UserWatchedVideo> userViewedset(@PathVariable Long id) throws URISyntaxException {

        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        Video video = videoRepository.findOne(id);
        if (video == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("video", "novideoexist")).body(null);
        }
        ZonedDateTime today = ZonedDateTime.now();
        UserWatchedVideo u = new UserWatchedVideo();
        u.setStartDateTime(today);
        u.setUser(user);
        u.setVideo(video);
        UserWatchedVideo result = userwatchedRepository.save(u);
        return ResponseEntity.created(new URI("/api/user-watched-videos" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userWatchedVideo", result.getId().toString()))
            .body(result);


    }


    /**
     * PUT  /videos : Updates an existing video.
     *
     * @param video the video to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated video,
     * or with status 400 (Bad Request) if the video is not valid,
     * or with status 500 (Internal Server Error) if the video couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/videos")
    @Timed
    public ResponseEntity<Video> updateVideo(@RequestBody Video video) throws URISyntaxException {
        log.debug("REST request to update Video : {}", video);
        if (video.getId() == null) {
            return createVideo(video);
        }
        Video result = videoRepository.save(video);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("video", video.getId().toString()))
            .body(result);
    }

    /**
     * GET  /videos : get all the videos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of videos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/videos")
    @Timed
    public ResponseEntity<List<Video>> getAllVideos(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Videos");
        Page<Video> page = videoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/videos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET  /videos : get all the videos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of videos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/videosbygame/{id}")
    @Timed
    public ResponseEntity<List<Video>> getAllVideosygame(@PathVariable Long id, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Videos");
        Game game = grepo.findOne(id);
        if (game == null) {
            ResponseEntity.badRequest().headers(HeaderUtil.createAlert("friendship", "nogameexist")).body(null);
        }
        Page<Video> page = videoRepository.findByGameId(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/videosbygame");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET  /videos/:id : get the "id" video.
     *
     * @param id the id of the video to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the video, or with status 404 (Not Found)
     */
    @GetMapping("/videos/{id}")
    @Timed
    public ResponseEntity<Video> getVideo(@PathVariable Long id) {
        log.debug("REST request to get Video : {}", id);
        Video video = videoRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(video)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /video/{id}/userPlayed : get the "id" video.
     *
     * @param id the id of the video
     * @return the ResponseEntity with status 200 (OK) and with body the video, or with status 404 (Not Found)
     */
    @GetMapping("/video/{id}/userPlayed")
    @Timed
    public ResponseEntity<List<UserWatchedVideo>> userViewedget(@PathVariable Long id) throws URISyntaxException {
        List<UserWatchedVideo> list = userwatchedRepository.findByuserAndVideoId(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get(), id);
        if (list.isEmpty()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("userwatched", "nowatchexist")).body(null);
        }

        return Optional.ofNullable(list)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/video/userPlayedAll")
    @Timed
    public ResponseEntity<List<UserWatchedVideo>> userViewedgetall() throws URISyntaxException {
        List<UserWatchedVideo> list = userwatchedRepository.findByuser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get());
        if (list.isEmpty()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("userwatched", "nowatchexist")).body(null);
        }

        return Optional.ofNullable(list)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/video/{id}/lastuserPlayed")
    @Timed
    public ResponseEntity<UserWatchedVideo> lastuserViewedget(@PathVariable Long id) throws URISyntaxException {
        List<UserWatchedVideo> list = userwatchedRepository.findByuserAndVideoId(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get(), id);
        if (list.isEmpty()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("userwatched", "nowatchexist")).body(null);
        }
        UserWatchedVideo like = list.get(list.size() - 1);

        return Optional.ofNullable(like)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/video/{id}/userFav")
    @Timed
    public ResponseEntity<UserFavVideo> userFavget(@PathVariable Long id) throws URISyntaxException {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        //Video video= videoRepository.findOne(id);
        UserFavVideo exist = userfavRepository.findByUserAndVideoId(user, id);

        return Optional.ofNullable(exist)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /**
     * DELETE  /videos/:id : delete the "id" video.
     *
     * @param id the id of the video to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/videos/{id}")
    @Timed
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        log.debug("REST request to delete Video : {}", id);
        videoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("video", id.toString())).build();
    }

}
