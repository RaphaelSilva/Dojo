import { FirebaseFirestore } from "@firebase/firestore-types"
import Room from "@/core/Room"

const configuration = {
  iceServers: [
    {
      urls: ["stun:stun1.l.google.com:19302", "stun:stun2.l.google.com:19302"],
    },
  ],
  iceCandidatePoolSize: 10,
}

export default class RoomController {

  private static ROOM_COLLECTION = "rooms"

  private db: FirebaseFirestore

  constructor(db: FirebaseFirestore) {
    this.db = db;
  }

  async AttachUserMediaTo(elVideo: HTMLVideoElement): Promise<MediaStream> {
    const stream = await navigator.mediaDevices.getUserMedia({
      video: true,
      audio: true,
    });
    
    elVideo.muted = true;
    elVideo.autoplay = true;
    elVideo.playsInline = true;

    elVideo.srcObject = stream

    return stream
  }

  async createRoom(localStream: MediaStream): Promise<Room> {
    const peerConnection = new RTCPeerConnection(configuration);

    const offer = await peerConnection.createOffer({ voiceActivityDetection: false });
    await peerConnection.setLocalDescription(offer)

    const roomRef = await this.db.collection(RoomController.ROOM_COLLECTION).add({ offer: offer })

    return new Room({
      id: roomRef.id,
      peerConnection: peerConnection,
      roomRef: roomRef,
      localStream: localStream
    })
  }

  async joinRoomById(roomId: string, localStream: MediaStream): Promise<Room> {
    const roomRef = await this.db.collection(RoomController.ROOM_COLLECTION).doc(roomId)
    const roomSnapshot = await roomRef.get();
    if (roomSnapshot.exists) {
      const peerConnection = new RTCPeerConnection(configuration)
      return new Room({
        id: roomId,
        roomRef: roomRef,
        peerConnection: peerConnection,
        localStream: localStream
      })
    } else {
      throw new Error(`roomId: ${roomId} does not exists!`)
    }
  }

  async hangUp(room: Room, localVideo: HTMLVideoElement, remotesStream: Array<MediaStream>): Promise<void> {
    console.log("HangginUp...")
    console.log("Getting localVideo to stop the tracks")

    room.unLoadTrack(localVideo.srcObject as MediaStream)
      
    console.log("stopping all tracks for remoteStream");
    remotesStream.forEach(remoteStream => {      
      room.unLoadTrack(remoteStream)
    })

    console.log("Close perrConnection");
    room.close()
    room.removeReferences()    
  }

}