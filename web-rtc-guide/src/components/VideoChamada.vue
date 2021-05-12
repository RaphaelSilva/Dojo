<template>
  <div>
    <p>{{ roomId }}</p>
    <div ref="buttons">
      <button
        class="mdc-button mdc-button--raised"
        ref="cameraBtn"
        @click="openUserMedia"
      >
        <i class="material-icons mdc-button__icon" aria-hidden="true"
          >perm_camera_mic</i
        >
        <span class="mdc-button__label">Open camera & microphone</span>
      </button>
      <button
        class="mdc-button mdc-button--raised"
        disabled
        ref="createBtn"
        @click="createRoom"
      >
        <i class="material-icons mdc-button__icon" aria-hidden="true"
          >group_add</i
        >
        <span class="mdc-button__label">Create room</span>
      </button>
      <button
        class="mdc-button mdc-button--raised"
        disabled
        ref="joinBtn"
        @click="joinRoom"
      >
        <i class="material-icons mdc-button__icon" aria-hidden="true">group</i>
        <span class="mdc-button__label">Join room</span>
      </button>
      <button
        class="mdc-button mdc-button--raised"
        disabled
        ref="hangupBtn"
        @click="hangUp"
      >
        <i class="material-icons mdc-button__icon" aria-hidden="true">close</i>
        <span class="mdc-button__label">Hangup</span>
      </button>
    </div>
    <div>
      <span ref="currentRoom"></span>
    </div>
    <div id="videos">
      <video
        ref="localVideo"
        id="localVideo"
        muted
        autoplay
        playsinline
      ></video>
      <video ref="remoteVideo" autoplay playsinline></video>
    </div>
    <div
      class="mdc-dialog"
      ref="room-dialog"
      role="alertdialog"
      aria-modal="true"
      aria-labelledby="my-dialog-title"
      aria-describedby="my-dialog-content"
    >
      <div class="mdc-dialog__container">
        <div class="mdc-dialog__surface">
          <h2 class="mdc-dialog__title" ref="my-dialog-title">Join room</h2>
          <div class="mdc-dialog__content" ref="my-dialog-content">
            Enter ID for room to join:
            <div class="mdc-text-field">
              <input
                type="text"
                id="my-text-field"
                class="mdc-text-field__input"
                v-model="roomId"
              />
              <label class="mdc-floating-label" for="my-text-field"
                >Room ID</label
              >
              <div class="mdc-line-ripple"></div>
            </div>
          </div>
          <footer class="mdc-dialog__actions">
            <button
              type="button"
              class="mdc-button mdc-dialog__button"
              data-mdc-dialog-action="no"
            >
              <span class="mdc-button__label">Cancel</span>
            </button>
            <button
              ref="confirmJoinBtn"
              type="button"
              class="mdc-button mdc-dialog__button"
              data-mdc-dialog-action="yes"
            >
              <span class="mdc-button__label">Join</span>
            </button>
          </footer>
        </div>
      </div>
      <div class="mdc-dialog__scrim"></div>
    </div>
  </div>
</template>

<script lang="ts">
import { MDCDialog } from "@material/dialog";
import { Component, Vue } from "vue-property-decorator";
import FirebaseSetting from "@/core/FirebaseSetting";
import { DocumentData, DocumentReference } from "@firebase/firestore-types";

const configuration = {
  iceServers: [
    {
      urls: ["stun:stun1.l.google.com:19302", "stun:stun2.l.google.com:19302"],
    },
  ],
  iceCandidatePoolSize: 10,
};

@Component
export default class VideoChamada extends Vue {
  roomDialog!: MDCDialog;
  peerConnection!: RTCPeerConnection;
  localStream!: MediaStream;
  remoteStream!: MediaStream;

  private roomId = "";

  remoteVideo!: HTMLVideoElement;
  localVideo!: HTMLVideoElement;
  cameraBtn!: HTMLButtonElement;
  joinBtn!: HTMLButtonElement;
  createBtn!: HTMLButtonElement;
  hangupBtn!: HTMLButtonElement;

  confirmJoinBtn!: HTMLButtonElement;

  currentRoom!: HTMLSpanElement;

  private db = FirebaseSetting.Instance().Initialize().getStore();

  mounted(): void {
    this.roomDialog = new MDCDialog(this.$refs["room-dialog"] as Element);

    this.remoteVideo = this.$refs["remoteVideo"] as HTMLVideoElement;
    this.localVideo = this.$refs["localVideo"] as HTMLVideoElement;
    this.cameraBtn = this.$refs["cameraBtn"] as HTMLButtonElement;
    this.joinBtn = this.$refs["joinBtn"] as HTMLButtonElement;
    this.createBtn = this.$refs["createBtn"] as HTMLButtonElement;
    this.hangupBtn = this.$refs["hangupBtn"] as HTMLButtonElement;
    this.confirmJoinBtn = this.$refs["confirmJoinBtn"] as HTMLButtonElement;

    this.currentRoom = this.$refs["currentRoom"] as HTMLSpanElement;
  }

