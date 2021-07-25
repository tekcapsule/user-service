package com.tekcapsule.userpreference.domain.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import in.devstream.core.domain.Command;
import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class DisableCommand extends Command {
    private String tenantId;
    private String userId;
}
