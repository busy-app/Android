import Foundation
import ComposeApp

class AudioPlayer: ApiAudioPlayerApi {
    let tickTock = TickTock()

    func play() {
        tickTock.play()
    }
}
