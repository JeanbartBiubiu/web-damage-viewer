<template>
  <div class="app-container">
    <el-card class="chart-card">
      <div v-if="loading" style="padding: 20px">
        <el-skeleton animated>
          <template #template>
            <div>
              <el-skeleton-item variant="text" style="width: 30%; height: 40px" />
              <div style="display: flex; justify-content: space-between; margin-top: 20px">
                <el-skeleton-item variant="text" style="width: 45%; height: 300px" />
                <el-skeleton-item variant="text" style="width: 45%; height: 300px" />
              </div>
            </div>
          </template>
        </el-skeleton>
      </div>
      <div v-else ref="chartRef" class="chart-container" />
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, reactive, nextTick } from "vue"
import { request } from "@/utils/service"
import * as echarts from "echarts"
console.log(echarts) // 检查 echarts 对象是否存在

interface PvUv {
  hour: number
  pv: number
  uv: number
}

const chartRef = ref<HTMLElement | null>(null)
const loading = ref(true)
const chartInstance = ref<echarts.ECharts | null>(null)
const pvUvData = reactive<{
  dates: string[]
  pvData: number[]
  uvData: number[]
}>({
  dates: [],
  pvData: [],
  uvData: []
})

// 获取统计数据
const fetchPvUvData = async () => {
  try {
    loading.value = true
    const res = await request<{ data: PvUv[] }>({
      url: "/pv/last30days",
      method: "get"
    })

    if (res.data && res.data.length > 0) {
      // 按小时排序
      const sortedData = res.data.sort((a, b) => a.hour - b.hour)

      // 提取数据
      pvUvData.dates = sortedData.map((item) => {
        // 将小时数转换为毫秒数
        const milliseconds = item.hour * 60 * 60 * 1000
        const date = new Date(milliseconds)
        // 格式化日期和小时数
        const month = String(date.getMonth() + 1).padStart(2, "0")
        const day = String(date.getDate()).padStart(2, "0")
        const hour = String(date.getHours()).padStart(2, "0")
        return `${month}-${day} ${hour}:00`
      })
      pvUvData.pvData = sortedData.map((item) => item.pv)
      pvUvData.uvData = sortedData.map((item) => item.uv)

      // 设置loading为false，让DOM先渲染出来
      loading.value = false

      // 等待DOM更新后初始化图表
      await nextTick(() => {
        setTimeout(initChart, 0)
      })
    } else {
      loading.value = false
    }
  } catch (error) {
    console.error("获取PV/UV数据失败:", error)
    loading.value = false
  }
}

// 初始化ECharts
const initChart = () => {
  if (!chartRef.value) {
    return
  }

  // 如果已存在实例则销毁
  if (chartInstance.value) {
    chartInstance.value.dispose()
  }

  // 创建新实例
  chartInstance.value = echarts.init(chartRef.value)

  // 配置选项
  const option = {
    title: {
      text: "最近30天PV/UV统计",
      left: "center"
    },
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "cross",
        label: {
          backgroundColor: "#6a7985"
        }
      }
    },
    legend: {
      data: ["页面访问量(PV)", "访客数(UV)"],
      bottom: "0%"
    },
    grid: {
      left: "3%",
      right: "4%",
      bottom: "10%",
      top: "15%",
      containLabel: true
    },
    xAxis: {
      type: "category",
      boundaryGap: false,
      data: pvUvData.dates
    },
    yAxis: {
      type: "value"
    },
    series: [
      {
        name: "页面访问量(PV)",
        type: "line",
        data: pvUvData.pvData,
        smooth: true,
        lineStyle: {
          width: 3,
          color: "#409EFF"
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {
              offset: 0,
              color: "rgba(64, 158, 255, 0.5)"
            },
            {
              offset: 1,
              color: "rgba(64, 158, 255, 0.1)"
            }
          ])
        }
      },
      {
        name: "访客数(UV)",
        type: "line",
        data: pvUvData.uvData,
        smooth: true,
        lineStyle: {
          width: 3,
          color: "#67C23A"
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            {
              offset: 0,
              color: "rgba(103, 194, 58, 0.5)"
            },
            {
              offset: 1,
              color: "rgba(103, 194, 58, 0.1)"
            }
          ])
        }
      }
    ]
  }

  // 渲染图表
  chartInstance.value.setOption(option)

  // 自适应窗口大小
  window.addEventListener("resize", () => {
    chartInstance.value?.resize()
  })
}

// 组件挂载时获取数据
onMounted(() => {
  fetchPvUvData()
})
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.chart-card {
  margin-bottom: 20px;
  width: 100%;
}

.chart-container {
  width: 100%;
  height: 400px;
}
</style>
