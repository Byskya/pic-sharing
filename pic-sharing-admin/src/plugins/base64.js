export default {
    install(Vue) {
        // 把base64转化成blob类型
        Vue.prototype.$base64ToBlob = function (base64) {
            const arr = base64.split(',')
            const mime = arr[0].match(/:(.*?);/)[1]
            const bStr = atob(arr[1])
            let n = bStr.length
            const u8arr = new Uint8Array(n)
            while (n--) {
                u8arr[n] = bStr.charCodeAt(n)
            }
            return new Blob([u8arr], { type: mime })
        }
    }
}
