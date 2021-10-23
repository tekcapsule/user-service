package com.tekcapsule.user.application.function;

import com.tekcapsule.core.domain.Origin;
import com.tekcapsule.core.utils.HeaderUtil;
import com.tekcapsule.user.application.function.input.UpdateInput;
import com.tekcapsule.user.application.config.AppConstants;
import com.tekcapsule.user.application.mapper.InputOutputMapper;
import com.tekcapsule.user.domain.command.UpdateCommand;
import com.tekcapsule.user.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class UpdateFunction implements Function<Message<UpdateInput>, Message<Void>> {

    private final UserService userService;

    public UpdateFunction(final UserService userService) {
        this.userService = userService;
    }


    @Override
    public Message<Void> apply(Message<UpdateInput> updateInputMessage) {
        UpdateInput updateInput = updateInputMessage.getPayload();

        log.info(String.format("Entering update user Function - User Id:%s",  updateInput.getUserId()));

        Origin origin = HeaderUtil.buildOriginFromHeaders(updateInputMessage.getHeaders());

        UpdateCommand updateCommand = InputOutputMapper.buildUpdateCommandFromUpdateInput.apply(updateInput, origin);
        userService.update(updateCommand);
        Map<String, Object> responseHeader = new HashMap();
        responseHeader.put(AppConstants.HTTP_STATUS_CODE_HEADER, HttpStatus.OK.value());

        return new GenericMessage(responseHeader);

    }
}