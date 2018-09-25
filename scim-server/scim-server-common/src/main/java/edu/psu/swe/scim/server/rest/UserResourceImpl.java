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

/**
 * 
 */
package edu.psu.swe.scim.server.rest;

import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;

import edu.psu.swe.scim.server.provider.Provider;
import edu.psu.swe.scim.server.provider.ProviderQualifier;
import edu.psu.swe.scim.server.provider.ProviderRegistry;
import edu.psu.swe.scim.spec.protocol.UserResource;
import edu.psu.swe.scim.spec.resources.ScimUser;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shawn
 *
 */
@Slf4j
@Stateless
public class UserResourceImpl extends BaseResourceTypeResourceImpl<ScimUser> implements UserResource {

//  @Inject
//  ProviderRegistry providerRegistry;
//  
//  @Inject
////  @ProviderQualifier(ScimUser.class)
//  Provider<ScimUser> provider;
  
  @Override
  public Provider<ScimUser> getProvider() {
    try {
      TypeLiteral<Provider<ScimUser>> typeLiteral = new TypeLiteral<Provider<ScimUser>>() {
      };
      Instance<Provider<ScimUser>> select = CDI.current().select(typeLiteral);
      return select.get();
    } catch (Exception e) {
      log.warn("Provider doesn't exist", e);
      return null;
    }
  }

}
