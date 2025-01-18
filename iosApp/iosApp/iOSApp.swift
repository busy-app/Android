import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self)
    var appDelegate: AppDelegate

    @Environment(\.scenePhase)
    var scenePhase: ScenePhase

    var rootHolder: RootHolder {
        appDelegate.rootHolder
    }

    var familyControl: FamilyControlApi {
        appDelegate.familyControl
    }

    var audioPlayer: ApiAudioPlayerApi {
        appDelegate.audioPlayer
    }

    var body: some Scene {
        WindowGroup {
            ComposeView(
                componentContext: rootHolder.componentContext,
                familyControl: familyControl,
                audioPlayer: audioPlayer
            )
            .ignoresSafeArea()
            .onChange(of: scenePhase) { _, newPhase in
                switch newPhase {
                case .background: LifecycleRegistryExtKt.stop(rootHolder.lifecycle)
                case .inactive: LifecycleRegistryExtKt.pause(rootHolder.lifecycle)
                case .active: LifecycleRegistryExtKt.resume(rootHolder.lifecycle)
                @unknown default: break
                }
            }
        }
    }
}
