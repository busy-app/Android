import ComposeApp
import Foundation

class RootHolder: ObservableObject {
    let lifecycle: LifecycleRegistry
    let componentContext: ComponentContext

    init() {
        lifecycle = LifecycleRegistryKt.LifecycleRegistry()

        componentContext = DefaultComponentContext(lifecycle: lifecycle)

        LifecycleRegistryExtKt.create(lifecycle)
    }

    deinit {
        // Destroy the root component before it is deallocated
        LifecycleRegistryExtKt.destroy(lifecycle)
    }
}
