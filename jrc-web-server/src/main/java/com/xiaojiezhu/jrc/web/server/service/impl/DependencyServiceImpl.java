package com.xiaojiezhu.jrc.web.server.service.impl;

import com.xiaojiezhu.jrc.common.exception.UnSupportConfigException;
import com.xiaojiezhu.jrc.common.resolve.ConfigResolve;
import com.xiaojiezhu.jrc.common.resolve.DefaultConfigResolve;
import com.xiaojiezhu.jrc.model.Dependency;
import com.xiaojiezhu.jrc.model.Version;
import com.xiaojiezhu.jrc.server.dao.mysql.DependencyDao;
import com.xiaojiezhu.jrc.web.server.service.DependencyService;
import com.xiaojiezhu.jrc.web.server.support.model.LimitResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author xiaojie.zhu
 */
@Service
public class DependencyServiceImpl implements DependencyService{
    public final static Logger LOG = LoggerFactory.getLogger(DependencyServiceImpl.class);
    @Autowired
    private DependencyDao dependencyDao;

    @Override
    public int addDependency(int versionId, int dependencyVersionId) {
        boolean exist = dependencyDao.versionDependencyExist(versionId, dependencyVersionId);
        if(exist){
            LOG.warn("Add dependency fail,the versionId:" + versionId +  " ,and dependencyId:" + dependencyVersionId + " is exist");
            return 1;
        }else if(versionId == dependencyVersionId){
            LOG.warn("Add dependency fail,the versionId can not equals dependencyVersionId");
            return 2;
        }else {
            dependencyDao.addDependency(versionId,dependencyVersionId);
            return 0;
        }
    }

    @Override
    public List<Long> getDependencyVersionId(int versionId) {
        return dependencyDao.getDependencyVersionId(versionId);
    }

    @Override
    public LimitResult getDependencyList(int versionId, int index, int size) {
        int start = (index -1) * size;
        List<Dependency> dependencies = dependencyDao.getDependencyList(versionId,start,size);
        long count = dependencyDao.countDependency(versionId);
        return new LimitResult(count,dependencies);
    }

    @Override
    public Map<String, String> getGlobalVersionConfig(int versionId) {
        DefaultConfigResolve resolve = new DefaultConfigResolve();
        resolveConfig(resolve,versionId);
        Map<String, String> configContent = resolve.resolve();
        return configContent;
    }


    /**
     * repeat read config dependency config data
     * @param configResolve
     * @param versionId
     */
    private void resolveConfig(DefaultConfigResolve configResolve,int versionId){
        String content = dependencyDao.getVersionConfigContent(versionId);
        if(content != null){
            try {
                configResolve.addConfig(content);
            } catch (UnSupportConfigException e) {
                LOG.error("error config data format,it is:" + content);
            }
        }

        List<Integer> dependencyIds = dependencyDao.getDependencyId(versionId);
        if(dependencyIds != null && dependencyIds.size() > 0){
            for(int i = 0 ; i < dependencyIds.size() ; i ++){
                Integer dependencyId = dependencyIds.get(i);
                if(dependencyId != null){
                    resolveConfig(configResolve,dependencyId);
                }

            }
        }

    }
}
