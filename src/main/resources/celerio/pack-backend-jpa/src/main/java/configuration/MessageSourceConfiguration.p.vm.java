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
$output.java($Configuration, "MessageSourceConfiguration")##

$output.require("org.springframework.context.MessageSource")##
$output.require("org.springframework.context.annotation.Bean")##
$output.require("org.springframework.context.annotation.Configuration")##
$output.require("org.springframework.context.support.ReloadableResourceBundleMessageSource")##

@Configuration
public class $output.currentClass {
    /**
     * Base message source to handle internationalization<p>
     * Order of basenames matters, the first found property is returned 
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasenames( // 
                // main resources
                "classpath:${LOCALIZATION.resourcePath}/application",
                "classpath:${LOCALIZATION.resourcePath}/messages", //
                // pages
                "classpath:/localization/pages/home", //
                "classpath:/localization/pages/login", //
                "classpath:/localization/pages/concurrentModificationResolution", //
                // default spring security messages
                "classpath:org/springframework/security/messages", //
                //  our bean validation messages
                "classpath:ValidationMessages", //
                // default conversion messages
                "classpath:javax/faces/Messages", //
                // generated domain resources
#foreach ($entity in $project.withoutManyToManyJoinEntities.list)
                "classpath:${DOMAIN_LOCALIZATION.resourcePath}/${entity.model.type}", //
#end
#foreach ($enumType in $project.enumTypes)
                "classpath:${DOMAIN_LOCALIZATION.resourcePath}/${enumType.model.type}", //
#end
                // default bean validation messages 
                "classpath:org/hibernate/validator/ValidationMessages" //
                );
        return messageSource;
    }
}
