import ComposeApp
import FamilyControls
import ManagedSettings

class FamilyControl: FamilyControlApi {
    private let managedSettingsStore: ManagedSettingsStore = .init()

    var authorizationCenter: AuthorizationCenter { .shared }

    func isAuthorized() -> Bool {
        authorizationCenter.authorizationStatus == .approved
    }

    func familyControlsAuthorize(
        onAuthorized: @escaping () -> Void,
        onDenied: @escaping () -> Void
    ) {
        Task {
            do {
                try await authorizationCenter.requestAuthorization(for: .individual)
                onAuthorized()
            } catch {
                print("authorization error: \(error)")
                onDenied()
            }
        }
    }

    func enableFamilyControls() {
        managedSettingsStore.shield.applicationCategories = .all()
        managedSettingsStore.shield.webDomainCategories = .all()
    }

    func disableFamilyControls() {
        managedSettingsStore.shield.applicationCategories = nil
        managedSettingsStore.shield.webDomainCategories = nil
    }
}
