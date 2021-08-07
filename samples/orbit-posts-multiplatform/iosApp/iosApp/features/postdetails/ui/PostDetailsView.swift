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
    
    @StateObject var postDetailsViewModel: PostDetailsViewModel

    var body: some View {
        Text(/*@START_MENU_TOKEN@*/"Hello, World!"/*@END_MENU_TOKEN@*/)
    }
}

/*struct PostDetailsView_Previews: PreviewProvider {
    static var previews: some View {
        PostDetailsView()
    }
}*/

extension PostDetailsView {
    static func create(postOverview: PostOverview) -> some View {
        PostDetailsView(postDetailsViewModel: .init(postOverview: postOverview))
    }
}
