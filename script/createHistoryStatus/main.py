from utils import device_create
from utils.history_item import HistoryItem
from datetime import datetime

db = device_create.get_creator("202.193.57.131", "bitkyTest")
LampStatusHistory = db.LampStatusHistory
LampStatusHistory.drop()
device_list = []
employees = db.Employee.find()
print('共需执行次数：' + str(employees.count()))
print('开始执行...')
i = 1
for employee in employees:
    if i % 100 == 0:
        print('正在执行第' + str(i) + '次')
    i += 1
    hisItem = {'_id': employee['_id']}
    hisInit = HistoryItem(datetime(2015, 4, 19), 100)
    hisItem['ChargeStatus'] = hisInit.ChargeStatus
    hisItem['WorkStatus'] = hisInit.WorkStatus
    device_list.append(hisItem)
print('正在向数据库写入数据...')
result = LampStatusHistory.insert_many(device_list)
print('写入完毕')
pass
