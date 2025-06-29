package com.mass_branches.service;

import com.mass_branches.dto.request.StagePostRequest;
import com.mass_branches.model.Budget;
import com.mass_branches.model.Stage;
import com.mass_branches.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StageService {
    private final StageRepository repository;

    public Stage create(StagePostRequest postRequest, Budget budget) {
        Stage stage = Stage.builder().budget(budget).orderIndex(postRequest.order()).name(postRequest.name()).build();

        return repository.save(stage);
    }
}
