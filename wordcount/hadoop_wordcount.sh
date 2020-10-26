hdfs dfs -rm -r -f wordcount_outdir

hadoop \
	jar "/opt/hadoop/share/hadoop/tools/lib/hadoop-streaming-3.1.2.jar"	\
	-mapper "python $PWD/mapper.py" \
	-reducer "python $PWD/reducer.py" \
	-input "wordcount" \
	-output "wordcount_outdir"