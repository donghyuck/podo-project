package com.podosoftware.competency.assessment;

import java.util.List;

import com.podosoftware.competency.competency.Competency;
import com.podosoftware.competency.job.Job;

import architecture.common.model.DateAware;
import architecture.common.user.User;

public interface Assessment extends DateAware {

    public enum State {
	INCOMPLETE, ASSESSED, DELETED, NONE;
    }

    public abstract long getAssessmentId();

    public abstract void setAssessmentId(long assessmentId);

    public abstract AssessmentPlan getAssessmentPlan();

    public abstract void setAssessmentPlan(AssessmentPlan assessment);

    public abstract State getState();

    public abstract void setState(State state);

    public abstract User getCandidate();

    public abstract void setCandidate(User user);

    public abstract List<User> getAssessors();

    public abstract void setAssessors(List<User> users);

    public abstract int getTotalScore();

    public abstract Job getJob();

    public abstract void setTotalScore(int totalScore);

    public abstract void setJob(Job job);

    public abstract long getJobLevelId();

    public abstract void setJobLevelId(long jobLevelId);

    public abstract int getJobLevel();

    public abstract void setJobLevel(int level);

    public abstract String getJobLevelName();

    public abstract void setJobLevelName(String jobLevelName);

    public abstract List<Competency> getCompetencies();

    public abstract void setCompetencies(List<Competency> competencies);

}
