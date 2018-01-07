package com.xiaojiezhu.jrc.web.server.service.impl;

import com.xiaojiezhu.jrc.common.config.Config;
import com.xiaojiezhu.jrc.model.Unit;
import com.xiaojiezhu.jrc.model.Version;
import com.xiaojiezhu.jrc.server.dao.mysql.UnitDao;
import com.xiaojiezhu.jrc.server.dao.mysql.VersionDao;
import com.xiaojiezhu.jrc.web.server.service.ConfigService;
import com.xiaojiezhu.jrc.web.server.service.helper.ConfigHelper;
import com.xiaojiezhu.jrc.web.server.support.exception.ex.UnitExistException;
import com.xiaojiezhu.jrc.web.server.support.model.LimitResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xiaojie.zhu
 */
@Service
public class ConfigServiceImpl implements ConfigService {
    public final static Logger LOG = LoggerFactory.getLogger(ConfigServiceImpl.class);
    @Autowired
    private ConfigHelper configHelper;
    @Autowired
    private UnitDao unitDao;
    @Autowired
    private VersionDao versionDao;

    @Override
    public int addUnit(Unit unit) {
        boolean exist = configHelper.isExistUnit(unit);
        if(exist){
            LOG.warn("group:" + unit.getGroup() + ",unit:" + unit.getUnit() + " is exist");
            return 1;
        }else {
            //default enable config
            unit.setEnable(true);
            configHelper.addUnit(unit);
            return 0;
        }
    }

    @Override
    public LimitResult listUnit(int index, int size, String unitName) {
        int start = (index - 1) * size;
        List<Unit> units = unitDao.listUnit(start, size, unitName);
        long count = unitDao.countUnit(unitName);
        return new LimitResult(count,units);
    }

    @Override
    public int addVersion(Version version) {
        boolean exist = configHelper.isExistVersion(version);
        if(exist){
            LOG.warn("group:" + version.getGroup() + ",unit:" + version.getUnit() + ",version:" + version.getVersion() + ",profile:" + version.getProfile() + " is exist");
            return 1;
        }else{
            //default enable
            version.setEnable(true);
            configHelper.addVersion(version);
            return 0;
        }
    }

    @Override
    public LimitResult listVersion(int index, int size,int unitId, String version,String profile) {
        int start = (index - 1) * size;
        List<Version> versions = versionDao.listVersion(start,size,unitId,version,profile);
        long count = versionDao.countVersion(unitId,version,profile);
        return new LimitResult(count,versions);
    }

    @Override
    public Config findConfigContentByVersionId(int versionId) {
        return configHelper.getRealConfigByVersionId(versionId);
    }

    @Override
    public void updateVersionConfigContent(int versionId, String configContent) {
        configHelper.updateVersionConfigContent(versionId,configContent);
    }
}