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
    
    @StateObject private var postListViewModel = PostListViewModel()

    var body: some View {
        NavigationView {
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    ForEach(postListViewModel.state.overviews, id: \.self) { overview in
                        
                        NavigationLink(destination: PostDetailsView.create(postOverview: overview)
                        ) {
                            PostListItemView(postOverview: overview)
                                //.frame(minHeight: 28) // 28 + 8 + 8 = 44
                                .padding(EdgeInsets(top: 8, leading: 16, bottom: 8, trailing: 16))
                            
                        }
                    }
                }
            }.navigationBarHidden(true)
        }
    }
}

/*struct PostListView_Previews: PreviewProvider {
    static var previews: some View {
        TextView("")
        //PostListView()
    }
}*/
