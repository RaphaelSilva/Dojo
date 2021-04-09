<template>
  <div>
    <button @click="createRoom" :disabled="!CanJoined">Create Room</button>
    <input type="text" v-model="RoomId" :disabled="!CanCreated" />
    <button @click="joinRoom" :disabled="!CanCreated">Join the Room</button>
    <div><video ref="localVideo"></video></div>
    <div v-for="remoteStream in remotesStram" :key="remoteStream.id" >
      <video ref="remote{{remoteStream.id}}"></video>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import Room from "@/core/Room";
import RoomController from "@/core/RoomController";

@Component({
  components: {},
})
export default class PeerVideo extends Vue {
  @Prop() roomController!: RoomController;

  room!: Room;

  RoomId = "";
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

  async createRoom(): Promise<void> {
    const localVideo = this.$refs["localVideo"] as HTMLVideoElement;
    try {
      const stream = await this.roomController.AttachUserMediaTo(
        localVideo
      );
      this.room = await this.roomController.createRoom(stream);
      this.room.localStream = stream;
      this.room.loadTrack(stream);
      this.CanJoined = false;
      this.CanCreated = false;
      this.RoomId = this.room.id;
    } catch (error) {
      console.log(error);
    }
  }

  async joinRoom(): Promise<void> {
    const localVideo = this.$refs["localVideo"] as HTMLVideoElement;
    try {
      const stream = await this.roomController.AttachUserMediaTo(localVideo);
      this.room = await this.roomController.joinRoomById(this.RoomId, stream);
      this.CanJoined = false;
      this.CanCreated = false;
    } catch (error) {
      console.log(error);
    }
  }
}
</script>
<style>
</style>