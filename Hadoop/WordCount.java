/*
  Baris 5 - 6
  Import library dari Java Package
*/
import java.io.IOException;
import java.util.StringTokenizer;

/*
  Baris 12 - 20
  Import library dari Hadoop Package untuk menjalankan fungsi Pembacaan, Penulisan File ke dalam HDFS dan menjalankan MapReduce
*/
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

// Deklarasi Class WordCount, nama file sebaiknya sama dengan nama Class.
public class WordCount {

  /*
    Inner class untuk pemetaan kata yang meng-extend class Mapper.
    https://hadoop.apache.org/docs/r3.2.0/api/org/apache/hadoop/mapreduce/Mapper.html
    
    Class Mapper<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
    
    Dalam hal ini, Key Input berupa Object, Value Input berupa Text, Key Output Berupa Text dan Value Out berupa Bilangan Bulat.
    
    Berdasarkan percobaan saya mengganti Text dengan String, program MapReduce tidak berhasil dijalankan.
    Kemungkinan besar native type seperti String dan Integer dll tidak dapat langsung digunakan.
    Tipe data yang dapat digunakan dapat dilihat pada link berikut : https://hadoop.apache.org/docs/r3.2.0/api/org/apache/hadoop/io/
    Sepertinya tipe data di Java sudah di ubah pada Hadoop dan diberi akhiran Writeable
  */
  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, IntWritable>{

    // Deklarasi dan instansiasi variable dengan nama one sebagai nilai default untuk jumlah kata yang masuk ke dalam Mapper
    private final static IntWritable one = new IntWritable(1);
    
    // Deklarasi dan instansiasi variable dengan nama word dengan tipe Text yang akan digunakan untuk menyimpan kata pada Mapper
    // Jika ingin mengubah Text menjadi String, maka VALUEIN dan KEYOUT harus menggunakan String agar tipe data nya konsisten.
    private Text word = new Text();

    /*
      Override fungsi map yang ada pada Class Mapper
      
      protected void map(KEYIN key,
                   VALUEIN value,
                   org.apache.hadoop.mapreduce.Mapper.Context context)
                   
      KEYIN, dan VALUEIN harus memiliki tipe yang sama sesuai dengan deklarasi di atas yakni Object dan Text.
     
    */
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      
      /*
        https://docs.oracle.com/javase/7/docs/api/java/util/StringTokenizer.html
        Class StringTokenizer digunakan untuk melakukan pemisahan kata dan menggunakan tanda spasi sebagai pemisah.
        
        Deklarasi dan instansiasi StringTokenizer. itr akan bertipe Iteratable yang dapat ditelusuri menggunakan looping
      */
      StringTokenizer itr = new StringTokenizer(value.toString());
      
      
      // Melakukan looping selama itr masih memiliki token
      while (itr.hasMoreTokens()) {
        // set Text word berdasarkan nilai yang ada pada itr
        word.set(itr.nextToken());
        /*
          Kirimkan data yang bertipe sesuai dengan deklarasi kelas ke konteks MapReduce
          KEYOUT = Text
          VALUEOUT = IntWrteable
        */
        context.write(word, one);
      }
    }
  }

  /*
    Inner class untuk melakukan penghitungan jumlah kata unik yang meng-extend class Reducer.
    https://hadoop.apache.org/docs/r3.2.0/api/org/apache/hadoop/mapreduce/Reducer.html
    
    Class Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
    
    KEYIN = Text
    VALUEIN = IntWriteable
    Kedua tipe data tersebut harus sesuai dengan tipe data KEYOUT dan VALUEOUT dari class Mapper
    
    KEYOUT = Text
    VALUEOUT = IntWriteable
  */
  public static class IntSumReducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    
    // Deklarasi dan instansiasi variable dengan nama result untuk menyimpan hasil penjumlahan kata unik
    private IntWritable result = new IntWritable();

    /*
      Override fungsi reduce yang ada pada Class Reducer
      
      protected void reduce(KEYIN key,
                      Iterable<VALUEIN> values,
                      org.apache.hadoop.mapreduce.Reducer.Context context)
                      
      VALUEIN merupakan kumpulan angka yang telah dihasilkan dari proses mapper, dimana satu buah key dapat memiliki beberapa value
    */
    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      // Untuk setiap proses reduce, jumlah awal di set menjadi 0
      int sum = 0;
      // loop sebanyak data yang ada pada values
      for (IntWritable val : values) {
        /*
          Lakukan penjumlahan,
          
          sum = sum + val.get()
        */
        sum += val.get();
      }
      
      // Set hasil akhir penjumlahan ke dalam variable result
      result.set(sum);
      
      /*
          Kirimkan data yang bertipe sesuai dengan deklarasi kelas ke konteks MapReduce
          KEYOUT = Text
          VALUEOUT = IntWrteable
        */
      context.write(key, result);
    }
  }

  // Fungsi utama program java
  public static void main(String[] args) throws Exception {
    /*
      http://hadoop.apache.org/docs/r3.2.0/api/org/apache/hadoop/conf/Configuration.html
      Deklarasi dan instansiasi variable conf
      
      Melalui variable conf, kita dapat mengubah configurasi hadoop yang ada pada core-site.xml melalui program.
    */
    Configuration conf = new Configuration();
    /*
      https://hadoop.apache.org/docs/r3.2.0/api/org/apache/hadoop/mapreduce/Job.html
      
      Membuat instance job dengan nama "word count" menggunakan pengaturan default
      
      Melalui variable job tersebut, dapat dilakukan pengaturan, mapper, reducer, tipe key dan output
    */
    Job job = Job.getInstance(conf, "word count");
    // Menetapkan Class utama yang akan dijalankan
    job.setJarByClass(WordCount.class);
    // Menetapkan Mapper class yang akan digunakan untuk proses mapping
    job.setMapperClass(TokenizerMapper.class);
    /*
      Combiner class adalah kelas opsional yang berada di antara Map dan Reduce
      Menetapkan IntSumReducer sebagai combiner class dan reducer class
    */
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    // Tipe data KeyOut dan ValueOut harus di tetapkan pada level job dan sesuai dengan tipe pada output reducer 
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    // Menambahkan path input berdasarkan parameter pertama pada command line saat menjalankan WordCount.java
    FileInputFormat.addInputPath(job, new Path(args[0]));
    // Menambahkan path output berdasarkan parameter kedua pada command line saat menjalankan WordCount.java
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    // Program akan selesai jika job telah selesai
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}