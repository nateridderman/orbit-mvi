//
//  PostDetailsView.swift
//  iosApp
//
//  Created by Matthew Dolan on 07/08/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PostDetailsView: View {
    
    @StateObject private var postDetailsViewModel: PostDetailsViewModelWrapper

    var body: some View {
        let state = postDetailsViewModel.state
        Text(state.postOverview.title)
        
        if let state = state as? PostDetailState.Details {
            Text(state.post.body)
        }
    }
}

/*struct PostDetailsView_Previews: PreviewProvider {
    static var previews: some View {
        PostDetailsView()
    }
}*/

extension PostDetailsView {
    static func create(postOverview: PostOverview) -> some View {
        let postDetailsViewModel = ViewModels().postDetailsViewModel(postOverview: postOverview).asStateObject()

        return PostDetailsView(postDetailsViewModel: postDetailsViewModel)
    }
}
