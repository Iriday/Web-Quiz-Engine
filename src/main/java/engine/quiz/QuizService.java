package engine.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuizService {
    @Autowired
    private QuizRepository quizRepo;

    public Quiz addQuiz(Quiz quiz, Principal principal) {
        throwIfAnswerIsInvalid(quiz.getOptions(), quiz.getAnswer());
        if (!onlyUniqueValues(quiz.getOptions()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate detected in options");
        quiz.setCreatedBy(principal.getName());
        return quizRepo.save(quiz);
    }

    public Quiz getQuiz(long id) {
        return quizRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Page<Quiz> getAllQuizzes(int page, int size, String sortBy) {
        return quizRepo.findAll(PageRequest.of(page, size, Sort.by(sortBy).ascending()));
    }

    public SolveQuizResponse solveQuiz(long id, Map<String, List<Integer>> mapAnswer) {
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

    public void deleteQuiz(long id, Principal principal) {
        Quiz quiz = quizRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!quiz.getCreatedBy().equals(principal.getName())) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        quizRepo.deleteById(id);
    }

    public Quiz updateQuiz(long id, Quiz updatedQuiz, Principal principal) {
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
