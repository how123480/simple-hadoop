#!/usr/bin/env python
import sys
import re

month_map={
	"Jan":"01",
	"Feb":"02",
	"Mar":"03",
	"Apr":"04",
	"May":"05",
	"Jun":"06",
	"Jul":"07",
	"Aug":"08",
	"Sep":"09",
	"Oct":"10",
	"Nov":"11",
	"Dec":"12"
}
def month_sub(time):
	for key in month_map:
		if(key in time):
			return time.replace(key,month_map[key])
	return time

for line in sys.stdin:
	try:
		time = re.split('\\[|\\]',line)[1]
	except:
		continue

	time = ":".join(time.split(":", 2)[:2])
	time = month_sub(time)

	time = re.split('\\/|\\:',time)
	time = "{}-{}-{} T {}:00:00.000".format(time[2],time[0],time[1],time[3])
	print '%s\t%s' % (time,1)