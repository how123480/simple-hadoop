hdfs dfs -rm -r -f log_outdir

hadoop \
	jar "/opt/hadoop/share/hadoop/tools/lib/hadoop-streaming-3.1.2.jar"	\
	-D mapred.map.tasks=6 \
	-mapper "$PWD/mapper.py" \
	-reducer "$PWD/reducer.py" \
	-input "log" \
	-output "log_outdir" \
	-file "$PWD/mapper.py" \
	-file "$PWD/reducer.py"