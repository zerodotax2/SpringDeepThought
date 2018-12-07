package ru.projects.prog_ja.dto.view.update;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public class UpdateProblemDTO {

    @NotNull
    private long problemId;

    @NotNull
    @Size(min = 10, max = 100)
    @Pattern(regexp = "^[А-я|\\s|\\d|\\w]+$")
    private String title;

    @NotNull
    @Pattern(regexp = "^[A-z]+$")
    private String difficult;

    @NotNull
    private String problemContent;

    @NotNull
    private String solutionContent;

    @NotNull
    @Size(min = 3, max = 5)
    private List<Long> tags;

    @NotNull
    @Size(min = 1, max = 100)
    private String answer;

    public UpdateProblemDTO() {
    }

    public UpdateProblemDTO(long problemId, String title, String difficult, String problemContent, String solutionContent, List<Long> tags, String answer) {
        this.problemId = problemId;
        this.title = title;
        this.difficult = difficult;
        this.problemContent = problemContent;
        this.solutionContent = solutionContent;
        this.tags = tags;
        this.answer = answer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDifficult() {
        return difficult;
    }

    public void setDifficult(String difficult) {
        this.difficult = difficult;
    }

    public String getProblemContent() {
        return problemContent;
    }

    public void setProblemContent(String problemContent) {
        this.problemContent = problemContent;
    }

    public String getSolutionContent() {
        return solutionContent;
    }

    public void setSolutionContent(String solutionContent) {
        this.solutionContent = solutionContent;
    }

    public List<Long> getTags() {
        return tags;
    }

    public void setTags(List<Long> tags) {
        this.tags = tags;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public long getProblemId() {
        return problemId;
    }

    public void setProblemId(long problemId) {
        this.problemId = problemId;
    }
}
