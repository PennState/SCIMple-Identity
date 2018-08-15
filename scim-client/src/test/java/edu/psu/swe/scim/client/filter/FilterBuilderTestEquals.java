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

package edu.psu.swe.scim.client.filter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.junit.Test;

import edu.psu.swe.scim.spec.protocol.filter.FilterParseException;
import edu.psu.swe.scim.spec.protocol.search.Filter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FilterBuilderTestEquals {
  
  @Test
  public void testEqualToStringString() throws UnsupportedEncodingException, FilterParseException {
    String encoded = FilterClient.builder().equalTo("address.streetAddress", "7714 Sassafrass Way").toString();
    Filter filter = new Filter(decode(encoded));
  }

  @Test
  public void testEqualToStringBoolean() throws UnsupportedEncodingException, FilterParseException {
    String encoded = FilterClient.builder().equalTo("address.active", true).toString();
    Filter filter = new Filter(decode(encoded));
  }

  @Test
  public void testEqualToStringDate() throws UnsupportedEncodingException, FilterParseException {
    String encoded = FilterClient.builder().equalTo("date.date", new Date()).toString();
    Filter filter = new Filter(decode(encoded));
  }

  @Test
  public void testEqualToStringLocalDate() throws UnsupportedEncodingException, FilterParseException {
    String encoded = FilterClient.builder().equalTo("date.date", LocalDate.now()).toString();
    Filter filter = new Filter(decode(encoded));
  }

  @Test
  public void testEqualToStringLocalDateTime() throws UnsupportedEncodingException, FilterParseException {
    String encoded = FilterClient.builder().equalTo("date.date", LocalDateTime.now()).toString();
    Filter filter = new Filter(decode(encoded));
  }

  @Test
  public void testEqualToStringInteger() throws UnsupportedEncodingException, FilterParseException {
    int i = 10;
    String encoded = FilterClient.builder().equalTo("int.int", i).toString();
    Filter filter = new Filter(decode(encoded));
  }

  @Test
  public void testEqualToStringLong() throws UnsupportedEncodingException, FilterParseException {
    long i = 10l;
    String encoded = FilterClient.builder().equalTo("long.long", i).toString();
    Filter filter = new Filter(decode(encoded));
  }

  @Test
  public void testEqualToStringFloat() throws UnsupportedEncodingException, FilterParseException {
    float i = 10.2f;
    String encoded = FilterClient.builder().equalTo("long.long", i).toString();
    Filter filter = new Filter(decode(encoded));
  }

  @Test
  public void testEqualToStringDouble() throws UnsupportedEncodingException, FilterParseException {
    double i = 10.2;
    String encoded = FilterClient.builder().equalTo("long.long", i).toString();
    Filter filter = new Filter(decode(encoded));
  }

  @Test
  public void testEqualNull() throws UnsupportedEncodingException, FilterParseException {
    String encoded = FilterClient.builder().equalNull("null.null").toString();
    Filter filter = new Filter(decode(encoded));
  }
  
  private String decode(String encoded) throws UnsupportedEncodingException {

    log.info(encoded);
    
    String decoded = URLDecoder.decode(encoded, "UTF-8").replace("%20", " ");
    
    log.info(decoded);
    
    return decoded;
  }
}
