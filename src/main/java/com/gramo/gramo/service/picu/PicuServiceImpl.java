package com.gramo.gramo.service.picu;

import com.gramo.gramo.entity.picu.Picu;
import com.gramo.gramo.entity.picu.PicuRepository;
import com.gramo.gramo.entity.plan.Plan;
import com.gramo.gramo.payload.request.PicuRequest;
import com.gramo.gramo.payload.response.PicuContentResponse;
import com.gramo.gramo.payload.response.PlanContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PicuServiceImpl implements PicuService{

    private final PicuRepository picuRepository;

    @Override
    public List<PicuContentResponse> getPicu(LocalDate date) {

        List<Picu> picuList = picuRepository.findAllByDate(date);
        List<PicuContentResponse> picuContentResponses = new ArrayList<>();

        for(Picu picu : picuList) {
            picuContentResponses.add(
                    PicuContentResponse.builder()
                            .userEmail(picu.getUserEmail())
                            .description(picu.getDescription())
                            .build()
            );
        }

        return picuContentResponses;

    }

    @Override
    public void createPicu(PicuRequest request) {
        picuRepository.save(
                Picu.builder()
                        .userEmail(request.getUserEmail())
                        .description(request.getDescription())
                        .date(request.getDate())
                        .build()
        );
    }

    @Override
    public void deletePicu(Long picuId) {
        picuRepository.deleteById(picuId);
    }

}
