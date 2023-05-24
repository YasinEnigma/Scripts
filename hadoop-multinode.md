# Hadoop 3.2.0 Multi Node Configuration

Please make sure you already have working hadoop installation on your first VM. If not please follow this [installation guide](https://gist.github.com/addingama/f665914340ec26f7efa80e86f53622e1#file-us-dts-hadoop-md)


We will make 1 master and 1 slave hadoop cluster. We can create another VM by cloning existing VM.

## Clone VM to create another node

- Open Virtual Box and make sure the VM not running
- Right click on the existing VM
- Choose `Clone` option

  ![image](https://user-images.githubusercontent.com/2896774/62746437-11738480-ba3f-11e9-953c-6f357569b998.png)
  
- You can give custom name.
- When asked about `Full Clone` or `Linked Clone`, you can choose `Full Clone` if you have enough Storage.
- Now we have 2 VM

  ![image](https://user-images.githubusercontent.com/2896774/62746516-8e9ef980-ba3f-11e9-93b3-d9c5cde83098.png)


## Check IP for each VM

- Run both of the VM
- Check IP on both VM by running `ifconfig -a`

  ![image](https://user-images.githubusercontent.com/2896774/62746636-46cca200-ba40-11e9-9cce-61cf7dce8d8e.png)

  Both of VM not assigned IP on the `enp0s8` interface
- Force `enp0s8` interface to get an IP by running `sudo dhclient enp0s8` on each VM in order (master to slave)
  
  ![image](https://user-images.githubusercontent.com/2896774/62746948-aaa39a80-ba41-11e9-946c-3beaa58dcab9.png)

  In this case my master node has IP `192.168.56.102` and slave node has `192.168.56.101`
  
  
  To make the IP address persist, we can edit `/etc/netplan/` and adjust the IP. You must use space instead of tab.
  
  ![image](https://user-images.githubusercontent.com/2896774/62763110-4d780b00-ba7a-11e9-805a-2889d6836758.png)
  
  
## Update `/etc/hostname` on each VM

- `sudo nano /etc/hostname` on each VM
- set `master.hadoop.dts` on master node and `slave1.hadoop.dts` on slave node

![image](https://user-images.githubusercontent.com/2896774/62758753-cf623700-ba6e-11e9-9753-6970d08b38c7.png)

  
## Update `/etc/hosts` on each VM

- `sudo nano /etc/hosts`
- Add these lines

  ```
    192.168.56.102 master.hadoop.dts
    192.168.56.101 master.hadoop.dts
    
  ```
  
  ![image](https://user-images.githubusercontent.com/2896774/62762998-f96d2680-ba79-11e9-828e-c914854fdb93.png)
  
- Restart networking
  
  ```
    sudo /etc/init.d/network-manager restart
  ```
  
  ![image](https://user-images.githubusercontent.com/2896774/62747355-9365ac80-ba43-11e9-8b10-7a50a2690070.png)

  
## Add Authorized Keys

All nodes need to communicate each other by ssh without password, the command below is used for copy user identity to all nodes each other. Therefore ssh connection between them does not need password anymore. 

On master node

```
  su - hadoop
  ssh-copy-id -i ~/.ssh/id_rsa.pub hadoop@slave1.hadoop.dts
```

On slave node

```
  su - hadoop
  ssh-copy-id -i ~/.ssh/id_rsa.pub hadoop@master.hadoop.dts
```

![image](https://user-images.githubusercontent.com/2896774/62747560-92814a80-ba44-11e9-9e1a-d7ff95c266fc.png)

Now you can test to `ssh hadoop@slave1.hadoop.dts`, there will be no password prompt

![image](https://user-images.githubusercontent.com/2896774/62747613-cfe5d800-ba44-11e9-8a9b-990e247618b8.png)

  
  
## Setting Configuration Files

### `~/hadoop/etc/hadoop/workers` (only in master node)

If you use hadoop version `2.*` you need to change `~/hadoop/etc/hadoop/slaves`

![image](https://user-images.githubusercontent.com/2896774/62758914-439cda80-ba6f-11e9-9369-cce0cbc060b6.png)


### `~/hadoop/etc/hadoop/core-site.xml` (all VM)

![image](https://user-images.githubusercontent.com/2896774/62762805-7fd53880-ba79-11e9-8c18-bb83b46289eb.png)

### `~/hadoop/etc/hadoop/hdfs-site.xml` (all VM)

![image](https://user-images.githubusercontent.com/2896774/62747929-23a4f100-ba46-11e9-9007-6731a9175cbf.png)

### `~/hadoop/etc/hadoop/yarn-site.xml` (all VM)

![image](https://user-images.githubusercontent.com/2896774/62758981-865eb280-ba6f-11e9-85db-9afdfe2bceab.png)

### Formatting Hadoop File System (only in master)

```
  hdfs namenode -format
```

### Starting Hadoop (only in master)

```
  ~/hadoop/sbin/start-all.sh
```

![image](https://user-images.githubusercontent.com/2896774/62748085-ce1d1400-ba46-11e9-943f-ac75f1f1e244.png)


### Check Available Nodes on `master.hadoop.dts:8088` or `192.168.56.102:8088`

![image](https://user-images.githubusercontent.com/2896774/62759035-b60dba80-ba6f-11e9-97f8-14a33e1e90f4.png)




  