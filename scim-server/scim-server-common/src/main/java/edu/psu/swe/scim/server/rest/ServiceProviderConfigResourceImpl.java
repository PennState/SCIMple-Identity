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

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.psu.swe.scim.server.configuration.ServerConfiguration;
import edu.psu.swe.scim.server.utility.EtagGenerator;
import edu.psu.swe.scim.spec.protocol.ServiceProviderConfigResource;
import edu.psu.swe.scim.spec.protocol.data.ErrorResponse;
import edu.psu.swe.scim.spec.schema.Meta;
import edu.psu.swe.scim.spec.schema.ServiceProviderConfiguration;
import edu.psu.swe.scim.spec.schema.ServiceProviderConfiguration.AuthenticationSchema;
import edu.psu.swe.scim.spec.schema.ServiceProviderConfiguration.BulkConfiguration;
import edu.psu.swe.scim.spec.schema.ServiceProviderConfiguration.FilterConfiguration;
import edu.psu.swe.scim.spec.schema.ServiceProviderConfiguration.SupportedConfiguration;

@Stateless
public class ServiceProviderConfigResourceImpl implements ServiceProviderConfigResource {

  @Inject
  ServerConfiguration serverConfiguration;

  @Inject
  EtagGenerator etagGenerator;
  
  public ServiceProviderConfigResourceImpl() {
    serverConfiguration = new ServerConfiguration();  
  }
  
  public void registerServerConfiguration(ServerConfiguration configuration) {
    serverConfiguration = configuration;  
  }
  
  @Override
  public Response getServiceProviderConfiguration(UriInfo uriInfo) {
    ServiceProviderConfiguration serviceProviderConfiguration = new ServiceProviderConfiguration();
    List<AuthenticationSchema> authenticationSchemas = serverConfiguration.getAuthenticationSchemas();
    BulkConfiguration bulk = serverConfiguration.getBulkConfiguration();
    SupportedConfiguration changePassword = serverConfiguration.getChangePasswordConfiguration();
    SupportedConfiguration etagConfig = serverConfiguration.getEtagConfiguration();
    FilterConfiguration filter = serverConfiguration.getFilterConfiguration();
    SupportedConfiguration patch = serverConfiguration.getPatchConfiguration();
    SupportedConfiguration sort = serverConfiguration.getSortConfiguration();
    String documentationUrl = serverConfiguration.getDocumentationUri();
    String externalId = serverConfiguration.getId();
    String id = serverConfiguration.getId();
    Meta meta = new Meta();
    String location = uriInfo.getAbsolutePath().toString();
    String resourceType = "ServiceProviderConfig";
    LocalDateTime now = LocalDateTime.now();

    meta.setCreated(now);
    meta.setLastModified(now);
    meta.setLocation(location);
    meta.setResourceType(resourceType);
    serviceProviderConfiguration.setAuthenticationSchemes(authenticationSchemas);
    serviceProviderConfiguration.setBulk(bulk);
    serviceProviderConfiguration.setChangePassword(changePassword);
    serviceProviderConfiguration.setDocumentationUrl(documentationUrl);
    serviceProviderConfiguration.setEtag(etagConfig);
    serviceProviderConfiguration.setExternalId(externalId);
    serviceProviderConfiguration.setFilter(filter);
    serviceProviderConfiguration.setId(id);
    serviceProviderConfiguration.setMeta(meta);
    serviceProviderConfiguration.setPatch(patch);
    serviceProviderConfiguration.setSort(sort);
    
    try {
      EntityTag etag = etagGenerator.generateEtag(serviceProviderConfiguration);
      return Response.ok(serviceProviderConfiguration).tag(etag).build();
    } catch (JsonProcessingException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
      return createETagErrorResponse();
    }
  }
  
  private Response createETagErrorResponse() {
    ErrorResponse er = new ErrorResponse(Status.INTERNAL_SERVER_ERROR, "Failed to generate the etag");
    return er.toResponse();
  }
}
