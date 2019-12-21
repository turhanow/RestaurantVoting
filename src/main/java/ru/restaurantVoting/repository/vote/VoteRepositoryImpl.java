package ru.restaurantVoting.repository.vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurantVoting.model.Vote;
import ru.restaurantVoting.repository.UserRepository;
import ru.restaurantVoting.repository.menu.MenuRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class VoteRepositoryImpl {
    private static final Sort SORT_DATE = new Sort(Sort.Direction.ASC, "date");

    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Transactional
    public Vote save(Vote vote, int userId, int menuId) {
        if (!vote.isNew() && get(vote.getId(), userId, menuId) == null) {
            return null;
        }
        vote.setUser(userRepository.getOne(userId));
        vote.setMenu(menuRepository.getOne(menuId));
        return voteRepository.save(vote);
    }

    public Vote get(int id, int userId, int menuId) {
        return voteRepository.get(id, userId, menuId);
    }

    public Vote getForUserAndDate(int userId, LocalDate date) {
        return voteRepository.getForUserAndDate(userId, date).orElse(null);
    }

    public List<Vote> getAll() {
        return voteRepository.findAll(SORT_DATE);
    }
}