  async createRoom(): Promise<void> {
    this.createBtn.disabled = true;
    this.joinBtn.disabled = true;
    console.log("Create PeerConnection with configuration: ", configuration);
    this.peerConnection = new RTCPeerConnection(configuration);
    this.registerPeerConnectionListeners();

    // Add code for creating a room here
    const offer = await this.peerConnection.createOffer({voiceActivityDetection: false});
    await this.peerConnection.setLocalDescription(offer);

    const roomWithOffer = {
      offer: {
        type: offer.type,
        sdp: offer.sdp,
      },
    };
    const roomRef = await this.db.collection("rooms").add(roomWithOffer); 
    this.roomId = roomRef.id;
    this.currentRoom.innerText = `Current room is ${this.roomId} - You are the caller!`;

    // Code for creating room above
    roomRef.onSnapshot(async (snapshot) => {
      console.log("Got updated room:", snapshot.data());
      const data = snapshot.data();
      if (data) {
        if (!this.peerConnection.currentRemoteDescription && data.answer) {
          console.log("Set remote description: ", data.answer);
          const answer = new RTCSessionDescription(data.answer);
          await this.peerConnection.setRemoteDescription(answer);
        }
      }
    });

    this.localStream.getTracks().forEach((track) => {
      this.peerConnection.addTrack(track, this.localStream);
    });

    // Code for collecting ICE candidates below
    const callerCandidatesCollection = roomRef.collection("callerCandidates");

    this.peerConnection.addEventListener("icecandidate", (event) => {
      if (!event.candidate) {
        console.log("Got final candidate!");
        return;
      }
      console.log("Got candidate: ", event.candidate);
      callerCandidatesCollection.add(event.candidate.toJSON());
    });

    // Code for collecting ICE candidates above

    this.peerConnection.addEventListener("track", (event) => {
      console.log("Got remote track:", event.streams[0]);
      event.streams[0].getTracks().forEach((track) => {
        console.log("Add a track to the remoteStream:", track);
        this.remoteStream.addTrack(track);
      });
    });

    roomRef.onSnapshot(async (snapshot) => {
      const data = snapshot.data();
      if (
        !this.peerConnection.currentRemoteDescription &&
        data &&
        data.answer
      ) {
        console.log("Got remote description: ", data.answer);
        const rtcSessionDescription = new RTCSessionDescription(data.answer);
        await this.peerConnection.setRemoteDescription(rtcSessionDescription);
      }
    });
    // Listening for remote session description above

    // Listen for remote ICE candidates below
    roomRef.collection("calleeCandidates").onSnapshot((snapshot) => {
      snapshot.docChanges().forEach(async (change) => {
        if (change.type === "added") {
          let data = change.doc.data();
          console.log(`Got new remote ICE candidate: ${JSON.stringify(data)}`);
          await this.peerConnection.addIceCandidate(new RTCIceCandidate(data));
        }
      });
    });
  }

  registerPeerConnectionListeners(): void {
    this.peerConnection.addEventListener("icegatheringstatechange", () => {
      console.log(
        `ICE gathering state changed: ${this.peerConnection.iceGatheringState}`
      );
    });

    this.peerConnection.addEventListener("connectionstatechange", () => {
      console.log(
        `Connection state change: ${this.peerConnection.connectionState}`
      );
    });

    this.peerConnection.addEventListener("signalingstatechange", () => {
      console.log(
        `Signaling state change: ${this.peerConnection.signalingState}`
      );
    });

    this.peerConnection.addEventListener("iceconnectionstatechange ", () => {
      console.log(
        `ICE connection state change: ${this.peerConnection.iceConnectionState}`
      );
    });
  }

  async openUserMedia(): Promise<void> {
    const stream = await navigator.mediaDevices.getUserMedia({
      video: true,
      audio: true,
    });
    this.localVideo.srcObject = stream;
    this.localStream = stream;
    this.remoteStream = new MediaStream();
    this.remoteVideo.srcObject = this.remoteStream;

    this.cameraBtn.disabled = true;
    this.joinBtn.disabled = false;
    this.createBtn.disabled = false;
    this.hangupBtn.disabled = false;
  }

  async confirmJoin(): Promise<void> {
    console.log("Join room: ", this.roomId);
    this.currentRoom.innerText = `Current room is ${this.roomId} - You are the callee!`;
    await this.joinRoomById(this.roomId);
  }

  joinRoom(): void {
    this.createBtn.disabled = true;
    this.joinBtn.disabled = true;

    this.confirmJoinBtn.addEventListener("click", this.confirmJoin, {
      once: true,
    });
    this.roomDialog.open();
  }

