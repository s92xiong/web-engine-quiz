package engine.dto;

public class QuizSolveResponseDTO {
    private boolean success;
    private String feedback;

    public QuizSolveResponseDTO(boolean success, String feedback) {
        this.success = success;
        this.feedback = feedback;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getFeedback() {
        return feedback;
    }
}
