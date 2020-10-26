source /etc/profile
hdfs dfs -rm -r -f log-outdir
rm -rf ./build
mkdir build
javac -d ./build Log.java
cd build
jar cvf Log.jar *
yarn jar Log.jar Log log log-outdir
cd ..
hdfs dfs -cat log-outdir/part-r-00000