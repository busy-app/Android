import Foundation

final class UserDefaultsStorage {
    public static let shared: UserDefaultsStorage = .init()

    @UserDefault(key: .blockerSettings, defaultValue: .init())
    var blockerSettings: BlockerSettings

    @UserDefault(key: .metronome, defaultValue: false)
    var metronome: Bool
}

@propertyWrapper
public struct UserDefault<T> {
    var getter: () -> T
    var setter: (T) -> Void

    public var wrappedValue: T {
        get { getter() }
        set { setter(newValue) }
    }

    init(key: UserDefaults.Keys, defaultValue: T) {
        getter = {
            UserDefaults.standard.object(forKey: key.rawValue) as? T
                ?? defaultValue
        }
        setter = { newValue in
            UserDefaults.standard.set(newValue, forKey: key.rawValue)
        }
    }

    init(key: UserDefaults.Keys, defaultValue: T) where T: RawRepresentable {
        getter = {
            guard
                let value = UserDefaults.standard.object(forKey: key.rawValue)
                    as? T.RawValue
            else {
                return defaultValue
            }
            return T(rawValue: value) ?? defaultValue
        }
        setter = { newValue in
            UserDefaults.standard.set(newValue.rawValue, forKey: key.rawValue)
        }
    }
}

public extension UserDefaults {
    enum Keys: String, CaseIterable {
        case blockerSettings = "blockerSettings"
        case metronome = "metronome"
    }
}
