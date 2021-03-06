package com.podosoftware.competency.spring.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import com.podosoftware.competency.assessment.AssessedEssentialElementScore;
import com.podosoftware.competency.assessment.Assessment;
import com.podosoftware.competency.assessment.AssessmentManager;
import com.podosoftware.competency.assessment.AssessmentNotFoundException;
import com.podosoftware.competency.assessment.AssessmentPlan;
import com.podosoftware.competency.assessment.AssessmentPlanNotFoundException;
import com.podosoftware.competency.assessment.AssessmentQuestion;
import com.podosoftware.competency.assessment.AssessmentStats;
import com.podosoftware.competency.assessment.DefaultAssessment;
import com.podosoftware.competency.assessment.DefaultAssessmentPlan;
import com.podosoftware.competency.assessment.JobSelection;
import com.podosoftware.competency.codeset.CodeSet;
import com.podosoftware.competency.codeset.CodeSetManager;
import com.podosoftware.competency.codeset.CodeSetNotFoundException;
import com.podosoftware.competency.competency.Competency;
import com.podosoftware.competency.competency.CompetencyManager;
import com.podosoftware.competency.job.Classification;
import com.podosoftware.competency.job.DefaultClassification;
import com.podosoftware.competency.job.Job;
import com.podosoftware.competency.job.JobManager;

import architecture.common.user.Company;
import architecture.common.user.CompanyManager;
import architecture.common.user.CompanyNotFoundException;
import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.web.model.ItemList;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.WebSiteNotFoundException;
import architecture.ee.web.util.WebSiteUtils;

@Controller("competency-assessment-data-controller")
@RequestMapping("/data/me/competency")
public class CompetencyAssessmentController {

    private Log log = LogFactory.getLog(getClass());

    @Inject
    @Qualifier("codeSetManager")
    private CodeSetManager codeSetManager;

    @Inject
    @Qualifier("companyManager")
    private CompanyManager companyManager;

    @Inject
    @Qualifier("competencyManager")
    private CompetencyManager competencyManager;

    @Inject
    @Qualifier("jobManager")
    private JobManager jobManager;

    @Inject
    @Qualifier("assessmentManager")
    private AssessmentManager assessmentManager;

    public JobManager getJobManager() {
	return jobManager;
    }

    public void setJobManager(JobManager jobManager) {
	this.jobManager = jobManager;
    }

    public CodeSetManager getCodeSetManager() {
	return codeSetManager;
    }

    public void setCodeSetManager(CodeSetManager codeSetManager) {
	this.codeSetManager = codeSetManager;
    }

    public CompanyManager getCompanyManager() {
	return companyManager;
    }

    public void setCompanyManager(CompanyManager companyManager) {
	this.companyManager = companyManager;
    }

    public CompetencyManager getCompetencyManager() {
	return competencyManager;
    }

    public void setCompetencyManager(CompetencyManager competencyManager) {
	this.competencyManager = competencyManager;
    }

    public AssessmentManager getAssessmentManager() {
	return assessmentManager;
    }

    public void setAssessmentManager(AssessmentManager assessmentManager) {
	this.assessmentManager = assessmentManager;
    }

    public CompetencyAssessmentController() {
    }

    @RequestMapping(value = "/assessment/get.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public Assessment getAssessment(
	    @RequestParam(value = "assessmentId", defaultValue = "0", required = false) long assessmentId)
	    throws AssessmentPlanNotFoundException, AssessmentNotFoundException {
	User user = SecurityHelper.getUser();
	if (!user.isAnonymous()) {
	    Assessment assessment = assessmentManager.getAssessment(assessmentId);
	    log.debug("JOB_LEVEL_ID:" + assessment.getJobLevelId());
	    if (assessment.getJob().getJobId() > 0) {
		List<Competency> competencies = Collections.EMPTY_LIST;
		if (assessment.getJobLevelId() > 0) {
		    log.debug("1. competency by job and jobLevel ----------- ");
		    competencies = competencyManager.getCompetenciesByJobAndJobLevel(assessment.getJob(),
			    assessment.getJobLevelId());
		}

		if (competencies.size() == 0) {
		    log.debug("2. competency by job -------------");
		    competencies = competencyManager.getCompetencies(assessment.getJob());
		}

		assessment.setCompetencies(competencies);
	    }

	    if (assessment.getAssessmentPlan().isFeedbackEnabled() && assessment.getAssessors().contains(user)) {
		return assessment;
	    } else {
		if (assessment.getCandidate().getUserId() == user.getUserId()) {
		    return assessment;
		}
	    }
	}
	throw new UnAuthorizedException();
    }

