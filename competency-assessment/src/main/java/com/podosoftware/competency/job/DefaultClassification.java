package com.podosoftware.competency.job;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import architecture.common.cache.CacheSizes;

public class DefaultClassification implements Classification {

	private Long classifiedMajorityId;

	private Long classifiedMiddleId;

	private Long classifiedMinorityId;

	private String classifiedMajorityName;

	private String classifiedMiddleName;

	private String classifiedMinorityName;

	public DefaultClassification() {
		
		this.classifiedMajorityId = -1L;
		this.classifiedMiddleId = -1L;
		this.classifiedMinorityId = -1L;
		
	}

	public DefaultClassification(Long classifiedMajorityId, Long classifiedMiddleId, Long classifiedMinorityId) {
		this.classifiedMajorityId = classifiedMajorityId;
		this.classifiedMiddleId = classifiedMiddleId;
		this.classifiedMinorityId = classifiedMinorityId;
	}


	public Long getClassifiedMajorityId() {
		return classifiedMajorityId;
	}

	public void setClassifiedMajorityId(Long classifiedMajorityId) {
		this.classifiedMajorityId = classifiedMajorityId;
	}

	public Long getClassifiedMiddleId() {
		return classifiedMiddleId;
	}

	public void setClassifiedMiddleId(Long classifiedMiddleId) {
		this.classifiedMiddleId = classifiedMiddleId;
	}

	public Long getClassifiedMinorityId() {
		return classifiedMinorityId;
	}

	public void setClassifiedMinorityId(Long classifiedMinorityId) {
		this.classifiedMinorityId = classifiedMinorityId;
	}

	public String getClassifiedMajorityName() {
		return classifiedMajorityName;
	}

	public void setClassifiedMajorityName(String classifiedMajorityName) {
		this.classifiedMajorityName = classifiedMajorityName;
	}

	public String getClassifiedMiddleName() {
		return classifiedMiddleName;
	}

	public void setClassifiedMiddleName(String classifiedMiddleName) {
		this.classifiedMiddleName = classifiedMiddleName;
	}

	public String getClassifiedMinorityName() {
		return classifiedMinorityName;
	}

	public void setClassifiedMinorityName(String classifiedMinorityName) {
		this.classifiedMinorityName = classifiedMinorityName;
	}
	
	@JsonIgnore
	public Map<String, Long> toMap(){
		Map<String, Long> additionalParameters = new HashMap<String, Long>(4);
		additionalParameters.put("majorityId", this.classifiedMajorityId);
		additionalParameters.put("middleId", this.classifiedMiddleId);
		additionalParameters.put("minorityId", this.classifiedMinorityId);
		return additionalParameters;
	}

	@JsonIgnore
	public int getCachedSize() {
		int totalSize = CacheSizes.sizeOfLong() 
				+ CacheSizes.sizeOfLong() 
				+ CacheSizes.sizeOfLong()
				+ CacheSizes.sizeOfString(classifiedMajorityName) 
				+ CacheSizes.sizeOfString(classifiedMiddleName)
				+ CacheSizes.sizeOfString(classifiedMinorityName);
		return totalSize;
	}

}
