import jwt
from datetime import datetime, timedelta


# 生成 JWT
def create_jwt(secret_key):
    expire_time = datetime.utcnow() + timedelta(seconds=10)
    payload = {'Author': 'Teashirt', 'exp': expire_time}
    jwt_token = jwt.encode(payload, secret_key, algorithm='HS256')
    return jwt_token


# 验证 JWT
def verify_jwt(Authorization, secret_key):
    try:
        jwt_token = Authorization[7:]
        # 使用密钥验证 JWT
        payload = jwt.decode(jwt_token, secret_key, algorithms=['HS256'])
        # 检查 JWT 是否在有效期内
        if datetime.utcnow() <= datetime.fromtimestamp(payload['exp']):
            return True
    except:
        pass
    return False


# 规范时间格式
def re_date(date):
    date = date.replace('年', '.').replace('月', '')
    if date[-1:] == '年':
        date = date[:-1]
    if '.' not in date:
        date = date + '.01'
    return date


# 计算时间差
def calculate_date_interval(date1, date2):
    date1_obj = datetime.strptime(date1, '%Y.%m')
    date2_obj = datetime.strptime(date2, '%Y.%m')
    diff = date2_obj - date1_obj
    years = diff.days // 365
    months = (diff.days % 365) // 30
    return years, months
