import { FirebaseFirestore } from "@firebase/firestore-types"
import Room from "@/core/Room"
import Peer from "./Peer"

const configuration = {
  iceServers: [
    {
      urls: ["stun:stun1.l.google.com:19302", "stun:stun2.l.google.com:19302"],
    },
  ],
  iceCandidatePoolSize: 10,
}

export default class RoomController {

  private static ROOMS = "rooms"
  private static ANSWER_CANDIDATES = "answerCandidates"

  private db: FirebaseFirestore

  constructor(db: FirebaseFirestore) {
    this.db = db;
  }

  async getMediaStream(): Promise<MediaStream> {
    const stream = await navigator.mediaDevices.getUserMedia({
      video: true,
      audio: true,
    });
    return stream
  }

  configElementVideo(elVideo: HTMLVideoElement, stream: MediaStream): void {
    elVideo.muted = true;
    elVideo.autoplay = true;
    elVideo.playsInline = true;
    elVideo.srcObject = stream;
  }

  async createRoom(localStream: MediaStream): Promise<Room> {
    const peerConnection = new RTCPeerConnection(configuration);

    const offer = await peerConnection.createOffer({ voiceActivityDetection: false });
    await peerConnection.setLocalDescription(offer)

    const roomRef = await this.db.collection(RoomController.ROOMS).add({
      offer: {
        sdp: offer.sdp,
        type: offer.type
      }
    })

    const peer = new Peer({ id: 'H-' + roomRef.id, Connection: peerConnection });
    peer.Connection.addEventListener("icecandidate", (event) => {
      const answerCandidates = roomRef.collection(RoomController.ANSWER_CANDIDATES);
      return event.candidate && answerCandidates.add(event.candidate.toJSON());
    })

    localStream.getTracks().forEach((track) => {
      peer.Connection.addTrack(track, localStream);
    });

    return new Room({
      id: roomRef.id,
      peerHost: peer,
      roomRef: roomRef,
      localStream: localStream
    });
  }
  
  addRemotePeer(room: Room, peer: Peer): void {    
    room.peerHost.Connection.addEventListener("track", (event) => {
      event.streams[0].getTracks().forEach((track) => {
        peer.Connection.addTrack(track);
      });      
    });
  }

  async joinRoomById(roomId: string, localStream: MediaStream): Promise<Room> {
    const roomRef = await this.db.collection(RoomController.ROOMS).doc(roomId)
    const roomSnapshot = await roomRef.get();
    if (roomSnapshot) {
      const peerConnection = new RTCPeerConnection(configuration)
      const peer = new Peer({ id: 'H-' + roomRef.id, Connection: peerConnection })
      localStream.getTracks().forEach((track) => {
        peer.Connection.addTrack(track, localStream);
      });
      const room = new Room({
        id: roomId,
        roomRef: roomRef,
        peerHost: peer,
        localStream: localStream
      })      
      return room;
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