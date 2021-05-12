import { DocumentData } from "@firebase/firestore-types";
import Room from "./Room";

export interface PeerData {
  id: string
  Connection: RTCPeerConnection
}

export default class Peer implements PeerData {  
  id: string
  Connection: RTCPeerConnection;

  constructor(peer: PeerData) {
    this.id = peer.id
    this.Connection = peer.Connection;
  }

  Enter(room: Room): Peer{
    room.peerClients?.includes(this);
    return this;
  }

  setRemote(answer: DocumentData): void{
    this.Connection.setRemoteDescription(answer);
  }

  Leave(room: Room): void{
    if(room.peerHost.id == this.id){
        room.close();
    }else{
      room.peerClients = room.peerClients?.filter(peer => {
        if(peer.id != this.id){
          this.Connection?.close();
          return true;
        } return false;
      })      
    }
  }

  
}