# Word Count Program

Program menghitung jumlah kata unik memanfaatkan MapReduce pada Hadoop.


## Requirements

- Hadoop HDFS dan Yarn berjalan dengan baik
- Semua konfigurasi pada `mapred-site.xml` telah mengikuti panduan [Instalasi Hadoop](https://gist.github.com/addingama/f665914340ec26f7efa80e86f53622e1).
- Sudah mengakses Ubuntu Server melalui terminal Windows menggunakan command `ssh -p 22 username@ipubuntuserver` 
  Contohnya `ssh -p 22 hadoop@192.168.85.3`, IP tersebut di dapatkan setelah mengikuti [Instalasi Hadoop](https://gist.github.com/addingama/f665914340ec26f7efa80e86f53622e1).
  
  
## Tahapan

1. Membuat folder input pada HDFS

    ```
      hdfs dfs -mkdir -p /word_count/input
    ```

    ![mapred_3](https://user-images.githubusercontent.com/2896774/62615928-d74e9980-b8fd-11e9-9216-ddab423d1209.png)

  
2. Membuat folder untuk menyimpan source code `WordCount.java` pada Ubuntu Server

    ```
      mkdir ~/workspace
      cd ~/workspace
      mkdir word_count
      cd word_count
    ```

    Full path untuk source code adalah `~/workspace/word_count`
  
3. Membuat file input dengan nama `words.txt` di dalam folder `word_count`

    ```
      nano words.txt
    ```

    Kemudian isikan dengan random word atau menggunakan nama Negara. Kemudian Save file tersebut.

    ![mapred_1](https://user-images.githubusercontent.com/2896774/62614138-593cc380-b8fa-11e9-918d-020454f7ee90.png)
  
4. Mengupload file `words.txt` ke `HDFS`

    ```
      hdfs dfs -put ~/workspace/word_count/words.txt /word_count/input/words.txt
    ```

    ![mapred_4](https://user-images.githubusercontent.com/2896774/62616108-47f5b600-b8fe-11e9-98f7-f175e9ef7480.png)


5. Menuliskan code `WordCount.java` ke dalam Ubuntu Server menggunakan nano command

    ```
      nano ~/workspace/word_count/WordCount.java
    ```

    Paste code dari situs [Hadoop Documentation](https://hadoop.apache.org/docs/r3.2.0/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html#Usage)

    ![mapred_5](https://user-images.githubusercontent.com/2896774/62616222-868b7080-b8fe-11e9-97d1-409cf516caf4.png)

  
  
6. Compile `WordCount.java`

    ```
      cd ~/workspace/word_count/
      hadoop com.sun.tools.javac.Main WordCount.java
    ```

    Perintah tersebut akan menghasilkan `3 buah file` dengan extensi `.class`

    ![mapred_6](https://user-images.githubusercontent.com/2896774/62616602-82ac1e00-b8ff-11e9-84f4-49708cf02fbd.png)

7. Generate `wc.jar` 
  
    ```
      jar cf wc.jar WordCount*.class
    ```

    ![mapred_7](https://user-images.githubusercontent.com/2896774/62616810-f3533a80-b8ff-11e9-8a38-f3f078d4742f.png)

8. Run file `wc.jar` menggunakan Hadoop. 
    Asumsi
    Input path `/word_count/input`
    Output path `/word_count/output` -> Pastikan folder ini belum ada di HDFS

    ```
       hadoop jar wc.jar WordCount /word_count/input /word_count/output
    ```

    Berikut hasil perintah di atas

    ```
      hadoop@dts:~/workspace/word_count$ hadoop jar wc.jar WordCount /word_count/input /word_count/outpput
      2019-08-07 10:45:50,982 WARN util.NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
      2019-08-07 10:45:52,366 INFO client.RMProxy: Connecting to ResourceManager at /0.0.0.0:8032
      2019-08-07 10:45:52,997 WARN mapreduce.JobResourceUploader: Hadoop command-line option parsing not performed. Implement the Tool interface and execute your application with ToolRunner to remedy this.
      2019-08-07 10:45:53,019 INFO mapreduce.JobResourceUploader: Disabling Erasure Coding for path: /tmp/hadoop-yarn/staging/hdfs/.staging/job_1565173480659_0001
      2019-08-07 10:45:53,462 INFO input.FileInputFormat: Total input files to process : 1
      2019-08-07 10:45:53,628 INFO mapreduce.JobSubmitter: number of splits:1
      2019-08-07 10:45:53,815 INFO Configuration.deprecation: yarn.resourcemanager.system-metrics-publisher.enabled is deprecated. Instead, use yarn.system-metrics-publisher.enabled
      2019-08-07 10:45:54,246 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1565173480659_0001
      2019-08-07 10:45:54,250 INFO mapreduce.JobSubmitter: Executing with tokens: []
      2019-08-07 10:45:54,626 INFO conf.Configuration: resource-types.xml not found
      2019-08-07 10:45:54,631 INFO resource.ResourceUtils: Unable to find 'resource-types.xml'.
      2019-08-07 10:45:55,189 INFO impl.YarnClientImpl: Submitted application application_1565173480659_0001
      2019-08-07 10:45:55,347 INFO mapreduce.Job: The url to track the job: http://dts:8088/proxy/application_1565173480659_0001/
      2019-08-07 10:45:55,352 INFO mapreduce.Job: Running job: job_1565173480659_0001
      2019-08-07 10:46:10,808 INFO mapreduce.Job: Job job_1565173480659_0001 running in uber mode : false
      2019-08-07 10:46:10,815 INFO mapreduce.Job:  map 0% reduce 0%

      2019-08-07 10:46:20,979 INFO mapreduce.Job:  map 100% reduce 0%
      2019-08-07 10:46:30,107 INFO mapreduce.Job:  map 100% reduce 100%
      2019-08-07 10:46:30,162 INFO mapreduce.Job: Job job_1565173480659_0001 completed successfully
      2019-08-07 10:46:30,367 INFO mapreduce.Job: Counters: 54
              File System Counters
                      FILE: Number of bytes read=106
                      FILE: Number of bytes written=444679
                      FILE: Number of read operations=0
                      FILE: Number of large read operations=0
                      FILE: Number of write operations=0
                      HDFS: Number of bytes read=210
                      HDFS: Number of bytes written=72
                      HDFS: Number of read operations=8
                      HDFS: Number of large read operations=0
                      HDFS: Number of write operations=2
                      HDFS: Number of bytes read erasure-coded=0
              Job Counters
                      Launched map tasks=1
                      Launched reduce tasks=1
                      Data-local map tasks=1
                      Total time spent by all maps in occupied slots (ms)=7917
                      Total time spent by all reduces in occupied slots (ms)=5661
                      Total time spent by all map tasks (ms)=7917
                      Total time spent by all reduce tasks (ms)=5661
                      Total vcore-milliseconds taken by all map tasks=7917
                      Total vcore-milliseconds taken by all reduce tasks=5661
                      Total megabyte-milliseconds taken by all map tasks=8107008
                      Total megabyte-milliseconds taken by all reduce tasks=5796864
              Map-Reduce Framework
                      Map input records=11
                      Map output records=11
                      Map output bytes=141
                      Map output materialized bytes=106
                      Input split bytes=113
                      Combine input records=11
                      Combine output records=7
                      Reduce input groups=7
                      Reduce shuffle bytes=106
                      Reduce input records=7
                      Reduce output records=7
                      Spilled Records=14
                      Shuffled Maps =1
                      Failed Shuffles=0
                      Merged Map outputs=1
                      GC time elapsed (ms)=189
                      CPU time spent (ms)=2370
                      Physical memory (bytes) snapshot=362192896
                      Virtual memory (bytes) snapshot=5402312704
                      Total committed heap usage (bytes)=180224000
                      Peak Map Physical memory (bytes)=232505344
                      Peak Map Virtual memory (bytes)=2697916416
                      Peak Reduce Physical memory (bytes)=129687552
                      Peak Reduce Virtual memory (bytes)=2704396288
              Shuffle Errors
                      BAD_ID=0
                      CONNECTION=0
                      IO_ERROR=0
                      WRONG_LENGTH=0
                      WRONG_MAP=0
                      WRONG_REDUCE=0
              File Input Format Counters
                      Bytes Read=97
              File Output Format Counters
                      Bytes Written=72
      hadoop@dts:~/workspace/word_count$

    ```

    Hasil eksekusi MapReduce dapat dilihat pada HDFS Browser

    ![mapred_8](https://user-images.githubusercontent.com/2896774/62617278-0adef300-b901-11e9-8bff-a0ec72a88b86.png)

9. Melihat hasil eksekusi `WordCount.java`

    ```
      hdfs dfs -cat /word_count/output/part-r-00000
    ```
    
    ![mapred_9](https://user-images.githubusercontent.com/2896774/62617587-bc7e2400-b901-11e9-81c9-f2b87b578682.png)
