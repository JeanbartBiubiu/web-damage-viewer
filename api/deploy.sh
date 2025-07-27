#!/bin/bash

# 定义第一个同步函数
sync_jar() {
    ssh root@etc 'sh /root/docker/stop.sh'
    rclone sync ./dataApi/target/dataApi-1.0-SNAPSHOT.jar etc:/root/docker/
}

# 定义第二个同步函数
sync_lib() {
    rclone sync ./dataApi/target/lib etc:/root/docker/lib
}

restart() {
    ssh root@etc 'sh /root/docker/restart.sh'
}