package org.orbitmvi.orbit.sample.posts.domain

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.orbitmvi.orbit.sample.posts.domain.repositories.PostOverview
import org.orbitmvi.orbit.sample.posts.domain.viewmodel.list.PostListViewModel
import org.orbitmvi.orbit.sample.posts.domain.viewmodel.detail.PostDetailsViewModel

actual class DependencyInjection(private val application: Application) {

    actual fun initialiseDependencyInjection(block: KoinAppDeclaration) {
        startKoin {
            androidContext(application)
            modules(commonModule(), androidModule())
            block()
        }
    }

    private fun androidModule() = module {
        viewModel { (postOverview: PostOverview) -> PostDetailsViewModel(get(), postOverview) }
        viewModel { PostListViewModel(get()) }
    }
}
