//
//  PostDetailsViewModel.swift
//  iosApp
//
//  Created by Matthew Dolan on 07/08/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import Combine
import shared

class PostDetailsViewModelWrapper : ObservableObject {

    @Published private(set) var state: PostDetailState

    private var wrapped: PostDetailsViewModel

    init(wrapped: PostDetailsViewModel) {
        self.wrapped = wrapped
        self.state = wrapped.container.stateFlow.value as! PostDetailState

        (wrapped.container.stateFlow.asPublisher() as AnyPublisher<PostDetailState, Never>)
                .receive(on: RunLoop.main)
                .assign(to: &$state)
    }

    deinit {
        wrapped.onCleared()
    }
}

extension PostDetailsViewModel {
    func asStateObject() -> PostDetailsViewModelWrapper {
        return PostDetailsViewModelWrapper(wrapped: self)
    }
}
