/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
 
* http://www.apache.org/licenses/LICENSE-2.0

* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package edu.psu.swe.scim.server.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import edu.psu.swe.scim.spec.protocol.ErrorMessageType;
import edu.psu.swe.scim.spec.protocol.data.ErrorResponse;
import edu.psu.swe.scim.spec.protocol.filter.FilterParseException;

public class FilterParseExceptionMapper implements ExceptionMapper<FilterParseException> {

  @Override
  public Response toResponse(FilterParseException exception) {
    ErrorResponse er = new ErrorResponse(Status.BAD_REQUEST, exception.getMessage());
    er.setScimType(ErrorMessageType.INVALID_FILTER);
    return er.toResponse();
  }

}
