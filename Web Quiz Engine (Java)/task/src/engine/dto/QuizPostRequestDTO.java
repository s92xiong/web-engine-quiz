package engine.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class QuizPostRequestDTO {
    @NotNull
    private String title;
    @NotNull
    private String text;
    @NotNull
    @Size(min = 2)
    private String[] options;
    private int[] answer;

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String[] getOptions() {
        return options;
    }

    public int[] getAnswer() {
        return answer;
    }
}
