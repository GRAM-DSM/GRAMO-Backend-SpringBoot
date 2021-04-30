package com.gramo.gramo.service.picu;

import com.gramo.gramo.entity.picu.Picu;
import com.gramo.gramo.entity.picu.PicuRepository;
import com.gramo.gramo.exception.PermissionMismatchException;
import com.gramo.gramo.exception.PicuNotFoundException;
import com.gramo.gramo.factory.UserFactory;
import com.gramo.gramo.mapper.PicuMapper;
import com.gramo.gramo.payload.request.PicuRequest;
import com.gramo.gramo.payload.response.PicuContentResponse;
import com.gramo.gramo.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PicuServiceImpl implements PicuService{

    private final PicuRepository picuRepository;
    private final AuthenticationFacade authenticationFacade;
    private final UserFactory userFactory;
    private final PicuMapper picuMapper;

    @Override
    public List<PicuContentResponse> getPicu(LocalDate date) {
        return picuRepository.findAllByDateOrderByIdDesc(date)
                .stream().map(picu -> picuMapper.toResponse(picu, userFactory.getAuthUser().getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void createPicu(PicuRequest request) {
        picuRepository.save(picuMapper.toPicu(request, userFactory.getAuthUser().getEmail()));
    }

    @Override
    public void deletePicu(Long picuId) {
        picuRepository.findById(picuId)
                .filter(picu -> picu.getUserEmail().equals(authenticationFacade.getUserEmail()))
                .ifPresentOrElse(picuRepository::delete,
                        PicuNotFoundException::new);
    }

}
