import { type RouteRecordRaw, createRouter } from "vue-router"
import { history, flatMultiLevelRoutes } from "./helper"
import routeSettings from "@/config/route"

const Layouts = () => import("@/layouts/index.vue")

/**
 * 常驻路由
 * 除了 redirect/403/404/login 等隐藏页面，其他页面建议设置 Name 属性
 */
export const constantRoutes: RouteRecordRaw[] = [
  {
    path: "/redirect",
    component: Layouts,
    meta: {
      hidden: true
    },
    children: [
      {
        path: ":path(.*)",
        component: () => import("@/views/redirect/index.vue")
      }
    ]
  },
  {
    path: "/403",
    component: () => import("@/views/error-page/403.vue"),
    meta: {
      hidden: true
    }
  },
  {
    path: "/404",
    component: () => import("@/views/error-page/404.vue"),
    meta: {
      hidden: true
    },
    alias: "/:pathMatch(.*)*"
  },
  {
    path: "/login",
    component: () => import("@/views/login/index.vue"),
    meta: {
      hidden: true
    }
  },
  {
    path: "/",
    component: Layouts,
    redirect: "/dashboard",
    children: [
      {
        path: "dashboard",
        component: () => import("@/views/dashboard/index.vue"),
        name: "Dashboard",
        meta: {
          title: "首页",
          svgIcon: "dashboard",
          affix: true
        }
      }
    ]
  },
  {
    path: "/global",
    component: Layouts,
    redirect: "/global",
    name: "Gloabl",
    meta: {
      title: "全局设置",
      elIcon: "Grid"
    },
    children: [
      {
        path: "okk",
        component: () => import("@/views/global/okk/index.vue"),
        name: "okk",
        meta: {
          title: "获取token",
          keepAlive: true
        }
      },
      {
        path: "selectGame",
        component: () => import("@/views/global/selectGame/index.vue"),
        name: "SelectGame",
        meta: {
          title: "选择游戏",
          keepAlive: true
        }
      },
      {
        path: "token",
        component: () => import("@/views/global/token/index.vue"),
        name: "SetToken",
        meta: {
          title: "token设置",
          keepAlive: true
        }
      }
    ]
  },
  {
    path: "/table",
    component: Layouts,
    redirect: "/table/element-plus",
    name: "Table",
    meta: {
      title: "表格",
      elIcon: "Grid"
    },
    children: [
      {
        path: "attribute",
        component: () => import("@/views/table/attribute/index.vue"),
        name: "AttributeMge",
        meta: {
          title: "属性管理",
          keepAlive: true
        }
      },
      {
        path: "level",
        component: () => import("@/views/table/level/index.vue"),
        name: "LevelMge",
        meta: {
          title: "等级管理",
          keepAlive: true
        }
      },
      {
        path: "individual",
        component: () => import("@/views/table/individual/index.vue"),
        name: "IndividualMge",
        meta: {
          title: "角色管理",
          keepAlive: true
        }
      },
      {
        path: "lifeCycle",
        component: () => import("@/views/table/lifeCycle/index.vue"),
        name: "LifeCycleMge",
        meta: {
          title: "生命周期管理",
          keepAlive: true
        }
      },
      {
        path: "equipment",
        component: () => import("@/views/table/equipment/index.vue"),
        name: "EquipmentMge",
        meta: {
          title: "装备管理",
          keepAlive: true
        }
      },
      {
        path: "skill",
        component: () => import("@/views/table/skill/index.vue"),
        name: "SkillMge",
        meta: {
          title: "技能管理",
          keepAlive: true
        }
      }
    ]
  },
  {
    path: "/example",
    component: Layouts,
    redirect: "/example",
    name: "Example",
    meta: {
      title: "测试新功能",
      svgIcon: "example"
    },
    children: [
      {
        path: "torch",
        component: () => import("@/views/example/torch/index.vue"),
        name: "Torch",
        meta: {
          title: "图片处理",
          keepAlive: true
        }
      },
      {
        path: "wasm",
        component: () => import("@/views/example/wasm/index.vue"),
        name: "Wasm",
        meta: {
          title: "wasm性能测试",
          keepAlive: true
        }
      }
    ]
  },
  {
    path: "/hook-demo",
    component: Layouts,
    redirect: "/hook-demo/use-fetch-select",
    name: "HookDemo",
    meta: {
      title: "Hook",
      elIcon: "Menu",
      alwaysShow: true
    },
    children: [
      {
        path: "use-fetch-select",
        component: () => import("@/views/hook-demo/use-fetch-select.vue"),
        name: "UseFetchSelect",
        meta: {
          title: "useFetchSelect"
        }
      },
      {
        path: "use-fullscreen-loading",
        component: () => import("@/views/hook-demo/use-fullscreen-loading.vue"),
        name: "UseFullscreenLoading",
        meta: {
          title: "useFullscreenLoading"
        }
      },
      {
        path: "use-watermark",
        component: () => import("@/views/hook-demo/use-watermark.vue"),
        name: "UseWatermark",
        meta: {
          title: "useWatermark"
        }
      }
    ]
  }
]

/**
 * 动态路由
 * 用来放置有权限 (Roles 属性) 的路由
 * 必须带有 Name 属性
 */
export const dynamicRoutes: RouteRecordRaw[] = [
  {
    path: "/permission",
    component: Layouts,
    redirect: "/permission/page",
    name: "Permission",
    meta: {
      title: "权限",
      svgIcon: "lock",
      roles: ["admin", "editor"], // 可以在根路由中设置角色
      alwaysShow: true // 将始终显示根菜单
    },
    children: [
      {
        path: "page",
        component: () => import("@/views/permission/page.vue"),
        name: "PagePermission",
        meta: {
          title: "页面级",
          roles: ["admin"] // 或者在子导航中设置角色
        }
      },
      {
        path: "directive",
        component: () => import("@/views/permission/directive.vue"),
        name: "DirectivePermission",
        meta: {
          title: "按钮级" // 如果未设置角色，则表示：该页面不需要权限，但会继承根路由的角色
        }
      }
    ]
  }
]

const router = createRouter({
  history,
  routes: routeSettings.thirdLevelRouteCache ? flatMultiLevelRoutes(constantRoutes) : constantRoutes
})

/** 重置路由 */
export function resetRouter() {
  // 注意：所有动态路由路由必须带有 Name 属性，否则可能会不能完全重置干净
  try {
    router.getRoutes().forEach((route) => {
      const { name, meta } = route
      if (name && meta.roles?.length) {
        router.hasRoute(name) && router.removeRoute(name)
      }
    })
  } catch {
    // 强制刷新浏览器也行，只是交互体验不是很好
    window.location.reload()
  }
}

export default router
