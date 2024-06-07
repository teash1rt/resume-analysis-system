export const getDescription = e => {
    const content = []
    if (e.summaryInfo.job_obj[0] !== undefined) content.push(`求职意向：${e.summaryInfo.job_obj[0]}`)
    if (e.summaryInfo.tag.total_work_time > 0) content.push(`工作年限：${e.summaryInfo.tag.total_work_time}年`)
    if (e.summaryInfo.custom_content.money_obj !== '') content.push(`薪资期望：${e.summaryInfo.custom_content.money_obj}`)
    if (content.length === 0) content.push('收到一份新简历!')
    return content
}
