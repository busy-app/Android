import ManagedSettings
import ManagedSettingsUI
import UIKit

class ShieldConfigurationExtension: ShieldConfigurationDataSource {
    func configuration(
        shielding name: String
    ) -> ShieldConfiguration {
        .init(
            backgroundBlurStyle: nil,
            backgroundColor: .background,
            icon: .blocked,
            title: .init(
                text: "Blocked by BUSY",
                color: .label
            ),
            subtitle: .init(
                text:
                    """
                    You can’t use \(name) while BUSY is active. 
                    To unblock: open BUSY app ➔ press ‘STOP’.
                    """,
                color: .label
            ),
            primaryButtonLabel: .init(text: "OK", color: .white),
            primaryButtonBackgroundColor: .buttonBackground,
            secondaryButtonLabel: nil
        )
    }

    override func configuration(
        shielding application: Application
    ) -> ShieldConfiguration {
        configuration(shielding: application.name)
    }

    override func configuration(
        shielding application: Application,
        in category: ActivityCategory
    ) -> ShieldConfiguration {
        configuration(shielding: application.name)
    }

    override func configuration(
        shielding webDomain: WebDomain
    ) -> ShieldConfiguration {
        configuration(shielding: webDomain.name)
    }

    override func configuration(
        shielding webDomain: WebDomain,
        in category: ActivityCategory
    ) -> ShieldConfiguration {
        configuration(shielding: category.name)
    }
}

extension Application {
    var name: String {
        localizedDisplayName ?? "App"
    }
}

extension WebDomain {
    var name: String {
        domain ?? "Domain"
    }
}

extension ActivityCategory {
    var name: String {
        localizedDisplayName ?? "Category"
    }
}
