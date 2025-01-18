import ComposeApp

import FamilyControls
import ManagedSettings

import SwiftUI

struct FamilyActivityPickerView: View {
    @State var selection = FamilyActivitySelection()
    @State var isPickerPresented: Bool = false

    let onDismiss: () -> Void

    @Environment(\.dismiss) private var dismiss

    @AppStorage(
        UserDefaults.Keys.blockerSettings.rawValue
    )
    var blockerSettings: BlockerSettings = .init()


    var body: some View {
        VStack {
            FamilyActivityPicker(selection: $selection)

            Button("Done") {
                dismiss()
            }
        }
        .background(Color(UIColor.systemGroupedBackground))
        .onAppear {
            selection.applicationTokens = blockerSettings.applicationTokens
            selection.categoryTokens = blockerSettings.categoryTokens
            selection.webDomainTokens = blockerSettings.domainTokens
        }
        .onChange(of: selection) { old, selection in
            blockerSettings.applicationTokens = selection.applicationTokens
            blockerSettings.categoryTokens = selection.categoryTokens
            blockerSettings.domainTokens = selection.webDomainTokens
        }
        .onDisappear {
            onDismiss()
        }
    }
}

