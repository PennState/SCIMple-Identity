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

import edu.psu.swe.scim.server.provider.Provider;
import edu.psu.swe.scim.spec.protocol.GroupResource;
import edu.psu.swe.scim.spec.resources.ScimGroup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Stateless
public class GroupResourceImpl extends BaseResourceTypeResourceImpl<ScimGroup> implements GroupResource {

//  @Inject
////  @ProviderQualifier(ScimGroup.class)
//  Provider<ScimGroup> provider;

  @Override
  public Provider<ScimGroup> getProvider() {
    try {
      TypeLiteral<Provider<ScimGroup>> typeLiteral = new TypeLiteral<Provider<ScimGroup>>() {
      };
      Instance<Provider<ScimGroup>> select = CDI.current().select(typeLiteral);
      return select.get();
    } catch (Exception e) {
      log.warn("Provider doesn't exist", e);
      return null;
    }
  }
  
}
