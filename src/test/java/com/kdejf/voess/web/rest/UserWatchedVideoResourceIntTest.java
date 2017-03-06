package com.kdejf.voess.web.rest;

import com.kdejf.voess.Voess2App;

import com.kdejf.voess.domain.UserWatchedVideo;
import com.kdejf.voess.repository.UserWatchedVideoRepository;

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

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.kdejf.voess.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserWatchedVideoResource REST controller.
 *
 * @see UserWatchedVideoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Voess2App.class)
public class UserWatchedVideoResourceIntTest {

    private static final ZonedDateTime DEFAULT_START_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Inject
    private UserWatchedVideoRepository userWatchedVideoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUserWatchedVideoMockMvc;

    private UserWatchedVideo userWatchedVideo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserWatchedVideoResource userWatchedVideoResource = new UserWatchedVideoResource();
        ReflectionTestUtils.setField(userWatchedVideoResource, "userWatchedVideoRepository", userWatchedVideoRepository);
        this.restUserWatchedVideoMockMvc = MockMvcBuilders.standaloneSetup(userWatchedVideoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserWatchedVideo createEntity(EntityManager em) {
        UserWatchedVideo userWatchedVideo = new UserWatchedVideo()
                .startDateTime(DEFAULT_START_DATE_TIME);
        return userWatchedVideo;
    }

    @Before
    public void initTest() {
        userWatchedVideo = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserWatchedVideo() throws Exception {
        int databaseSizeBeforeCreate = userWatchedVideoRepository.findAll().size();

        // Create the UserWatchedVideo

        restUserWatchedVideoMockMvc.perform(post("/api/user-watched-videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userWatchedVideo)))
            .andExpect(status().isCreated());

        // Validate the UserWatchedVideo in the database
        List<UserWatchedVideo> userWatchedVideoList = userWatchedVideoRepository.findAll();
        assertThat(userWatchedVideoList).hasSize(databaseSizeBeforeCreate + 1);
        UserWatchedVideo testUserWatchedVideo = userWatchedVideoList.get(userWatchedVideoList.size() - 1);
        assertThat(testUserWatchedVideo.getStartDateTime()).isEqualTo(DEFAULT_START_DATE_TIME);
    }

    @Test
    @Transactional
    public void createUserWatchedVideoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userWatchedVideoRepository.findAll().size();

        // Create the UserWatchedVideo with an existing ID
        UserWatchedVideo existingUserWatchedVideo = new UserWatchedVideo();
        existingUserWatchedVideo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserWatchedVideoMockMvc.perform(post("/api/user-watched-videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingUserWatchedVideo)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UserWatchedVideo> userWatchedVideoList = userWatchedVideoRepository.findAll();
        assertThat(userWatchedVideoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserWatchedVideos() throws Exception {
        // Initialize the database
        userWatchedVideoRepository.saveAndFlush(userWatchedVideo);

        // Get all the userWatchedVideoList
        restUserWatchedVideoMockMvc.perform(get("/api/user-watched-videos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userWatchedVideo.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDateTime").value(hasItem(sameInstant(DEFAULT_START_DATE_TIME))));
    }

    @Test
    @Transactional
    public void getUserWatchedVideo() throws Exception {
        // Initialize the database
        userWatchedVideoRepository.saveAndFlush(userWatchedVideo);

        // Get the userWatchedVideo
        restUserWatchedVideoMockMvc.perform(get("/api/user-watched-videos/{id}", userWatchedVideo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userWatchedVideo.getId().intValue()))
            .andExpect(jsonPath("$.startDateTime").value(sameInstant(DEFAULT_START_DATE_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingUserWatchedVideo() throws Exception {
        // Get the userWatchedVideo
        restUserWatchedVideoMockMvc.perform(get("/api/user-watched-videos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserWatchedVideo() throws Exception {
        // Initialize the database
        userWatchedVideoRepository.saveAndFlush(userWatchedVideo);
        int databaseSizeBeforeUpdate = userWatchedVideoRepository.findAll().size();

        // Update the userWatchedVideo
        UserWatchedVideo updatedUserWatchedVideo = userWatchedVideoRepository.findOne(userWatchedVideo.getId());
        updatedUserWatchedVideo
                .startDateTime(UPDATED_START_DATE_TIME);

        restUserWatchedVideoMockMvc.perform(put("/api/user-watched-videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserWatchedVideo)))
            .andExpect(status().isOk());

        // Validate the UserWatchedVideo in the database
        List<UserWatchedVideo> userWatchedVideoList = userWatchedVideoRepository.findAll();
        assertThat(userWatchedVideoList).hasSize(databaseSizeBeforeUpdate);
        UserWatchedVideo testUserWatchedVideo = userWatchedVideoList.get(userWatchedVideoList.size() - 1);
        assertThat(testUserWatchedVideo.getStartDateTime()).isEqualTo(UPDATED_START_DATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingUserWatchedVideo() throws Exception {
        int databaseSizeBeforeUpdate = userWatchedVideoRepository.findAll().size();

        // Create the UserWatchedVideo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserWatchedVideoMockMvc.perform(put("/api/user-watched-videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userWatchedVideo)))
            .andExpect(status().isCreated());

        // Validate the UserWatchedVideo in the database
        List<UserWatchedVideo> userWatchedVideoList = userWatchedVideoRepository.findAll();
        assertThat(userWatchedVideoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserWatchedVideo() throws Exception {
        // Initialize the database
        userWatchedVideoRepository.saveAndFlush(userWatchedVideo);
        int databaseSizeBeforeDelete = userWatchedVideoRepository.findAll().size();

        // Get the userWatchedVideo
        restUserWatchedVideoMockMvc.perform(delete("/api/user-watched-videos/{id}", userWatchedVideo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserWatchedVideo> userWatchedVideoList = userWatchedVideoRepository.findAll();
        assertThat(userWatchedVideoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
