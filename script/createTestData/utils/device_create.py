from pymongo import MongoClient
import socket


def get_creator(hostname, database):
    """根据已有的IP地址及端口号创建 MongoDB 客户端"""
    client = MongoClient(socket.gethostbyname(hostname), 27017)
    return client[database]
