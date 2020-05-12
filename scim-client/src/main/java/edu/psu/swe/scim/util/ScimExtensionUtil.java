package edu.psu.swe.scim.util;

import java.util.Collection;
import java.util.Map;

import edu.psu.swe.scim.spec.extension.ScimExtensionRegistry;
import edu.psu.swe.scim.spec.resources.ScimExtension;
import edu.psu.swe.scim.spec.resources.ScimResource;

/**
 * 
 * @author mez5001
 *
 * This class exposes the SCIM extension methods without requiring that the project using the client knows 
 * how the ScimExtensionRegistry works
 */
public class ScimExtensionUtil {

  /**
   * Register a single extension for a single resource 
   * @param rClass ScimResource class
   * @param eClass Extension class
   */
  public static void registerExtension(Class<? extends ScimResource> rClass, Class<? extends ScimExtension> eClass) {
    ScimExtensionRegistry.getInstance().registerExtension(rClass, eClass);
  }
  
  /**
   * Register multiple extensions through a map
   * @param extensions Map containing all of the ScimResources and the accompanying Extensions
   */
  public static void registerExtensions(Map<Class<? extends ScimResource>, Collection<Class<? extends ScimExtension>>> extensions) {
    for(Map.Entry<Class<? extends ScimResource>, Collection<Class<? extends ScimExtension>>> entry : extensions.entrySet()) {
      Class<? extends ScimResource> resource = entry.getKey();
      for(Class<? extends ScimExtension> extension : entry.getValue()) {
        ScimExtensionRegistry.getInstance().registerExtension(resource, extension);
      }
    }
  }
}
