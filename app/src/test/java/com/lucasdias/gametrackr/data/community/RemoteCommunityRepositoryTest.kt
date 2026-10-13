package com.lucasdias.gametrackr.data.community

import com.lucasdias.gametrackr.core.network.CommunityApi
import com.lucasdias.gametrackr.feature.app.community.LikeStatus
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class RemoteCommunityRepositoryTest {
    private val server = MockWebServer()
    private val json =
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    private lateinit var repository: RemoteCommunityRepository

    @Before
    fun setUp() {
        server.start()
        val api =
            Retrofit
                .Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(CommunityApi::class.java)
        repository = RemoteCommunityRepository(api, json)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `fetch posts maps the paginated response to a page`() =
        runBlocking {
            server.enqueue(MockResponse().setResponseCode(HTTP_OK).setBody(POSTS_BODY))

            val page = repository.fetchPosts(page = CURRENT_PAGE, perPage = PER_PAGE, communityId = null).getOrThrow()

            assertEquals(listOf(POST_TITLE), page.items.map { it.title })
            assertEquals(CURRENT_PAGE, page.currentPage)
            assertEquals(LAST_PAGE, page.lastPage)
            assertEquals(TOTAL, page.total)
        }

    @Test
    fun `toggle post like returns the like status from the api`() =
        runBlocking {
            server.enqueue(MockResponse().setResponseCode(HTTP_OK).setBody(LIKE_BODY))

            val status = repository.togglePostLike(POST_ID).getOrThrow()

            assertEquals(LikeStatus(isLiked = true, likes = LIKES), status)
        }

    @Test
    fun `fetch post comments nests replies under their root comment`() =
        runBlocking {
            server.enqueue(MockResponse().setResponseCode(HTTP_OK).setBody(POST_WITH_COMMENTS_BODY))

            val comments = repository.fetchPostComments(POST_ID).getOrThrow()

            assertEquals(listOf(ROOT_COMMENT_ID), comments.map { it.id })
            assertEquals(listOf(REPLY_ID, NESTED_REPLY_ID), comments.single().replies.map { it.id })
        }

    private companion object {
        const val HTTP_OK = 200
        const val POST_ID = 7L
        const val POST_TITLE = "Best boss fight?"
        const val CURRENT_PAGE = 1
        const val LAST_PAGE = 3
        const val PER_PAGE = 20
        const val TOTAL = 45
        const val LIKES = 12
        const val POSTS_BODY =
            """{"data":[{"id":$POST_ID,"title":"$POST_TITLE","slug":"best-boss-fight","community_id":1,"author_id":1}],""" +
                """"current_page":$CURRENT_PAGE,"last_page":$LAST_PAGE,"per_page":$PER_PAGE,"total":$TOTAL}"""
        const val ROOT_COMMENT_ID = 1L
        const val REPLY_ID = 2L
        const val NESTED_REPLY_ID = 3L
        const val POST_WITH_COMMENTS_BODY =
            """{"id":$POST_ID,"title":"$POST_TITLE","slug":"best-boss-fight","community_id":1,"author_id":1,"comments":[""" +
                """{"id":$ROOT_COMMENT_ID,"content":"Root","author_id":1,"post_id":$POST_ID},""" +
                """{"id":$REPLY_ID,"content":"Reply","author_id":1,"post_id":$POST_ID,"parent_id":$ROOT_COMMENT_ID},""" +
                """{"id":$NESTED_REPLY_ID,"content":"Nested","author_id":1,"post_id":$POST_ID,"parent_id":$REPLY_ID}]}"""
        const val LIKE_BODY = """{"message":"Post liked","is_liked":true,"likes":$LIKES}"""
    }
}
