soruce /etc/profile
hdfs dfs -rm -r -f log-outdir
javac -d ./build WordCount.java
cd build
jar cvf WordCount.jar *
yarn jar WordCount.jar WordCount log log-outdir
cd ..
hdfs dfs -cat log-outdir/part-r-00000