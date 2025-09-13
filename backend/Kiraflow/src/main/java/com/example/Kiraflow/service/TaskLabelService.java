package com.example.Kiraflow.service;

import com.example.Kiraflow.dto.LabelDto;
import com.example.Kiraflow.entity.Label;
import com.example.Kiraflow.entity.TaskEntity;
import com.example.Kiraflow.entity.TaskLabel;
import com.example.Kiraflow.entity.TaskLabelId;
import com.example.Kiraflow.repository.LabelRepository;
import com.example.Kiraflow.repository.TaskLabelRepository;
import com.example.Kiraflow.repository.TaskRepository;
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
    private final PermissionService permissionService;

    @Transactional
    public List<LabelDto> addLabelToTask(UUID taskEntityId, UUID labelId) {
        TaskEntity taskEntity = taskRepo.findById(taskEntityId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        permissionService.requireProjectMember(taskEntity.getColumn().getBoard().getProject().getId());

        Label label = labelRepo.findById(labelId).orElseThrow(() -> new IllegalArgumentException("Label not found"));

        if (!taskLabelRepo.existsByTaskEntity_IdAndLabel_Id(taskEntityId, labelId)) {
            TaskLabel tl = new TaskLabel();
            TaskLabelId id = new TaskLabelId();
            id.setTaskId(taskEntityId);
            id.setLabelId(labelId);
            tl.setId(id);
            tl.setTaskEntity(taskEntity);
            tl.setLabel(label);
            taskLabelRepo.save(tl);
        }
        return listLabelsForTask(taskEntityId);
    }

    @Transactional
    public List<LabelDto> removeLabelFromTask(UUID taskEntityId, UUID labelId) {
        TaskEntity taskEntity = taskRepo.findById(taskEntityId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        permissionService.requireProjectMember(taskEntity.getColumn().getBoard().getProject().getId());

        taskLabelRepo.deleteByTaskEntity_IdAndLabel_Id(taskEntityId, labelId);
        return listLabelsForTask(taskEntityId);
    }

    public List<LabelDto> listLabelsForTask(UUID taskEntityId) {
        TaskEntity taskEntity = taskRepo.findById(taskEntityId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        permissionService.requireProjectMember(taskEntity.getColumn().getBoard().getProject().getId());

        return taskLabelRepo.findAllByTaskEntity_Id(taskEntityId).stream()
                .map(tl -> {
                    Label l = tl.getLabel();
                    return new LabelDto(l.getId(), l.getOrg() == null ? null : l.getOrg().getId(), l.getName(), l.getColor());
                })
                .collect(Collectors.toList());
    }
}
