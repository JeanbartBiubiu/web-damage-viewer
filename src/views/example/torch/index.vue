<template>
  <div>
    <button @click="resizeImage">调整图片大小</button>
    <button @click="testPower">测试性能</button>
    <img ref="originalImageRef" :src="originalImageUrl" alt="Original Image" />
    <img ref="resizedImageRef" :src="resizedImageUrl" alt="Resized Image" />
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { Jimp } from "jimp";
import UploadImg64 from '@/components/UploadImg64/index.vue';
// 定义引用和响应式变量
const originalImageRef = ref<HTMLImageElement | null>(null);
const resizedImageRef = ref<HTMLImageElement | null>(null);
const originalImageUrl = ref<string>('../images/6361.png');
const resizedImageUrl = ref<string>('');

const resizeImage = async () => {
  try {
    // 使用 Jimp 读取原始图片
    const image = await Jimp.read(originalImageUrl.value);
    // 调整图片大小，这里将宽度调整为 400px，高度按比例缩放
    image.resize({ w: 64,h:64 });

    // 将处理后的图片转换为 buffer
    const buffer = await image.getBuffer("image/jpeg");
    // 创建 Blob 对象
    const blob = new Blob([buffer], { type: "image/jpeg" });
    // 生成可用于 img 标签 src 属性的 URL
    resizedImageUrl.value = URL.createObjectURL(blob);
  } catch (error) {
    console.error('图片调整大小出错:', error);
  }
};


// 定义 testPower 函数
function testPower() {
  // 记录开始时间
  const startTime = Date.now();
  const limit = 100000000;
  // 创建一个布尔数组，初始值都设为 true，表示都是质数
  const isPrime = new Array(limit + 1).fill(true);
  // 0 和 1 不是质数
  isPrime[0] = false;
  isPrime[1] = false;

  // 直线筛算法
  for (let i = 2; i * i <= limit; i++) {
    if (isPrime[i]) {
      for (let j = i * i; j <= limit; j += i) {
        isPrime[j] = false;
      }
    }
  }

  // 统计质数的数量
  let primeCount = 0;
  for (let i = 2; i <= limit; i++) {
    if (isPrime[i]) {
      primeCount++;
    }
  }

  // 记录结束时间
  const endTime = Date.now();
  // 计算执行时间
  const executionTime = endTime - startTime;
  // 打印执行时间
  console.log("执行时间: " + executionTime + " 毫秒");
  return primeCount;
}

</script>

<style scoped>
img {
  border: 1px solid black;
  margin-top: 10px;
}
</style>
