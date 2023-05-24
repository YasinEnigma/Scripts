# Sales Country

Program menghitung jumlah sales berdasarkan Negara


Sumber Contoh Program : [https://www.guru99.com/create-your-first-hadoop-program.html](https://www.guru99.com/create-your-first-hadoop-program.html)
Telah dimodifikasi agar sesuai dengan Hadoop 3.2.0

Sumber Data : [SalesJan2009.csv](https://drive.google.com/uc?export=download&id=1tP8AJGSgDXwI12r2Ap07GyamMj1o0iDD)
                                 
## Tahapan

1. Buat folder pada HDFS untuk menyimpan input dan output files. Jalankan command berikut pada Terminal Ubuntu Server
                                 
   ```
    hdfs dfs -mkdir -p /sales_country/input
   ```
2. Buat folder baru pada ubuntu server dengan path `~/workspace/sales`
  
    ```
      mkdir ~/workspace
      mkdir ~/workspace/sales
    ```
   
3. Download file `SalesJan2009.csv` ke folder `Downloads` pada Windows Anda

4. Buka terminal dan masuk ke folder download `cd Downloads`

5. Updload file `SalesJan2009.csv` ke Ubuntu VM menggunakan perintah SCP ke dalam folder `~/workspace/sales`.
   
   ```
    scp SalesJan2009.csv hadoop@192.168.85.3:~/workspace/sales
   ```
   
   Anda dapat memastikan bahwa file telah berhasil terupload ke ubuntu server dengan menjalankan command berikut
   
   ```
    ls -al ~/workspace/sales
   ```
   
6. Selanjutnya dapat mengikuti [Panduan Menjalankan MapReduce](https://gist.github.com/addingama/b0089c9244aeeeffca568239768eb059#file-word_count_mapred-md) dengan menyesuaikan nama class, jar, dan path folder


## Source Code SalesCountry.java

```
  /*
    Import library dari Java Package 
  */ 
  
  import java.io.IOException; 
  import java.util.StringTokenizer; 
  /*
    Import library dari Hadoop Package untuk menjalankan fungsi Pembacaan, Penulisan File ke dalam HDFS dan
  menjalankan MapReduce 
  */ 
  import org.apache.hadoop.conf.Configuration; 
  import org.apache.hadoop.fs.Path; 
  import org.apache.hadoop.io.IntWritable; 
  import org.apache.hadoop.io.LongWritable; 
  import org.apache.hadoop.io.Text;
  import org.apache.hadoop.mapreduce.Job; 
  import org.apache.hadoop.mapreduce.Mapper; 
  import org.apache.hadoop.mapreduce.Reducer; 
  import org.apache.hadoop.mapreduce.lib.input.FileInputFormat; 
  import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat; 
  
  public class SalesCountry {
  
    public static class SalesMapper extends Mapper <LongWritable, Text, Text, IntWritable> {
    
      private final static IntWritable one = new IntWritable(1);
      
      public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // Membaca 1 baris string dengan tanda , sebagai pemisah
        String valueString = value.toString();
        
        // Memisahkan string menjadi array of string dengan tanda koma sebagai pemisah
        String[] SingleCountryData = valueString.split(",");
        
        // Mengambil data negara, data negara terdapat pada kolom ke 8 atau index 7
        context.write(new Text(SingleCountryData[7]), one);
      }
    }

    public static class SalesCountryReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    
      public void reduce(Text t_key, Iterable<IntWritable> values, Context context) throws IOException,
  InterruptedException {
  
        Text key = t_key;
        
        int frequencyForCountry = 0;
        
        for (IntWritable val: values) {
          // frequencyForCountry = frequencyForCountry + val.get()
          frequencyForCountry += val.get();
        }
        context.write(key, new IntWritable(frequencyForCountry));
      }
    }

    // Pada dasarnya penjelasannya sama dengan program WordCount.java, yang menjadi pembeda adalah nama-nama class Mapper, Combiner dan Reducer yang digunakan
    public static void main(String[] args) throws Exception {
          Configuration conf = new Configuration();
          Job job = Job.getInstance(conf, "sales country");
          job.setJarByClass(SalesCountry.class);
          job.setMapperClass(SalesMapper.class);
          job.setCombinerClass(SalesCountryReducer.class);
          job.setReducerClass(SalesCountryReducer.class);
          job.setOutputKeyClass(Text.class);
          job.setOutputValueClass(IntWritable.class);
          FileInputFormat.addInputPath(job, new Path(args[0]));
          FileOutputFormat.setOutputPath(job, new Path(args[1]));
          System.exit(job.waitForCompletion(true) ? 0 : 1);
      }
  }
```

Berikut hasil running code tersebut

![mapred_10](https://user-images.githubusercontent.com/2896774/62673708-06611b80-b98f-11e9-82ec-ab3b3efb1ac2.png)


![mapred_11](https://user-images.githubusercontent.com/2896774/62673795-4fb16b00-b98f-11e9-92a4-083024af0a74.png)
