#!/bin/bash

# 定义第一个同步函数
sync_jar() {
    ssh root@ecs 'sh /root/docker/stop.sh'
    rclone sync ./target/dataApi-1.0-SNAPSHOT.jar ecs:/root/docker/
}

# 定义第二个同步函数
sync_lib() {
    rclone sync ./target/lib ecs:/root/docker/lib
}

restart() {
    ssh root@etc 'sh /root/docker/restart.sh'
}
