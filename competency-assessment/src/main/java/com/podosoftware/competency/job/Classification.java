package com.podosoftware.competency.job;

import java.util.Map;

import architecture.common.cache.Cacheable;

public interface Classification extends Cacheable {

    public Long getClassifyType();

    public void setClassifyType(Long classifyType);

    public String getClassifyTypeName();

    public void setClassifyTypeName(String classifyTypeName);

    public Long getClassifiedMajorityId();

    public Long getClassifiedMiddleId();

    public Long getClassifiedMinorityId();

    public void setClassifiedMajorityId(Long classifiedMajorityId);

    public void setClassifiedMiddleId(Long classifiedMiddleId);

    public void setClassifiedMinorityId(Long classifiedMinorityId);

    public String getClassifiedMajorityName();

    public String getClassifiedMiddleName();

    public String getClassifiedMinorityName();

    public void setClassifiedMajorityName(String classifiedMajorityName);

    public void setClassifiedMiddleName(String classifiedMiddleName);

    public void setClassifiedMinorityName(String classifiedMinorityName);

    public abstract Map<String, Long> toMap();

}
