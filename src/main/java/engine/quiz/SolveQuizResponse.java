package engine.quiz;

public class SolveQuizResponse {
    private final boolean success;
    private final String feedback;

    public SolveQuizResponse(boolean success, String feedback) {
        this.success = success;
        this.feedback = feedback;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getFeedback() {
        return feedback;
    }
}
