/*
 * Copyright 2021 Mikołaj Leszczyński & Appmattus Limited
 * Copyright 2020 Babylon Partners Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * File modified by Mikołaj Leszczyński & Appmattus Limited
 * See: https://github.com/orbit-mvi/orbit-mvi/compare/c5b8b3f2b83b5972ba2ad98f73f75086a89653d3...main
 */

package org.orbitmvi.orbit.sample.posts.data.posts.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.Url
import io.ktor.http.contentType
import org.orbitmvi.orbit.sample.posts.data.posts.model.CommentData
import org.orbitmvi.orbit.sample.posts.data.posts.model.PostData
import org.orbitmvi.orbit.sample.posts.data.posts.model.UserData
import org.orbitmvi.orbit.sample.posts.domain.repositories.Status

class PostNetworkDataSource(private val client: HttpClient) {

    suspend fun getPost(id: Int): Status<PostData> {
        return try {
            Status.Success(client.getJson("posts/$id"))
        } catch (e: Exception) {
            println(e)
            Status.Failure(e)
        }
    }

    suspend fun getPosts(): List<PostData> {
        return try {
            client.getJson<List<PostData>>("posts").sortedBy { it.title }
        } catch (e: Exception) {
            println(e)
            emptyList()
        }
    }

    suspend fun getUsers(): List<UserData> {
        return try {
            client.getJson("users")
        } catch (e: Exception) {
            println(e)
            emptyList()
        }
    }

    suspend fun getUser(id: Int): UserData? {
        return try {
            client.getJson<UserData>("users/$id")
        } catch (e: Exception) {
            println(e)
            null
        }
    }

    suspend fun getComments(): List<CommentData> {
        return try {
            client.getJson("comments")
        } catch (e: Exception) {
            println(e)
            emptyList()
        }
    }

    suspend fun getComments(postId: Int): List<CommentData> {
        return try {
            client.getJson("posts/$postId/comments")
        } catch (e: Exception) {
            println(e)
            emptyList()
        }
    }

    private suspend inline fun <reified T> HttpClient.getJson(urlString: String) = get<T>(Url(urlString)) {
        contentType(ContentType.Application.Json)
    }
}
