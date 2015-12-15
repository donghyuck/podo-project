package com.podosoftware.competency.assessment;

import java.util.Date;
import java.util.List;

import architecture.common.cache.Cacheable;
import architecture.common.model.PropertyAware;

/**
 * 
 * 
 * 
 * @author donghyuck
 *
 */
public interface RatingScheme extends PropertyAware, Cacheable {
	
	public Integer getObjectType();

	public void setObjectType(Integer objectType) ;

	public Long getObjectId() ;

	public void setObjectId(Long objectId);
	
	public abstract Long getRatingSchemeId();
	
	public abstract void setRatingSchemeId(long ratingSchemeId);
	
	public abstract String getName();
	
	public abstract void setName(String name);
	
	public abstract String getDescription();
	
	public abstract void setDescription(String description);
	
	public abstract int getScale();
	
	public abstract void setScale(int scale);
	
	public List<RatingLevel> getRatingLevels();
	
	public void setRatingLevels(List<RatingLevel> ratingLevels);

	public Date getCreationDate() ;

	public void setCreationDate(Date creationDate) ;

	public Date getModifiedDate() ;

	public void setModifiedDate(Date modifiedDate) ;
	
}