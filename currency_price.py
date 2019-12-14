import requests
import schedule 
import time 
import os 
import platform 
import sys

# Detect Operation System
os_name = platform.system()

if os_name == "Linux":
    os.system("clear")
elif os_name == "Windows":
    os.system("cls")
elif os_name == "Darwin":
    os.system("clear")

# API link
api_url = "https://currency.jafari.pw/json"

def get_price():
    response = requests.get(api_url).json()
    print(f"------- {response['Currency'][0]['Currency']} => Selled: {response['Currency'][0]['Sell']} & Buyed: {response['Currency'][0]['Buy']}")

# Task scheduling 
schedule.every(10).seconds.do(get_price)

while True:
    schedule.run_pending()
    time.sleep(2)
