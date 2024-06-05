export const debounce = (fn, delay, ...value) => {
    let timer = null
    return () => {
        const args = value
        clearTimeout(timer)
        timer = setTimeout(function () {
            fn.apply(this, args)
        }, delay)
    }
}
