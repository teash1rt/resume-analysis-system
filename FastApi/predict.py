import torch


# 分类预测
def label_predict(sentence, tokenizer1, model1):
    encoding = tokenizer1.encode_plus(
        sentence,
        add_special_tokens=True,
        padding='max_length',
        truncation=True,
        max_length=128,
        return_tensors='pt'
    )

    input_ids = encoding['input_ids'].squeeze()
    attention_mask = encoding['attention_mask'].squeeze()
    with torch.no_grad():
        outputs = model1(input_ids.unsqueeze(
            0), attention_mask=attention_mask.unsqueeze(0))
        logits = outputs.logits

    _, predicted_label = torch.max(logits, dim=1)

    return predicted_label.item()


# 岗位匹配
def job_predict(sentence, tokenizer2, model2):
    encoding = tokenizer2.encode_plus(
        sentence,
        add_special_tokens=True,
        padding='max_length',
        truncation=True,
        max_length=512,
        return_tensors='pt'
    )

    input_ids = encoding['input_ids'].squeeze()
    attention_mask = encoding['attention_mask'].squeeze()
    with torch.no_grad():
        outputs = model2(input_ids.unsqueeze(
            0), attention_mask=attention_mask.unsqueeze(0))
        logits = outputs.logits

    # 获取预测的标签和对应的预测概率值
    predicted_probs = torch.softmax(logits, dim=1)
    # predicted_label = torch.argmax(predicted_probs, dim=1)

    predicted_labels = {}
    for index, value in enumerate(predicted_probs.squeeze().tolist()):
        # 设置概率阈值 超过该阈值的可以作为候选项 此处 0.1 较合理
        if value >= 0.1:
            predicted_labels[index] = value

    return dict(sorted(predicted_labels.items(), key=lambda x: x[1], reverse=True))


# 命名实体预测
def ner_predict(sentence, tokenizer3, model3):
    if len(sentence) > 500:
        return [[] for _ in range(3)]
    inputs = tokenizer3.encode_plus([sentence],
                                    truncation=True,
                                    padding=True,
                                    return_tensors='pt',
                                    is_split_into_words=True)
    with torch.no_grad():
        outputs = model3(inputs)
    preds = outputs.argmax(dim=2)[0]
    result = ''
    res = [[] for _ in range(3)]
    tmp = ''
    current_flag = -1

    for i in range(len(preds)):
        if inputs['attention_mask'][0][i] == 1:
            result += tokenizer3.decode(inputs['input_ids'][0][i])+' '
            result += str(preds[i].item())+' '
            num = preds[i].item()
            # num 不为0和7和#表示该词为关键词
            if (num != 0 and num != 7 and num != '#'):
                # 关键词开始为奇数
                if (num & 1):
                    # 将形如 广5东6广5州6 拆成两个词
                    if (len(tmp) > 1):
                        res[(current_flag-1)//2].append(tmp)
                        tmp = ''
                    current_flag = num
                    tmp += tokenizer3.decode(inputs['input_ids'][0][i])
                    # 防止形如 X4X4 出现
                elif (num & 1 == 0 and current_flag != -1):
                    tmp += tokenizer3.decode(inputs['input_ids'][0][i])
            else:
                if (len(tmp) > 1):
                    # current_flag 1对应姓名下标0，3对应组织下表1，5对应地点下表2
                    res[(current_flag-1)//2].append(tmp)
                    tmp = ''
                    current_flag = -1
    return res
