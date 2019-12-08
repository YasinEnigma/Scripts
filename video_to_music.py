import os 

try :
    url = input("please enter your file path : ")
    new_file = input("please enter you new file path: ")

    os.system("ffmpeg -i {} {}".format(url, new_file))

except :
    raise Exception("The input data is invalid!")

print("\n\n==========================================\n\n")
print("The Process Ended!")
