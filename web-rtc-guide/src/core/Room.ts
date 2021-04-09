import {
  DocumentData,
  DocumentReference
} from "@firebase/firestore-types"

export interface DataRoom {
  id: string
  peerConnection: RTCPeerConnection
  roomRef: DocumentReference<DocumentData>
  localStream: MediaStream
  remotesStream?: Array<MediaStream>
  //offer: RTCSessionDescriptionInit  
}

export default class Room implements DataRoom {
  id: string
  //offer: RTCSessionDescriptionInit;
  peerConnection: RTCPeerConnection
  roomRef: DocumentReference<DocumentData>
  localStream: MediaStream
  remotesStream?: Array<MediaStream>

  constructor(room: DataRoom) {
    this.id = room.id
    //this.offer = room.offer
    this.peerConnection = room.peerConnection
    this.roomRef = room.roomRef
    this.localStream = room.localStream
    this.remotesStream = room.remotesStream
  }

  loadTrack(stream: MediaStream): void {
    stream.getTracks().forEach((track) => {
      this.peerConnection.addTrack(track, stream);
    });
  }

  unLoadTrack(stream: MediaStream): void {
    stream.getTracks().forEach((track) => { track.stop() })
  }

  close(): void {
    this.peerConnection.close()
  }

  async removeReferences(): Promise<void> {
    console.log("Delete room to database");
    const calleeCandidates = await this.roomRef.collection("calleeCandidates").get();
    calleeCandidates.forEach(async (candidate) => {
      const str = `Candidate calleeCandidates ${candidate.data}!`;
      await candidate.ref.delete();
      console.log(`${str} deleted!`);
    });
    const callerCandidates = await this.roomRef.collection("callerCandidates").get();
    callerCandidates.forEach(async (candidate) => {
      const str = `Candidate callerCandidates ${candidate.data}!`;
      await candidate.ref.delete();
      console.log(`${str} deleted!`);
    })
    await this.roomRef.delete();
  }



}