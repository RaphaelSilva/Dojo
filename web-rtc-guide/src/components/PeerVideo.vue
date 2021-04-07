<template>
  <div>
    <button @click="createRoom">Create Room</button>
    <div><video ref="localVideo"></video></div>
    <!-- <div v-if="remotesStram != null" v-for="remoteStream in remotesStram" :key="remoteStream.id" >
      <video ref="remote{{remoteStream.id}}"></video>
    </div> -->
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import FirebaseSetting from "@/core/FirebaseSetting";
import Room from "@/core/Room";
import RoomController from "@/core/RoomController";

const fbase = FirebaseSetting.InitializeInstance();

@Component({
  components: {},
})
export default class PeerVideo extends Vue {
  room!: Room;
  roomController!: RoomController;

  created(): void {
    this.roomController = new RoomController(fbase.getStore());
  }

  async createRoom(): Promise<void> {
    const localVideo = this.$refs["localVideo"] as HTMLVideoElement;
    try {
      const stream = await this.roomController.AttachUserMediaTo(localVideo)
      this.room = await this.roomController.createRoom(stream);      
    } catch (error) {
      console.log(error);
    }
  }

  async joinRoom(): Promise<void> {
    const localVideo = this.$refs["localVideo"] as HTMLVideoElement;
    try {
      const stream = await this.roomController.AttachUserMediaTo(localVideo)
      this.room = await this.roomController.joinRoomById("raphael", stream);      
    } catch (error) {
      console.log(error);
    }
  }
}
</script>
<style>
</style>