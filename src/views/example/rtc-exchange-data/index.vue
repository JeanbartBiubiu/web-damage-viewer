<template>
  <div>
    <h2>WebRTC DataChannel（Vue版）</h2>
    <button @click="startConnection">创建连接</button>
    <button @click="setRemoteSDP">设置远端SDP</button>
    <button @click="sendMessage">发送消息</button>

    <h3>本地SDP</h3>
    <textarea v-model="localSDP" rows="10" cols="60" readonly />

    <h3>远端SDP</h3>
    <textarea v-model="remoteSDP" rows="10" cols="60" />

    <h3>日志</h3>
    <pre>{{ log.join("\n") }}</pre>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue"

const localSDP = ref("")
const remoteSDP = ref("")
const log = ref<string[]>([])

let pc: RTCPeerConnection | null = null
let dataChannel: RTCDataChannel | null = null

function appendLog(msg: string) {
  log.value.push(msg)
}

async function startConnection() {
  pc = new RTCPeerConnection({
    iceServers: [{ urls: "stun:stun.l.google.com:19302" }]
  })

  pc.onicecandidate = (event) => {
    if (!event.candidate) {
      localSDP.value = JSON.stringify(pc?.localDescription)
    }
  }

  pc.onconnectionstatechange = () => {
    appendLog("连接状态：" + pc?.connectionState)
  }

  pc.ondatachannel = (event) => {
    const receiveChannel = event.channel
    receiveChannel.onmessage = (e) => {
      appendLog("收到消息: " + e.data)
    }
    receiveChannel.onopen = () => appendLog("接收通道打开")
    receiveChannel.onclose = () => appendLog("接收通道关闭")
  }

  dataChannel = pc.createDataChannel("chat")
  dataChannel.onopen = () => appendLog("发送通道打开")
  dataChannel.onclose = () => appendLog("发送通道关闭")

  const offer = await pc.createOffer()
  await pc.setLocalDescription(offer)
}

async function setRemoteSDP() {
  if (!pc) {
    appendLog("请先创建连接")
    return
  }

  try {
    const sdp = JSON.parse(remoteSDP.value)
    await pc.setRemoteDescription(sdp)

    if (sdp.type === "offer") {
      const answer = await pc.createAnswer()
      await pc.setLocalDescription(answer)

      // 等待 ICE 收集完成
      pc.onicecandidate = (event) => {
        if (!event.candidate) {
          localSDP.value = JSON.stringify(pc?.localDescription)
          appendLog("已生成应答SDP，请复制给发起方")
        }
      }
    }
  } catch (err) {
    appendLog("设置远端SDP失败: " + err)
  }
}

function sendMessage() {
  const msg = prompt("输入要发送的消息:")
  if (dataChannel && dataChannel.readyState === "open") {
    dataChannel.send(msg === null ? "出错拉" : msg)
    appendLog("发送消息: " + msg)
  } else {
    appendLog("通道未连接")
  }
}
</script>

<style scoped>
textarea {
  width: 100%;
  margin-bottom: 1em;
}
pre {
  background: #f0f0f0;
  padding: 0.5em;
  white-space: pre-wrap;
}
</style>
