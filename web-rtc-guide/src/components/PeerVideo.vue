<template>
  <div>
    <button @click="createRoom" :disabled="!CanJoined">Create Room</button>
    <input type="text" v-model="RoomId" :disabled="!CanCreated" />
    <button @click="joinRoom" :disabled="!CanCreated">Join the Room</button>
    <div class="local">
      <video ref="localVideo"></video>
    </div>
    <div class="remote">
      <video ref="remoteVideo"></video>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.local {
  color: aqua;
  padding: 5;
}

.remote {
  padding: 5;
  color: aqua;
}
</style> 

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import Room from "@/core/Room";
import RoomController from "@/core/RoomController";
import Peer from "@/core/Peer";

@Component({
  components: {},
})
export default class PeerVideo extends Vue {
  @Prop() roomController!: RoomController;

  room!: Room;
  RoomId = "";
  LocalPeer!: Peer;
  RemotePeer!: Peer;
  CanCreated = true;
  CanJoined = true;

  created(): void {
    if (this.roomController == null) {
      throw new Error("RoomController can not be null");
    }
    if (!(this.roomController instanceof RoomController)) {
      throw new Error("RoomController Incorrect instance");
    }
  }

  mounted(): void {
    //this.joinRoom();
  }

  async createRoom(): Promise<void> {
    const localVideo = this.$refs["localVideo"] as HTMLVideoElement;
    const key = window.localStorage.getItem("classId");
    console.log(key);
    try {
      const stream = await this.roomController.getMediaStream();
      this.roomController.configElementVideo(localVideo, stream);
      this.room = await this.roomController.createRoom(stream);
      this.room.RegisterRecivePeer((peer, answer) => {
        peer.setRemote(answer);
        return this.$refs[`remote${peer.id}`] as HTMLVideoElement;
      });
      this.CanJoined = false;
      this.CanCreated = false;
      this.RoomId = this.room.id;
      // When hang off clean this variable
      window.localStorage.setItem("classId", this.room.id);
    } catch (error) {
      console.log(error);
    }
  }

  async joinRoom(): Promise<void> {
    const localVideo = this.$refs["localVideo"] as HTMLVideoElement;
    try {
      const stream = await this.roomController.getMediaStream();
      this.roomController.configElementVideo(localVideo, stream);
      this.room = await this.roomController.joinRoomById(this.RoomId, stream);
      this.room.RegisterRecivePeer((peer, answer) => {
        peer.setRemote(answer); 
        return this.$refs[`RemoteVideo`] as HTMLVideoElement;
      });
      this.CanJoined = false;
      this.CanCreated = false;
      // Fix Change to Start...
      //this.room.RefreshPeer();
    } catch (error) {
      console.log(error);
    }
  }
}
</script>
<style>
</style>