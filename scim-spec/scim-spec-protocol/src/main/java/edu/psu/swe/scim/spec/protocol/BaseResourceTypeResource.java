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

package edu.psu.swe.scim.spec.protocol;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import edu.psu.swe.scim.spec.adapter.FilterWrapper;
import edu.psu.swe.scim.spec.protocol.attribute.AttributeReference;
import edu.psu.swe.scim.spec.protocol.attribute.AttributeReferenceListWrapper;
import edu.psu.swe.scim.spec.protocol.data.ListResponse;
import edu.psu.swe.scim.spec.protocol.data.PatchRequest;
import edu.psu.swe.scim.spec.protocol.data.SearchRequest;
import edu.psu.swe.scim.spec.protocol.exception.ScimException;
import edu.psu.swe.scim.spec.protocol.search.SortOrder;
import edu.psu.swe.scim.spec.resources.ScimResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.jaxrs.PATCH;

@Api(tags="SCIM", hidden=true)
public interface BaseResourceTypeResource<T> {

  /**
   * @see <a href="https://tools.ietf.org/html/rfc7644#section-3.4.1">Scim spec,
   *      retrieving known resources</a>
   * @return
   * @throws ScimException 
   * @throws UnableToRetrieveResourceException 
   */
  @GET
  @Path("{id}")
  @Produces(Constants.SCIM_CONTENT_TYPE)
  @ApiOperation(value="Find by id", produces=Constants.SCIM_CONTENT_TYPE, response=ScimResource.class, code=200)
  @ApiResponses(value={
                  @ApiResponse(code=400, message="Bad Request"),
                  @ApiResponse(code=404, message="Not found"),
                  @ApiResponse(code=500, message="Internal Server Error"),
                  @ApiResponse(code=501, message="Not Implemented")
                })
    default Response getById(@ApiParam(value="id", required=true) @PathParam("id") String id, 
                             @ApiParam(value="attributes", required=false) @QueryParam("attributes") AttributeReferenceListWrapper attributes,
                             @ApiParam(value="excludedAttributes", required=false) @QueryParam("excludedAttributes") AttributeReferenceListWrapper excludedAttributes) throws ScimException {
    return Response.status(Status.NOT_IMPLEMENTED).build();
  }

  /**
   * @see <a href="https://tools.ietf.org/html/rfc7644#section-3.4.2">Scim spec,
   *      query resources</a>
   * @return
   */
  @GET
  @Produces(Constants.SCIM_CONTENT_TYPE)
  @ApiOperation(value="Find by a combination of query parameters", produces=Constants.SCIM_CONTENT_TYPE, response=ListResponse.class, code=200)
  @ApiResponses(value={
                  @ApiResponse(code=400, message="Bad Request"),
                  @ApiResponse(code=404, message="Not found"),
                  @ApiResponse(code=500, message="Internal Server Error"),
                  @ApiResponse(code=501, message="Not Implemented")
                })
  default Response query(@ApiParam(value="attributes", required=false) @QueryParam("attributes") AttributeReferenceListWrapper attributes,
                                 @ApiParam(value="excludedAttributes", required=false) @QueryParam("excludedAttributes") AttributeReferenceListWrapper excludedAttributes,
                                 @ApiParam(value="filter", required=false) @QueryParam("filter") FilterWrapper filterWrapper,
                                 @ApiParam(value="sortBy", required=false) @QueryParam("sortBy") AttributeReference sortBy,
                                 @ApiParam(value="sortOrder", required=false) @QueryParam("sortOrder") SortOrder sortOrder,
                                 @ApiParam(value="startIndex", required=false) @QueryParam("startIndex") Integer startIndex,
                                 @ApiParam(value="count", required=false) @QueryParam("count") Integer count) throws ScimException {
    return Response.status(Status.NOT_IMPLEMENTED).build();
  }

