from utils import device_create
from utils import namecreater
from datetime import datetime
import random

db = device_create.get_creator("202.193.56.205", "bitkyTest")
device = db.Device
employee = db.Employee
device.drop()
employee.drop()

# 生成并插入 device 集合
result = device.insert_many(
    [{'GroupId': group_id,
      'DeviceId': device_id,
      'ChargeStatus': 0,
      'WorkStatus': 0,
      'ChargeStatusTime': datetime.utcnow(),
      'WorkStatusTime': datetime.utcnow(),
      'RemainChargeTime': 500,
      'CardNumber': hex(random.randint(1, 0xFFFFFFFF))[2:]}
     for group_id in range(1, 101)
     for device_id in range(1, 101)])

# 从数据库中获取到 device 集合
device_list = [device.find_one({'_id': device_id}) for device_id in result.inserted_ids]

# 插入完整的 employee 并更新为完整的 device
for device_item in device_list:
    employee_item = namecreater.random_employee_from_device(device_item)
    employee_item_result = employee.insert_one(employee_item)
    device.update_one(device_item, {'$set': {'EmployeeObjectId': str(employee_item_result.inserted_id)}})
