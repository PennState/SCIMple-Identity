# NOTE: This project has been moved to the Apache Directory Project.  
**The new Repo is at https://github.com/apache/directory-scimple**
**Over the next few weeks we will be adjusting package names and migrating tickets.** 
**For those using the library, your patience is appreciated.**

**Issue tracking has been turned off for this project.  All issues must be submitted**
**against Apache's Jira issue tracker at https://issues.apache.org/jira/projects/SCIMPLE/issues.**

# SCIM

Penn State's Open Source JavaEE implementation of the Simple Cross-domain Identity
Management (SCIM) version 2.0 specification (RFC7642, RFC7643 and RFC7644).  This
project is constructing a SCIM framework using first-principles from the released
specifications.  Much of the code is being adapted from Penn State's "Friends of
Penn State" (FPS) implementation.

## History

In June of 2013, we embarked on replacing the FPS system that now serves over
3MM non-privileged accounts.  Our goals for this rewrite were to eliminate the
redundant persistence (data was stored in both an RDBMS and LDAP) as well as
to replace the ancient and inefficient XML-based APIs with an industry standard
API.  The SCIM specification was, at the time still almost two years away from
being released, but even the early versions of the specification showed the
promise of a modern API.

The FPS implementation of SCIM has been in production for over two years and
as stated above is managing over 3MM identities.  The system is adding almost
400K identities per year and uses LDAP for persistence.

## Goals

Now that the specification has been ratified, we're writing a generic implementation
from first-principles with the goal of forming a community around the core
features but allowing customization via ResourceType "providers".  The modules
that implement the specification require Java SE8.  The server implementations
require Java EE7.

The goals for the project are as follows:

-   Declarative creation of new ResourceTypes and Extensions.
-   Example implementations of the server with various persistence paradigms.
-   Dynamic generation of the Schema and ResourceType end-points.
-   A suite of tools to allow the creation of SCIM resources that can then be
    verified against the appropriate schemas.
-   A set of verification tests to ensure the project meets the specification
    and to allow inter-operability testing.
    
Tutorials and demonstrations
----------------------------

-   [Tutorial showing how to create a SCIM server with an extension](https://www.youtube.com/watch?v=YuAOcmLYyaM) - This approximately 20 minute video provides instruction on how to create
    a SCIM server using specifying a basic ScimPerson and a simple extension.  

-   [TIER demonstration server](https://scim.psu.edu/tier/v2) - Penn State has
    deployed a demonstration server that provides a memory-based persistence
    (reset during restarts) for the Internet2 and SCIM communities to use for
    testing purposes.  This server currently implements the SCIM User resource
    with an eduPerson extension.  It also implements a custom resource that
    returns a full eduPerson.  Feel free to try out SCIM - here are some
    working URIs to get you started:
    -   https://scim.psu.edu/tier/v2/ServiceProviderConfig
    -   https://scim.psu.edu/tier/v2/ResourceTypes
    -   https://scim.psu.edu/tier/v2/Schemas
    -   https://scim.psu.edu/tier/v2/Users
    -   https://scim.psu.edu/tier/v2/Users/1234567890
    -   https://scim.psu.edu/tier/v2/EduPeople
    -   https://scim.psu.edu/tier/v2/EduPeople/1234567890

Example code
------------
    
Examples of the project's declarative syntax are shown below.  The first example
shows how a SCIM resource is declared:

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ScimResourceType(
      id = ScimUser.RESOURCE_NAME,
      name = ScimUser.RESOURCE_NAME,
      schema = ScimUser.SCHEMA_URI,
      description = "Top level ScimUser",
      endpoint = "/Users"
    )
    @XmlRootElement(name = ScimUser.RESOURCE_NAME)
    @XmlAccessorType(XmlAccessType.FIELD)
    public class ScimUser extends ScimResource {
    
      public static final String RESOURCE_NAME = "User";
      public static final String SCHEMA_URI = "urn:ietf:params:scim:schemas:core:2.0:User";
    
      @ScimAttribute(
        description="A Boolean value indicating the User's administrative status.",
        type=Type.BOOLEAN
      )
      
      @XmlElement(name = "active")
      Boolean active = true;
    
      @ScimAttribute(
        type = Type.COMPLEX,
        description="A physical mailing address for this User, as described in (address Element). Canonical Type Values of work, home, and other. The value attribute is a complex type with the following sub-attributes."
      )
      @XmlElement(name = "addresses")
      List<Address> addresses;

In the example above, annotations are used at the class level and member level
to declare a new SCIM resource and its attributes respectively.  The example
below shows the equivalent declaration for a resource Extension:

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.NONE)
    @ScimExtensionType(
      required = false,
      name = "EnterpriseUser",
      id = EnterpriseExtension.URN,
      description = "Attributes commonly used in representing users that belong to, or act on behalf of, a business or enterprise."
    )
    @Data
    public class EnterpriseExtension implements ScimExtension {
    
      public static final String URN = "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User";
    
        @XmlType
        @XmlAccessorType(XmlAccessType.NONE)
        @Data
        public static class Manager {
    
          @ScimAttribute(
            description = "The \"id\" of the SCIM resource representing the user's manager.  RECOMMENDED."
          )
      
          @XmlElement
          private String value;
    
          @ScimAttribute(
            description = "The URI of the SCIM resource representing the User's manager.  RECOMMENDED."
          )
          
          @XmlElement
          private String $ref;

This example shows how an extension is declared at the class level, but also
provides an example of how complex SCIM types can be simply defined as
suitably annotated inner classes.

Implementations are fully customizable without altering the core server code.
The example below shows how the implementation for a provider is declared:

    public class InMemoryUserService implements Provider<ScimUser> {

Implementing the provider interface allows the customization of create,
retrieve, update and delete methods (as well as find).  Customization is
flexible - if your system implements soft deletes, create a delete method that
simply sets a flag and alter the find and retrieve methods to only return
"undeleted" resources.

      @Inject
      EndpointUtil endpointUtil;
      .....
      .....
      URL groupEndpoint = endpointUtil.getEndpoint(ScimGroup.class);
      URL userEndpoint = endpointUtil.getEndpoint(ScimUser.class);
     
With reference type returns developers may need to access the base endpoints used to execute the SCIM request.  
The example above illustrates injecting an endpointUtil class, which is a helper class that provides that specific 
information.  If this identity provider does not provide a a provisioner for the endpoint requested null is returned.
