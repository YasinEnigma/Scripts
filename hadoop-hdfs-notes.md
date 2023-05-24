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
  