    @RequestMapping(value = "/assessment/test/list.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public List<AssessmentQuestion> listAssessmentQuestion(
	    @RequestParam(value = "assessmentId", defaultValue = "0", required = false) long assessmentId)
	    throws AssessmentPlanNotFoundException, AssessmentNotFoundException {
	User user = SecurityHelper.getUser();
	Assessment assessment = assessmentManager.getAssessment(assessmentId);
	return assessmentManager.getUserAssessmentQuestions(assessment);
    }

    @RequestMapping(value = "/assessment/test/scores.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public List<AssessedEssentialElementScore> listAssessmentEssentialElementSummary(
	    @RequestParam(value = "assessmentId", defaultValue = "0", required = false) long assessmentId)
	    throws AssessmentPlanNotFoundException, AssessmentNotFoundException {
	User user = SecurityHelper.getUser();
	Assessment assessment = assessmentManager.getAssessment(assessmentId);
	return assessmentManager.getUserAssessedSummaries(assessment);
    }

    @RequestMapping(value = "/assessment/test/update.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public List<AssessmentQuestion> updateAssessmentAnswers(@RequestBody List<AssessmentQuestion> answers)
	    throws AssessmentPlanNotFoundException, AssessmentNotFoundException {
	User user = SecurityHelper.getUser();
	Assessment assessment = null;
	int totalScore = 0;
	for (AssessmentQuestion q : answers) {
	    if (assessment == null)
		assessment = assessmentManager.getAssessment(q.getAssessmentId());
	    totalScore = totalScore + q.getScore();
	    q.setAssessorId(user.getUserId());
	}
	assessment.setTotalScore(totalScore);
	assessment.setState(Assessment.State.ASSESSED);
	assessmentManager.saveOrUpdateUserAssessmentScores(assessment, user, answers);
	return assessmentManager.getUserAssessmentQuestions(assessment);
    }

    @RequestMapping(value = "/assessment/list.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public List<Assessment> listAssessment(
	    @RequestParam(value = "assessmentPlanId", defaultValue = "0", required = false) long assessmentPlanId,
	    @RequestParam(value = "state", required = false) String state) throws AssessmentPlanNotFoundException {
	User user = SecurityHelper.getUser();
	if (!user.isAnonymous()) {
	    AssessmentPlan plan = new DefaultAssessmentPlan(assessmentPlanId);
	    return assessmentManager.getUserAssessments(user, plan, state);
	}
	return Collections.EMPTY_LIST;
    }

    @RequestMapping(value = "/assessment/create.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public Assessment createAssessment(@RequestBody DefaultAssessment assessment)
	    throws AssessmentPlanNotFoundException {
	User user = SecurityHelper.getUser();
	Assessment assessmentToUse = new DefaultAssessment();
	;
	if (!user.isAnonymous()) {
	    assessmentToUse.setAssessmentPlan(assessment.getAssessmentPlan());
	    assessmentToUse.setCandidate(user);
	    assessmentToUse.setJob(assessment.getJob());
	    assessmentToUse.setJobLevel(assessment.getJobLevel());
	    assessmentManager.saveOrUpdateUserAssessment(assessmentToUse);
	}
	return assessmentToUse;
    }

    @RequestMapping(value = "/assessment/plan/list.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public List<AssessmentPlan> listAssessmentPlan() {
	User user = SecurityHelper.getUser();
	if (!user.isAnonymous()) {
	    List<AssessmentPlan> list = assessmentManager.getUserAssessmentPlans(user);
	    Collections.sort(list, new Comparator<AssessmentPlan>() {
		public int compare(AssessmentPlan o1, AssessmentPlan o2) {
		    if (o1.getStartDate().after(o2.getStartDate())) {
			return 1;
		    }
		    return 0;
		}
	    });

	    return list;
	}
	return Collections.EMPTY_LIST;
    }

    @RequestMapping(value = "/assessment/plan/stats/list.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public List<AssessmentStats> listAssessmentStats() {
	User user = SecurityHelper.getUser();
	if (!user.isAnonymous()) {
	    List<AssessmentPlan> list = assessmentManager.getUserAssessmentPlans(user);
	    Collections.sort(list, new Comparator<AssessmentPlan>() {
		public int compare(AssessmentPlan o1, AssessmentPlan o2) {
		    if (o1.getStartDate().after(o2.getStartDate())) {
			return 1;
		    }
		    return 0;
		}
	    });
	    return toAssessmentStatsList(list);
	}
	return Collections.EMPTY_LIST;
    }

    private List<AssessmentStats> toAssessmentStatsList(List<AssessmentPlan> assessmentPlans) {
	User user = SecurityHelper.getUser();
	List<AssessmentStats> list = new ArrayList<AssessmentStats>(assessmentPlans.size());
	for (AssessmentPlan plan : assessmentPlans) {
	    AssessmentStats stats = assessmentManager.getAssessmentStats(plan, user);
	    list.add(stats);
	}
	return list;
    }

    @RequestMapping(value = "/assessment/job/list.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public List<Job> listJob(
	    @RequestParam(value = "assessmentId", defaultValue = "0", required = false) Integer assessmentId)
	    throws AssessmentPlanNotFoundException {
	User user = SecurityHelper.getUser();

	if (assessmentId <= 0)
	    return Collections.EMPTY_LIST;

	AssessmentPlan assessment = assessmentManager.getAssessmentPlan(assessmentId);
	int objectType = assessment.getObjectType();
	long objectId = assessment.getObjectId();
	List<Job> list = new ArrayList<Job>();
	for (JobSelection jobSelection : assessment.getJobSelections()) {
	    Classification classify = new DefaultClassification(jobSelection.getClassifyType(),
		    jobSelection.getClassifiedMajorityId(), jobSelection.getClassifiedMiddleId(),
		    jobSelection.getClassifiedMinorityId());
	    List<Job> jobs = jobManager.getJobs(objectType, objectId, classify);
	    list.addAll(jobs);
	}
	return list;
    }

    @RequestMapping(value = "/job/category/list.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public List<CodeSet> listJobCategory(
	    @RequestParam(value = "codeSetId", defaultValue = "0", required = false) Integer codeSetId)
	    throws CodeSetNotFoundException {
	User user = SecurityHelper.getUser();
	if (codeSetId > 0) {
	    CodeSet codeset = codeSetManager.getCodeSet(codeSetId);
	    return codeSetManager.getCodeSets(codeset);
	}
	return Collections.EMPTY_LIST;
    }

    @RequestMapping(value = "/job/list.json", method = RequestMethod.POST)
    @ResponseBody
    public ItemList listJob(@RequestParam(value = "companyId", defaultValue = "0", required = false) Long companyId,
	    @RequestParam(value = "classifyType", defaultValue = "0", required = false) Long classifyType,
	    @RequestParam(value = "classifiedMajorityId", defaultValue = "0", required = false) Long classifiedMajorityId,
	    @RequestParam(value = "classifiedMiddleId", defaultValue = "0", required = false) Long classifiedMiddleId,
	    @RequestParam(value = "classifiedMinorityId", defaultValue = "0", required = false) Long classifiedMinorityId,
	    @RequestParam(value = "startIndex", defaultValue = "0", required = false) Integer startIndex,
	    @RequestParam(value = "pageSize", defaultValue = "0", required = false) Integer pageSize,
	    NativeWebRequest request) {
	User user = SecurityHelper.getUser();
	Company company = user.getCompany();
	if (companyId > 0) {
	    try {
		company = companyManager.getCompany(companyId);
	    } catch (CompanyNotFoundException e) {
	    }
	} else {
	    try {
		WebSite site = WebSiteUtils.getWebSite(request.getNativeRequest(HttpServletRequest.class));
		company = site.getCompany();
	    } catch (WebSiteNotFoundException e1) {
	    }
	}

	List<Job> items = Collections.EMPTY_LIST;
	int totalCount = 0;
	Classification classify = new DefaultClassification(classifyType, classifiedMajorityId, classifiedMiddleId,
		classifiedMinorityId);
	if (classify.getClassifyType() > 0 || classify.getClassifiedMajorityId() > 0
		|| classify.getClassifiedMiddleId() > 0 || classify.getClassifiedMinorityId() > 0) {
	    totalCount = jobManager.getJobCount(company, classify);
	    if (totalCount > 0) {
		if (pageSize > 0) {
		    items = jobManager.getJobs(company, classify, startIndex, pageSize);
		} else {
		    items = jobManager.getJobs(company, classify);
		}
	    }

	} else {
	    totalCount = jobManager.getJobCount(company);
	    if (totalCount > 0) {
		if (pageSize > 0) {
		    items = jobManager.getJobs(company, startIndex, pageSize);
		} else {
		    items = jobManager.getJobs(company);
		}
	    }
	}
	ItemList list = new ItemList(items, totalCount);
	return list;
    }

}
