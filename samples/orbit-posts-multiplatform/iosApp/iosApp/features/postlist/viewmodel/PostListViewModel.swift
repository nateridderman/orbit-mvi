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

class PostListViewModel: ObservableObject {
    
    @Published var state: PostListState = PostListState(overviews: [])

    private let repository = PostDataRepository(networkDataSource: PostNetworkDataSource(client: HttpClientFactoryKt.httpClientFactory()),
                                        avatarUrlGenerator: AvatarUrlGenerator())
    
    init() {
        getOverviews().map { (overviews) -> PostListState in
            PostListState(overviews: overviews)
        }
        .replaceError(with: PostListState(overviews: []))
        .assign(to: &$state)
    }
    
    private func getOverviews() -> AnyPublisher<[PostOverview], Error> {
        Deferred {
            Future<[PostOverview], Error> { promise in
                self.repository.getOverviews { (postOverviews: [PostOverview]?, error: Error?) in
                    if let error = error {
                        promise(.failure(error))
                    } else {
                        promise(.success(postOverviews!))
                    }
                }
            }
        }.eraseToAnyPublisher()
    }
}
