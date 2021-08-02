/*
 * Copyright 2021 Mikołaj Leszczyński & Appmattus Limited
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
 */

package org.orbitmvi.orbit.sample.posts.app.features.postlist.ui

import android.os.Bundle
import android.os.Parcel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import io.ktor.util.InternalAPI
import io.ktor.util.encodeBase64
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.sample.posts.android.R
import org.orbitmvi.orbit.sample.posts.app.common.AppBar
import org.orbitmvi.orbit.sample.posts.app.common.NavigationEvent
import org.orbitmvi.orbit.sample.posts.app.common.navigate
import org.orbitmvi.orbit.sample.posts.app.features.postlist.viewmodel.OpenPostNavigationEvent
import org.orbitmvi.orbit.sample.posts.app.features.postlist.viewmodel.PostListViewModel

@Composable
fun PostListScreen(navController: NavController, viewModel: PostListViewModel) {

    val state = viewModel.container.stateFlow.collectAsState().value

    LaunchedEffect(viewModel) {
        launch {
            viewModel.container.sideEffectFlow.collect { handleSideEffect(navController, it) }
        }
    }

    Column {
        AppBar(stringResource(id = R.string.app_name))

        LazyColumn {
            itemsIndexed(state.overviews) { index, post ->
                if (index != 0) Divider(color = colorResource(id = R.color.separator), modifier = Modifier.padding(horizontal = 16.dp))

                PostListItem(post) {
                    viewModel.onPostClicked(it)
                }
            }
        }
    }
}


private fun handleSideEffect(navController: NavController, sideEffect: NavigationEvent) {
    when (sideEffect) {
        is OpenPostNavigationEvent -> {


            @Suppress("EXPERIMENTAL_API_USAGE_ERROR")
            val serializedBytes = Parcel.obtain().run {
                writeParcelable(sideEffect.post, 0)
                marshall()
            }.encodeBase64()



            //sideEffect.post.writeToParcel()

            navController.navigate("detail/$serializedBytes")
            //navController.currentBackStackEntry?.arguments = Bundle().apply { putParcelable("item", sideEffect.post) }
        }
    }
}
