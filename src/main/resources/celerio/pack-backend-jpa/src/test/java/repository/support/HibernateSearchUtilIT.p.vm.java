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
$output.generateIf($project.getHibernateSearchUsed(), $project.isDefaultSchema())##
$output.javaTest($RepositorySupport, "HibernateSearchUtilIT")##

$output.requireStatic("org.fest.assertions.Assertions.assertThat")##
$output.requireStatic("org.hibernate.search.jpa.Search.getFullTextEntityManager")##
$output.require("javax.inject.Inject")##
$output.require("javax.persistence.EntityManager")##
$output.require("javax.persistence.PersistenceContext")##
$output.require("com.jaxio.jpa.querybyexample.*")##
$output.require("org.junit.Before")##
$output.require("org.junit.Test")##
$output.require("org.junit.runner.RunWith")##
$output.require("org.springframework.test.context.ContextConfiguration")##
$output.require("org.springframework.test.context.junit4.SpringJUnit4ClassRunner")##
$output.require("org.springframework.transaction.annotation.Transactional")##
$output.require($project.accountEntity.model)##
$output.requireMetamodel($project.accountEntity.model)##
$output.require($project.accountEntity.repository)##

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring/applicationContext-test.xml" })
@Transactional
public class HibernateSearchUtilIT {

    @Inject
    private AccountRepository accountRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Before
    public void buildExample() {
        accountRepository.save(jeanLouis());
        accountRepository.save(jeanMichel());
        entityManager.flush();
        getFullTextEntityManager(entityManager).flushToIndexes();
    }

    private Account jeanMichel() {
        return accountRepository.getNew() //
                .email("jmdexample@example.com") //
                .firstName("Jean-Michel") //
                .lastName("EXAMPLE") //
                .username("jmexample") //
                .password("xxx") //
                .isEnabled(true);
    }

    private Account jeanLouis() {
        return accountRepository.getNew() //
                .email("jlsample@example.com") //
                .firstName("Jean-Louis") //
                .lastName("SAMPLE") //
                .username("jlsample") //
                .password("xxx") //
                .isEnabled(true);
    }

    @Test
    public void term() {
        SearchParameters sp = new SearchParameters() //
    		.term("jeaan");
        assertThat(accountRepository.find(sp)).hasSize(2);
    }

    @Test
    public void termOn() {
        SearchParameters sp = new SearchParameters() //
            .term(Account_.firstName, "louhis");
        assertThat(accountRepository.find(sp)).hasSize(1);
    }

    @Test
    public void termOnManyProperties() {
        SearchParameters sp = new SearchParameters() //
			.term(Account_.email, "sammple") //
			.term(Account_.lastName, "sammple");
        assertThat(accountRepository.find(sp)).hasSize(2);
    }

    @Test
    public void termOnManyNoMatch() {
        SearchParameters sp = new SearchParameters() //
			.term(Account_.email, "sammple") //
			.term(Account_.firstName, "sammple");
        assertThat(accountRepository.find(sp)).isEmpty();
    }

    @Test
    public void termOnWithSimilariy() {
        SearchParameters sp = new SearchParameters() //
			.searchSimilarity(0.99f) //
			.term(Account_.firstName, "louhis"); //
        assertThat(accountRepository.find(sp)).isEmpty();
    }

    @Test
    public void termOnNoMatch() {
        SearchParameters sp = new SearchParameters() //
			.term(Account_.email, "louhis");
        assertThat(accountRepository.find(sp)).isEmpty();
    }

    @Test(expected = IllegalArgumentException.class)
    public void searchOnUninedexedAttribute() {
         SearchParameters sp = new SearchParameters() //
 			.searchSimilarity(0.99f) //
 			.term(Account_.description, "louhis");
         accountRepository.find(sp);
    }
}