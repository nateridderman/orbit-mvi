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
    
    @SceneStorage("sceneStorage") var sceneStorage = ""

    @StateObject private var postDetailsViewModel: PostDetailsViewModelWrapper

    var body: some View {
        let state = postDetailsViewModel.state
        Text(state.postOverview.title)
        
        Text(sceneStorage)

        if let state = state as? PostDetailState.Details {
            Text(state.post.body).onTapGesture {
                sceneStorage = "bob"
            }
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
        print("PostDetailsView.create")
        
        let postDetailsViewModel = PostDetailsViewModelWrapper(wrapped: ViewModels().postDetailsViewModel(postOverview: postOverview))

        return PostDetailsView(postDetailsViewModel: postDetailsViewModel)
    }
}
