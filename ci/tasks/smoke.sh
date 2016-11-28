#!/bin/sh

sleep 10;

i=0

while [[ $i -ne 10 ]]; do
	code=`curl -LI http://demo-onlinestore-ui-temp.{$app_domain}/ -o /dev/null -w '%{http_code}\n' -s`;
	if [[ ${code} = "200" ]]; then
	    echo ${code};
		echo "ok";
	else
		echo "abort!!";
		exit 1
	fi
	i=`expr $i + 1`;
	sleep 1;
done