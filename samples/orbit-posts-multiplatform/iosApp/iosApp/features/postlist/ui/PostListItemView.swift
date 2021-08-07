//
//  PostListItemView.swift
//  iosApp
//
//  Created by Matthew Dolan on 04/08/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PostListItemView: View {
    var postOverview: PostOverview

    var body: some View {
        HStack(alignment: .center) {
            AsyncImage(
                url: URL(string:postOverview.avatarUrl)!,
                placeholder: { Circle().foregroundColor(Color.tertiarySystemGroupedBackground) },
                image: { Image(uiImage: $0).resizable() }
            ).frame(width: 32, height: 32)
            
            VStack(alignment: .leading) {
                Text(postOverview.username).font(.system(.caption))
                Text(postOverview.title).font(.system(.body))
            }.padding(8)
            
            Spacer()
        }
    }
}

struct PostListItemView_Previews: PreviewProvider {
    static var previews: some View {
        PostListItemView(postOverview: PostOverview(id: 0, avatarUrl: "https://upload.wikimedia.org/wikipedia/commons/a/ab/Apple-logo.png", title: "title", username: "username"))
    }
}
