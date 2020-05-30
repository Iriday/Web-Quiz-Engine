package engine.quiz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuizCompletionInfoRepository extends PagingAndSortingRepository<QuizCompletionInfo, Long> {
    Page<QuizCompletionInfo> findAllByCompletedBy(String userName, Pageable pageable);
}
