<template>
  <div id="app">
    <img alt="Vue logo" src="./assets/logo.png" />
    <PeerVideo :roomController="roomController" />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import PeerVideo from "@/components/PeerVideo.vue";
import FirebaseSetting from "@/core/FirebaseSetting";
import RoomController from "@/core/RoomController";
const fbase = FirebaseSetting.InitializeInstance();

@Component({
  components: {
    PeerVideo,
  },
})
export default class App extends Vue {
  roomController = new RoomController(fbase.getStore());

}
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
