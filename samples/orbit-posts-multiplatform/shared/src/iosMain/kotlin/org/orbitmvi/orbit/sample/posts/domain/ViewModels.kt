package org.orbitmvi.orbit.sample.posts.domain

import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.sample.posts.domain.repositories.PostOverview
import org.orbitmvi.orbit.sample.posts.domain.viewmodel.list.PostListViewModel
import org.orbitmvi.orbit.sample.posts.domain.viewmodel.detail.PostDetailsViewModel

object ViewModels : KoinComponent {
    fun postListViewModel() = get<PostListViewModel>()

    fun postDetailsViewModel(postOverview: PostOverview) = get<PostDetailsViewModel> { parametersOf(postOverview) }
}
