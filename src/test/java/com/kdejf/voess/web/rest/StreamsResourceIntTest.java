package com.kdejf.voess.web.rest;

import com.kdejf.voess.Voess2App;

import com.kdejf.voess.domain.Streams;
import com.kdejf.voess.repository.StreamsRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StreamsResource REST controller.
 *
 * @see StreamsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Voess2App.class)
public class StreamsResourceIntTest {

    private static final String DEFAULT_STREAMER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STREAMER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STREAMER_URL = "AAAAAAAAAA";
    private static final String UPDATED_STREAMER_URL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_STREAMER_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_STREAMER_PHOTO = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_STREAMER_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_STREAMER_PHOTO_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_STREAMER_STATE = false;
    private static final Boolean UPDATED_STREAMER_STATE = true;

    @Inject
    private StreamsRepository streamsRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restStreamsMockMvc;

    private Streams streams;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StreamsResource streamsResource = new StreamsResource();
        ReflectionTestUtils.setField(streamsResource, "streamsRepository", streamsRepository);
        this.restStreamsMockMvc = MockMvcBuilders.standaloneSetup(streamsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Streams createEntity(EntityManager em) {
        Streams streams = new Streams()
                .streamerName(DEFAULT_STREAMER_NAME)
                .streamerUrl(DEFAULT_STREAMER_URL)
                .streamerPhoto(DEFAULT_STREAMER_PHOTO)
                .streamerPhotoContentType(DEFAULT_STREAMER_PHOTO_CONTENT_TYPE)
                .streamerState(DEFAULT_STREAMER_STATE);
        return streams;
    }

    @Before
    public void initTest() {
        streams = createEntity(em);
    }

    @Test
    @Transactional
    public void createStreams() throws Exception {
        int databaseSizeBeforeCreate = streamsRepository.findAll().size();

        // Create the Streams

        restStreamsMockMvc.perform(post("/api/streams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(streams)))
            .andExpect(status().isCreated());

        // Validate the Streams in the database
        List<Streams> streamsList = streamsRepository.findAll();
        assertThat(streamsList).hasSize(databaseSizeBeforeCreate + 1);
        Streams testStreams = streamsList.get(streamsList.size() - 1);
        assertThat(testStreams.getStreamerName()).isEqualTo(DEFAULT_STREAMER_NAME);
        assertThat(testStreams.getStreamerUrl()).isEqualTo(DEFAULT_STREAMER_URL);
        assertThat(testStreams.getStreamerPhoto()).isEqualTo(DEFAULT_STREAMER_PHOTO);
        assertThat(testStreams.getStreamerPhotoContentType()).isEqualTo(DEFAULT_STREAMER_PHOTO_CONTENT_TYPE);
        assertThat(testStreams.isStreamerState()).isEqualTo(DEFAULT_STREAMER_STATE);
    }

    @Test
    @Transactional
    public void createStreamsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = streamsRepository.findAll().size();

        // Create the Streams with an existing ID
        Streams existingStreams = new Streams();
        existingStreams.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStreamsMockMvc.perform(post("/api/streams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingStreams)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Streams> streamsList = streamsRepository.findAll();
        assertThat(streamsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStreams() throws Exception {
        // Initialize the database
        streamsRepository.saveAndFlush(streams);

        // Get all the streamsList
        restStreamsMockMvc.perform(get("/api/streams?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(streams.getId().intValue())))
            .andExpect(jsonPath("$.[*].streamerName").value(hasItem(DEFAULT_STREAMER_NAME.toString())))
            .andExpect(jsonPath("$.[*].streamerUrl").value(hasItem(DEFAULT_STREAMER_URL.toString())))
            .andExpect(jsonPath("$.[*].streamerPhotoContentType").value(hasItem(DEFAULT_STREAMER_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].streamerPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_STREAMER_PHOTO))))
            .andExpect(jsonPath("$.[*].streamerState").value(hasItem(DEFAULT_STREAMER_STATE.booleanValue())));
    }

    @Test
    @Transactional
    public void getStreams() throws Exception {
        // Initialize the database
        streamsRepository.saveAndFlush(streams);

        // Get the streams
        restStreamsMockMvc.perform(get("/api/streams/{id}", streams.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(streams.getId().intValue()))
            .andExpect(jsonPath("$.streamerName").value(DEFAULT_STREAMER_NAME.toString()))
            .andExpect(jsonPath("$.streamerUrl").value(DEFAULT_STREAMER_URL.toString()))
            .andExpect(jsonPath("$.streamerPhotoContentType").value(DEFAULT_STREAMER_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.streamerPhoto").value(Base64Utils.encodeToString(DEFAULT_STREAMER_PHOTO)))
            .andExpect(jsonPath("$.streamerState").value(DEFAULT_STREAMER_STATE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStreams() throws Exception {
        // Get the streams
        restStreamsMockMvc.perform(get("/api/streams/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStreams() throws Exception {
        // Initialize the database
        streamsRepository.saveAndFlush(streams);
        int databaseSizeBeforeUpdate = streamsRepository.findAll().size();

        // Update the streams
        Streams updatedStreams = streamsRepository.findOne(streams.getId());
        updatedStreams
                .streamerName(UPDATED_STREAMER_NAME)
                .streamerUrl(UPDATED_STREAMER_URL)
                .streamerPhoto(UPDATED_STREAMER_PHOTO)
                .streamerPhotoContentType(UPDATED_STREAMER_PHOTO_CONTENT_TYPE)
                .streamerState(UPDATED_STREAMER_STATE);

        restStreamsMockMvc.perform(put("/api/streams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStreams)))
            .andExpect(status().isOk());

        // Validate the Streams in the database
        List<Streams> streamsList = streamsRepository.findAll();
        assertThat(streamsList).hasSize(databaseSizeBeforeUpdate);
        Streams testStreams = streamsList.get(streamsList.size() - 1);
        assertThat(testStreams.getStreamerName()).isEqualTo(UPDATED_STREAMER_NAME);
        assertThat(testStreams.getStreamerUrl()).isEqualTo(UPDATED_STREAMER_URL);
        assertThat(testStreams.getStreamerPhoto()).isEqualTo(UPDATED_STREAMER_PHOTO);
        assertThat(testStreams.getStreamerPhotoContentType()).isEqualTo(UPDATED_STREAMER_PHOTO_CONTENT_TYPE);
        assertThat(testStreams.isStreamerState()).isEqualTo(UPDATED_STREAMER_STATE);
    }

    @Test
    @Transactional
    public void updateNonExistingStreams() throws Exception {
        int databaseSizeBeforeUpdate = streamsRepository.findAll().size();

        // Create the Streams

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStreamsMockMvc.perform(put("/api/streams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(streams)))
            .andExpect(status().isCreated());

        // Validate the Streams in the database
        List<Streams> streamsList = streamsRepository.findAll();
        assertThat(streamsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStreams() throws Exception {
        // Initialize the database
        streamsRepository.saveAndFlush(streams);
        int databaseSizeBeforeDelete = streamsRepository.findAll().size();

        // Get the streams
        restStreamsMockMvc.perform(delete("/api/streams/{id}", streams.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Streams> streamsList = streamsRepository.findAll();
        assertThat(streamsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
