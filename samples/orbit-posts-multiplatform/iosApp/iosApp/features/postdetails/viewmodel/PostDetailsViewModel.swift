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
