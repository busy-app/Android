import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    private let settings = NSUserDefaultsSettings(delegate: UserDefaults.standard)
    let componentContext: ComponentContext
    let familyControl: FamilyControlApi

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt
            .MainViewController(
                componentContext: componentContext,
                settings: settings,
                familyControl: familyControl
            )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}



