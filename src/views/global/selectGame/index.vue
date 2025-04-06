<template>
  <div class="app-container">
    <el-card>
      <el-form>
        <el-form-item label="选择游戏">
          <el-select :filterable="true" v-model="selectedGame" placeholder="请选择游戏" style="width: 300px">
            <el-option
              v-for="game in gameList"
              :key="game.gameCode"
              :label="game.gameName"
              :value="game.gameCode ?? ''"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref } from "vue"
import { ElMessage } from "element-plus"
import { GameInfo, GameInfoResponse, getGameInfo } from "@/api/game"

const gameList = ref<GameInfo[]>([])
const selectedGame = ref("")

const getGameOption = () => {
  getGameInfo()
    // 指定响应数据的类型
    .then(({ data }: { data: GameInfoResponse }) => {
      gameList.value = data.list
      const savedGame = localStorage.getItem("currentSchema")
      if (savedGame) {
        selectedGame.value = savedGame
      }
    })
    .catch(() => {
      // 这里应该是 gameList.value = []，而不是 tableData.value = []
      gameList.value = []
      ElMessage.error("获取游戏列表失败")
    })
}

getGameOption()

const handleSave = () => {
  if (selectedGame.value) {
    localStorage.setItem("currentSchema", selectedGame.value)
    ElMessage.success("保存成功")
  } else {
    ElMessage.warning("请选择游戏")
  }
}
</script>
