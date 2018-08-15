#!/bin/bash
#将sparkstreaming生成的hdfs文件合并成大文件
#将合成完成后的小文件删除
#合并小文件每个小时后过15分钟执行一次，合并上一个小时的小文件
#删除小文件，每天过后50分钟，执行删除前一天的小文件
# 15 * * * * me.sh merge
# 50 0 * * * me.sh delete
# vi /var/spool/cron/crontabs
# 15 * * * * /usr/local/shell/mergesmallfile.sh merge
# 50 0 * * * /usr/local/shell/mergesmallfile.sh delete
LOG_TYPE_1=userconnectionlog
APP1=app1
#上一天
varlastday=`date +%Y%m%d -d  '-1 days'`
#varlastday=`date +%Y%m%d`
#varlasthour=`date +%H`
varlasthour=`date +%H -d  '-1hours'`
varday=`date +%Y%m%d`
source /etc/profile
#echo $varlastday
#echo $varlasthour
#echo $varday
case "$1" in
 merge)
  echo "merge" $LOG_TYPE_1 $varday $varlasthour >>~/sparkstramingcron.log
  java -cp /usr/local/src/common-1.0-SNAPSHOT.jar com.isec.ida.common.utils.MergeSmallFilesToHDFS merge $APP1 $LOG_TYPE_1 $varday $varlasthour
  ;;
 delete)
  echo "del" $LOG_TYPE_1 $varday >>~/sparkstramingcron.log
  java -cp /usr/local/src/common-1.0-SNAPSHOT.jar com.isec.ida.common.utils.MergeSmallFilesToHDFS delete $APP1 $LOG_TYPE_1 $varday $vaelasthour
  ;;
 *)
  echo "please enter args" >>~/sparkstramingcron.log
  ;;
esac
