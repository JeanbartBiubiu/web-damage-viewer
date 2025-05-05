// core
import { createApp } from "vue"
import App from "@/App.vue"
import store from "@/store"
import router from "@/router"
import "@/router/permission"
// load
import { loadSvg } from "@/icons"
import { loadPlugins } from "@/plugins"
import { loadDirectives } from "@/directives"
// css
import "uno.css"
import "normalize.css"
import "element-plus/dist/index.css"
import "element-plus/theme-chalk/dark/css-vars.css"
import "vxe-table/lib/style.css"
import "vxe-table-plugin-element/dist/style.css"
import "@/styles/index.scss"

import vue3GoogleLogin from "vue3-google-login"
import agconnect from "@hw-agconnect/api"
import "@hw-agconnect/auth"
import "@hw-agconnect/instance"

const app = createApp(App)

/** 加载插件 */
loadPlugins(app)
/** 加载全局 SVG */
loadSvg(app)
/** 加载自定义指令 */
loadDirectives(app)

app.use(store).use(router)

app.use(vue3GoogleLogin, {
  clientId: "281073904047-bjv1dcnups9jkdnqb94t66stcve0smi9.apps.googleusercontent.com"
})
const agConnectConfig = {
  agcgw: {
    url: "connect-drcn.dbankcloud.cn",
    backurl: "connect-drcn.hispace.hicloud.com",
    websocketurl: "connect-ws-drcn.hispace.dbankcloud.cn",
    websocketbackurl: "connect-ws-drcn.hispace.dbankcloud.com"
  },
  agcgw_all: {
    SG: "connect-dra.dbankcloud.cn",
    SG_back: "connect-dra.hispace.hicloud.com",
    CN: "connect-drcn.dbankcloud.cn",
    CN_back: "connect-drcn.hispace.hicloud.com",
    RU: "connect-drru.hispace.dbankcloud.ru",
    RU_back: "connect-drru.hispace.dbankcloud.cn",
    DE: "connect-dre.dbankcloud.cn",
    DE_back: "connect-dre.hispace.hicloud.com"
  },
  websocketgw_all: {
    SG: "connect-ws-dra.hispace.dbankcloud.cn",
    SG_back: "connect-ws-dra.hispace.dbankcloud.com",
    CN: "connect-ws-drcn.hispace.dbankcloud.cn",
    CN_back: "connect-ws-drcn.hispace.dbankcloud.com",
    RU: "connect-ws-drru.hispace.dbankcloud.ru",
    RU_back: "connect-ws-drru.hispace.dbankcloud.cn",
    DE: "connect-ws-dre.hispace.dbankcloud.cn",
    DE_back: "connect-ws-dre.hispace.dbankcloud.com"
  },
  client: {
    cp_id: "30086000574403798",
    product_id: "461323198429888697",
    client_id: "1677760031711346368",
    client_secret: "7A425A971E20C0D464EAF691C4D633D311BBBBC45AF72799DDDDDDD6FA0E6ED1",
    project_id: "461323198429888697"
  },
  oauth_client: { client_id: "114108731", client_type: 7 },
  app_info: { app_id: "245150415728384057" },
  service: {
    analytics: {
      collector_url: "datacollector-drcn.dt.hicloud.com,datacollector-drcn.dt.dbankcloud.cn",
      collector_url_cn: "datacollector-drcn.dt.hicloud.com,datacollector-drcn.dt.dbankcloud.cn",
      collector_url_de: "datacollector-dre.dt.hicloud.com,datacollector-dre.dt.dbankcloud.cn",
      collector_url_ru: "datacollector-drru.dt.dbankcloud.ru,datacollector-drru.dt.hicloud.com",
      collector_url_sg: "datacollector-dra.dt.hicloud.com,datacollector-dra.dt.dbankcloud.cn",
      resource_id: "p1",
      channel_id: ""
    },
    ml: { mlservice_url: "ml-api-drcn.ai.dbankcloud.com,ml-api-drcn.ai.dbankcloud.cn" },
    cloudstorage: {
      storage_url: "https://agc-storage-drcn.platform.dbankcloud.cn",
      storage_url_ru: "https://agc-storage-drru.cloud.huawei.ru",
      storage_url_sg: "https://ops-dra.agcstorage.link",
      storage_url_de: "https://ops-dre.agcstorage.link",
      storage_url_cn: "https://agc-storage-drcn.platform.dbankcloud.cn",
      storage_url_ru_back: "https://agc-storage-drru.cloud.huawei.ru",
      storage_url_sg_back: "https://agc-storage-dra.cloud.huawei.asia",
      storage_url_de_back: "https://agc-storage-dre.cloud.huawei.eu",
      storage_url_cn_back: "https://agc-storage-drcn.cloud.huawei.com.cn"
    },
    search: { url: "https://search-drcn.cloud.huawei.com" },
    edukit: { edu_url: "edukit.cloud.huawei.com.cn", dh_url: "edukit.cloud.huawei.com.cn" }
  },
  region: "CN",
  configuration_version: "3.0"
}
// 初始化SDK
agconnect.instance().configInstance(agConnectConfig)
router.isReady().then(() => {
  app.mount("#app")
})
