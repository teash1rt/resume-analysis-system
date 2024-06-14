db.createCollection
("loc_coll")
 db.loc_coll.insertMany([
     {location: "北京", count: 0},
     {location: "天津", count: 0},
     {location: "河北", count: 0},
     {location: "山西", count: 0},
     {location: "内蒙古", count: 0},
     {location: "辽宁", count: 0},
     {location: "吉林", count: 0},
     {location: "黑龙江", count: 0},
     {location: "上海", count: 0},
     {location: "江苏", count: 0},
     {location: "浙江", count: 0},
     {location: "安徽", count: 0},
     {location: "福建", count: 0},
     {location: "江西", count: 0},
     {location: "山东", count: 0},
     {location: "河南", count: 0},
     {location: "湖北", count: 0},
     {location: "湖南", count: 0},
     {location: "广东", count: 0},
     {location: "广西", count: 0},
     {location: "海南", count: 0},
     {location: "重庆", count: 0},
     {location: "四川", count: 0},
     {location: "贵州", count: 0},
     {location: "云南", count: 0},
     {location: "西藏", count: 0},
     {location: "陕西", count: 0},
     {location: "甘肃", count: 0},
     {location: "青海", count: 0},
     {location: "宁夏", count: 0},
     {location: "新疆", count: 0},
     {location: "台湾", count: 0},
     {location: "香港", count: 0},
     {location: "澳门", count: 0}
 ])

 db.createCollection("edu_coll")
 db.edu_coll.insertMany([
     {education: "小学", count: 0},
     {education: "初中", count: 0},
     {education: "高中", count: 0},
     {education: "中专", count: 0},
     {education: "大专", count: 0},
     {education: "本科", count: 0},
     {education: "学士", count: 0},
     {education: "硕士", count: 0},
     {education: "博士", count: 0}
 ])

db.createCollection("experience_coll")
db.experience_coll.insertOne({
    rids:[],
    total_work_time:[],
    total_company_count:[]
})