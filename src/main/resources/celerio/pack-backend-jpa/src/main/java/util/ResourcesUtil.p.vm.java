## Copyright 2015 JAXIO http://www.jaxio.com
##
## Licensed under the Apache License, Version 2.0 (the "License");
## you may not use this file except in compliance with the License.
## You may obtain a copy of the License at
##
##    http://www.apache.org/licenses/LICENSE-2.0
##
## Unless required by applicable law or agreed to in writing, software
## distributed under the License is distributed on an "AS IS" BASIS,
## WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
## See the License for the specific language governing permissions and
## limitations under the License.
##
$output.java($Util, "ResourcesUtil")##

$output.require("javax.inject.Inject")##
$output.require("org.springframework.context.MessageSource")##
$output.require($Context, "LocaleHolder")##

/**
 * ${output.currentClass} allows you to retrieve localized resources for the locale present in the current thread of execution.
 * It can be used from both Spring beans (simple dependency injection) and from non spring beans. In the later case,
 * you obtain the reference thanks to the static method ${output.currentClass}.getInstance()
 */
#if($output.isAbstract())
// Add the following line in subclass :
// @Named
// @Singleton
// @Lazy(false)
// public class $output.currentRootClass extends ${output.currentRootClass}Base {
//    
//   private static $output.currentRootClass instance;
//   public static $output.currentRootClass getInstance() {
//       return instance;
//   }
//   public ${output.currentRootClass}() {
//       instance = this;
//   }
// }
#else
$output.dynamicAnnotationTakeOver("javax.inject.Named","javax.inject.Singleton", "org.springframework.context.annotation.Lazy(false)")##
#end
public class $output.currentClass {
#if(!$output.isAbstract())
    private static ${output.currentClass} instance;
    public static ${output.currentClass} getInstance() {
        return instance;
    }

#end
    @Inject
    private MessageSource messageSource;
#if(!$output.isAbstract())

    public ${output.currentClass}() {
        instance = this;
    }
#end

    /**
     * Return the {@link MessageSource} that backs this ${output.currentClass}.
     */
    public MessageSource getMessageSource() {
        return messageSource;
    }

    // Note: do not replace the 3 methods below by one taking Object... args as second argument, EL 3.0 would cry.

    public String getProperty(String key) {
        if (key == null) {
            return "";
        }

        return messageSource.getMessage(key, new Object[0], LocaleHolder.getLocale());
    }

    public String getProperty(String key, Object arg) {
        if (key == null) {
            return "";
        }

        return messageSource.getMessage(key, new Object[]{arg}, LocaleHolder.getLocale());
    }

    /**
     * Return the property value for the contextual locale.
     * If no value is found, the given key is returned.
     */
    public String getProperty(String key, Object[] args) {
        if (key == null) {
            return "";
        }

        return messageSource.getMessage(key, args, LocaleHolder.getLocale());
    }

    /**
     * Same as {@link ${pound}getProperty(String, Object...)} but use the count to choose the proper key.
     * Used when the message varies depending on the context. 
     * <p>
     * For example: 
     * <ul>
     * <li>there is no result</li>
     * <li>there is one result</li>
     * <li>there are n results</li>
     * </ul>
     * @param key the base key
     */
    public String getPluralableProperty(String key, int count) {
        if (key == null) {
            return "";
        }

        switch (count) {
        case 0:
            return getProperty(key + "_0");
        case 1:
            return getProperty(key + "_1");
        default:
            return getProperty(key + "_n", count);
        }
   }
}