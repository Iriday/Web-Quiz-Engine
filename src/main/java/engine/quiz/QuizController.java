package engine.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
public class QuizController {
    @Autowired
    private QuizService quizService;

    @PostMapping(path = "/api/quizzes")
    public Quiz addQuiz(@Valid @RequestBody Quiz quiz, @AuthenticationPrincipal Principal principal) {
        return quizService.addQuiz(quiz, principal);
    }

    @GetMapping(path = "/api/quizzes/{id}")
    public Quiz getQuiz(@PathVariable long id) {
        return quizService.getQuiz(id);
    }

    @GetMapping(path = "/api/quizzes")
    public Iterable<Quiz> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @PostMapping(path = "/api/quizzes/{id}/solve")
    public SolveQuizResponse solveQuiz(@PathVariable long id, @RequestBody Map<String, List<Integer>> mapAnswer) {
        return quizService.solveQuiz(id, mapAnswer);
    }

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/api/quizzes/{id}")
    public void deleteQuiz(@PathVariable long id, @AuthenticationPrincipal Principal principal) {
        quizService.deleteQuiz(id, principal);
    }

    @PutMapping(path = "/api/quizzes/{id}")
    public Quiz updateQuiz(@PathVariable long id, @Valid @RequestBody Quiz updatedQuiz, @AuthenticationPrincipal Principal principal) {
        return quizService.updateQuiz(id, updatedQuiz, principal);
    }
}
