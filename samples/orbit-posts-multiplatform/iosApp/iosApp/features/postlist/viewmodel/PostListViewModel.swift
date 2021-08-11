//
// Copyright 2021 Mikołaj Leszczyński & Appmattus Limited
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
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
