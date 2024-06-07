import { ref, shallowRef } from 'vue'
import { resumeApi } from '@/api'

export const useResumePreview = () => {
    const dialogVisible = ref(false)
    const resumeInfo = shallowRef(null)

    const previewResume = rid => {
        resumeApi.getResumeInfo({ rid }).then(res => {
            resumeInfo.value = { ...JSON.parse(res.data.summaryInfo), ...JSON.parse(res.data.detailInfo) }
            dialogVisible.value = true
        })
    }

    return { dialogVisible, resumeInfo, previewResume }
}
