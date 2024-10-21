<template>
  <div class="app-container">
    <el-card v-loading="loading" shadow="never" class="search-wrapper">
      <el-form ref="IndivExampleA" :inline="true" :model="aObj">
        <el-form-item prop="Attk" label="攻击力">
          <el-input v-model="IndivExampleA.Attk" placeholder="请输入" />
        </el-form-item>
      </el-form>
    </el-card>
  </div>
  <div class="app-container">
    <el-card v-loading="loading" shadow="never" class="search-wrapper">
      <el-form ref="IndivExampleB" :inline="true" :model="bObj">
        <el-form-item prop="Attk" label="攻击力">
          <el-input v-model="IndivExampleB.Attk" placeholder="请输入" />
        </el-form-item>
      </el-form>
    </el-card>
  </div>
  <div ref="chartContainer" style="width: 800px; height: 800px;"></div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue"
import * as echarts from "echarts"
import {IndivExample} from "@/utils/chartExapmle"
import 'echarts-gl';

const chartContainer = ref<HTMLElement | null>(null)

const IndivExampleA = ref<IndivExample>(new IndivExample())
const IndivExampleB = ref<IndivExample>(new IndivExample())
const aObj = new IndivExample()
const bObj = new IndivExample()

onMounted(() => {
  if (chartContainer.value) {
    const myChart = echarts.init(chartContainer.value)
    const option = {
      tooltip: {},
      backgroundColor: "#fff",
      visualMap: {
        show: false,
        dimension: 2,
        min: -100,
        max: 100,
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
        type: "value"
      },
      yAxis3D: {
        type: "value"
      },
      zAxis3D: {
        type: "value"
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
            // show: false
          },
          equation: {
            x: {
              step: 1
            },
            y: {
              step: 1
            },
            z: function (x:number, y:number) {
              aObj.Attk = x;
              bObj.HuJia = y;
              console.log("打印："+aObj.Attack(bObj))
              if (Number.isNaN(aObj.Attack(bObj))){
                console.log("wawawa")
                return Math.random()*100
              } else{
                return aObj.Attack(bObj)
              }
            }
          }
        }
      ]
    }
    myChart.setOption(option)
    console.log("渲染咯")
  }
})
</script>

<style scoped>
/* 你可以在这里添加任何必要的样式 */
</style>
