package com.integration.poc.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.integration.poc.dtos.internal.Handle;
import com.integration.poc.models.RuntimeVariables;
import com.integration.poc.repositories.IRuntimeVariablesRepository;
import com.integration.poc.services.IMapBuilder;

@Service
public class HandlerExecutorImpl {

  @Autowired
  IMapBuilder mapBuilder;
  
  @Autowired
  IRuntimeVariablesRepository runTimeRepo;

  public boolean executeHandles(String apiKey, List<Handle> handles) {
    Integer wfId=null;
    Optional<RuntimeVariables> runTime=runTimeRepo.findByWfId(wfId);
    RuntimeVariables runTimeVariables;
    Map<String, Object> map =new HashMap<>();

    if(runTime.isPresent()) {
      runTimeVariables=runTime.get();
    map = runTimeVariables.getValue();
    if (CollectionUtils.isEmpty(handles)) {
      return true;
    }
    for (Handle handle : handles) {
      executeHandler(apiKey, handle);
    }
    Handle lastHandle = handles.get(handles.size() - 1);
    return (boolean) mapBuilder.getMap(map,apiKey, String.valueOf(lastHandle.getHandlerId()));
    }
    return false;
   
  }

  private void executeHandler(String apiKey, Handle handle) {
    Integer wfId=null;
    Optional<RuntimeVariables> runTime=runTimeRepo.findByWfId(wfId);
    RuntimeVariables runTimeVariables;
    Map<String, Object> map =new HashMap<>();

    if(runTime.isPresent()) {
      runTimeVariables=runTime.get();
    map = runTimeVariables.getValue();
    String operator = handle.getOperator();
    Object operand1 = fetchOperand(handle.getOperand1());
    Object operand2 = fetchOperand(handle.getOperand2());
    boolean result = false;
    if (operator.equalsIgnoreCase("EQUALS")) {
      result = executeEquals(operand1, operand2);
    }
    map=mapBuilder.putMap(map,apiKey, String.valueOf(handle.getHandlerId()), result);
    }
  }

  private Object fetchOperand(Object operand) {
    Integer wfId=null;
    Optional<RuntimeVariables> runTime=runTimeRepo.findByWfId(wfId);
    RuntimeVariables runTimeVariables;
    Map<String, Object> map =new HashMap<>();

    if(runTime.isPresent()) {
      runTimeVariables=runTime.get();
    map = runTimeVariables.getValue();
    String runtmVarCheck = operand.toString();
    if (Util.checkRunTimeParameter(runtmVarCheck)) {
      String runtimeId = Util.getMatchedValues(runtmVarCheck)
          .get(0);
      int firstDotIndex = runtimeId.indexOf(".");
      String apiKey = runtimeId.substring(0, firstDotIndex);
      String id = runtimeId.substring(firstDotIndex + 1);
      return mapBuilder.getMap(map,apiKey, id);
    }
    return operand;
    }
    return null;
  }

  private <T> boolean executeEquals(T operand1, T operand2) {
    return operand1.equals(operand2);
  }

}
