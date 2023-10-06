import info
import re
import numpy as np
import predict
from collections import OrderedDict
import utils


def re_basedata(basic_data, data):
    if basic_data['tel'] == '':
        tel = re.findall(info.tel_pattern(), data)
        if tel:
            basic_data['tel'] = tel[0]
    if basic_data['email'] == '':
        email = re.findall(info.email_pattern(), data)
        if email:
            basic_data['email'] = email[0]
    if basic_data['age'] == 0:
        age = re.search(info.age_pattern(), data)
        if age:
            if age.group(1):
                basic_data['age'] = int(age.group(1))
            else:
                basic_data['age'] = int(age.group(2))
    if basic_data['birth'] == '':
        birth = re.search(info.birth_pattern(), data)
        if birth:
            basic_data['birth'] = birth.group()
            basic_data['age'] = 2023 - int(basic_data['birth'][:4])+1
    edu = re.findall(info.edu_pattern(), data)
    if edu:
        for e in edu:
            # 找到的学历大于当前的学历就更新
            if info.edu_map()[e] > info.edu_map()[basic_data['edu']]:
                basic_data['edu'] = e
    return basic_data


def handle_basedata(data, basic_data, total_data, tokenizer3, model3):
    for data in data[0]:
        # 先跑NER 选出姓名 地点 学历
        output_prediction = predict.ner_predict(
            data, tokenizer3, model3)
        if len(output_prediction[0]) and basic_data['name'] == '' and re.match(info.chinese_str(), output_prediction[0][0]):
            basic_data['name'] = output_prediction[0][0]
        elif len(output_prediction[1]):
            basic_data['college'].extend(output_prediction[1])
        elif len(output_prediction[2]):
            basic_data['loc'].extend(output_prediction[2])
        # 再跑正则匹配更新label
        basic_data = re_basedata(basic_data, data)

    fixed_college = []
    for x in basic_data['college']:
        for keyword in info.college_endword():
            keyword_position = x.find(keyword)
            if keyword_position != -1:
                fixed_college.append(x[:keyword_position + len(keyword)])
                # 注意这里内循环需要是 keyword 并且必须 break 防止识别两次 优先级见 info
                break
    basic_data['college'] = list(OrderedDict.fromkeys(fixed_college))
    basic_data['loc'] = list(OrderedDict.fromkeys(basic_data['loc']))[:2]

    # 给学校加tag
    if np.intersect1d(basic_data['college'], info.college985()).size > 0:
        total_data['tag']['edu_tag'].append('985')
        total_data['score'] += info.score_map()['985']
    elif np.intersect1d(basic_data['college'], info.college211()).size > 0:
        total_data['tag']['edu_tag'].append('211')
        total_data['score'] += info.score_map()['211']

    # 给籍贯加tag
    if len(basic_data['loc']) > 0:
        loc_pattern = '|'.join(info.province())
        for loc in basic_data['loc']:
            matches = re.findall(loc_pattern, loc)
            if matches:
                total_data['tag']['loc_tag'] = matches[0]
                break

    # 最高学历加tag
    if len(basic_data['edu']) > 0:
        total_data['tag']['edu_tag'].append(basic_data['edu'])
        total_data['score'] += info.score_map()[basic_data['edu']]

    # 删除空字段
    key_to_del = []
    for key, value in basic_data.items():
        if value == '' or value == [] or value == 0:
            key_to_del.append(key)
    for key in key_to_del:
        del basic_data[key]


def handle_job_obj(total_data):
    raw_jobs = total_data['job_obj']
    fixed_jobs = []
    # 找首个个特殊符号 直接截断后半段加入答案 没找到符号直接加入答案
    for job in raw_jobs:
        pos = job.find(':')
        if pos == -1:
            pos = job.find('/')
        if pos == -1:
            pos = job.find('：')
        if pos == -1:
            fixed_jobs.append(job.replace(' ', ''))
        else:
            fixed_jobs.append(job[pos+1:].replace(' ', ''))
    fixed_jobs = list(OrderedDict.fromkeys(fixed_jobs))
    total_data['job_obj'] = fixed_jobs


