//
//  PostListView.swift
//  iosApp
//
//  Created by Matthew Dolan on 05/08/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import Combine
import shared

struct PostListView: View {

    @StateObject private var postListViewModel = PostListViewModelWrapper(wrapped: ViewModels().postListViewModel())

    @State private var showingNavigation: ContentViewNavigation?

    var body: some View {
        NavigationView {
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    ForEach(postListViewModel.state.overviews, id: \.self) { overview in
                        Button(action: {
                            postListViewModel.onPostClickedPost(post: overview)
                        }) {
                            PostListItemView(postOverview: overview)
                                .padding(EdgeInsets(top: 8, leading: 16, bottom: 8, trailing: 16))
                        }
                    }
                }
            }
            .navigationBarTitle("Orbit Posts Sample")
            .navigationBarHidden(true)
            .navigation(item: $showingNavigation, destination: presentNavigation)
            .onReceive(postListViewModel.sideEffect, perform: { navigationEvent in
                if (navigationEvent is OpenPostNavigationEvent) {
                    showingNavigation = .postDetails(post: (navigationEvent as! OpenPostNavigationEvent).post)
                }

            })
        }
    }

    @ViewBuilder
    func presentNavigation(_ navigation: ContentViewNavigation) -> some View {
      switch navigation {
      case .postDetails(let post):
        PostDetailsView.create(postOverview: post)
      }
    }

    enum ContentViewNavigation {
      case postDetails(post: PostOverview)
    }
}

/*struct PostListView_Previews: PreviewProvider {
    static var previews: some View {
        TextView("")
        //PostListView()
    }
}*/
