package com.gramo.gramo.service.picu;

import com.gramo.gramo.entity.picu.Picu;
import com.gramo.gramo.entity.picu.PicuRepository;
import com.gramo.gramo.entity.user.UserRepository;
import com.gramo.gramo.exception.PermissionMismatchException;
import com.gramo.gramo.exception.PicuNotFoundException;
import com.gramo.gramo.exception.UserNotFoundException;
import com.gramo.gramo.payload.request.PicuRequest;
import com.gramo.gramo.payload.response.PicuContentResponse;
import com.gramo.gramo.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PicuServiceImpl implements PicuService{

    private final PicuRepository picuRepository;
    private final AuthenticationFacade authenticationFacade;
    private final UserRepository userRepository;

    @Override
    public List<PicuContentResponse> getPicu(LocalDate date) {
        List<Picu> picuList = picuRepository.findAllByDateOrderByIdDesc(date);
        List<PicuContentResponse> picuContentResponses = new ArrayList<>();

        for(Picu picu : picuList) {
            picuContentResponses.add(
                    PicuContentResponse.builder()
                            .userName(userRepository.findById(authenticationFacade.getUserEmail())
                                    .orElseThrow(UserNotFoundException::new).getName())
                            .description(picu.getDescription())
                            .picuId(picu.getId())
                            .build()
            );
        }

        return picuContentResponses;

    }

    @Override
    public void createPicu(PicuRequest request) {
        picuRepository.save(
                Picu.builder()
                        .userEmail(authenticationFacade.getUserEmail())
                        .description(request.getDescription())
                        .date(request.getDate())
                        .build()
        );
    }

    @Override
    public void deletePicu(Long picuId) {
        Picu picu = picuRepository.findById(picuId)
                .orElseThrow(PicuNotFoundException::new);

        if(!picu.getUserEmail().equals(authenticationFacade.getUserEmail())) {
            throw new PermissionMismatchException();
        }

        picuRepository.delete(picu);
    }

}
