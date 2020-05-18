package engine;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@RestController
public class QuizController {
    private final List<Quiz> quizzes = new ArrayList<>();

    public QuizController() {
    }

    @PostMapping(path = "/api/quizzes")
    public Quiz addQuiz(@Valid @RequestBody Quiz quiz) {
        throwIfAnswerIsInvalid(quiz.getOptions(), quiz.getAnswer());
        if (!containsUniqueValues(quiz.getOptions())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate detected in options");

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

    @GetMapping(path = "/api/quizzes")
    public List<Quiz> getAllQuizzes() {
        return quizzes;
    }

    @PostMapping(path = "/api/quizzes/{id}/solve")
    public SolveQuizResponse solveQuiz(@PathVariable int id, @RequestBody Map<String, int[]> mapAnswer) {
        for (Quiz quiz : quizzes) {
            if (quiz.getId() == id) {
                int[] answer = mapAnswer.get("answer");
                if (answer == null || mapAnswer.size() != 1) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect data");
                }
                throwIfAnswerIsInvalid(quiz.getOptions(), answer);

                if (Arrays.equals(Arrays.stream(quiz.getAnswer()).sorted().toArray(), Arrays.stream(answer).sorted().toArray())) {
                    return new SolveQuizResponse(true, "Congratulations, you're right!");
                } else {
                    return new SolveQuizResponse(false, "Wrong answer! Please, try again.");
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    private static void throwIfAnswerIsInvalid(String[] options, int[] answer) {
        if (Arrays.stream(answer).distinct().toArray().length != answer.length
                || answer.length > options.length
                || (answer.length > 0 &&
                (IntStream.of(answer).max().orElseThrow() >= options.length || IntStream.of(answer).min().orElseThrow() < 0))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "answer is not valid");
        }
    }

    private static boolean containsUniqueValues(String[] data) {
        return Arrays.stream(data).distinct().toArray().length == data.length;
    }
}
