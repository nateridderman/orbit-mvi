//
//  Navigation.swift
//  iosApp
//
//  Created by Matthew Dolan on 09/08/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import SwiftUI

// Based on https://www.fivestars.blog/articles/programmatic-navigation/
extension NavigationLink where Label == EmptyView {
  public init?<V>(
    item: Binding<V?>,
    destination: @escaping (V) -> Destination
  ) {
    if let value = item.wrappedValue {
      let isActive: Binding<Bool> = Binding(
        get: { item.wrappedValue != nil },
        set: { value in
          // There's shouldn't be a way for SwiftUI to set `true` here.
          if !value {
            item.wrappedValue = nil
          }
        }
      )

      self.init(
        destination: destination(value),
        isActive: isActive,
        label: { EmptyView() }
      )
    } else {
      return nil
    }
  }
}

extension View {
  func navigation<V, Destination: View>(
    item: Binding<V?>,
    destination: @escaping (V) -> Destination
  ) -> some View {
    background(NavigationLink(item: item, destination: destination))
  }
}
