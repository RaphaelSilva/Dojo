import {
  DocumentData,
  DocumentReference,
  DocumentSnapshot
} from "@firebase/firestore-types"
import Peer from "./Peer"

type RecivedPeer<T> = (peer: Peer, data: DocumentSnapshot<T>) => HTMLVideoElement

export interface DataRoom {
  id: string
  peerHost: Peer
  peerClients?: Array<Peer>
  roomRef: DocumentReference<DocumentData>
  localStream: MediaStream
  remotesStream?: Array<MediaStream>
  //offer: RTCSessionDescriptionInit  
}

export default class Room implements DataRoom {
  id: string
  peerHost: Peer
  peerClients?: Array<Peer>
  roomRef: DocumentReference<DocumentData>
  localStream: MediaStream
  remotesStream?: Array<MediaStream>


  constructor(room: DataRoom) {
    this.id = room.id
    this.peerHost = room.peerHost
    this.roomRef = room.roomRef
    this.localStream = room.localStream
    this.remotesStream = room.remotesStream
  }

  unLoadTrack(stream: MediaStream): void {
    stream.getTracks().forEach((track) => { track.stop() })
  }

  RegisterRecivePeer(onRecivedPeer: RecivedPeer<DocumentData>): void {
    this.roomRef.onSnapshot(async (snapshot) => {
      const data = snapshot.data();
      if (!this.peerHost.Connection) throw new Error("Cant find a connection");
      if (!this.peerHost.Connection.currentRemoteDescription && data?.answer) {
        onRecivedPeer(this.peerHost, data.answer)
      }
    });
  }


  close(): void {
    this.peerHost.Connection?.close()
    this.peerClients?.forEach(peer => {
      peer.Connection?.close()
    })
  }

  /**
   * Remove Peer of Room from origin
   */
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