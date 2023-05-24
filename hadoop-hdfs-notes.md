# Hadoop Notes

## HDFS

### Commands

- Check hdfs directory content on root directory `/`


  ```
    hdfs dfs -ls /
  ```
  
- Create a new folder, you need to provide a full path for the directory

  ```
    hdfs dfs -mkdir /hadoop
  ```
  
  If you want to create directory inside directory, provide full path with `-p` parameter. `-p` will auto create missing directory.
  
  ```
    hdfs dfs -mkdir -p /custom/directory
  ```
  
- Remove hdfs directory 


  ```
    hdfs dfs -rm -r /dir_name
  ```
  
- Remove hdfs file 


  ```
    hdfs dfs -rm /file_name
  ```
  
- Add  (put) file from Local FileSystem to HDFS 


  ```
    hdfs dfs -put filename_in_fs /filename_in_hdfs
  ```
  
- Get file from HDFS to local FileSystem


  ```
    hdfs dfs -get /filename_in_hdfs filename_in_fs
  ```
  
- Namenode format


  ```
    hdfs namenode -format
  ```
  
- Print a file content


  ```
    hdfs dfs -cat /filename_in_hdfs | head
  ```
  
  
