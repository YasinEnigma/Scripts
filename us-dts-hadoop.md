# Install Hadoop to Ubuntu Server 18.04

## Environments
- Microsoft Windows 10
- Virtualbox 6.0

## Requirements
- Ubuntu Server 18.04 already installed.

## Virtual Box Network Confirguration

- Create a new `Host Network` adapter from Virtual Box
    ![vbox_host_manager_1](https://user-images.githubusercontent.com/2896774/62597543-ede0fa80-b8d5-11e9-9fd4-6c0f013ac7ee.png)
- Fill the IP values to follow below image
    ![vbox_host_manager_2](https://user-images.githubusercontent.com/2896774/62597578-05b87e80-b8d6-11e9-88d2-fad0714d4c42.png)
- Update VM network configuration by clicking `Gear` icon and select Tab `Network`.

    Make sure the first Adapter set to `NAT`
   
   ![vbox_host_manager_3](https://user-images.githubusercontent.com/2896774/62597740-7e1f3f80-b8d6-11e9-99f9-6718dc4d2dcd.png)
   
   And make sure the second adapter to use `Host Only` Network
    
   ![vbox_host_manager_4](https://user-images.githubusercontent.com/2896774/62597774-9abb7780-b8d6-11e9-9561-dce40af5f74e.png)

- Check network configuration
    
    - Start our VM then login
    - Execute this command `if config`, make sure it listed 3 network adapters. The network name maybe different. One of the network should have IP `192.168.85.*`
    
        ![vbox_host_manager_5](https://user-images.githubusercontent.com/2896774/62598014-672d1d00-b8d7-11e9-8803-8580e7a8d7ae.png)

        This configuration will allow the Windows Host to access VM and VM can access the internet from Windows Host.
        
    - To login to VM from windows host, we can use `command prompt` and type `ssh hadoop@192.168.85.3`
        ![vbox_host_manager_6](https://user-images.githubusercontent.com/2896774/62598300-7a8cb800-b8d8-11e9-97b4-046aa047136c.png)
        
        

## Installation

### Java


1. Prerequisite

    Before begining the installation run login shell as the sudo user and update the current packages installed

    ```
      sudo apt update
      sudo apt upgrade
    ```

2. Install Java 11 on Ubuntu 18.04

    You need to add the following PPA to your Ubuntu system. This PPA contains a package oracle-java11-installer having the Java installation script.

    ```
        sudo add-apt-repository ppa:linuxuprising/java
    ```

    Then install Java Runtime Environment (JRE)

    ```
      sudo apt install default-jre
    ```

    Install Java Development Kit (JDK)

    ```
      sudo apt install default-jdk
    ```

    Both this command will automatically add java executable into environment variable. 
    To be save, reload the environment variables using `source ~/.bashrc`.
    To check JRE is working, please run `java` or `java --version` command on terminal.
    
    ![vbox_host_manager_7](https://user-images.githubusercontent.com/2896774/62598441-eb33d480-b8d8-11e9-96e7-c8f50ee64e3d.png)

    To check JDK is working, please run `javac` command on terminal.
    
    ![vbox_host_manager_7](https://user-images.githubusercontent.com/2896774/62598441-eb33d480-b8d8-11e9-96e7-c8f50ee64e3d.png)


### Hadoop

1. Create user for Hadoop

    We recommend creating a normal (nor root) account for Hadoop working. To create an account using the following command.

    ```
      adduser hadoop
    ```

    After creating the account, it also required to set up key-based ssh to its own account. To do this use execute following commands.

    ```
      su - hadoop
      ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa
      cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
      chmod 0600 ~/.ssh/authorized_keys
    ```

    Now, SSH to localhost with Hadoop user. This should not ask for the password but the first time it will prompt for adding RSA to the list of known hosts.

    ```
    ssh localhost
    exit
    ```

2.  Download Hadoop Source Archive

    In this step, download hadoop 3.2.0 source archive file using below command.
    
    ```
        cd ~
        wget http://www-eu.apache.org/dist/hadoop/common/hadoop-3.2.0/hadoop-3.2.0.tar.gz
        tar xzf hadoop-3.2.0.tar.gz
        mv hadoop-3.2.0 hadoop
    ```
 
 3. Setup Hadoop Pseudo-Distributed Mode
 
    Setup the environment variables used by the Hadoop. Edit `~/.bashrc` file and append following values at end of file.
    
    ```
        export JAVA_HOME=/usr/lib/jvm/default-java
        export HADOOP_HOME=/home/hadoop/hadoop
        export HADOOP_INSTALL=$HADOOP_HOME
        export HADOOP_MAPRED_HOME=$HADOOP_HOME
        export HADOOP_COMMON_HOME=$HADOOP_HOME
        export HADOOP_HDFS_HOME=$HADOOP_HOME
        export YARN_HOME=$HADOOP_HOME
        export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native
        export HADOOP_CONFIG_DIR=$HADOOP_HOME/share/hadoop/common/lib
        export PATH=$PATH:$HADOOP_HOME/sbin:$HADOOP_HOME/bin
    ```
    
    Then, apply the changes in the current running environment
    
    ```
        source ~/.bashrc
    ```
    
    Now edit `$HADOOP_HOME/etc/hadoop/hadoop-env.sh` file and set `JAVA_HOME` environment variable. Change the JAVA path as per install on your system. This path may vary as per your operating system version and installation source. So make sure you are using the correct path.
    
    ```
        nano $HADOOP_HOME/etc/hadoop/hadoop-env.sh
    ```
    
    Then add this line
    
    ```
        export JAVA_HOME=/usr/lib/jvm/default-java
        export HADOOP_CLASSPATH+=" $HADOOP_CONF_DIR/lib/*.jar"
    ```
  
  4. Setup Hadoop Configuration Files
    
        Hadoop has many configuration files, which need to configure as per requirements of your Hadoop infrastructure. Let’s start with the configuration with basic Hadoop single node cluster setup. first, navigate to below location
    
      ```
          cd $HADOOP_HOME/etc/hadoop
      ```

      Edit `core-site.xml` -> `nano core-site.xml`

      ```
          <configuration>
              <property>
                <name>fs.default.name</name>
                  <value>hdfs://localhost:9000</value>
              </property>
          </configuration>
      ```

      Edit `hdfs-site.xml` -> `nano hdfs-site.xml`

      ```
          <configuration>
              <property>
               <name>dfs.replication</name>
               <value>1</value>
              </property>

              <property>
                <name>dfs.name.dir</name>
                  <value>file:///home/hadoop/hadoopdata/hdfs/namenode</value>
              </property>

              <property>
                <name>dfs.data.dir</name>
                  <value>file:///home/hadoop/hadoopdata/hdfs/datanode</value>
              </property>

              <property>
                  <name>dfs.permissions.enabled</name>
                  <value>false</value>
              </property>
          </configuration>
      ```

      Edit `mapred-site.xml` -> `nano mapred-site.xml`

      ```
          <configuration>
              <property>
                <name>mapreduce.framework.name</name>
                <value>yarn</value>
            </property>
            <property>
                <name>mapreduce.application.classpath</name>
                            <value>$HADOOP_MAPRED_HOME/share/hadoop/mapreduce/*,$HADOOP_MAPRED_HOME/share/hadoop/mapreduce/lib/*,$HADOOP_MAPRED_HOME/share/hadoop/common/*,$HADOOP_MAPRED_HOME/share/hadoop/common/lib/*,$HADOOP_MAPRED_HOME/share/hadoop/yarn/*,$HADOOP_MAPRED_HOME/share/hadoop/yarn/lib/*,$HADOOP_MAPRED_HOME/share/hadoop/hdfs/*,$HADOOP_MAPRED_HOME/share/hadoop/hdfs/lib/*</value>
            </property>
            <property>
                    <name>mapreduce.map.env</name>
                    <value>HADOOP_MAPRED_HOME=$HADOOP_MAPRED_HOME</value>
            </property>
            <property>
                    <name>mapreduce.reduce.env</name>
                    <value>HADOOP_MAPRED_HOME=$HADOOP_MAPRED_HOME</value>
            </property>
            <property>
                    <name>yarn.app.mapreduce.am.env</name>
                    <value>HADOOP_MAPRED_HOME=$HADOOP_MAPRED_HOME</value>
            </property>
          </configuration>
      ```

      Edit `yarn-site.xml` -> `nano yarn-site.xml`

      ```
         <configuration>
           <property>
            <name>yarn.nodemanager.aux-services</name>
              <value>mapreduce_shuffle</value>
           </property>
          </configuration>
      ```
  
  5. Format Namenode
  
       Now format the namenode using the following command, make sure that Storage directory is

       ```
           hdfs namenode -format
       ```

       Sample output:

       ```
            WARNING: /home/hadoop/hadoop/logs does not exist. Creating.
            2018-05-02 17:52:09,678 INFO namenode.NameNode: STARTUP_MSG:
            /************************************************************
            STARTUP_MSG: Starting NameNode
            STARTUP_MSG:   host = tecadmin/127.0.1.1
            STARTUP_MSG:   args = [-format]
            STARTUP_MSG:   version = 3.1.2
            ...
            ...
            ...
            2018-05-02 17:52:13,717 INFO common.Storage: Storage directory /home/hadoop/hadoopdata/hdfs/namenode has been successfully formatted.
            2018-05-02 17:52:13,806 INFO namenode.FSImageFormatProtobuf: Saving image file /home/hadoop/hadoopdata/hdfs/namenode/current/fsimage.ckpt_0000000000000000000 using no compression
            2018-05-02 17:52:14,161 INFO namenode.FSImageFormatProtobuf: Image file /home/hadoop/hadoopdata/hdfs/namenode/current/fsimage.ckpt_0000000000000000000 of size 391 bytes saved in 0 seconds .
            2018-05-02 17:52:14,224 INFO namenode.NNStorageRetentionManager: Going to retain 1 images with txid >= 0
            2018-05-02 17:52:14,282 INFO namenode.NameNode: SHUTDOWN_MSG:
            /************************************************************
            SHUTDOWN_MSG: Shutting down NameNode at tecadmin/127.0.1.1
            ************************************************************/
       ```

   6. Start Hadoop Cluster
   
        Let’s start your Hadoop cluster using the scripts provides by Hadoop. Just navigate to your `$HADOOP_HOME/sbin` directory and execute scripts one by one.

        ```
            cd $HADOOP_HOME/sbin/
        ```
        
        Now execute start-dfs.sh script.
        
        ```
            ./start-dfs.sh
        ```
        
        ![vbox_host_manager_9](https://user-images.githubusercontent.com/2896774/62599124-e708b680-b8da-11e9-9fe9-113f23b46e5b.png)


        
        Then execute start-yarn.sh script.
        
        ```
            ./start-yarn.sh
        ```
        
        ![vbox_host_manager_10](https://user-images.githubusercontent.com/2896774/62599189-17e8eb80-b8db-11e9-8c10-da36257d5d98.png)
        
        
        To make it easier to start hadoop from any directory, you can create an alias by modifying `~/.bashrc` and add these lines
        
        ```
            alias hstart=$HADOOP_HOME/sbin/start-all.sh
            alias hstop=$HADOOP_HOME/sbin/stop-all.sh
        ```
        
        Then run `source ~/.bashrc`, now we can start or stop hadoop services from any directory with `hstart` or `hstop` command.
        
        

### Virtual Box

This steps only valid if you do not want to setup multiple VM on windows because the port forwarding will conflict if you duplicate the VM.

   Implement port forwarding so the windows can communicate with vbox.
    
- Open Machine setting, not Virtualbox setting
- Click tab `Network`
- On tab `Adapter 1`, make sure it's `NAT` then click `Advanced`
- Click button `Port Forwarding`
- Add new rules, leave all default value, also leave `Host IP` and `Guest IP` empty. Only edit `Host Port` and `Guest Port`. Host port is the windows and guest port for the ubuntu server.

    |  Host Port  | Guest Port  |
    |---|---|
    | 2202  | 22  |
    | 9870  | 9870  |
    | 9864  | 9864  |
    | 8088  | 8088  |
    
    For more info about available ports on Hadoop 3.2.0 can be found on this [link](all available ports for forwarding https://kontext.tech/docs/DataAndBusinessIntelligence/p/default-ports-used-by-hadoop-services-hdfs-mapreduce-yarn)

- Click `OK` button


### Access Hadoop from Windows

1. You can login to ubuntu server through windows command prompt using this command
    
    ```
       ssh hadoop@192.168.85.3
    ```
    
2. To access the Hadoop Web GUI, you can open this link through browser on your windows machine

    - [http://192.168.85.3:9870](http://192.168.85.3:9870) -> HDFS Web UI
        ![localhost_9870](https://user-images.githubusercontent.com/2896774/62596859-845fec80-b8d3-11e9-94bd-e58081c76739.png)
   
    - [http://192.168.85.3:9864](http://192.168.85.3:9864) -> DataNode Web UI
        ![localhost_9864](https://user-images.githubusercontent.com/2896774/62596815-4d89d680-b8d3-11e9-8811-19644d11ac68.png)
    
    - [http://192.168.85.3:8088](http://192.168.85.3:8088) -> Yarn Web UI
        ![localhost_8088](https://user-images.githubusercontent.com/2896774/62596751-10254900-b8d3-11e9-90fa-c62ddfe34748.jpg)
        



### Frequently Asked Question (FAQ)

- Java command not found
    
    Please run this command `echo $JAVA_HOME` to see if the java added to environment variable, if it return empty string then you have to add it on `~/.bashrc` using `nano` or other editor. 
    
    Add this line `export JAVA_HOME="/usr/lib/jvm/default-java"` -> you need to modify the path based on your java installation inside `/usr/lib/jvm`.
    
    After that, you need to reload the updated setting using this command `source ~/.bashrc`. Now the `java` command should work.
    
    If the problem still persist, please follow my Java Installation above
    
- Hadoop or hdfs command not found

    Please make sure you have these values inside your `.bashrc` file
    
    ![vbox_host_manager_11](https://user-images.githubusercontent.com/2896774/62600025-5089c480-b8dd-11e9-86f9-c6ec46127e2d.png)

    Please adjust `$HADOOP_HOME` value to your hadoop instalation path.
    
    Then reload the env file using  this command `source ~/.bashrc`, now the hdfs or hadoop command should work
    
    
    
- `Failed to retrive data from /webhdfs/v1/?op=LISTSTATUS; Server Error` when trying to browse HDFS directory on the browser

    Run this command on your terminal
    
    ```
        cd $HADOOP_HOME/share/hadoop/common/lib
        wget https://jcenter.bintray.com/javax/activation/javax.activation-api/1.2.0/javax.activation-api-1.2.0.jar
    ```
    
    Then make sure on your `~/.bashrc` contain this line `export HADOOP_CONFIG_DIR=$HADOOP_HOME/share/hadoop/common/lib`
    
    Make sure `$HADOOP_HOME/etc/hadoop/hadoop-env.sh` contain this line `export HADOOP_CLASSPATH+=" $HADOOP_CONF_DIR/lib/*.jar"`
    
    Then restart the hadoop services
    
    
- class `com.sun.tools.javac.Main` not found.

    1. Find your `java-jdk` location, usually on `/usr/lib/jvm/default-java`, if not installed you can run `sudo apt install default-jdk`
    
    2. Add this line to your `~/.bashrc`
    
        ```
            export HADOOP_CLASSPATH=$JAVA_HOME/lib/tools.jar
        ```
        
    3. Reload configuration `source ~/.bashrc`
    
    
- After cloning existing VM, the IP not changed.

    We need to renew the interface to get a new IP so that each VM can have unique IP Address.
    
    1. Check interface name using `ifconfig -a`
    
        ![a1](https://user-images.githubusercontent.com/2896774/62677552-467bca80-b99e-11e9-8cdf-672c0cf15626.png)

        Our Interface name is `enp0s8` with IP `192.168.85.3`
        
        Now we can run this command to reset our interface
        
        ```
            sudo dhclient -v -r enp0s8
            ifconfig -a
        ```
        
        ![a2](https://user-images.githubusercontent.com/2896774/62677744-e76a8580-b99e-11e9-83ab-859d37e891a7.png)

        To get a new IP, please run this command
        
        ```
            sudo dhclient enp0s8
            ifconfig -a
        ```
        
        ![a3](https://user-images.githubusercontent.com/2896774/62677893-5b0c9280-b99f-11e9-8802-e9bf446b1bb9.png)

        
        Now we have a different IP address for each VM and can communicate between them
        
        ![a4](https://user-images.githubusercontent.com/2896774/62678007-ad4db380-b99f-11e9-8785-f7d458b0ebec.png)
        
        
- How to set domain name for each VM instead of remembering the IP address on windows
    
    1. Open notepad as administrator
    
    2. Open file `c://Windows/System32/drivers/etc/hosts` -> make sure to show all files
    
        ![a5](https://user-images.githubusercontent.com/2896774/62679825-2dc2e300-b9a5-11e9-9c54-bb839fae6098.png)
        
    3. Add these lines
    
        ```
            192.168.85.3 master.hadoop.dts
            192.168.85.5 node1.hadoop.dts
        ```
        
        ![a6](https://user-images.githubusercontent.com/2896774/62679893-64006280-b9a5-11e9-919b-9c2e2d6915d7.png)

    4. Now we can access the domain name on the windows browser
    
        ![a7](https://user-images.githubusercontent.com/2896774/62679993-b3df2980-b9a5-11e9-9d39-c625ede043e0.png)
        
        ![a8](https://user-images.githubusercontent.com/2896774/62680000-bb063780-b9a5-11e9-8d6b-2322f93ce6a8.png)

    5. You can ssh using that domain name too
        
        ![a9](https://user-images.githubusercontent.com/2896774/62680146-0e788580-b9a6-11e9-9d2d-6c88417c1697.png)




