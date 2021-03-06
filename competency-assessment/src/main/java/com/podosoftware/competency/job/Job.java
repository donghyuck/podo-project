package com.podosoftware.competency.job;

import java.util.Date;
import java.util.List;

import architecture.common.cache.Cacheable;
import architecture.common.model.PropertyAware;

public interface Job extends PropertyAware, Cacheable {

    public Long getJobId();

    public void setJobId(Long jobId);

    public Long getObjectId();

    public Integer getObjectType();

    public void setObjectType(Integer objectType);

    public void setObjectId(Long objectId);

    public Classification getClassification();

    public void setClassification(Classification classfication);

    public String getName();

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

    public Date getCreationDate();

    public void setCreationDate(Date creationDate);

    public Date getModifiedDate();

    public void setModifiedDate(Date modifiedDate);

    public void setJobLevels(List<JobLevel> jobLevels);

    public List<JobLevel> getJobLevels();

}
