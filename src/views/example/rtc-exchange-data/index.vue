<template>
  <div>
    <h2>WebRTC Chat + 房间匹配</h2>

    <label>
      房间号：
      <input v-model="roomId" placeholder="输入房间ID" />
    </label>
    <br />
    <button @click="joinRoom">加入房间</button>
    <button @click="sendMessage">发送消息</button>

    <h3>日志</h3>
    <pre>{{ log.join("\n") }}</pre>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue"

const roomId = ref("")
const log = ref<string[]>([])
let ws: WebSocket
let pc: RTCPeerConnection | null = null
let dataChannel: RTCDataChannel
let myId = ""
let peerId = ""

function appendLog(msg: string) {
  log.value.push(`[${new Date().toLocaleTimeString()}] ${msg}`)
}

function sendWS(obj: any) {
  if (ws && ws.readyState === WebSocket.OPEN) {
    ws.send(JSON.stringify(obj))
  }
}

async function startConnection(isCaller: boolean) {
  try {
    pc = new RTCPeerConnection({
      iceServers: [
        { urls: "stun:stun.l.google.com:19302" },
        { urls: "stun:stun1.l.google.com:19302" },
        { urls: "stun:stun2.l.google.com:19302" }
      ]
    })

    pc.onicecandidate = (event) => {
      if (event.candidate && peerId) {
        appendLog(`发送ICE候选: ${event.candidate.candidate}`)
        sendWS({
          type: "signal",
          to: peerId,
          data: {
            type: "candidate",
            candidate: event.candidate.candidate,
            sdpMid: event.candidate.sdpMid,
            sdpMLineIndex: event.candidate.sdpMLineIndex
          }
        })
      } else {
        appendLog("ICE候选收集完成")
      }
    }

    pc.oniceconnectionstatechange = () => {
      appendLog(`ICE连接状态: ${pc?.iceConnectionState}`)
    }

    pc.ondatachannel = (event) => {
      const receive = event.channel
      receive.onopen = () => {
        appendLog("接收通道打开")
        dataChannel = receive // 保存接收通道的引用
      }
      receive.onclose = () => appendLog("接收通道关闭")
      receive.onmessage = (e) => appendLog(`收到消息: ${e.data}`)
    }

    pc.onconnectionstatechange = () => {
      appendLog(`连接状态: ${pc?.connectionState}`)
      if (pc?.connectionState === "connected") {
        appendLog("WebRTC连接已建立")
      }
    }

    if (isCaller) {
      dataChannel = pc.createDataChannel("chat")
      dataChannel.onopen = () => {
        appendLog("发送通道打开")
      }
      dataChannel.onclose = () => appendLog("发送通道关闭")
      dataChannel.onerror = (error) => appendLog(`数据通道错误: ${error}`)

      const offer = await pc.createOffer({
        offerToReceiveAudio: false,
        offerToReceiveVideo: false
      })
      await pc.setLocalDescription(offer)
      appendLog("创建并发送offer")
      sendWS({
        type: "signal",
        to: peerId,
        data: pc.localDescription
      })
    }
  } catch (error) {
    appendLog(`创建连接失败: ${error}`)
  }
}

function joinRoom() {
  if (!roomId.value) {
    appendLog("请输入房间号")
    return
  }

  try {
    ws = new WebSocket("ws://localhost:3333/api/ws")

    ws.onopen = () => {
      appendLog("WebSocket 已连接")
    }

    ws.onmessage = async (event) => {
      try {
        const msg = JSON.parse(event.data)
        appendLog(`收到消息: ${JSON.stringify(msg)}`)

        let from = ""
        let data = null
        switch (msg.type) {
          case "sessionId":
            myId = msg.sessionId
            appendLog(`你的ID: ${myId}`)
            sendWS({ type: "join", roomId: roomId.value })
            break

          case "peer-joined":
            peerId = msg.peerId
            appendLog(`发现新用户: ${peerId}`)
            await startConnection(true) // 我是发起方
            break

          case "signal":
            from = msg.from
            data = msg.data
            if (!peerId) peerId = from

            if (data.type === "offer") {
              appendLog("收到offer")
              await startConnection(false)
              await pc?.setRemoteDescription(new RTCSessionDescription(data))
              const answer = await pc?.createAnswer()
              await pc?.setLocalDescription(answer!)
              appendLog("发送answer")
              sendWS({ type: "signal", to: peerId, data: pc?.localDescription })
            } else if (data.type === "answer") {
              appendLog("收到answer")
              await pc?.setRemoteDescription(new RTCSessionDescription(data))
            } else if (data.candidate) {
              appendLog(`收到ICE候选: ${data.candidate}`)
              try {
                const iceCandidate = new RTCIceCandidate({
                  candidate: data.candidate,
                  sdpMid: data.sdpMid,
                  sdpMLineIndex: data.sdpMLineIndex
                })
                await pc?.addIceCandidate(iceCandidate)
                appendLog("成功添加ICE候选")
              } catch (error) {
                appendLog(`添加ICE候选失败: ${error}`)
              }
            }
            break

          case "peer-left":
            appendLog("对方离开房间")
            peerId = ""
            if (pc) {
              pc.close()
              pc = null
            }
            break
        }
      } catch (error) {
        appendLog(`处理消息失败: ${error}`)
      }
    }

    ws.onerror = (error) => {
      appendLog(`WebSocket错误: ${error}`)
    }

    ws.onclose = () => {
      appendLog("WebSocket 已关闭")
      if (pc) {
        pc.close()
        pc = null
      }
    }
  } catch (error) {
    appendLog(`创建WebSocket连接失败: ${error}`)
  }
}

function sendMessage() {
  if (!dataChannel || dataChannel.readyState !== "open") {
    appendLog("通道未连接，无法发送消息")
    return
  }

  const msg = prompt("输入消息：")
  // todo webrtc 消息有大小限制，需要自己分片
  if (msg) {
    try {
      dataChannel.send(msg)
      appendLog(`发送消息: ${msg}`)
    } catch (error) {
      appendLog(`发送消息失败: ${error}`)
    }
  }
}
</script>

<style scoped>
input {
  margin-bottom: 10px;
  display: block;
}
pre {
  background: #f5f5f5;
  padding: 0.5em;
  white-space: pre-wrap;
}
</style>
