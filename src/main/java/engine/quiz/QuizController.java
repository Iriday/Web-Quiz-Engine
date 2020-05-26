package engine.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class QuizController {
    @Autowired
    QuizRepository quizRepo;

    @PostMapping(path = "/api/quizzes")
    public Quiz addQuiz(@Valid @RequestBody Quiz quiz, @AuthenticationPrincipal Principal principal) {
        throwIfAnswerIsInvalid(quiz.getOptions(), quiz.getAnswer());
        if (!onlyUniqueValues(quiz.getOptions()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate detected in options");
        quiz.setCreatedBy(principal.getName());
        return quizRepo.save(quiz);
    }

    @GetMapping(path = "/api/quizzes/{id}")
    public Quiz getQuiz(@PathVariable long id) {
        return quizRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/api/quizzes")
    public Iterable<Quiz> getAllQuizzes() {
        return quizRepo.findAll();
    }

    @PostMapping(path = "/api/quizzes/{id}/solve")
    public SolveQuizResponse solveQuiz(@PathVariable long id, @RequestBody Map<String, List<Integer>> mapAnswer) {
        Quiz quiz = quizRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        List<Integer> answer = mapAnswer.get("answer");
        if (answer == null || mapAnswer.size() != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect data");
        }
        throwIfAnswerIsInvalid(quiz.getOptions(), answer);

        if (equalsIgnoreOrder(quiz.getAnswer(), answer)) {
            return new SolveQuizResponse(true, "Congratulations, you're right!");
        } else {
            return new SolveQuizResponse(false, "Wrong answer! Please, try again.");
        }
    }

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/api/quizzes/{id}")
    public void deleteQuiz(@PathVariable long id, @AuthenticationPrincipal Principal principal) {
        Quiz quiz = quizRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!quiz.getCreatedBy().equals(principal.getName())) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        quizRepo.deleteById(id);
    }

    @PutMapping(path = "/api/quizzes/{id}")
    public Quiz updateQuiz(@PathVariable long id, @Valid @RequestBody Quiz updatedQuiz, @AuthenticationPrincipal Principal principal) {
        Quiz quiz = quizRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!quiz.getCreatedBy().equals(principal.getName())) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        updatedQuiz.setId(id);
        return addQuiz(updatedQuiz, principal);
    }

    private static void throwIfAnswerIsInvalid(List<String> options, List<Integer> answer) {
        if (answer.stream().distinct().count() != answer.size()
                || answer.size() > options.size()
                || (answer.size() > 0 &&
                (answer.stream().max(Integer::compare).orElseThrow() >= options.size() || answer.stream().min(Integer::compare).orElseThrow() < 0))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "answer is not valid");
        }
    }

    private static <T> boolean onlyUniqueValues(List<T> data) {
        return data.stream().distinct().count() == data.size();
    }

    private static <T> boolean equalsIgnoreOrder(List<T> one, List<T> two) {
        return one.stream().sorted().collect(Collectors.toList()).equals(two.stream().sorted().collect(Collectors.toList()));
    }
}
