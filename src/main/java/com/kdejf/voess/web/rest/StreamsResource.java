package com.kdejf.voess.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.kdejf.voess.domain.Streams;

import com.kdejf.voess.repository.StreamsRepository;
import com.kdejf.voess.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Streams.
 */
@RestController
@RequestMapping("/api")
public class StreamsResource {

    private final Logger log = LoggerFactory.getLogger(StreamsResource.class);
        
    @Inject
    private StreamsRepository streamsRepository;

    /**
     * POST  /streams : Create a new streams.
     *
     * @param streams the streams to create
     * @return the ResponseEntity with status 201 (Created) and with body the new streams, or with status 400 (Bad Request) if the streams has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/streams")
    @Timed
    public ResponseEntity<Streams> createStreams(@RequestBody Streams streams) throws URISyntaxException {
        log.debug("REST request to save Streams : {}", streams);
        if (streams.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("streams", "idexists", "A new streams cannot already have an ID")).body(null);
        }
        Streams result = streamsRepository.save(streams);
        return ResponseEntity.created(new URI("/api/streams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("streams", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /streams : Updates an existing streams.
     *
     * @param streams the streams to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated streams,
     * or with status 400 (Bad Request) if the streams is not valid,
     * or with status 500 (Internal Server Error) if the streams couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/streams")
    @Timed
    public ResponseEntity<Streams> updateStreams(@RequestBody Streams streams) throws URISyntaxException {
        log.debug("REST request to update Streams : {}", streams);
        if (streams.getId() == null) {
            return createStreams(streams);
        }
        Streams result = streamsRepository.save(streams);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("streams", streams.getId().toString()))
            .body(result);
    }

    /**
     * GET  /streams : get all the streams.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of streams in body
     */
    @GetMapping("/streams")
    @Timed
    public List<Streams> getAllStreams() {
        log.debug("REST request to get all Streams");
        List<Streams> streams = streamsRepository.findAll();
        return streams;
    }

    /**
     * GET  /streams/:id : get the "id" streams.
     *
     * @param id the id of the streams to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the streams, or with status 404 (Not Found)
     */
    @GetMapping("/streams/{id}")
    @Timed
    public ResponseEntity<Streams> getStreams(@PathVariable Long id) {
        log.debug("REST request to get Streams : {}", id);
        Streams streams = streamsRepository.findOne(id);
        return Optional.ofNullable(streams)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /streams/:id : delete the "id" streams.
     *
     * @param id the id of the streams to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/streams/{id}")
    @Timed
    public ResponseEntity<Void> deleteStreams(@PathVariable Long id) {
        log.debug("REST request to delete Streams : {}", id);
        streamsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("streams", id.toString())).build();
    }

}
