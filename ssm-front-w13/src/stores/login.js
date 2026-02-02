import { defineStore } from "pinia";
import { ref } from "vue";

export const useLoginStore = defineStore('login', () => {

    const name = ref('')
    const token = ref('')
    const avatar = ref('')
    const userId = ref(null)
    const loginCode = ref('')
    const userType = ref('') // 用户类型：admin/reporter

    const setName = (loginName) => {
        name.value = loginName;
    }

    const removeName = () => {
        name.value = ''
    }

    const setToken = (newToken) => {
        token.value = newToken;
    }

    const removeToken = () => {
        token.value = ''
    }

    const setAvatar = (path) => {
        avatar.value = path || ''
    }

    const removeAvatar = () => {
        avatar.value = ''
    }

    const setUserId = (id) => {
        userId.value = id
    }

    const removeUserId = () => {
        userId.value = null
    }

    const setLoginCode = (code) => {
        loginCode.value = code || ''
    }

    const removeLoginCode = () => {
        loginCode.value = ''
    }

    const setUserType = (type) => {
        userType.value = type || ''
    }

    const removeUserType = () => {
        userType.value = ''
    }

    return {
        name,
        token,
        avatar,
        userId,
        loginCode,
        userType,
        setName,
        removeName,
        setToken,
        removeToken,
        setAvatar,
        removeAvatar,
        setUserId,
        removeUserId,
        setLoginCode,
        removeLoginCode,
        setUserType,
        removeUserType
    }
}, {
    // 持久化存储
    persist: true
});