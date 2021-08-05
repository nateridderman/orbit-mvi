import SwiftUI
import shared

struct ContentView: View {
	let greet = Greeting().greeting()

	var body: some View {
		PostListItemView(postOverview: PostOverview(id: 0, avatarUrl: "https://upload.wikimedia.org/wikipedia/commons/a/ab/Apple-logo.png", title: "Matthew Dolan", username: "Director"))
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
	ContentView()
	}
}
