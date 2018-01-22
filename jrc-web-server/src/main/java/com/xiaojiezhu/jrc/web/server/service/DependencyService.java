package com.xiaojiezhu.jrc.web.server.service;

import com.xiaojiezhu.jrc.web.server.support.model.LimitResult;

import java.util.List;
import java.util.Map;

/**
 * Dependency service interface
 * @author xiaojie.zhu
 */
public interface DependencyService {

    /**
     * Add a dependency to an other version id
     * @param versionId
     * @param dependencyVersionId
     * @return 0 success, 1 exist,2 versionId equals dependencyVersionId
     */
    int addDependency(int versionId,int dependencyVersionId);

    /**
     * Get the selected version config ids
     * @param versionId the config version id
     * @return
     */
    List<Long> getDependencyVersionId(int versionId);

    /**
     * Get the selected version config
     * @param versionId the version id
     * @param index the page index
     * @param size the page size
     * @return
     */
    LimitResult getDependencyList(int versionId, int index, int size);

    /**
     * Get the global version config, with dependency version config
     * @param versionId
     * @return
     */
    Map<String, String> getGlobalVersionConfig(int versionId);
}