  /**
   * @see <a href="https://tools.ietf.org/html/rfc7644#section-3.3">Scim spec,
   *      query resources</a>
   * @return
   */
  @POST
  @Consumes(Constants.SCIM_CONTENT_TYPE)
  @Produces(Constants.SCIM_CONTENT_TYPE)
  @ApiOperation(value = "Create", produces=Constants.SCIM_CONTENT_TYPE, consumes=Constants.SCIM_CONTENT_TYPE, response = ScimResource.class, code = 201)
  @ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 409, message = "Conflict"), @ApiResponse(code = 500, message = "Internal Server Error"), @ApiResponse(code = 501, message = "Not Implemented") })
  default Response create(T resource,
                          @ApiParam(value="attributes", required=false) @QueryParam("attributes") AttributeReferenceListWrapper attributes,
                          @ApiParam(value="excludedAttributes", required=false) @QueryParam("excludedAttributes") AttributeReferenceListWrapper excludedAttributes) throws ScimException {
    return Response.status(Status.NOT_IMPLEMENTED).build();
  }

  /**
   * @see <a href="https://tools.ietf.org/html/rfc7644#section-3.4.3">Scim spec,
   *      query with post</a>
   * @return
   */
  @POST
  @Path("/.search")
  @Produces(Constants.SCIM_CONTENT_TYPE)
  @ApiOperation(value = "Search", produces=Constants.SCIM_CONTENT_TYPE, response = ListResponse.class, code = 200)
  @ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 500, message = "Internal Server Error"), @ApiResponse(code = 501, message = "Not Implemented") })
  default Response find(SearchRequest request) throws ScimException {
    return Response.status(Status.NOT_IMPLEMENTED).build();
  }

  /**
   * @see <a href="https://tools.ietf.org/html/rfc7644#section-3.5.1">Scim spec,
   *      update</a>
   * @return
   */
  @PUT
  @Path("{id}")
  @Consumes(Constants.SCIM_CONTENT_TYPE)
  @Produces(Constants.SCIM_CONTENT_TYPE)
  @ApiOperation(value = "Update", produces=Constants.SCIM_CONTENT_TYPE, consumes=Constants.SCIM_CONTENT_TYPE, response = ScimResource.class, code = 200)
  @ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 500, message = "Internal Server Error"), @ApiResponse(code = 501, message = "Not Implemented") })
  default Response update(T resource, 
                          @PathParam("id") String id,
                          @ApiParam(value="attributes", required=false) @QueryParam("attributes") AttributeReferenceListWrapper attributes,
                          @ApiParam(value="excludedAttributes", required=false) @QueryParam("excludedAttributes") AttributeReferenceListWrapper excludedAttributes) throws ScimException {
    return Response.status(Status.NOT_IMPLEMENTED).build();
  }

  @PATCH
  @Path("{id}")
  @Consumes(Constants.SCIM_CONTENT_TYPE)
  @Produces(Constants.SCIM_CONTENT_TYPE)
  @ApiOperation(value = "Patch a portion of the backing store", produces=Constants.SCIM_CONTENT_TYPE, consumes=Constants.SCIM_CONTENT_TYPE, code = 204)
  @ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 404, message = "Not found"), @ApiResponse(code = 500, message = "Internal Server Error"), @ApiResponse(code = 501, message = "Not Implemented") })
  default Response patch(PatchRequest patchRequest,
                         @PathParam("id") String id,
                         @ApiParam(value="attributes", required=false) @QueryParam("attributes") AttributeReferenceListWrapper attributes,
                         @ApiParam(value="excludedAttributes", required=false) @QueryParam("excludedAttributes") AttributeReferenceListWrapper excludedAttributes) throws ScimException {
    return Response.status(Status.NOT_IMPLEMENTED).build();
  }

  @DELETE
  @Path("{id}")
  @ApiOperation(value = "Delete from the backing store", code = 204)
  @ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 404, message = "Not found"), @ApiResponse(code = 500, message = "Internal Server Error"), @ApiResponse(code = 501, message = "Not Implemented") })
  default Response delete(@ApiParam(value = "id", required = true) @PathParam("id") String id) throws ScimException {
    return Response.status(Status.NOT_IMPLEMENTED).build();
  }
}
