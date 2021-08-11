package org.orbitmvi.orbit.sample.posts.domain

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.orbitmvi.orbit.sample.posts.domain.repositories.PostOverview
import org.orbitmvi.orbit.sample.posts.domain.viewmodel.list.PostListViewModel
import org.orbitmvi.orbit.sample.posts.domain.viewmodel.detail.PostDetailsViewModel

actual class DependencyInjection {
    actual fun initialiseDependencyInjection(block: KoinAppDeclaration) {
        startKoin {
            modules(commonModule(), iosModule())
            block()
        }
    }

    private fun iosModule() = module {
        factory { (postOverview: PostOverview) -> PostDetailsViewModel(get(), postOverview) }
        factory { PostListViewModel(get()) }
    }
}
