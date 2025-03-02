declare module "@/utils/wasm/firstwasm.wasm-runtime.js" {
  export const TeaVM: {
    wasm: {
      load: (path: string) => Promise<{
        instance: {
          exports: {
            testPower: (limit: number) => number
            fibonacci: (n: number) => number
          }
        }
      }>
    }
  }
}
