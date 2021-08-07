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

class PostDetailsViewModel: ObservableObject {

    @Published var postOverview: PostOverview

    init(postOverview: PostOverview) {
        self.postOverview = postOverview

        /*state = RoleState(
            header: RoleState.Header(
                logoUrl: experience.logoUrl,
                title: role.title,
                team: role.team,
                period: role.period
            )
        )

        loadDetails()*/
    }
}

/*struct PostDetailsViewModel_Previews: PreviewProvider {
    static var previews: some View {
        PostDetailsViewModel()
    }
}*/
