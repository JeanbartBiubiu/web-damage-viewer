export const getToken = () => {
  return localStorage.getItem("token")
}

export const getCurrentSchema = () => {
  return localStorage.getItem("currentSchema")
}

export const setToken = (token: string) => {
}
export const removeToken = () => {
}
