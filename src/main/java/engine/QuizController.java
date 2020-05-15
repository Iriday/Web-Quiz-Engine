package engine;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuizController {
    private final Quiz testQuiz;

    public QuizController() {
        testQuiz = new Quiz("The java Logo", "What is depicted on the Java logo?", new String[]{"Robot", "Tea leaf", "Cup of coffee", "bug"});
    }

    @GetMapping(path = "/api/quiz")
    public Quiz getQuiz() {
        return testQuiz;
    }

    @PostMapping(path = "/api/quiz")
    public SolveQuizResponse solveQuiz(@RequestParam("answer") int answer) {
        if (answer == 2) {
            return new SolveQuizResponse(true, "Congratulations, you're right!");
        } else {
            return new SolveQuizResponse(false, "Wrong answer! Please, try again.");
        }
    }
}
