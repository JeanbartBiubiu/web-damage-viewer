<template>
  <el-button>
    <div @click="showLoginDialog">
      <span>华为登录</span>
    </div>
  </el-button>

  <el-dialog
    v-model="dialogVisible"
    title="华为账号登录"
    width="400px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <el-form :model="loginForm" label-width="80px">
      <el-form-item label="邮箱">
        <el-input v-model="loginForm.email" type="email" placeholder="请输入邮箱" />
      </el-form-item>
      <el-form-item label="验证码">
        <div class="verify-code-input">
          <el-input v-model="loginForm.verifyCode" placeholder="请输入验证码" />
          <el-button type="primary" :disabled="countdown > 0" @click="requestVerifyCode">
            {{ countdown > 0 ? `${countdown}s后重试` : "获取验证码" }}
          </el-button>
        </div>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="login">登录</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive, watch } from "vue"
import { ElMessage } from "element-plus"
import agconnect from "@hw-agconnect/api"
import { SSOBody } from "@/api/login/huawei/types/login"

const props = defineProps<{
  callback?: (response: SSOBody) => void
}>()

// 登录弹窗控制
const dialogVisible = ref(false)
const countdown = ref(0)
let countdownTimer: number | null = null

// 登录表单数据
const loginForm = reactive({
  email: "",
  verifyCode: ""
})

// 监听弹窗关闭，重置表单和倒计时
watch(dialogVisible, (newVal) => {
  if (!newVal) {
    // 重置表单数据
    loginForm.email = ""
    loginForm.verifyCode = ""
    // 清除倒计时
    if (countdownTimer) {
      clearInterval(countdownTimer)
      countdownTimer = null
    }
    countdown.value = 0
  }
})

// 显示登录弹窗
const showLoginDialog = () => {
  const token = localStorage.getItem("token")
  if (token) {
    ElMessage.success("已登录")
    dialogVisible.value = false
    return
  }
  dialogVisible.value = true
}

// 申请登录验证码
const requestVerifyCode = () => {
  if (!loginForm.email) {
    ElMessage.warning("请输入邮箱")
    return
  }

  agconnect
    .auth()
    .requestEmailVerifyCode(
      loginForm.email,
      agconnect.auth.Action.ACTION_REGISTER_LOGIN,
      "zh_CN", // 发送验证码的语言
      120 // 发送间隔时间
    )
    .then(() => {
      ElMessage.success("验证码申请成功")
      countdown.value = 60
      // 清除之前的定时器
      if (countdownTimer) {
        clearInterval(countdownTimer)
      }
      // 设置新的定时器
      countdownTimer = window.setInterval(() => {
        countdown.value--
        if (countdown.value <= 0) {
          if (countdownTimer) {
            clearInterval(countdownTimer)
            countdownTimer = null
          }
        }
      }, 1000)
    })
    .catch((error) => {
      ElMessage.error(`验证码申请失败: ${error.message}`)
    })
}

// 登录函数
const login = () => {
  if (!loginForm.email || !loginForm.verifyCode) {
    ElMessage.warning("请输入邮箱和验证码")
    return
  }

  const credential = agconnect.auth.EmailAuthProvider.credentialWithVerifyCode(
    loginForm.email,
    "",
    // @ts-ignore
    loginForm.verifyCode
  )

  agconnect
    .auth()
    .signIn(credential)
    .then(async (user) => {
      ElMessage.success("登录成功")
      dialogVisible.value = false
      if (props.callback) {
        const jwt = await user
          .getUser()
          .getToken(false)
          .then(
            (token) => {
              console.log(token.getToken())
              return token.getToken()
            },
            (error) => {
              console.error("获取token失败", error)
              return ""
            }
          )
          .catch((error) => {
            console.error("获取token失败", error)
            return ""
          })
          .finally(() => {})
        props.callback({
          jwt: jwt
        })
      }
    })
    .catch((error) => {
      ElMessage.error(`登录失败: ${error.message}`)
    })
}
</script>

<style scoped>
.verify-code-input {
  display: flex;
  gap: 10px;
}

.verify-code-input .el-input {
  flex: 1;
}

.verify-code-input .el-button {
  width: 120px;
}
</style>
