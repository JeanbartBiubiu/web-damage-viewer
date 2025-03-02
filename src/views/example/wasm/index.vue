<template>
  <div>
    <input v-model="limit" type="number" placeholder="请输入 limit 值">
    <button @click="loadWasm">测试wasm性能</button>
    <button @click="loadJs">测试js性能</button>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { TeaVM } from "@/utils/wasm/firstwasm.wasm-runtime.js"

// 使用 ref 创建响应式变量 limit
const limit = ref(0);

async function loadWasm() {
  // 统计执行时间
  const teavmInstance = await TeaVM.wasm.load("/wasm/firstwasm.wasm")
  const startTime = Date.now();
  var number = teavmInstance.instance.exports.fibonacci(limit.value);
  // 计算执行时间
  const endTime = Date.now();
  const executionTime = endTime - startTime;
  // 输出结果和执行时间
  console.log(`数量: ${number}`);
  console.log(`执行时间: ${executionTime} 毫秒`);
}

function calJs() {
  let isPrime = new Array(limit.value + 1).fill(true);

  // 初始化所有数为质数
  for (let i = 2; i <= limit.value; i++) {
    isPrime[i] = true;
  }

  // 直线筛算法
  for (let i = 2; i * i <= limit.value; i++) {
    if (isPrime[i]) {
      for (let j = i * i; j <= limit.value; j += i) {
        isPrime[j] = false;
      }
    }
  }

  // 统计质数的数量
  let primeCount = 0;
  for (let i = 2; i <= limit.value; i++) {
    if (isPrime[i]) {
      primeCount++;
    }
  }
  return primeCount;
}

async function loadJs() {
  // 统计执行时间
  const startTime = Date.now();
  var number = fibonacci(limit.value);
  // 计算执行时间
  const endTime = Date.now();
  console.log(`数量: ${number}`);
  const executionTime = endTime - startTime;
  // 输出结果和执行时间
  console.log(`执行时间: ${executionTime} 毫秒`);
}

function fibonacci(n: number): number {
  if (n <= 1) {
    return n;
  }
  return fibonacci(n - 1) + fibonacci(n - 2);
}

</script>

<style scoped>
img {
  border: 1px solid black;
  margin-top: 10px;
}
</style>
