package engine;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class QuizController {
    private final List<Quiz> quizzes = new ArrayList<>();

    public QuizController() {
    }

    @PostMapping(path = "/api/quizzes")
    public Quiz addQuiz(@RequestBody Quiz quiz) {
        quizzes.add(quiz);
        return quiz;
    }

    @GetMapping(path = "/api/quizzes/{id}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable int id) {
        for (Quiz quiz : quizzes) {
            if (quiz.getId() == id) return new ResponseEntity<>(quiz, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = "/api/quizzes/{id}/solve")
    public ResponseEntity<SolveQuizResponse> solveQuiz(@PathVariable int id, @RequestParam("answer") int answer) {
        for (Quiz quiz : quizzes) {
            if (quiz.getId() == id) {
                if (quiz.getAnswer() == answer) {
                    return new ResponseEntity<>(new SolveQuizResponse(true, "Congratulations, you're right!"), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new SolveQuizResponse(false, "Wrong answer! Please, try again."), HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
