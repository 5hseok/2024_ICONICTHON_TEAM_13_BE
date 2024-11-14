package com.prochord.server.controller;

import com.prochord.server.dto.professor.response.ProfessorResponse;
import com.prochord.server.global.exception.dto.SuccessStatusResponse;
import com.prochord.server.global.exception.message.SuccessMessage;
import com.prochord.server.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    @GetMapping("/professor/{id}")
    public ResponseEntity<SuccessStatusResponse<ProfessorResponse>> getProfessorWithInterests(@PathVariable Long id) {
        ProfessorResponse response = professorService.getProfessorWithInterests(id);
        return ResponseEntity.status(HttpStatus.OK).body(SuccessStatusResponse.of(SuccessMessage.FETCH_SUCCESS, response));
    }

    @GetMapping("/professors")
    public ResponseEntity<SuccessStatusResponse<List<ProfessorResponse>>> getProfessorsWithInterests() {
        List<ProfessorResponse> response = professorService.getAllProfessorsWithInterests();
        return ResponseEntity.status(HttpStatus.OK).body(SuccessStatusResponse.of(SuccessMessage.FETCH_SUCCESS, response));
    }
}