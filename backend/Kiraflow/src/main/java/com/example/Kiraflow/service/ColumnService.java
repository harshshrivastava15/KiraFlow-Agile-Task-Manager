package com.example.Kiraflow.service;

import com.example.Kiraflow.dto.ColumnDto;
import com.example.Kiraflow.dto.CreateColumnRequest;
import com.example.Kiraflow.entity.Board;
import com.example.Kiraflow.entity.ColumnEntity;
import com.example.Kiraflow.repository.BoardRepository;
import com.example.Kiraflow.repository.ColumnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

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
}
