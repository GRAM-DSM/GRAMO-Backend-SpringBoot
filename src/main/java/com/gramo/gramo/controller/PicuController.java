package com.gramo.gramo.controller;

import com.gramo.gramo.payload.request.PicuRequest;
import com.gramo.gramo.payload.response.PicuContentResponse;
import com.gramo.gramo.payload.response.PicuListResponse;
import com.gramo.gramo.service.picu.PicuService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/calendar/picu")
@RestController
public class PicuController {

    private final PicuService picuService;

    @GetMapping("/{picuDate}")
    public PicuListResponse getPicu(@DateTimeFormat(pattern = "yyyy-MM-dd")
                                    @PathVariable LocalDate picuDate) {
        return picuService.getPicu(picuDate);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPicu(@RequestBody @Valid PicuRequest picuRequest) {
        picuService.createPicu(picuRequest);
    }

    @DeleteMapping("/{picuId}")
    public void deletePicu(@PathVariable Long picuId) {
        picuService.deletePicu(picuId);
    }

}
