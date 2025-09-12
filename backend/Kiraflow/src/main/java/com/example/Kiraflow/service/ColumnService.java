package com.example.Kiraflow.service;

import com.example.Kiraflow.dto.ColumnDto;
import com.example.Kiraflow.dto.CreateColumnRequest;
import com.example.Kiraflow.dto.UpdateColumnRequest;
import com.example.Kiraflow.entity.Board;
import com.example.Kiraflow.entity.ColumnEntity;
import com.example.Kiraflow.repository.BoardRepository;
import com.example.Kiraflow.repository.ColumnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColumnService {
    private final ColumnRepository columnRepo;
    private final BoardRepository boardRepo;

    public ColumnDto create(CreateColumnRequest req) {
        Board board = boardRepo.findById(req.boardId())
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));
        ColumnEntity c = new ColumnEntity();
        c.setId(UUID.randomUUID());
        c.setTitle(req.title());
        c.setPositionIndex(req.positionIndex() == null ? 0 : req.positionIndex());
        c.setBoard(board);
        ColumnEntity saved = columnRepo.save(c);
        return new ColumnDto(saved.getId(), board.getId(), saved.getTitle(), saved.getPositionIndex());
    }

    public List<ColumnDto> listByBoard(UUID boardId) {
        return columnRepo.findAllByBoardIdOrderByPositionIndex(boardId).stream()
                .map(c -> new ColumnDto(c.getId(), c.getBoard().getId(), c.getTitle(), c.getPositionIndex()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ColumnDto update(UUID id, UpdateColumnRequest req) {
        ColumnEntity c = columnRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Column not found"));
        c.setTitle(req.title());
        c.setPositionIndex(req.positionIndex());
        ColumnEntity saved = columnRepo.save(c);
        return new ColumnDto(saved.getId(), saved.getBoard().getId(), saved.getTitle(), saved.getPositionIndex());
    }

    public void delete(UUID id) {
        // optional: cascade or check tasks exist
        columnRepo.deleteById(id);
    }
}
