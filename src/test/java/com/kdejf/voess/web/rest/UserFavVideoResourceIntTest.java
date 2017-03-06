package com.kdejf.voess.web.rest;

import com.kdejf.voess.Voess2App;

import com.kdejf.voess.domain.UserFavVideo;
import com.kdejf.voess.repository.UserFavVideoRepository;

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
 * Test class for the UserFavVideoResource REST controller.
 *
 * @see UserFavVideoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Voess2App.class)
public class UserFavVideoResourceIntTest {

    private static final ZonedDateTime DEFAULT_START_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Inject
    private UserFavVideoRepository userFavVideoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUserFavVideoMockMvc;

    private UserFavVideo userFavVideo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserFavVideoResource userFavVideoResource = new UserFavVideoResource();
        ReflectionTestUtils.setField(userFavVideoResource, "userFavVideoRepository", userFavVideoRepository);
        this.restUserFavVideoMockMvc = MockMvcBuilders.standaloneSetup(userFavVideoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserFavVideo createEntity(EntityManager em) {
        UserFavVideo userFavVideo = new UserFavVideo()
                .startDateTime(DEFAULT_START_DATE_TIME);
        return userFavVideo;
    }

    @Before
    public void initTest() {
        userFavVideo = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserFavVideo() throws Exception {
        int databaseSizeBeforeCreate = userFavVideoRepository.findAll().size();

        // Create the UserFavVideo

        restUserFavVideoMockMvc.perform(post("/api/user-fav-videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userFavVideo)))
            .andExpect(status().isCreated());

        // Validate the UserFavVideo in the database
        List<UserFavVideo> userFavVideoList = userFavVideoRepository.findAll();
        assertThat(userFavVideoList).hasSize(databaseSizeBeforeCreate + 1);
        UserFavVideo testUserFavVideo = userFavVideoList.get(userFavVideoList.size() - 1);
        assertThat(testUserFavVideo.getStartDateTime()).isEqualTo(DEFAULT_START_DATE_TIME);
    }

    @Test
    @Transactional
    public void createUserFavVideoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userFavVideoRepository.findAll().size();

        // Create the UserFavVideo with an existing ID
        UserFavVideo existingUserFavVideo = new UserFavVideo();
        existingUserFavVideo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserFavVideoMockMvc.perform(post("/api/user-fav-videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingUserFavVideo)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UserFavVideo> userFavVideoList = userFavVideoRepository.findAll();
        assertThat(userFavVideoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserFavVideos() throws Exception {
        // Initialize the database
        userFavVideoRepository.saveAndFlush(userFavVideo);

        // Get all the userFavVideoList
        restUserFavVideoMockMvc.perform(get("/api/user-fav-videos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userFavVideo.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDateTime").value(hasItem(sameInstant(DEFAULT_START_DATE_TIME))));
    }

    @Test
    @Transactional
    public void getUserFavVideo() throws Exception {
        // Initialize the database
        userFavVideoRepository.saveAndFlush(userFavVideo);

        // Get the userFavVideo
        restUserFavVideoMockMvc.perform(get("/api/user-fav-videos/{id}", userFavVideo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userFavVideo.getId().intValue()))
            .andExpect(jsonPath("$.startDateTime").value(sameInstant(DEFAULT_START_DATE_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingUserFavVideo() throws Exception {
        // Get the userFavVideo
        restUserFavVideoMockMvc.perform(get("/api/user-fav-videos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserFavVideo() throws Exception {
        // Initialize the database
        userFavVideoRepository.saveAndFlush(userFavVideo);
        int databaseSizeBeforeUpdate = userFavVideoRepository.findAll().size();

        // Update the userFavVideo
        UserFavVideo updatedUserFavVideo = userFavVideoRepository.findOne(userFavVideo.getId());
        updatedUserFavVideo
                .startDateTime(UPDATED_START_DATE_TIME);

        restUserFavVideoMockMvc.perform(put("/api/user-fav-videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserFavVideo)))
            .andExpect(status().isOk());

        // Validate the UserFavVideo in the database
        List<UserFavVideo> userFavVideoList = userFavVideoRepository.findAll();
        assertThat(userFavVideoList).hasSize(databaseSizeBeforeUpdate);
        UserFavVideo testUserFavVideo = userFavVideoList.get(userFavVideoList.size() - 1);
        assertThat(testUserFavVideo.getStartDateTime()).isEqualTo(UPDATED_START_DATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingUserFavVideo() throws Exception {
        int databaseSizeBeforeUpdate = userFavVideoRepository.findAll().size();

        // Create the UserFavVideo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserFavVideoMockMvc.perform(put("/api/user-fav-videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userFavVideo)))
            .andExpect(status().isCreated());

        // Validate the UserFavVideo in the database
        List<UserFavVideo> userFavVideoList = userFavVideoRepository.findAll();
        assertThat(userFavVideoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserFavVideo() throws Exception {
        // Initialize the database
        userFavVideoRepository.saveAndFlush(userFavVideo);
        int databaseSizeBeforeDelete = userFavVideoRepository.findAll().size();

        // Get the userFavVideo
        restUserFavVideoMockMvc.perform(delete("/api/user-fav-videos/{id}", userFavVideo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserFavVideo> userFavVideoList = userFavVideoRepository.findAll();
        assertThat(userFavVideoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
