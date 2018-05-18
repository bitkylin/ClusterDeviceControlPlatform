from datetime import timedelta
import random


def gen_charge(i, start):
    return {'Time': (start + timedelta(days=i // 3)
                     + timedelta(hours=random.randint(1, 8))
                     + timedelta(minutes=random.randint(0, 59))
                     + timedelta(seconds=random.randint(0, 59))),
            'Status': random.randint(1, 3)}


def gen_work(i, start):
    return {'Time': (start + timedelta(days=i // 3)
                     + timedelta(hours=random.randint(0, 8))
                     + timedelta(minutes=random.randint(0, 59))
                     + timedelta(seconds=random.randint(0, 59))),
            'Status': random.randint(1, 10)}


class HistoryItem:
    def __init__(self, start, count):
        self.ChargeStatus = [gen_charge(i, start + timedelta(hours=i % 3 * 8)) for i in range(0, count * 3)]
        self.WorkStatus = [gen_work(i, start + timedelta(hours=i % 3 * 8)) for i in range(0, count * 3)]
