package com.podosoftware.competency.assessment;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.podosoftware.competency.assessment.json.AssessmentPlanJsonDeserializer;
import com.podosoftware.competency.job.Job;
import com.podosoftware.competency.job.json.JobJsonDeserializer;

import architecture.common.model.json.UserDeserializer;
import architecture.common.model.support.DateAwareSupport;
import architecture.common.user.User;

public class DefaultAssessment extends DateAwareSupport implements Assessment {

	private long assessmentId;
	private AssessmentPlan assessmentPlan;
	private Job job;
	private List<User> assessors;
	private User candidate;
	private State state;
	private int jobLevel;
	private int totalScore;
	
	public DefaultAssessment() {
		this.assessmentId = -1L;
		this.totalScore = 0;
		this.state = State.NONE;
		this.jobLevel = 0;
		this.assessors = Collections.EMPTY_LIST;
	}

	
	
	public int getJobLevel() {
		return jobLevel;
	}



	public void setJobLevel(int jobLevel) {
		this.jobLevel = jobLevel;
	}



	public AssessmentPlan getAssessmentPlan() {
		return assessmentPlan;
	}

	@JsonDeserialize(using=AssessmentPlanJsonDeserializer.class)
	public void setAssessmentPlan(AssessmentPlan assessmentPlan) {
		this.assessmentPlan = assessmentPlan;
	}

	public Job getJob() {
		return job;
	}

	@JsonDeserialize(using=JobJsonDeserializer.class)
	public void setJob(Job job) {
		this.job = job;
	}

	public List<User> getAssessors() {
		return assessors;
	}

	@JsonIgnore
	public void setAssessors(List<User> assessors) {
		this.assessors = assessors;
	}

	public User getCandidate() {
		return candidate;
	}

	@JsonDeserialize(using=UserDeserializer.class)
	public void setCandidate(User candidate) {
		this.candidate = candidate;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public long getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(long assessmentId) {
		this.assessmentId = assessmentId;
	}

	@Override
	public String toString() {
		return "DefaultAssessment [assessmentId=" + assessmentId + ", assessmentPlan=" + assessmentPlan + ", job=" + job
				+ ", assessors=" + assessors + ", candidate=" + candidate + ", state=" + state + ", totalScore="
				+ totalScore + "]";
	}
	

}
