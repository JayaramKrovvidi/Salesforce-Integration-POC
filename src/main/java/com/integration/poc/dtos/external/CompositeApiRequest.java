package com.integration.poc.dtos.external;

import java.util.List;
import com.integration.poc.dtos.internal.GenericApiRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompositeApiRequest {
  private List<GenericApiRequest> requestList;
}
