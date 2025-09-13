package com.example.Kiraflow.service;

import com.example.Kiraflow.dto.LabelDto;
import com.example.Kiraflow.entity.*;
import com.example.Kiraflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskLabelService {
    private final TaskRepository taskRepo;
    private final LabelRepository labelRepo;
    private final TaskLabelRepository taskLabelRepo;

    @Transactional
    public List<LabelDto> addLabelToTask(UUID taskEntityId, UUID labelId) {
        TaskEntity taskEntity = taskRepo.findById(taskEntityId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        Label label = labelRepo.findById(labelId)
                .orElseThrow(() -> new IllegalArgumentException("Label not found"));

        if (!taskLabelRepo.existsByTaskEntity_IdAndLabel_Id(taskEntityId, labelId)) {
            TaskLabel tl = new TaskLabel();
            TaskLabelId id = new TaskLabelId();
            id.setTaskId(taskEntityId);
            id.setLabelId(labelId);
            tl.setId(id);
            tl.setTaskEntity(taskEntity);   // ✅ matches your entity field
            tl.setLabel(label);
            taskLabelRepo.save(tl);
        }
        return listLabelsForTask(taskEntityId);
    }

    @Transactional
    public List<LabelDto> removeLabelFromTask(UUID taskEntityId, UUID labelId) {
        taskLabelRepo.deleteByTaskEntity_IdAndLabel_Id(taskEntityId, labelId);
        return listLabelsForTask(taskEntityId);
    }

    @Transactional(readOnly = true)
    public List<LabelDto> listLabelsForTask(UUID taskEntityId) {
        return taskLabelRepo.findAllByTaskEntity_Id(taskEntityId).stream()
                .map(tl -> {
                    Label l = tl.getLabel();
                    return new LabelDto(
                            l.getId(),
                            l.getOrg() == null ? null : l.getOrg().getId(),
                            l.getName(),
                            l.getColor()
                    );
                })
                .collect(Collectors.toList());
    }
}
