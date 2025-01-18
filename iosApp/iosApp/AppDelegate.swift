import Foundation
import UIKit
import ComposeApp

class AppDelegate: NSObject, UIApplicationDelegate {
    let rootHolder: RootHolder = RootHolder()

    let familyControl: FamilyControlApi = FamilyControl()

    let audioPlayer: ApiAudioPlayerApi = AudioPlayer()

    func applicationWillTerminate(_ application: UIApplication) {
        familyControl.disableFamilyControls()
    }
}
