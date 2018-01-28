package com.xiaojiezhu.jrc.web.server.service.impl;

import com.xiaojiezhu.jrc.web.server.service.RemoteConfigService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author xiaojie.zhu
 */
@Service
public class DefaultRemoteConfigService implements RemoteConfigService {

    @Override
    public Map<String, ?> getGlobalVersionConfig(String group, String unit, String version, String profile) {
        return null;
    }
}
