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

package edu.psu.swe.scim.server.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import edu.psu.swe.scim.server.schema.Registry;
import edu.psu.swe.scim.spec.protocol.SchemaResource;
import edu.psu.swe.scim.spec.protocol.data.ListResponse;
import edu.psu.swe.scim.spec.schema.Meta;
import edu.psu.swe.scim.spec.schema.Schema;

@Stateless
public class SchemaResourceImpl implements SchemaResource {
  
  @Inject
  Registry registry;
  
  @Context 
  private UriInfo uriInfo;
  
  @Override
  public Response getAllSchemas(String filter) {

    if (filter != null) {
      return Response.status(Status.FORBIDDEN).build();
    }
    
    ListResponse<Schema> listResponse = new ListResponse<>();
    Collection<Schema> schemas = registry.getAllSchemas();
    
    for (Schema schema : schemas) {
      Meta meta = new Meta();
      meta.setLocation(uriInfo.getAbsolutePathBuilder().path(schema.getId()).build().toString());
      meta.setResourceType(Schema.RESOURCE_NAME);
      
      schema.setMeta(meta);
    }
    
    listResponse.setItemsPerPage(schemas.size());
    listResponse.setStartIndex(1);
    listResponse.setTotalResults(schemas.size());
    
    List<Schema> objectList = new ArrayList<>(schemas);
    listResponse.setResources(objectList);
    
    return Response.ok(listResponse).build();
  }

  @Override
  public Response getSchema(String urn) {
    
    Schema schema = registry.getSchema(urn);
    if (schema == null){
      return Response.status(Status.NOT_FOUND).build();  
    }
    
    Meta meta = new Meta();
    meta.setLocation(uriInfo.getAbsolutePath().toString());
    meta.setResourceType(Schema.RESOURCE_NAME);
    
    schema.setMeta(meta);
    
    return Response.ok(schema).build();
    
  }
}
