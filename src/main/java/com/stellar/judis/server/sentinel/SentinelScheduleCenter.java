package com.stellar.judis.server.sentinel;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;

import java.util.*;

/**
 * @author firo
 * @version 1.0
 * @date 2021/1/8 17:09
 */
public class SentinelScheduleCenter {
    private HashedWheelTimer timer;
    private Map<String, List<Timeout>> masterTimeoutMap;

    public SentinelScheduleCenter() {
        // 时间轮 延时任务
        timer = new HashedWheelTimer();
        masterTimeoutMap = new HashMap<>();
    }

}
