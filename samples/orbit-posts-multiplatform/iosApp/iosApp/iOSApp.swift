import SwiftUI
import shared

@main
struct iOSApp: App {
    
    init() {
        DependencyInjection().initialiseDependencyInjection { _ in }
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
