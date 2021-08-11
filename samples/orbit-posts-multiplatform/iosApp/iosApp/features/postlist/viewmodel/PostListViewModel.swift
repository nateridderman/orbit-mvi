//
//  PostListViewModel.swift
//  iosApp
//
//  Created by Matthew Dolan on 06/08/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import Combine
import shared

class PostListViewModelWrapper : ObservableObject {

    @Published private(set) var state: PostListState
    private(set) var sideEffect: AnyPublisher<NavigationEvent, Never>

    private var wrapped: PostListViewModel

    init(wrapped: PostListViewModel) {
        self.wrapped = wrapped
        self.state = wrapped.container.stateFlow.value as! PostListState
        self.sideEffect = wrapped.container.sideEffectFlow.asPublisher() as AnyPublisher<NavigationEvent, Never>

        (wrapped.container.stateFlow.asPublisher() as AnyPublisher<PostListState, Never>)
                .receive(on: RunLoop.main)
                .assign(to: &$state)
    }

    func onPostClicked(post: PostOverview) {
        wrapped.onPostClicked(post: post)
    }

    deinit {
        wrapped.onCleared()
    }
}

extension PostListViewModel {
    func asStateObject() -> PostListViewModelWrapper {
        return PostListViewModelWrapper(wrapped: self)
    }
}
