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
        HStack {
            AsyncImage(
                url: URL(string:postOverview.avatarUrl)!,
                placeholder: { Circle().foregroundColor(Color.tertiarySystemGroupedBackground) },
                image: { Image(uiImage: $0).resizable() }
            ).frame(width: 40, height: 40)
            
            VStack(alignment: .leading) {
                Text(postOverview.title).font(.system(.body))
                Text(postOverview.username).font(.system(.caption))
            }.padding(8)
        }
    }
}

struct PostListItemView_Previews: PreviewProvider {
    static var previews: some View {
        PostListItemView(postOverview: PostOverview(id: 0, avatarUrl: "https://en.wikipedia.org/wiki/Portable_Network_Graphics#/media/File:PNG_transparency_demonstration_1.png", title: "title", username: "username"))
    }
}
