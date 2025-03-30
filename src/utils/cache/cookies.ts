export const getToken = () => {
  return localStorage.getItem("token")
}

export const getCurrentSchema = () => {
  return localStorage.getItem("currentSchema")
}

export const setToken = (token: string) => {
  Cookies.set(CacheKey.TOKEN, token)
}
export const removeToken = () => {
  Cookies.remove(CacheKey.TOKEN)
}
