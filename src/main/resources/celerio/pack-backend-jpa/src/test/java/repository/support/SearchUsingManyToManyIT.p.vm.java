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
$output.generateIf($project.isDefaultSchema())##
$output.javaTest($RepositorySupport, "SearchUsingManyToManyIT")

$output.requireStatic("com.google.common.collect.Lists.newArrayList")##
$output.requireStatic("org.fest.assertions.Assertions.assertThat")##
$output.require("javax.inject.Inject")##
$output.require("com.jaxio.jpa.querybyexample.SearchParameters")##
$output.require("org.junit.Test")##
$output.require("org.junit.runner.RunWith")##
$output.require("org.springframework.test.context.ContextConfiguration")##
$output.require("org.springframework.test.context.junit4.SpringJUnit4ClassRunner")##
$output.require($project.accountEntity.model)##
$output.requireMetamodel($project.accountEntity.model)##
$output.require($project.accountEntity.repository)##
$output.require($project.roleEntity.repository)##
$output.require($Model, "Address_")##

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring/applicationContext-test.xml" })
public class $output.currentClass {
    @Inject
    private AccountRepository accountRepository;

    @Inject
    private RoleRepository roleRepository;

    @Test
    public void useAND() {
        Account account = buildAccount();
        verifySize(account, sp().useAndInXToMany(), 1);
        verifySize(account, sp().useAndInXToMany().asc(Account_.email), 1);
        verifySize(account, sp().useAndInXToMany().distinct(), 1);
        verifySize(account, sp().useAndInXToMany().distinct().asc(Account_.email), 1);
        verifySize(account, sp().useAndInXToMany().distinct().fetch(Account_.homeAddress), 1);
        verifySize(account, sp().useAndInXToMany().distinct().fetch(Account_.homeAddress).asc(Account_.homeAddress, Address_.city), 1);
    }

    @Test
    public void useOR() {
        Account account = buildAccount();
        verifySize(account, sp().useOrInXToMany(), 4);
        verifySize(account, sp().useOrInXToMany().asc(Account_.email), 4);
        verifySize(account, sp().useOrInXToMany().distinct(), 3);
        verifySize(account, sp().useOrInXToMany().distinct().asc(Account_.email), 3);
        verifySize(account, sp().useOrInXToMany().distinct().fetch(Account_.homeAddress), 3);
        verifySize(account, sp().useOrInXToMany().distinct().fetch(Account_.homeAddress).asc(Account_.homeAddress, Address_.city), 3);
    }

    private Account buildAccount() {
        Account account = new Account();
        account.setSecurityRoles(newArrayList(roleRepository.getByRoleName("ROLE_ADMIN"), roleRepository.getByRoleName("ROLE_USER")));
        return account;
    }

    private void verifySize(Account account, SearchParameters s, int expectedSize) {
        assertThat(accountRepository.find(account, s)).hasSize(expectedSize);
        assertThat(accountRepository.findCount(account, s)).isEqualTo(expectedSize);
    }
    
    public static SearchParameters sp() {
        return new SearchParameters();
    }
}