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
$output.java($Configuration, "ValidationConfiguration")##

$output.require("org.springframework.context.MessageSource")##
$output.require("org.springframework.context.annotation.Bean")##
$output.require("org.springframework.context.annotation.Configuration")##
$output.require("org.springframework.validation.beanvalidation.LocalValidatorFactoryBean")##

@Configuration
public class $output.currentClass {
    /**
     * Enable the bean validation provider, and configure it to use the messageSource when resolving properties
     */
    @Bean
    public LocalValidatorFactoryBean validator(MessageSource messageSource) {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource);
        return validator;
    }
}