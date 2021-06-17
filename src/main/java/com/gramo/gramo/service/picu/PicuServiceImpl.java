package com.gramo.gramo.service.picu;

import com.gramo.gramo.entity.picu.Picu;
import com.gramo.gramo.entity.picu.PicuRepository;
import com.gramo.gramo.exception.InvalidCalendarAccessException;
import com.gramo.gramo.exception.PermissionMismatchException;
import com.gramo.gramo.exception.PicuNotFoundException;
import com.gramo.gramo.factory.UserFactory;
import com.gramo.gramo.mapper.PicuMapper;
import com.gramo.gramo.payload.request.PicuRequest;
import com.gramo.gramo.payload.response.PicuContentResponse;
import com.gramo.gramo.payload.response.PicuListResponse;
import com.gramo.gramo.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public PicuListResponse getPicu(LocalDate date) {
        List<PicuContentResponse>  picuList = picuRepository.findAllByDateOrderByIdDesc(date)
                .stream().map(picu -> picuMapper.toResponse(picu, userFactory.getUserName(picu.getUserEmail())))
                .collect(Collectors.toList());
        return new PicuListResponse(picuList);
    }

    @Override
    public void createPicu(PicuRequest request) {
        picuRepository.save(picuMapper.toPicu(request, userFactory.getAuthUser().getEmail()));
    }

    @Override
    public void deletePicu(Long picuId) {
        Picu picu = picuRepository.findById(picuId)
                .orElseThrow(InvalidCalendarAccessException::new);
        if(!picu.getUserEmail().equals(authenticationFacade.getUserEmail())) {
            throw new PermissionMismatchException();
        }

        picuRepository.delete(picu);
    }

}
