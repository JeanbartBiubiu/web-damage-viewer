<script lang="ts" setup>
import { ref, onMounted, reactive } from "vue"
import * as echarts from "echarts"
import {AttackTime, IndivExample} from "@/utils/chartExapmle"
import 'echarts-gl';
import { type FormInstance, type FormRules, ElMessage, ElMessageBox } from "element-plus"

const chartContainer = ref<HTMLElement | null>(null)

const IndivExampleA = ref<FormInstance | null>(null)
const IndivExampleB = ref<FormInstance | null>(null)
// 使用 reactive 创建响应式对象
const aObj = reactive({
  Attk: 350,
  HuJia: 100,
  Speed: 2.0,
  Chuantou: 30,
  ChuantouBaifenbi: 0.3,
  Baoji: 1
})

const bObj = reactive({
  Attk: 350,
  HuJia: 211,
  Speed: 1.3,
  Chuantou: 0,
  ChuantouBaifenbi: 0,
  Baoji: 0
})
onMounted(() => {
  updateChart()
})

function updateChart(){
  console.log(chartContainer.value)
  if (chartContainer.value) {
    let innerAObj = new IndivExample(aObj.Attk,aObj.Speed,aObj.HuJia,aObj.Chuantou,aObj.ChuantouBaifenbi,aObj.Baoji);
    let innerBObj = new IndivExample(bObj.Attk,bObj.Speed,bObj.HuJia,bObj.Chuantou,bObj.ChuantouBaifenbi,bObj.Baoji);
    const myChart = echarts.init(chartContainer.value)
    var option = {
      tooltip: {},
      backgroundColor: "#fff",
      visualMap: {
        show: true,
        dimension: 2,
        min: -1,
        max: 1,
        inRange: {
          color: [
            "#313695",
            "#4575b4",
            "#74add1",
            "#abd9e9",
            "#e0f3f8",
            "#ffffbf",
            "#fee090",
            "#fdae61",
            "#f46d43",
            "#d73027",
            "#a50026"
          ]
        }
      },
      xAxis3D: {
        type: "value",
        name: "攻击力",
        min:0,
        max: 600,
      },
      yAxis3D: {
        type: "value",
        name: "护甲",
        min:0,
        max: 500,
      },
      zAxis3D: {
        type: "value",
        name: "伤害",
        min:0
      },
      grid3D: {
        viewControl: {
          // projection: 'orthographic'
        }
      },
      series: [
        {
          type: "surface",
          wireframe: {
             show: true
          },
          equation: {
            x: {
              step: 10,
              min:0,
              max:600
            },
            y: {
              step: 15,
              min:0,
              max:500
            },
            z: function (x:number, y:number) {
              console.log("x=",x)
              console.log("y=",y)
              innerAObj.Attk = aObj.Attk + x;
              innerBObj.HuJia = bObj.HuJia + y;
              if (Number.isNaN(innerAObj.Attack(innerBObj))){
                const d = Math.random()*100
                console.log(d)
                return d
              } else{
                const d = AttackTime(10,innerAObj,innerBObj)
                console.log(d)
                return d
              }
            }
          }
        }
      ]
    }
    myChart.setOption(option)
  }
}
</script>

<template>
  <div class="app-container">
    <el-card shadow="never" class="search-wrapper">
      <el-form ref="IndivExampleA" :inline="true" :model="aObj">
        <el-form-item prop="Attk" label="攻击力">
          <el-input v-model="aObj.Attk" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="Hujia" label="护甲">
          <el-input v-model="aObj.HuJia" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="Speed" label="攻速">
          <el-input v-model="aObj.Speed" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="Chuantou" label="穿甲">
          <el-input v-model="aObj.Chuantou" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="ChuantouBaifenbi" label="穿甲百分比">
          <el-input v-model="aObj.ChuantouBaifenbi" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="Baoji" label="暴击几率">
          <el-input v-model="aObj.Baoji" placeholder="请输入" />
        </el-form-item>
      </el-form>
    </el-card>
  </div>
  <div class="app-container">
    <el-card shadow="never" class="search-wrapper">
      <el-form ref="IndivExampleB" :inline="true" :model="bObj">
        <el-form-item prop="Attk" label="攻击力">
          <el-input v-model="bObj.Attk" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="Hujia" label="护甲">
          <el-input v-model="bObj.HuJia" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="Speed" label="攻速">
          <el-input v-model="bObj.Speed" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="Chuantou" label="穿甲">
          <el-input v-model="bObj.Chuantou" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="ChuantouBaifenbi" label="穿甲百分比">
          <el-input v-model="bObj.ChuantouBaifenbi" placeholder="请输入" />
        </el-form-item>
        <el-form-item prop="Baoji" label="暴击几率">
          <el-input v-model="bObj.Baoji" placeholder="请输入" />
        </el-form-item>
      </el-form>
    </el-card>
  </div>
  <div class="app-container">
    <el-button @click="updateChart">更新图表</el-button>
  </div>
  <div ref="chartContainer" style="width: 1000px; height: 1000px;"></div>
</template>

<style scoped>
/* 你可以在这里添加任何必要的样式 */
</style>
