package com.crawler.domains.inferences;

import com.crawler.domains.inferences.mappers.InferenceMapper;
import com.crawler.domains.inferences.models.Inference;
import com.crawler.domains.inferences.models.InferenceDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InferenceService {

    private final InferenceRepository inferenceRepository;
    private final InferenceMapper inferenceMapper;

    public InferenceDTO saveInference(InferenceDTO inferenceDTO) {
        Inference inference = inferenceMapper.toEntity(inferenceDTO);
        Inference savedInference = inferenceRepository.save(inference);
        return inferenceMapper.toDto(savedInference);
    }
}