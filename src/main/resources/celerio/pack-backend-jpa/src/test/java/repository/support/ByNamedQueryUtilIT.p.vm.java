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
$output.generateIf($project.isAccountEntityPresent())##
$output.skipIf($project.accountEntity.isVirtual())##
$output.javaTest($RepositorySupport, "ByNamedQueryUtilIT")

$output.require("javax.inject.Inject")##
$output.require("com.jaxio.jpa.querybyexample.*")##
$output.require("org.junit.Test")##
$output.require("org.junit.runner.RunWith")##
$output.require("org.springframework.test.context.ContextConfiguration")##
$output.require("org.springframework.test.context.junit4.SpringJUnit4ClassRunner")##
$output.require("org.springframework.transaction.annotation.Transactional")##
$output.requireStatic("org.fest.assertions.Assertions.assertThat")##

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring/applicationContext-test.xml" })
@Transactional
public class $output.currentClass {

    @Inject
    private ByNamedQueryUtil byNamedQueryUtil;

#if (!$project.accountEntity.isVirtual())
    @Test
    public void allAccountsUsingNamedQuery() {
        SearchParameters searchParameters = new SearchParameters().namedQuery("$project.accountEntity.jpa.selectAllNamedQuery");
		assertThat(byNamedQueryUtil.findByNamedQuery(searchParameters)).hasSize(#if($project.isDefaultSchema())53#{else}1#{end});
    }

    @Test
    public void allAccountsUsingNativeNamedQuery() {
        SearchParameters searchParameters = new SearchParameters().namedQuery("$project.accountEntity.jpa.selectAllNativeNamedQuery");
		assertThat(byNamedQueryUtil.findByNamedQuery(searchParameters)).hasSize(#if($project.isDefaultSchema())53#{else}1#{end});
    }
#else
    @Test
    public void createAccountTableToSeeNamedQueryTests() {
        log.info("Create an account table to see generated named query utils");
    }
#end
}