def handle_experience(total_data, tokenizer3, model3):
    company = []
    year_interval = 0
    month_interval = 0
    for exp in total_data['experience']:
        # 跑NER找出所有工作单位
        output_prediction = predict.ner_predict(exp, tokenizer3, model3)
        company.extend(output_prediction[1])
        # 计算工作年限
        matches = re.findall(info.work_time(), exp)
        if matches:
            for match in matches:
                date1 = utils.re_date(match[0])
                date2 = '2023.04' if match[1] == '今' or match[1] == '至今' else utils.re_date(
                    match[1])
                try:
                    years, months = utils.calculate_date_interval(date1, date2)
                    if years >= 0:
                        year_interval += years
                        month_interval += months
                except Exception as e:
                    print(e)

    year_interval += month_interval // 12
    month_interval %= 12
    # 受限于Ner的准确性 仅在自定义下的关键字下识别
    # FIXME 择优选择下面两种之一
    # fixed_company = []
    # for x in company:
    #     for keyword in info.college_endword():
    #         keyword_position = x.find(keyword)
    #         if keyword_position != -1 and x[:keyword_position+len(keyword)] != keyword:
    #             fixed_company.append(x[:keyword_position+len(keyword)])
    fixed_company = [x for keyword in info.company_endword()
                     for x in company if x.endswith(keyword) and x != keyword]
    fixed_company = list(OrderedDict.fromkeys(fixed_company))
    total_data['tag']['experience_tag'].extend(fixed_company)
    if year_interval < 60 and year_interval >= 0:
        total_data['tag']['total_work_time'] = year_interval if month_interval == 0 else year_interval + 1
    else:
        total_data['tag']['total_work_time'] = 0
    # 工作经验分值计算 工作单位和工作年限权重为 1.5
    total_data['score'] += 1.2 * \
        len(fixed_company) + 0.1 * (year_interval * 12 + month_interval)


def handle_ability(total_data):
    # 提前判断是否已经找到了 优化一下性能
    CET6_flag, CET4_flag, Photoshop_flag, Office_flag, NCRE_flag = False, False, False, False, False
    CET6_patter = '|'.join(info.self_ability()[0])
    CET4_patter = '|'.join(info.self_ability()[1])
    Photoshop_patter = '|'.join(info.self_ability()[2])
    Office_patter = '|'.join(info.self_ability()[3])
    NCRE_patter = '|'.join(info.self_ability()[4])
    # 找出CET Photoshop Office 计算机等级考试 的tag
    for item in total_data['ability']:
        if CET6_flag == False:
            matches = re.findall(CET6_patter, item)
            if matches:
                total_data['tag']['ability'].append('CET6')
                total_data['score'] += info.score_map()['CET6']
                CET6_flag = True
        if CET6_flag == False and CET4_flag == False:
            matches = re.findall(CET4_patter, item)
            if matches:
                total_data['tag']['ability'].append('CET4')
                total_data['score'] += info.score_map()['CET4']
                CET4_flag = True
        if Photoshop_flag == False:
            matches = re.findall(Photoshop_patter, item)
            if matches:
                total_data['tag']['ability'].append('Photoshop')
                Photoshop_flag = True
        if Office_flag == False:
            matches = re.findall(Office_patter, item)
            if matches:
                total_data['tag']['ability'].append('Office')
                Office_flag = True
        if NCRE_flag == False:
            matches = re.findall(NCRE_patter, item)
            if matches:
                total_data['tag']['ability'].append('计算机等级考试')
                total_data['score'] += info.score_map()['NCRE']
                NCRE_flag = True


def handle_job_fit(total_data, tokenizer2, model2):
    work_time = total_data['tag']['total_work_time']
    edu = total_data['basic_data']['edu'] if 'edu' in total_data['basic_data'] else ''
    experience = ''.join(total_data['experience'])
    office_ability = True if re.findall(
        '|'.join(info.self_ability()[3]), ''.join(total_data['ability'])) else False
    age = total_data['basic_data']['age'] if 'age' in total_data['basic_data'] else 0

    predicted_labels = predict.job_predict(experience, tokenizer2, model2)
    for key in predicted_labels:
        # 概率比 '暂无' 更小的就没有匹配的必要
        if key == 0:
            break
        if info.edu_map()[edu] >= info.edu_map()[info.job_fit()[key][1]] and work_time >= info.job_fit()[key][2] and \
                (office_ability or not office_ability and info.job_fit()[key][3]) and age >= info.job_fit()[key][4]:
            total_data['job_fit'].append(info.job_fit()[key][0])
    if len(total_data['job_fit']) == 0:
        total_data['job_fit'].append('暂无')