  peerConnectionTrack(event: RTCTrackEvent): void {
    console.log("Got remote track:", event.streams[0]);
    event.streams[0].getTracks().forEach((track: MediaStreamTrack) => {
      console.log("Add a track to the remoteStream:", track);
      this.remoteStream.addTrack(track);
    });
  }

  async joinRoomById(roomId: string): Promise<void> {
    const roomRef = this.db.collection("rooms").doc(`${roomId}`);
    const roomSnapshot = await roomRef.get();
    console.log("Got room:", roomSnapshot.exists);

    if (roomSnapshot.exists) {
      console.log("Create PeerConnection with configuration: ", configuration);
      this.peerConnection = new RTCPeerConnection(configuration);
      this.registerPeerConnectionListeners();

      this.localStream.getTracks().forEach((track) => {
        this.peerConnection.addTrack(track, this.localStream);
      });

      // Code for collecting ICE candidates below
      const calleeCandidatesCollection = roomRef.collection("calleeCandidates");
      this.peerConnection.addEventListener("icecandidate", (event) => {
        if (!event.candidate) {
          console.log("Got final candidate!");
          return;
        }
        console.log("Got candidate: ", event.candidate);
        calleeCandidatesCollection.add(event.candidate.toJSON());
      });

      // Code for collecting ICE candidates above

      this.peerConnection.addEventListener("track", this.peerConnectionTrack);

      // Code for creating SDP answer below
      const dataOffer = roomSnapshot.data();
      if (dataOffer != null) {
        const offer = dataOffer.offer;
        console.log("Got offer:", offer);
        await this.peerConnection.setRemoteDescription(
          new RTCSessionDescription(offer)
        );
      }
      const answer = await this.peerConnection.createAnswer();
      console.log("Created answer:", answer);
      await this.peerConnection.setLocalDescription(answer);

      const roomWithAnswer = {
        answer: {
          type: answer.type,
          sdp: answer.sdp,
        },
      };
      await roomRef.update(roomWithAnswer);
      // Code for creating SDP answer above

      // Listening for remote ICE candidates below
      roomRef.collection("callerCandidates").onSnapshot((snapshot) => {
        snapshot.docChanges().forEach(async (change) => {
          if (change.type === "added") {
            let data = change.doc.data();
            console.log(
              `Got new remote ICE candidate: ${JSON.stringify(data)}`
            );
            await this.peerConnection.addIceCandidate(
              new RTCIceCandidate(data)
            );
          }
        });
      });
    }
  }

  async collectIceCandidates(
    roomRef: DocumentReference<DocumentData>,
    peerConnection: RTCPeerConnection,
    localName: string,
    remoteName: string
  ): Promise<void> {
    const candidatesCollection = roomRef.collection(localName);

    peerConnection.addEventListener("icecandidate", (event) => {
      if (event.candidate) {
        const json = event.candidate.toJSON();
        candidatesCollection.add(json);
      }
    });

    roomRef.collection(remoteName).onSnapshot((snapshot) => {
      snapshot.docChanges().forEach((change) => {
        if (change.type === "added") {
          const candidate = new RTCIceCandidate(change.doc.data());
          peerConnection.addIceCandidate(candidate);
        }
      });
    });
  }

  async hangUp(): Promise<void> {    
    console.log("HangginUp...");
    console.log("Getting localVideo to stop the tracks");
    const tracks = (this.localVideo.srcObject as MediaStream).getTracks();
    tracks.forEach((track) => {
      track.stop();
    });
    console.log("stopping all tracks for remoteStream");
    if (this.remoteStream) {
      this.remoteStream.getTracks().forEach((track) => track.stop());
    }
    console.log("Close perrConnection");
    if (this.peerConnection) {
      this.peerConnection.close();
    }
    console.log("Return button states and disable src of video element");
    this.localVideo.srcObject = null;
    this.remoteVideo.srcObject = null;
    this.cameraBtn.disabled = false;
    this.joinBtn.disabled = true;
    this.createBtn.disabled = true;
    this.hangupBtn.disabled = true;
    this.currentRoom.innerText = "";

    // Delete room on hangup
    if (this.roomId) {
      console.log("Delete room to database");
      const roomRef = this.db.collection("rooms").doc(this.roomId);
      const calleeCandidates = await roomRef
        .collection("calleeCandidates")
        .get();
      calleeCandidates.forEach(async (candidate) => {
        // add .ref.
        var str = `Candidate calleeCandidates ${candidate.data}!`;        
        await candidate.ref.delete();
        console.log(`${str} deleted!`);        
      });
      const callerCandidates = await roomRef
        .collection("callerCandidates")
        .get();
      callerCandidates.forEach(async (candidate) => {
        // add .ref.        
        var str = `Candidate callerCandidates ${candidate.data}!`;        
        await candidate.ref.delete();
        console.log(`${str} deleted!`);        
      });
      await roomRef.delete();
    }

    document.location.reload(true);
  }
}
</script>

